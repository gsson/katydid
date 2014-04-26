package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;

import java.nio.ByteBuffer;

public class Skip extends AbstractDataTester {
	private final int size;

	public Skip(String name, int size) {
		super(name);
		this.size = size;
	}

	@Override
	public int lengthOfItem(int itemIndex) {
		return size;
	}

	@Override
	public int itemCount() {
		return 1;
	}

	@Override
	public ComparisonStatus compareToLevel0(TestingContext context) {
		return skip(context);
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
