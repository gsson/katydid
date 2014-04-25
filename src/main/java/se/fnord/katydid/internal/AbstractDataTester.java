package se.fnord.katydid.internal;

import se.fnord.katydid.DataTester;

import java.util.Objects;

public abstract class AbstractDataTester implements DataTester {

	protected abstract String formatValue(Object o);

	protected abstract int sizeOf(int itemIndex);

	protected abstract int itemCount();

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
