package se.fnord.katydid.internal;

import java.nio.ByteBuffer;

public class Bytes extends AbstractDataTester {
	private final byte[] values;

	public Bytes(String name, byte... values) {
		super(name);
		this.values = values;
	}

	@Override
	protected int sizeOf(int itemIndex) {
		return 1;
	}

	@Override
	protected int itemCount() {
		return values.length;
	}

	@Override
	public void compareToLevel0(TestingContext context) {
		ByteBuffer bb = context.buffer();
		assertHasRemaining(context, values.length);
		for (int i = 0; i < values.length; i++) {
			assertEquals(context, i, values[i], bb.get());
		}
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
