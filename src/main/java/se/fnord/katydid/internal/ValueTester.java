package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.util.Objects;

import static se.fnord.katydid.ComparisonStatus.SKIP;

public abstract class ValueTester extends AbstractDataTester {
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
