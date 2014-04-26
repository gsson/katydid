package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

public class Struct extends AbstractDataTester {
	private final DataTester[] values;

	public Struct(String name, DataTester... values) {
		super(name);
		this.values = values;
	}

	@Override
	public ComparisonStatus doCompareTo(int pass, TestingContext context) {
		ComparisonStatus status = ComparisonStatus.EQUAL;
		for (DataTester c : values) {
			status = ComparisonStatus.worst(c.compareTo(pass, context), status);
			if (status == ComparisonStatus.ERROR)
				break;
		}
		return status;
	}

	@Override
	public int passCount() {
		int mp = 0;
		for (DataTester c : values)
			mp = Math.max(mp, c.passCount());
		return mp;
	}

	@Override
	public String formatValue(Object v) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lengthOfItem(int itemIndex) {
		return values[itemIndex].length();
	}

	@Override
	public int itemCount() {
		return values.length;
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		for (DataTester c : values) {
			c.toBuffer(bb);
		}
	}
}
