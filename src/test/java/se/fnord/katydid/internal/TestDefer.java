package se.fnord.katydid.internal;

import org.junit.Test;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.internal.Util.bytes;

public class TestDefer {
	private static final void assertSuccess(int pass, DataTester tester, ByteBuffer bb) {
		TestingContext tc = new TestingContext(bb);
		tester.compareTo(pass, tc);
		tc.assertSuccess(tester);
	}

	@Test
	public void testDeferPasses() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 2);
		Defer defer = new Defer(s8);

		assertEquals(defer.length(), s8.length());
		assertEquals(defer.passCount(), s8.passCount() + 1);
		assertSuccess(0, defer, bytes(1));
		assertSuccess(2, defer, bytes(1));
	}

	@Test(expected=AssertionError.class)
	public void testDeferFails() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 2);
		Defer defer = new Defer(s8);
		assertSuccess(1, defer, bytes(1));
	}

}