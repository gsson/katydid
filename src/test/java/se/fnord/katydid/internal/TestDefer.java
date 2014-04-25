package se.fnord.katydid.internal;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TestDefer {
	@Test
	public void testDeferPasses() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 2);
		Defer defer = new Defer(s8);

		assertEquals(defer.length(), s8.length());
		assertEquals(defer.maxPass(), s8.maxPass() + 1);

		defer.compareTo(0, new TestingContext(TestInt.bytes(1)));
		defer.compareTo(2, new TestingContext(TestInt.bytes(1)));
	}

	@Test(expected=AssertionError.class)
	public void testDeferFails() {
		Int s8 = new Int("s8", Int.IntFormat.SIGNED, 1, 2);
		Defer defer = new Defer(s8);

		defer.compareTo(1, new TestingContext(TestInt.bytes(1)));
	}

}
