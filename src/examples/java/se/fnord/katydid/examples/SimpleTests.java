package se.fnord.katydid.examples;

import org.junit.Test;
import se.fnord.katydid.DataAsserts;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static se.fnord.katydid.DataTesters.*;

public class SimpleTests {
	public static byte[] asBytes(Number ... values) {
		byte[] bytes = new byte[values.length];
		for (int i = 0; i < values.length; i++)
			bytes[i] = values[i].byteValue();
		return bytes;
	}

	public ByteBuffer createTestData() {
		final ByteBuffer actual = ByteBuffer.allocate(16);
		actual.putInt(16);
		actual.putLong(0x0123456789abcdefL);
		actual.put(asBytes(1, 2, 3, 4));
		actual.flip();
		return actual;
	}

	@Test
	public void testBlockHeader1() {
		final DataTester expected = struct(
				u32(16),
				h64(0x0123456789abcdefL),
				u8(1, 2, 3, 4)
		);

		final ByteBuffer actual = createTestData();

		DataAsserts.assertExact(expected, actual);
	}

	@Test
	public void testBlockHeader2() {
		final DataTester expected = struct("blockHeader",
				u32("length", 16),
				h64("magic", 0x0123456789abcdefL),
				u8("data", 1, 2, 3, 4)
		);

		final ByteBuffer actual = createTestData();

		DataAsserts.assertExact(expected, actual);
	}

	public DataTester length(int length) {
		return u32("length", length);
	}

	public DataTester blockHeader(DataTester ... testers) {
		return struct("blockHeader", testers);
	}

	public DataTester magic(long value) {
		return h64("magic", value);
	}

	public DataTester data(Number ... values) {
		return u8("data", values);
	}

	@Test
	public void testBlockHeader3() {
		final DataTester expected = blockHeader(
				length(16),
				magic(0x0123456789abcdefL),
				data(1, 2, 3, 4)
		);

		final ByteBuffer actual = createTestData();

		DataAsserts.assertExact(expected, actual);
	}

	@Test
	public void testBlockHeader4() {
		final DataTester expected = blockHeader(
				defer(length(16)),
				magic(0x0123456789abcdefL),
				data(1, 2, 3, 4)
		);

		final ByteBuffer actual = createTestData();

		DataAsserts.assertExact(expected, actual);
	}
}
