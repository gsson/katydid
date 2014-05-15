package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.TestingContext;

import java.nio.ByteBuffer;

import static se.fnord.katydid.ComparisonStatus.CONTINUE;
import static se.fnord.katydid.internal.Util.mask;

public class IntTester extends ValueTester {
	public enum IntFormat {HEX, SIGNED, UNSIGNED};

	private final int elementWidth;
	private final Number[] values;
	private final IntFormatter formatter;

	public IntTester(String name, IntFormat format, int elementWidth, Number... values) {
		super(name);
		this.elementWidth = elementWidth;
		this.values = values;
		this.formatter = formatterFor(elementWidth, format);
	}

	private void write(ByteBuffer bb, long value) {
		Util.write(bb, elementWidth, value);
	}

	@Override
	public int lengthOfItem(int itemIndex) {
		return elementWidth;
	}

	@Override
	public int itemCount() {
		return values.length;
	}

	@Override
	protected ComparisonStatus compareItem0(TestingContext context, int itemIndex) {
		checkEquals(context, itemIndex, values[itemIndex].longValue(), context.read(elementWidth));
		return CONTINUE;
	}

	@Override
	public String formatValue(Object v) {
		return formatter.format(((Number) v).longValue());
	}

	@Override
	public String formatItem(String name, int index) {
		if (values.length == 1)
			return name;

		return String.format("%s[%d]", name, index);
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		for (final Number v : values) {
			write(bb, v.longValue());
		}
	}

	static interface IntFormatter {
		String format(long value);
	}

	static long signBit(int width) {
		return 1L << (width * 8 - 1);
	}

	static IntTester.IntFormatter formatterFor(int width, IntFormat format) {
		if (width == 0) {
			throw new IllegalArgumentException();
		}

		switch (format) {
		case HEX:
			return hexFormatterFor(width);
		case SIGNED:
			return signedFormatterFor(width);
		case UNSIGNED:
			return unsignedFormatterFor(width);
		}
		throw new IllegalArgumentException();
	}

	private static IntTester.IntFormatter hexFormatterFor(int width) {
		final long mask = mask(width);
		final String format = String.format("%%0%dx", width * 2);
		return new IntTester.IntFormatter() {
			@Override
			public String format(long value) {
				return String.format(format, value & mask);
			}
		};
	}

	private static IntTester.IntFormatter signedFormatterFor(final int width) {
		final long mask = mask(width);
		final long signBit = signBit(width);
		return new IntTester.IntFormatter() {
			@Override
			public String format(long value) {
				if (width < 8) {
					value &= mask;

					// Most hackish sign-extension for negative values
					if ((value & signBit) != 0L) {
						value = -(~(value - 1L) & mask);
					}
				}
				return Long.toString(value);
			}
		};
	}

	private static IntTester.IntFormatter unsignedFormatterFor(int width) {
		final long mask = mask(width);
		return new IntTester.IntFormatter() {
			@Override
			public String format(long value) {
				value &= mask;
				// The value is already unsigned, no magic needed.
				if (value >= 0)
					return Long.toString(value);

				// Unsigned divide by 10
				final long quotient = (value >>> 1) / 5;
				final long remainder = value - quotient * 10;
				return Long.toString(quotient) + remainder;
			}
		};
	}
}
