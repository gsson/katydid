package se.fnord.katydid.internal;

import se.fnord.katydid.DataAsserts;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;
import java.util.*;

import static se.fnord.katydid.internal.HexFormat.formatOffset;

public class TestingContext {
	private final ByteBuffer buffer;
	private final int startPosition;
	private final int startLimit;
	private final Deque<String> names;
	private final List<String> messages;

	public TestingContext(ByteBuffer buffer) {
		this.buffer = buffer;
		this.startPosition = buffer.position();
		this.startLimit = buffer.limit();
		this.names = new ArrayDeque<>();
		this.messages = new ArrayList<>();
	}

	public void addFailure(DataTester tester, int itemIndex, String message, Object ... formatArgs) {
		final Formatter formatter = new Formatter();
		int n = HexFormat.hexLength(buffer.limit());
		int startOffset = buffer.position() - tester.lengthOfItem(itemIndex);
		int endOffset = buffer.position() - 1;
		if (startOffset == endOffset)
			formatter.format("%s at %s: ", tester.formatName(this, itemIndex), formatOffset(n, startOffset));
		else
			formatter.format("%s at %s-%s: ", tester.formatName(this, itemIndex), formatOffset(n, startOffset), formatOffset(n, endOffset));
		formatter.format(message, formatArgs);
		messages.add(formatter.toString());
	}

	public void addFailure(String message, Object ... formatArgs) {
		messages.add(String.format(message, formatArgs));
	}

	public void assertSuccess(DataTester root) {
		if (messages.isEmpty())
			return;

		final StringBuilder sb = new StringBuilder();
		sb.append("Test failed:").append(System.lineSeparator());
		for (String message : messages)
			sb.append("* ").append(message).append(System.lineSeparator());

		sb.append("Expected bytes:").append(System.lineSeparator());
		HexFormat.format(sb, DataAsserts.asBuffer(root));

		buffer.position(startPosition).limit(startLimit);
		sb.append("Actual bytes:").append(System.lineSeparator());
		HexFormat.format(sb, buffer);
		throw new AssertionError(sb.toString());
	}

	int globalPosition() {
		return buffer.position();
	}

	public ByteBuffer buffer() {
		return buffer;
	}

	public void down(String name) {
		names.addLast(names.isEmpty() ? name : (name() + "." + name));
	}

	public void up() {
		names.pollLast();
	}

	public String name() {
		return names.peekLast();
	}
}
