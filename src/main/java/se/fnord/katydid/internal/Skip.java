package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;

import java.nio.ByteBuffer;

import static se.fnord.katydid.ComparisonStatus.SKIP;

public class Skip extends AbstractTester {
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
	protected ComparisonStatus compareItem0(TestingContext context, int itemIndex) {
		return SKIP;
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		for (int i = 0; i < size; i++)
			bb.put((byte) 0);
	}

}
