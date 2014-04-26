package se.fnord.katydid.internal;

import se.fnord.katydid.DataAsserts;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;
import java.util.*;

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

	public void addFailure(String message) {
		messages.add(message);
	}

	public void assertSuccess(DataTester root) {
		if (messages.isEmpty())
			return;

		final StringBuilder sb = new StringBuilder();
		sb.append("Test failed:").append(System.lineSeparator());
		for (String message : messages)
			sb.append("* ").append(message).append(System.lineSeparator());

		sb.append("Expected:").append(System.lineSeparator());
		HexFormat.format(sb, DataAsserts.asBuffer(root));

		buffer.position(startPosition).limit(startLimit);
		sb.append("Actual:").append(System.lineSeparator());
		HexFormat.format(sb, buffer);
		throw new AssertionError(sb.toString());
	}

	int globalPosition(int localPosition) {
		return this.startPosition + localPosition;
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
