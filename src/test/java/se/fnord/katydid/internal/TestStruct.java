package se.fnord.katydid.internal;

import org.junit.Test;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static se.fnord.katydid.DataAsserts.assertExact;
import static se.fnord.katydid.DataTesters.u8;
import static se.fnord.katydid.internal.Util.bytes;

public class TestStruct {
	@Test(expected=IllegalArgumentException.class)
	public void testFormatNonExistingItem() {
		StructTester l = new StructTester("fnord", u8("a", 1), u8("b", 2));
		l.formatItem("fnord", 3);
	}

	@Test
	public void testFormatItem() {
		StructTester l = new StructTester("fnord", u8("a", 1), u8("b", 2));
		assertEquals("fnord.a", l.formatItem("fnord", 0));

		l = new StructTester("fnord", u8("a", 1));
		assertEquals("fnord.a", l.formatItem("fnord", 0));
	}

	@Test(expected=AssertionError.class)
	public void testFailingSubItem() {
		StructTester l = new StructTester("fnord", u8(1));
		assertExact(l, bytes(2));
	}

	@Test(expected=AssertionError.class)
	public void testFailingLength() {
		StructTester l = new StructTester("fnord", u8(1), u8(2));
		assertExact(l, bytes(1));
	}

	public void testStruct() {
		StructTester l = new StructTester("fnord", u8(1));
		assertExact(l, bytes(1));
	}
}
