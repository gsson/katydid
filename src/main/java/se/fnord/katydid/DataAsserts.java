package se.fnord.katydid;

import se.fnord.katydid.internal.TestingContextImpl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DataAsserts {
	private static void assertNext(TestingContext tc, DataTester dataTester, ByteBuffer bb) {
		int m = dataTester.passCount();
		int pos = bb.position();
		for (int i = 0; i < m; i++) {
			bb.position(pos);
			if (tc.compareTo(dataTester, i) == ComparisonStatus.ABORT)
				break;
		}
	}

	/**
	 * Asserts that the next sequence of bytes matches the tester.
	 * On successful return, the position of {@code bb} is positioned on {@code bb.position() + tester.length()}. The position is undefined in the case of an error.
	 *
	 * @throws java.lang.AssertionError if a value does not match or {@code bb} contains too few bytes.
	 * @param tester the tester to use when verifying the buffer.
	 * @param bb the ByteBuffer to test
	 * @return {@code bb}
	 */
	public static ByteBuffer assertNext(DataTester tester, ByteBuffer bb) {
		TestingContextImpl tc = new TestingContextImpl(bb);
		assertNext(tc, tester, bb);
		tc.assertSuccess(tester);
		return bb;
	}

	/**
	 * Asserts that the remaining bytes of {@code bb} exactly matches the tester.
	 * {@code bb} is not modified.
	 *
	 * @throws java.lang.AssertionError if a value does not match or {@code bb} does not contain exactly {@code tester.length()} bytes.
	 * @param tester the tester to use when verifying the buffer.
	 * @param bb the ByteBuffer to test
	 */
	public static void assertExact(DataTester tester, ByteBuffer bb) {
		ByteBuffer bb2 = bb.slice().order(bb.order());
		TestingContextImpl tc = new TestingContextImpl(bb2);
		if (bb2.remaining() != tester.length())
			tc.addFailure("%d bytes data expected, was %d bytes", tester.length(), bb.remaining());
		assertNext(tc, tester, bb2);
		tc.assertSuccess(tester);
	}

	/**
	 * Asserts that {@code bytes} exactly matches the tester.
	 * {@code bytes} is treated as being big endian.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     assertExact(tester, ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN))
	 * </code>
	 *
	 * @throws java.lang.AssertionError if a value does not match or {@code bb} does not contain exactly {@code tester.length()} bytes.
	 * @param tester the tester to use when verifying the buffer.
	 * @param bytes the bytes to test
	 */
	public static void assertExact(DataTester tester, byte[] bytes) {
		assertExact(tester, ByteOrder.BIG_ENDIAN, bytes);
	}

	/**
	 * Asserts that {@code bytes} exactly matches the tester.
	 * {@code bytes} is treated as being big endian.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     assertExact(tester, ByteBuffer.wrap(bytes))
	 * </code>
	 *
	 * @throws java.lang.AssertionError if a value does not match or {@code bb} does not contain exactly {@code tester.length()} bytes.
	 * @param tester the tester to use when verifying the buffer.
	 * @param bytes the bytes to test
	 */
	public static void assertExact(DataTester tester, ByteOrder order, byte[] bytes) {
		assertExact(tester, ByteBuffer.wrap(bytes).order(order));
	}

	/**
	 * Creates a ByteBuffer with contents that will match {@code tester} when used as input to {@code assertExact()}
	 *
	 * @param tester the tester to use when generating the buffer.
	 * @return the ByteBuffer containing the generated data.
	 */
	public static ByteBuffer asBuffer(DataTester tester) {
		int length = tester.length();
		ByteBuffer bb = ByteBuffer.allocate(length);
		tester.toBuffer(bb);
		bb.flip();
		return bb;
	}

	/**
	 * Creates a ByteBuffer with contents that will match {@code tester} when used as input to {@code assertExact()}
	 *
	 * @param order the byte order to use when generating the buffer.
	 * @param tester the tester to use when generating the buffer.
	 * @return the ByteBuffer containing the generated data.
	 */
	public static ByteBuffer asBuffer(ByteOrder order, DataTester tester) {
		int length = tester.length();
		ByteBuffer bb = ByteBuffer.allocate(length).order(order);
		tester.toBuffer(bb);
		bb.flip();
		return bb;
	}
}
