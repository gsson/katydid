package se.fnord.katydid.examples;

import org.junit.Test;
import se.fnord.katydid.DataAsserts;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static se.fnord.katydid.DataAsserts.assertExact;
import static se.fnord.katydid.DataTesters.*;

public class SimpleTests {
	public static final int HEADER_LENGTH = 8 + 4;

	public static byte[] asBytes(Number ... values) {
		byte[] bytes = new byte[values.length];
		for (int i = 0; i < values.length; i++)
			bytes[i] = values[i].byteValue();
		return bytes;
	}

	public static ByteBuffer createTestData() {
		final ByteBuffer actual = ByteBuffer.allocate(16);
		actual.putLong(0x0123456789abcdefL);
		actual.putInt(16);
		actual.put(asBytes(1, 2, 3, 4));
		actual.flip();
		return actual;
	}

	public static ByteBuffer createInvalidTestData() {
		final ByteBuffer actual = ByteBuffer.allocate(16);
		actual.putLong(0xfedcba9876543210L);
		actual.putInt(17);
		actual.put(asBytes(2, 3, 4, 5));
		actual.flip();
		return actual;
	}

	public static DataTester magic(long value) {
		return h64("magic", value);
	}

	public static DataTester length(int length) {
		return u32("length", length);
	}

	public static DataTester data(Number ... values) {
		return u8("data", values);
	}

	public static DataTester block(DataTester ... testers) {
		return struct("block", testers);
	}

	public static DataTester header(int dataLength) {
		return struct("header",
			magic(0x0123456789abcdefL),
			length(HEADER_LENGTH + dataLength));
	}

	public static DataTester blockForData(Number ... data) {
		final DataTester body = data(data);
		final DataTester header = header(body.length());
		return block(header, body);
	}

	@Test
	public void testBlockHeader1() {
		final DataTester expected = struct(
				h64(0x0123456789abcdefL),
				u32(16),
				u8(1, 2, 3, 4)
		);

		assertExact(expected, createTestData());
	}

	@Test
	public void testBlockHeader2() {
		final DataTester expected = struct("block",
				h64("magic", 0x0123456789abcdefL),
				u32("length", 16),
				u8("data", 1, 2, 3, 4)
		);

		assertExact(expected, createTestData());
	}

	@Test
	public void testBlockHeader3() {
		final DataTester expected = struct(
			magic(0x0123456789abcdefL),
			length(16),
			data(1, 2, 3, 4)
		);

		assertExact(expected, createTestData());
	}

	@Test
	public void testBlockHeader4() {
		assertExact(blockForData(1, 2, 3, 4), createTestData());
	}

	@Test(expected=AssertionError.class)
	public void testInvalidData() {
		assertExact(blockForData(1, 2, 3, 4), createInvalidTestData());
	}
}
