package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;

import java.nio.ByteBuffer;

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
	public ComparisonStatus compareToLevel0(TestingContext context) {
		ByteBuffer bb = context.buffer();
		if (!checkHasRemaining(context, values.length))
			return ComparisonStatus.ERROR;
		for (int i = 0; i < values.length; i++) {
			if (!checkEquals(context, i, values[i], bb.get()))
				return ComparisonStatus.NOT_EQUAL;
		}
		return ComparisonStatus.EQUAL;
	}

	@Override
	public String formatName(TestingContext context, int index) {
		return String.format("%s[%d]", context.name(), index);
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
