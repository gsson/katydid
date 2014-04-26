package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;
import java.util.Objects;

public abstract class AbstractDataTester implements DataTester {
	private final String name;
	private int length = -1;
	public AbstractDataTester(String name) {
		this.name = name;
	}

	protected abstract String formatValue(Object o);

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
		if (length == -1)
			length = localPosition(itemCount());
		return length;
	}



	private int localPosition(int itemIndex) {
		int position = 0;
		for (int i = 0; i < itemIndex; i++)
			position += lengthOfItem(i);
		return position;
	}

	protected boolean checkHasRemaining(TestingContext context, int itemIndex) {
		int remaining = context.buffer().remaining();
		if (remaining < lengthOfItem(itemIndex)) {
			context.addFailure(this, itemIndex, "Buffer underflow. Element needs %d additional bytes", lengthOfItem(itemIndex) - remaining);
			return false;
		}
		return true;
	}

	protected boolean checkEquals(TestingContext context, int itemIndex, Object a, Object b) {
		if (!Objects.equals(a, b)) {
			context.addFailure(this, itemIndex, "Value differs: %s != %s", formatValue(a), formatValue(b));
			return false;
		}
		return true;
	}
}
