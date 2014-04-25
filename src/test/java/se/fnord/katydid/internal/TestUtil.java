package se.fnord.katydid.internal;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class TestUtil {
	@Test
	public void testReadLittleEndian() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.putLong(0x1122334455667788L);
		bb.flip();
		assertEquals(0x1122334455667788L, Util.read(bb, 8));
	}

	@Test
	public void testReadBigEndian() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putLong(0x1122334455667788L);
		bb.flip();
		assertEquals(0x1122334455667788L, Util.read(bb, 8));
	}

	@Test
	public void testWriteLittleEndian() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		Util.write(bb, 8, 0x1122334455667788L);
		bb.flip();
		assertEquals(0x1122334455667788L, bb.getLong());
	}

	@Test
	public void testWriteBigEndian() {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.BIG_ENDIAN);
		Util.write(bb, 8, 0x1122334455667788L);
		bb.flip();
		assertEquals(0x1122334455667788L, bb.getLong());
	}

}
