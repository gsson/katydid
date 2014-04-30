package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;
import se.fnord.katydid.TestingContext;

import java.nio.ByteBuffer;

import static java.lang.Math.max;

public abstract class CompositeTester extends AbstractTester {
	private final DataTester[] values;

	public CompositeTester(String name, DataTester... values) {
		super(name);
		this.values = values;
	}

	@Override
	public ComparisonStatus compareItem(TestingContext context, int pass, int itemIndex) {
		return context.compareTo(values[itemIndex], pass);
	}

	@Override
	public int passCount() {
		int mp = 0;
		for (DataTester c : values)
			mp = max(mp, c.passCount());
		return mp;
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
	public String formatItem(String name, int itemIndex) {
		checkItemIndex(itemIndex);
		return name + formatChild(itemIndex, values[itemIndex]);
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		for (DataTester c : values) {
			c.toBuffer(bb);
		}
	}
}
