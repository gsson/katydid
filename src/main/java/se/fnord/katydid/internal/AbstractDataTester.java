package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class AbstractDataTester implements DataTester {
	private final String name;
	public AbstractDataTester(String name) {
		this.name = name;
	}

	protected abstract String formatValue(Object o);

	protected abstract int sizeOf(int itemIndex);

	protected abstract int itemCount();

	public String formatName(TestingContext context, int index) {
		return context.name();
	}

	protected ComparisonStatus compareToLevel0(TestingContext context) {
		throw new UnsupportedOperationException();
	}

	protected ComparisonStatus doCompareTo(int pass, TestingContext context) {
		if (pass == 0)
			return compareToLevel0(context);
		else
			return skip(context);

	}

	@Override
	public final ComparisonStatus compareTo(int pass, TestingContext context) {
		context.down(name);
		int start = context.buffer().position();
		try {
			return doCompareTo(pass, context);
		}
		finally {
			context.buffer().position(start + length());
			context.up();
		}
	}


	@Override
	public int passCount() {
		return 1;
	}

	protected ComparisonStatus skip(TestingContext context) {
		ByteBuffer bb = context.buffer();
		if (!checkHasRemaining(context, length()))
			return ComparisonStatus.ERROR;
		bb.position(bb.position() + length());
		return ComparisonStatus.EQUAL;
	}

	public int length() {
		return localPosition(itemCount());
	}

	private int localPosition(int itemIndex) {
		int localPosition = 0;
		for (int i = 0; i < itemIndex; i++) {
			localPosition += sizeOf(i);
		}
		return localPosition;
	}

	private String formatLocation(TestingContext context, int itemIndex) {
		int localPos = localPosition(itemIndex);
		int globalPos = context.globalPosition(localPos);

		return String.format("<local: %04x, global: %04x>", localPos, globalPos);
	}

	protected boolean checkHasRemaining(TestingContext context, int itemIndex) {
		int remaining = context.buffer().remaining();
		if (remaining < sizeOf(itemIndex)) {
			String message = String.format("%s: Buffer underflows at %s. Element needs %d additional bytes.",
					formatName(context, itemIndex), formatLocation(context, itemIndex), length() - localPosition(itemIndex) - remaining);
			context.addFailure(message);
			return false;
		}
		return true;
	}

	protected boolean checkEquals(TestingContext context, int itemIndex, Object a, Object b) {
		if (!Objects.equals(a, b)) {
			String message = String.format("%s: Value at %s differs: %s != %s",
					formatName(context, itemIndex), formatLocation(context, itemIndex), formatValue(a), formatValue(b));
			context.addFailure(message);
			return false;
		}
		return true;
	}
}
