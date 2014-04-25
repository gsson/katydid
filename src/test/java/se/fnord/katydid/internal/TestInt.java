package se.fnord.katydid.internal;

import org.junit.Test;

import java.nio.ByteBuffer;

import static junit.framework.Assert.assertEquals;

public class TestInt {
	@Test
	public void testFormatU8() {
		final Int.IntFormatter formatter = Int.formatterFor(1, Int.IntFormat.UNSIGNED);

		assertEquals("0", formatter.format(0x00));
		assertEquals("255", formatter.format(0xff));
		assertEquals("255", formatter.format(0x1ff));
	}

	@Test
	public void testFormatS8() {
		final Int.IntFormatter formatter = Int.formatterFor(1, Int.IntFormat.SIGNED);

		assertEquals("0", formatter.format(0));
		assertEquals("127", formatter.format(0x7f));
		assertEquals("-1", formatter.format(0xff));
		assertEquals("-128", formatter.format(0x80));

		assertEquals("-1", formatter.format(0x1ff));
	}

	@Test
	public void testFormatH8() {
		final Int.IntFormatter formatter = Int.formatterFor(1, Int.IntFormat.HEX);

		assertEquals("00", formatter.format(0x00));
		assertEquals("7f", formatter.format(0x7f));
		assertEquals("ff", formatter.format(0xff));
		assertEquals("80", formatter.format(0x80));

		assertEquals("ff", formatter.format(0x1ff));
	}

	@Test
	public void testFormatS64() {
		final Int.IntFormatter formatter = Int.formatterFor(8, Int.IntFormat.SIGNED);

		assertEquals("0", formatter.format(0));
		assertEquals("9223372036854775807", formatter.format(Long.MAX_VALUE));
		assertEquals("-1", formatter.format(-1L));
		assertEquals("-9223372036854775808", formatter.format(Long.MIN_VALUE));
	}

	@Test
	public void testFormatU64() {
		final Int.IntFormatter formatter = Int.formatterFor(8, Int.IntFormat.UNSIGNED);

		assertEquals("0", formatter.format(0));
		assertEquals("9223372036854775807", formatter.format(Long.MAX_VALUE));
		assertEquals("18446744073709551615", formatter.format(-1L));
		assertEquals("9223372036854775808", formatter.format(Long.MIN_VALUE));
	}

	@Test
	public void testFormatH64() {
		final Int.IntFormatter formatter = Int.formatterFor(8, Int.IntFormat.HEX);

		assertEquals("0000000000000000", formatter.format(0));
		assertEquals("7fffffffffffffff", formatter.format(Long.MAX_VALUE));
		assertEquals("ffffffffffffffff", formatter.format(-1L));
		assertEquals("8000000000000000", formatter.format(Long.MIN_VALUE));
	}

	private static ByteBuffer bytes(Number... vv) {
		ByteBuffer bb = ByteBuffer.allocate(vv.length);
		for (Number v : vv) {
			bb.put(v.byteValue());
		}
		bb.flip();
		return bb;
	}

	private static ByteBuffer shorts(Number... vv) {
		ByteBuffer bb = ByteBuffer.allocate(vv.length * Short.SIZE / Byte.SIZE);
		for (Number v : vv) {
			bb.putShort(v.shortValue());
		}
		bb.flip();
		return bb;
	}

	@Test
	public void testInt8Succeeds() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 1, 2, 3, 4);
		assertEquals(4, s8.length());
		s8.compareTo(new TestingContext(bytes(1, 2, 3, 4)));
	}

	@Test(expected = AssertionError.class)
	public void testInt8Fails() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 1, 2, 3, 4);
		assertEquals(4, s8.length());
		s8.compareTo(new TestingContext(bytes(1, 2, 3, 5)));
	}
}
