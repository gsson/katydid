package se.fnord.katydid;

import se.fnord.katydid.internal.TestingContext;

import java.nio.ByteBuffer;

public class DataAsserts {

	public static void assertStartsWith(DataTester dataTester, ByteBuffer bb) {
		dataTester.compareTo(new TestingContext(bb));
	}

	public static void assertEquals(DataTester dataTester, ByteBuffer bb) {
		assertStartsWith(dataTester, bb);
		if (dataTester.length() != bb.remaining())
			throw new AssertionError(String.format("Expected %d bytes, was %d bytes", dataTester.length(), bb.remaining()));
	}

	public static void assertStartsWith(DataTester dataTester, byte[] bytes) {
		assertStartsWith(dataTester, bytes);
		if (dataTester.length() != bytes.length)
			throw new AssertionError(String.format("Expected %d bytes, was %d bytes", dataTester.length(), bytes.length));
	}
}
