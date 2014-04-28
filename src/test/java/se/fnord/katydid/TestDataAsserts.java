package se.fnord.katydid;

import org.junit.Test;

import java.nio.ByteBuffer;

import static se.fnord.katydid.internal.Util.bytes;
import static se.fnord.katydid.DataTesters.h8;
import static se.fnord.katydid.DataTesters.struct;

public class TestDataAsserts {
	@Test
	public void testH8() {
		DataAsserts.assertExact(h8("ap", 1, 2, 3, 4), bytes(1, 2, 3, 4));
	}


	@Test
	public void testStruct() {
		DataAsserts.assertExact(
				struct("struct",
						h8("ap", 1, 2, 3, 4)),
				bytes(1, 2, 3, 4));
	}
}