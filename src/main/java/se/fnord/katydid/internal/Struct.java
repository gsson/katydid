package se.fnord.katydid.internal;

import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

public class Struct extends AbstractDataTester {
	private final DataTester[] values;

	public Struct(String name, DataTester... values) {
		super(name);
		this.values = values;
	}

	@Override
	public void doCompareTo(int pass, TestingContext context) {
		for (DataTester c : values) {
			c.compareTo(pass, context);
		}
	}

	@Override
	public int maxPass() {
		int mp = 0;
		for (DataTester c : values)
			mp = Math.max(mp, c.maxPass());
		return mp;
	}

	@Override
	public String formatValue(Object v) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected int sizeOf(int itemIndex) {
		return values[itemIndex].length();
	}

	@Override
	protected int itemCount() {
		return values.length;
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		for (DataTester c : values) {
			c.toBuffer(bb);
		}
	}
}
