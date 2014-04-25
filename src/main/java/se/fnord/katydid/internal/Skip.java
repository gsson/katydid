package se.fnord.katydid.internal;

import java.nio.ByteBuffer;

public class Skip extends AbstractDataTester {
	private final String name;
	private final int size;

	public Skip(String name, int size) {
		this.name = name;
		this.size = size;
	}

	@Override
	protected int sizeOf(int itemIndex) {
		return size;
	}

	@Override
	protected int itemCount() {
		return 1;
	}

	@Override
	public void compareTo(TestingContext context) {
		context.down(name);
		try {
			ByteBuffer bb = context.buffer();
			assertHasRemaining(context, size);
			for (int i = 0; i < size; i++)
				bb.get();
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
	public void toBuffer(ByteBuffer bb) {
		for (int i = 0; i < size; i++)
			bb.put((byte) 0);
	}

}
