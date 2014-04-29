package se.fnord.katydid;

import se.fnord.katydid.internal.TestingContextImpl;

import java.nio.ByteBuffer;

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

	public static void assertNext(DataTester dataTester, ByteBuffer bb) {
		TestingContextImpl tc = new TestingContextImpl(bb);
		assertNext(tc, dataTester, bb);
		tc.assertSuccess(dataTester);
	}

	public static void assertExact(DataTester dataTester, ByteBuffer bb) {
		ByteBuffer bb2 = bb.slice();
		TestingContextImpl tc = new TestingContextImpl(bb2);
		if (bb2.remaining() != dataTester.length())
			tc.addFailure("%d bytes data expected, was %d bytes", dataTester.length(), bb.remaining());
		assertNext(tc, dataTester, bb2);
		tc.assertSuccess(dataTester);
	}

	public static void assertExact(DataTester dataTester, byte[] bytes) {
		assertExact(dataTester, ByteBuffer.wrap(bytes));
	}

	public static ByteBuffer asBuffer(DataTester dataTester) {
		int length = dataTester.length();
		ByteBuffer bb = ByteBuffer.allocate(length);
		dataTester.toBuffer(bb);
		bb.flip();
		return bb;
	}
}
