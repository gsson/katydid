package se.fnord.katydid;

import se.fnord.katydid.internal.TestingContext;

import java.nio.ByteBuffer;

public class DataAsserts {

	public static void assertNext(DataTester dataTester, ByteBuffer bb) {
		int m = dataTester.passCount();
		TestingContext tc = new TestingContext(bb);
		int pos = bb.position();
		for (int i = 0; i < m; i++) {
			bb.position(pos);
			dataTester.compareTo(i, tc);
		}
	}

	public static void assertExact(DataTester dataTester, ByteBuffer bb) {
		ByteBuffer bb2 = bb.slice();
		assertNext(dataTester, bb2);
		if (bb2.hasRemaining())
			throw new AssertionError(String.format("Expected %d bytes, was %d bytes", dataTester.length(), bb.remaining()));
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
