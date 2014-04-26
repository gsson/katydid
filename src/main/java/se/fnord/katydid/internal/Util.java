package se.fnord.katydid.internal;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class Util {
	private static final Charset UTF8 = Charset.forName("utf-8");
	private Util() {
		throw new IllegalAccessError();
	}

	private static long swap(int elementWidth, long value) {
		return Long.reverseBytes(value) >>> (8 * (8 - elementWidth));
	}

	public static long read(ByteBuffer bb, int elementWidth) {
		final long value = readBE(bb, elementWidth);
		if (bb.order() == ByteOrder.BIG_ENDIAN) {
			return value;
		}
		return swap(elementWidth, value);
	}

	private static long readBE(ByteBuffer bb, int elementWidth) {
		final int p = bb.position();
		long value = 0;
		for (int j = 0; j < elementWidth; j++) {
			value = (value << 8) | (bb.get(p + j) & 0xff);
		}
		bb.position(p + elementWidth);
		return value;
	}

	public static void write(ByteBuffer bb, int elementWidth, long value) {
		if (bb.order() == ByteOrder.BIG_ENDIAN) {
			value = swap(elementWidth, value);
		}
		writeBE(bb, elementWidth, value);
	}

	private static void writeBE(ByteBuffer bb, int elementWidth, long value) {
		final int p = bb.position();
		for (int j = 0; j < elementWidth; j++) {
			bb.put(p + j, (byte) value);
			value >>>= 8;
		}
		bb.position(p + elementWidth);
	}

	public static ByteBuffer utf8(String string) {
		return UTF8.encode(string);
	}

	public static ByteBuffer bytes(Number... vv) {
		ByteBuffer bb = ByteBuffer.allocate(vv.length);
		for (Number v : vv) {
			bb.put(v.byteValue());
		}
		bb.flip();
		return bb;
	}

	public static ByteBuffer shorts(Number... vv) {
		ByteBuffer bb = ByteBuffer.allocate(vv.length * Short.SIZE / Byte.SIZE);
		for (Number v : vv) {
			bb.putShort(v.shortValue());
		}
		bb.flip();
		return bb;
	}
}
