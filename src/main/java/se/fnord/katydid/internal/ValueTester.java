package se.fnord.katydid.internal;

import java.util.Objects;

public abstract class ValueTester extends AbstractTester {
	public ValueTester(String name) {
		super(name);
	}

	protected String formatValue(Object v) {
		throw new UnsupportedOperationException();
	}

	protected boolean checkEquals(TestingContext context, int itemIndex, Object a, Object b) {
		if (!Objects.equals(a, b)) {
			context.addFailure("Value differs: %s != %s", formatValue(a), formatValue(b));
			return false;
		}
		return true;
	}
}
