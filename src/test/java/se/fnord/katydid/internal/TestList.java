package se.fnord.katydid.internal;

import org.junit.Test;
import se.fnord.katydid.DataTester;
import se.fnord.katydid.DataTesters;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.DataAsserts.assertExact;
import static se.fnord.katydid.DataTesters.u8;
import static se.fnord.katydid.internal.Util.bytes;

public class TestList {
	@Test(expected=IllegalArgumentException.class)
	public void testFormatNonExistingItem() {
		ListTester l = new ListTester("fnord", u8(1), u8(2));
		l.formatItem("fnord", 3);
	}

	@Test
	public void testFormatItem() {
		ListTester l = new ListTester("fnord", u8(1), u8(2));
		assertEquals("fnord[0]", l.formatItem("fnord", 0));

		l = new ListTester("fnord", u8(1));
		assertEquals("fnord[0]", l.formatItem("fnord", 0));
	}

	@Test(expected=AssertionError.class)
	public void testFailingSubItem() {
		ListTester l = new ListTester("fnord", u8(1));
		assertExact(l, bytes(2));
	}

	@Test(expected=AssertionError.class)
	public void testFailingLength() {
		ListTester l = new ListTester("fnord", u8(1), u8(2));
		assertExact(l, bytes(1));
	}

	public void testList() {
		ListTester l = new ListTester("fnord", u8(1));
		assertExact(l, bytes(1));
	}
}
