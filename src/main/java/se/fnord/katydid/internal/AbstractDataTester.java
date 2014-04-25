package se.fnord.katydid.internal;

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

	protected void compareToLevel0(TestingContext context) {
		throw new UnsupportedOperationException();
	}

	protected void doCompareTo(int pass, TestingContext context) {
		if (pass == 0)
			compareToLevel0(context);
		else
			skip(context);

	}

	@Override
	public final void compareTo(int pass, TestingContext context) {
		context.down(name);
		try {
			doCompareTo(pass, context);
		}
		finally {
			context.up();
		}
	}


	@Override
	public int maxPass() {
		return 0;
	}

	protected void skip(TestingContext context) {
		ByteBuffer bb = context.buffer();
		for (int i = 0; i < itemCount(); i++) {
			assertHasRemaining(context, i);
			bb.position(bb.position() + sizeOf(i));
		}
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

	protected void assertHasRemaining(TestingContext context, int itemIndex) {
		int remaining = context.buffer().remaining();
		if (remaining < sizeOf(itemIndex)) {
			throw new AssertionError(String.format("%s: Buffer underflows at %s. Element needs %d additional bytes.",
					formatName(context, itemIndex), formatLocation(context, itemIndex), length() - localPosition(itemIndex) - remaining));
		}
	}

	protected void assertEquals(TestingContext context, int itemIndex, Object a, Object b) {
		if (!Objects.equals(a, b)) {
			String message = String.format("%s: Value at %s differs: %s != %s",
					formatName(context, itemIndex), formatLocation(context, itemIndex), formatValue(a), formatValue(b));
			throw new AssertionError(message);
		}
	}
}
