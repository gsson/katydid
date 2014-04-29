package se.fnord.katydid.internal;

import org.junit.Test;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.internal.Util.bytes;

public class TestDefer {
	private static final void assertSuccess(int pass, DataTester tester, ByteBuffer bb) {
		TestingContextImpl tc = new TestingContextImpl(bb);
		tc.compareTo(tester, pass);
		tc.assertSuccess(tester);
	}

	@Test
	public void testDeferPasses() {
		IntTester s8 = new IntTester("s8", IntTester.IntFormat.SIGNED, 1, 2);
		DeferringTesterModifier defer = new DeferringTesterModifier(s8);

		assertEquals(defer.length(), s8.length());
		assertEquals(defer.passCount(), s8.passCount() + 1);
		assertSuccess(0, defer, bytes(1));
		assertSuccess(2, defer, bytes(1));
	}

	@Test(expected=AssertionError.class)
	public void testDeferFails() {
		IntTester s8 = new IntTester("s8", IntTester.IntFormat.SIGNED, 1, 2);
		DeferringTesterModifier defer = new DeferringTesterModifier(s8);
		assertSuccess(1, defer, bytes(1));
	}

}
