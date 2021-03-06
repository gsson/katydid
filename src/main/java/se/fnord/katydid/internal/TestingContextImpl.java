package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataAsserts;
import se.fnord.katydid.DataTester;
import se.fnord.katydid.TestingContext;

import java.nio.ByteBuffer;
import java.util.*;

import static java.lang.Math.min;
import static se.fnord.katydid.ComparisonStatus.ABORT;
import static se.fnord.katydid.ComparisonStatus.CONTINUE;
import static se.fnord.katydid.internal.HexFormat.formatOffset;

public class TestingContextImpl implements TestingContext {
	private final ByteBuffer buffer;
	private final int startPosition;
	private final int startLimit;
	private final Deque<SubContext> testers;
	private final List<String> messages;

	private static class SubContext {
		private int itemIndex;
		private DataTester tester;

		public SubContext(DataTester tester) {
			this.tester = tester;
			this.itemIndex = -1;
		}

		public boolean hasNextIndex() {
			return itemIndex < (tester.itemCount() - 1);
		}

		public int currentIndex() {
			return itemIndex;
		}

		public int nextIndex() {
			return ++itemIndex;
		}

		public DataTester tester() {
			return tester;
		}
	}

	protected boolean checkHasRemaining(int required) {
		int remaining = buffer.remaining();
		if (remaining < required) {
			addFailure("Buffer underflow. Element needs %d additional bytes", required - remaining);
			return false;
		}
		return true;
	}

	@Override
	public byte read() {
		return buffer.get();
	}

	@Override
	public long read(int elementWidth) {
		return Util.read(buffer, elementWidth);
	}

	@Override
	public void write(int elementWidth, long value) {
		Util.write(buffer, elementWidth, value);
	}

	private ComparisonStatus compareItem(DataTester dataTester, int pass, int itemIndex) {
		int itemLength = dataTester.lengthOfItem(itemIndex);
		if (!checkHasRemaining(itemLength))
			return ABORT;

		int itemEnd = buffer.position() + itemLength;
		ComparisonStatus s = dataTester.compareItem(this, pass, itemIndex);
		buffer.position(itemEnd);
		return s;
	}

	@Override
	public ComparisonStatus compareTo(DataTester dataTester, int pass) {
		down(dataTester);
		int testerEnd = buffer.position() + dataTester.length();
		try {
			SubContext currentContext = currentContext();
			while (currentContext.hasNextIndex()) {
				ComparisonStatus s = compareItem(currentContext.tester, pass, currentContext.nextIndex());
				if (!s.shouldContinue())
					return s.propagate();
			}
			return CONTINUE;
		}
		finally {
			buffer.position(min(buffer.limit(), testerEnd));
			up();
		}
	}
	public TestingContextImpl(ByteBuffer buffer) {
		this.buffer = buffer;
		this.startPosition = buffer.position();
		this.startLimit = buffer.limit();
		this.testers = new ArrayDeque<>();
		this.messages = new ArrayList<>();
	}

	private String formatFailure(SubContext c, String message, Object ... formatArgs) {
		final int itemIndex = c.currentIndex();
		final DataTester tester = c.tester();
		final Formatter formatter = new Formatter();
		int n = HexFormat.hexLength(buffer.limit());
		int startOffset = buffer.position() - tester.lengthOfItem(itemIndex);
		int endOffset = buffer.position() - 1;
		if (startOffset == endOffset)
			formatter.format("%s at %s: ", tester.formatItem(name(), itemIndex), formatOffset(n, startOffset));
		else
			formatter.format("%s at %s-%s: ", tester.formatItem(name(), itemIndex), formatOffset(n, startOffset), formatOffset(n, endOffset));
		formatter.format(message, formatArgs);
		return formatter.toString();
	}

	@Override
	public void addFailure(String message, Object... formatArgs) {
		SubContext c = currentContext();
		if (c != null) {
			messages.add(formatFailure(c, message, formatArgs));
		}
		else {
			messages.add(String.format(message, formatArgs));
		}
	}

	public void assertSuccess(DataTester root) {
		if (messages.isEmpty())
			return;

		final int maxOffset = Math.max(buffer.limit(), root.length());
		final StringBuilder sb = new StringBuilder();

		sb.append("Test failed:").append(System.lineSeparator());
		for (String message : messages)
			sb.append("* ").append(message).append(System.lineSeparator());

		sb.append("Expected bytes:").append(System.lineSeparator());
		HexFormat.format(sb, DataAsserts.asBuffer(root), maxOffset);

		buffer.position(startPosition).limit(startLimit);
		sb.append("Actual bytes:").append(System.lineSeparator());
		HexFormat.format(sb, buffer, maxOffset);
		throw new AssertionError(sb.toString());
	}

	private void down(DataTester tester) {
		testers.addLast(new SubContext(tester));
	}

	private void up() {
		testers.removeLast();
	}

	private SubContext currentContext() {
		return testers.peekLast();
	}

	private String name() {
		StringBuilder sb = new StringBuilder();
		Iterator<SubContext> t = testers.iterator();
		if (t.hasNext()) {
			SubContext parent = t.next();
			sb.append(parent.tester().name());
			while (t.hasNext()) {
				SubContext child = t.next();
				sb.append(parent.tester.formatChild(parent.currentIndex(), child.tester()));
				parent = child;
			}
		}
		return sb.toString();
	}
}
