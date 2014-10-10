package se.fnord.katydid;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.DataTesters.*;
import static se.fnord.katydid.DataTesters.struct;
import static se.fnord.katydid.internal.Util.bytes;

public class TestDataAsserts {
	@Test
	public void testH8() {
		DataAsserts.assertExact(h8("ap", 1, 2, 3, 4), bytes(1, 2, 3, 4));
	}

	@Test
	public void testHexBytes() {
		DataAsserts.assertExact(DataTesters.hex("ap", "010203040a0B"), bytes(1, 2, 3, 4, 10, 11));
		DataAsserts.assertExact(DataTesters.hex("ap", "1"), bytes(1));
	}

	@Test
	public void testStruct() {
		DataAsserts.assertExact(
			struct("struct",
				h8("ap", 1, 2, 3, 4)),
			bytes(1, 2, 3, 4));
	}

	@Test
	public void testAssertExactByteOrderWithBuffer() {
		ByteBuffer bigEndian = ByteBuffer.allocate(4);
		bigEndian.order(ByteOrder.BIG_ENDIAN);
		bigEndian.putInt(0x12345678);
		bigEndian.flip();
		DataAsserts.assertExact(h32(0x12345678), bigEndian);

		ByteBuffer littleEndian = ByteBuffer.allocate(4);
		littleEndian.order(ByteOrder.LITTLE_ENDIAN);
		littleEndian.putInt(0x12345678);
		littleEndian.flip();
		DataAsserts.assertExact(h32(0x12345678), littleEndian);
	}

	@Test
	public void testAssertExactByteOrderWithByteArray() {
		DataAsserts.assertExact(h32(0x12345678), ByteOrder.BIG_ENDIAN, new byte[] { 0x12, 0x34, 0x56, 0x78 });
		DataAsserts.assertExact(h32(0x12345678), ByteOrder.LITTLE_ENDIAN, new byte[] { 0x78, 0x56, 0x34, 0x12 });
	}

	@Test
	public void testAsBuffer() {
		ByteBuffer bigEndian = ByteBuffer.wrap(new byte[] { 0x12, 0x34, 0x56, 0x78 });
		bigEndian.order(ByteOrder.BIG_ENDIAN);
		assertEquals(bigEndian, DataAsserts.asBuffer(ByteOrder.BIG_ENDIAN, h32(0x12345678)));

		ByteBuffer littleEndian = ByteBuffer.wrap(new byte[] { 0x78, 0x56, 0x34, 0x12 });
		littleEndian.order(ByteOrder.LITTLE_ENDIAN);
		assertEquals(littleEndian, DataAsserts.asBuffer(ByteOrder.LITTLE_ENDIAN, h32(0x12345678)));
	}

	@Test(expected = AssertionError.class)
	public void testUnderflow() {
		DataAsserts.assertExact(
			struct("struct",
				h8("ap", 1, 2, 3, 4)),
			bytes(1, 2, 3));
	}
}
