package se.fnord.katydid.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.internal.Util.bytes;

public class TestDefer {
	@Test
	public void testDeferPasses() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 2);
		Defer defer = new Defer(s8);

		assertEquals(defer.length(), s8.length());
		assertEquals(defer.passCount(), s8.passCount() + 1);

		defer.compareTo(0, new TestingContext(bytes(1)));
		defer.compareTo(2, new TestingContext(bytes(1)));
	}

	@Test(expected=AssertionError.class)
	public void testDeferFails() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 2);
		Defer defer = new Defer(s8);

		defer.compareTo(1, new TestingContext(bytes(1)));
	}

}