package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;
import java.util.Objects;

import static se.fnord.katydid.ComparisonStatus.SKIP;

public abstract class AbstractDataTester implements DataTester {
	private final String name;
	private int length = -1;
	public AbstractDataTester(String name) {
		this.name = name;
	}

	public String formatChild(int itemIndex, DataTester child) {
		return String.format("[%d]", itemIndex);
	}

	@Override
	public String formatItem(String name, int itemIndex) {
		return String.format("%s[%d]", name, itemIndex);
	}

	@Override
	public ComparisonStatus compareItem(TestingContext context, int pass, int itemIndex) {
		if (pass == 0)
			return compareItem0(context, itemIndex);
		return SKIP;
	}

	protected ComparisonStatus compareItem0(TestingContext context, int itemIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int passCount() {
		return 1;
	}

	public int length() {
		if (length == -1)
			length = localPosition(itemCount());
		return length;
	}

	private int localPosition(int itemIndex) {
		int position = 0;
		for (int i = 0; i < itemIndex; i++)
			position += lengthOfItem(i);
		return position;
	}

	public String name() {
		return name;
	}
}
