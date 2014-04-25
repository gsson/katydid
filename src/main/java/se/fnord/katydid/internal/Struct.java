package se.fnord.katydid.internal;

import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

public class Struct extends AbstractDataTester {
	private final String name;
	private final DataTester[] values;

	public Struct(String name, DataTester... values) {
		this.name = name;
		this.values = values;
	}

	@Override
	public void compareTo(TestingContext context) {
		context.down(name);
		try {
			for (DataTester c : values) {
				c.compareTo(context);
			}
		}
		finally {
			context.up();
		}
	}

	@Override
	public String formatName(TestingContext context, int index) {
		return context.name();
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
