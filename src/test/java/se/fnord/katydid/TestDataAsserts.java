package se.fnord.katydid;

import org.junit.Test;

import static se.fnord.katydid.DataTesters.h8;
import static se.fnord.katydid.DataTesters.struct;
import static se.fnord.katydid.internal.Util.bytes;

public class TestDataAsserts {
	@Test
	public void testH8() {
		DataAsserts.assertExact(h8("ap", 1, 2, 3, 4), bytes(1, 2, 3, 4));
	}

	@Test
	public void testHexBytes() {
		DataAsserts.assertExact(DataTesters.bytes("ap", "010203040a0B"), bytes(1, 2, 3, 4, 10, 11));
		DataAsserts.assertExact(DataTesters.bytes("ap", "1"), bytes(1));
	}

	@Test
	public void testStruct() {
		DataAsserts.assertExact(
			struct("struct",
				h8("ap", 1, 2, 3, 4)),
			bytes(1, 2, 3, 4));
	}

	@Test(expected = AssertionError.class)
	public void testUnderflow() {
		DataAsserts.assertExact(
			struct("struct",
				h8("ap", 1, 2, 3, 4)),
			bytes(1, 2, 3));
	}
}
