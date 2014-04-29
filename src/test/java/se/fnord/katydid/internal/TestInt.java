package se.fnord.katydid.internal;

import org.junit.Test;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.internal.Util.bytes;

public class TestInt {
	private static final void assertSuccess(int pass, DataTester tester, ByteBuffer bb) {
		TestingContextImpl tc = new TestingContextImpl(bb);
		tc.compareTo(tester, pass);
		tc.assertSuccess(tester);
	}

	@Test
	public void testFormatItem() {
		IntTester s8 = new IntTester("fnord", IntTester.IntFormat.SIGNED, 1, 1, 2, 3, 4);
		assertEquals("fnord[0]", s8.formatItem("fnord", 0));
		assertEquals("fnord[3]", s8.formatItem("fnord", 3));

		s8 = new IntTester("fnord", IntTester.IntFormat.SIGNED, 1, 1);
		assertEquals("fnord", s8.formatItem("fnord", 0));
	}

	@Test
	public void testFormatU8() {
		final IntTester.IntFormatter formatter = IntTester.formatterFor(1, IntTester.IntFormat.UNSIGNED);

		assertEquals("0", formatter.format(0x00));
		assertEquals("255", formatter.format(0xff));
		assertEquals("255", formatter.format(0x1ff));
	}

	@Test
	public void testFormatS8() {
		final IntTester.IntFormatter formatter = IntTester.formatterFor(1, IntTester.IntFormat.SIGNED);

		assertEquals("0", formatter.format(0));
		assertEquals("127", formatter.format(0x7f));
		assertEquals("-1", formatter.format(0xff));
		assertEquals("-128", formatter.format(0x80));

		assertEquals("-1", formatter.format(0x1ff));
	}

	@Test
	public void testFormatH8() {
		final IntTester.IntFormatter formatter = IntTester.formatterFor(1, IntTester.IntFormat.HEX);

		assertEquals("00", formatter.format(0x00));
		assertEquals("7f", formatter.format(0x7f));
		assertEquals("ff", formatter.format(0xff));
		assertEquals("80", formatter.format(0x80));

		assertEquals("ff", formatter.format(0x1ff));
	}

	@Test
	public void testFormatS64() {
		final IntTester.IntFormatter formatter = IntTester.formatterFor(8, IntTester.IntFormat.SIGNED);

		assertEquals("0", formatter.format(0));
		assertEquals("9223372036854775807", formatter.format(Long.MAX_VALUE));
		assertEquals("-1", formatter.format(-1L));
		assertEquals("-9223372036854775808", formatter.format(Long.MIN_VALUE));
	}

	@Test
	public void testFormatU64() {
		final IntTester.IntFormatter formatter = IntTester.formatterFor(8, IntTester.IntFormat.UNSIGNED);

		assertEquals("0", formatter.format(0));
		assertEquals("9223372036854775807", formatter.format(Long.MAX_VALUE));
		assertEquals("18446744073709551615", formatter.format(-1L));
		assertEquals("9223372036854775808", formatter.format(Long.MIN_VALUE));
	}

	@Test
	public void testFormatH64() {
		final IntTester.IntFormatter formatter = IntTester.formatterFor(8, IntTester.IntFormat.HEX);

		assertEquals("0000000000000000", formatter.format(0));
		assertEquals("7fffffffffffffff", formatter.format(Long.MAX_VALUE));
		assertEquals("ffffffffffffffff", formatter.format(-1L));
		assertEquals("8000000000000000", formatter.format(Long.MIN_VALUE));
	}

	@Test
	public void testInt8Succeeds() {
		IntTester s8 = new IntTester("s8", IntTester.IntFormat.SIGNED, 1, 1, 2, 3, 4);
		assertEquals(4, s8.length());
		assertSuccess(0, s8, bytes(1, 2, 3, 4));
	}

	@Test
	public void testInt8SucceedsOnOtherPasses() {
		IntTester s8 = new IntTester("s8", IntTester.IntFormat.SIGNED, 1, 1, 2, 3, 4);
		assertEquals(4, s8.length());
		assertSuccess(-1, s8, bytes(1, 2, 3, 5));
		assertSuccess(1, s8, bytes(1, 2, 3, 5));
	}

	@Test(expected = AssertionError.class)
	public void testInt8Fails() {
		IntTester s8 = new IntTester("s8", IntTester.IntFormat.SIGNED, 1, 1, 2, 3, 4);
		assertEquals(4, s8.length());
		assertSuccess(0, s8, bytes(1, 2, 3, 5));
	}
}
