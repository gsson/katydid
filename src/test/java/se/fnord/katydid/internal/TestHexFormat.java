package se.fnord.katydid.internal;

import org.junit.Test;
import se.fnord.katydid.internal.HexFormat;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.internal.HexFormat.format;
import static se.fnord.katydid.internal.HexFormat.formatOffset;
import static se.fnord.katydid.internal.HexFormat.hexLength;
import static se.fnord.katydid.internal.Util.bytes;
import static se.fnord.katydid.internal.Util.utf8;

public class TestHexFormat {

	private static String formatHexByte(int b) {
		StringBuilder sb = new StringBuilder();
		HexFormat.formatHexByte(sb, (byte) b);
		return sb.toString();
	}

	private static String formatLine(ByteBuffer bb, int offsetWidth) {
		StringBuilder sb = new StringBuilder();
		HexFormat.formatLine(sb, bb, offsetWidth);
		return sb.toString();
	}

	@Test
	public void testFormatLine() {
		assertEquals("00: 01 02 03 04                                      | ....             ", formatLine(bytes(1, 2, 3, 4), 2));
		assertEquals("000: 01 02 03 04                                      | ....             ", formatLine(bytes(1, 2, 3, 4), 3));

		assertEquals("00: 30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46 | 01234567 89ABCDEF", formatLine(utf8("0123456789ABCDEF"), 2));

		ByteBuffer bb = utf8("0123456789ABCDEF");
		bb.position(2);
		bb.limit(16 - 2);
		assertEquals("00:       32 33 34 35 36 37  38 39 41 42 43 44       |   234567 89ABCD  ", formatLine(bb, 2));
	}

	@Test
	public void testFormatBuffer() {
		ByteBuffer bb = utf8("0123456789ABCDEF0123456789ABCDEF");
		assertEquals(
				"00: 30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46 | 01234567 89ABCDEF\n" +
				"10: 30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46 | 01234567 89ABCDEF\n",
				format(bb)
		);
		bb.position(2);
		bb.limit(32 - 2);
		assertEquals(
				"00:       32 33 34 35 36 37  38 39 41 42 43 44 45 46 |   234567 89ABCDEF\n" +
				"10: 30 31 32 33 34 35 36 37  38 39 41 42 43 44       | 01234567 89ABCD  \n",
				format(bb)
		);
		bb.position(16);
		bb.limit(32);
		assertEquals(
				"10: 30 31 32 33 34 35 36 37  38 39 41 42 43 44 45 46 | 01234567 89ABCDEF\n",
				format(bb)
		);
	}

	@Test
	public void testFormatHexByte() {
		assertEquals("00", formatHexByte(0));
		assertEquals("08", formatHexByte(0x08));
		assertEquals("10", formatHexByte(0x10));
		assertEquals("80", formatHexByte(0x80));
		assertEquals("ff", formatHexByte(0xff));
	}

	@Test
	public void testFormatOffset() {
		assertEquals("00", formatOffset(2, 0));

		assertEquals("0000", formatOffset(4, 0));
		assertEquals("0001", formatOffset(4, 1));
		assertEquals("8000", formatOffset(4, 0x8000));
		assertEquals("80000", formatOffset(4, 0x80000));

		assertEquals("00000000", formatOffset(8, 0));
		assertEquals("7fffffff", formatOffset(8, Integer.MAX_VALUE));
	}

	@Test
	public void testHexLength() {
		assertEquals(1, hexLength(0x0));
		assertEquals(1, hexLength(0x1));
		assertEquals(1, hexLength(0xf));
		assertEquals(2, hexLength(0x80));
		assertEquals(3, hexLength(0x100));
		assertEquals(8, hexLength(Integer.MAX_VALUE));
		assertEquals(8, hexLength(-1));
		assertEquals(8, hexLength(Integer.MIN_VALUE));
	}
}
