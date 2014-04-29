package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static se.fnord.katydid.ComparisonStatus.ABORT;
import static se.fnord.katydid.ComparisonStatus.CONTINUE;
import static se.fnord.katydid.ComparisonStatus.SKIP;

public class Bytes extends AbstractDataTester {
	private final byte[] values;

	public Bytes(String name, byte... values) {
		super(name);
		this.values = values;
	}

	@Override
	public int lengthOfItem(int itemIndex) {
		return 1;
	}

	@Override
	public int itemCount() {
		return values.length;
	}

	@Override
	public ComparisonStatus compareItem0(TestingContext context, int itemIndex) {
		if (!checkEquals(context, itemIndex, values[itemIndex], context.read()))
			return SKIP;
		return CONTINUE;
	}

	@Override
	public String formatValue(Object v) {
		return String.format("%02x", v);
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		bb.put(values);
	}
}
