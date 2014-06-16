package se.fnord.katydid.internal;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class HexFormat {
	private static final char[] ZEROES = "00000000".toCharArray();
	private static final char[] PRINTABLE = generatePrintable();

	private static char[] generatePrintable() {
		char[] printable = new char[256];
		Arrays.fill(printable, '.');
		for (int i = 0x21; i < 0x7f; i++)
			printable[i] = (char) i;
		return printable;
	}

	static int hexLength(int offset) {
		if (offset == 0)
			return 1;
		return 8 - (Integer.numberOfLeadingZeros(offset) >>> 2);
	}

	static String formatOffset(int width, int offset) {
		StringBuilder sb = new StringBuilder();
		HexFormat.formatOffset(sb, width, offset);
		return sb.toString();
	}

	private static int lineOffset(ByteBuffer bb) {
		return bb.position() & ~0xf;
	}

	static void formatHexByte(StringBuilder sb, byte b) {
		if ((b & 0xf0) == 0)
			sb.append('0');
		sb.append(Integer.toHexString(b & 0xff));
	}

	private static char printable(byte b) {
		return PRINTABLE[b & 0xff];

	}

	private static void formatAsciiLine(StringBuilder sb, ByteBuffer bb) {
		int lineStart = lineOffset(bb);
		int lineEnd = lineStart + 0x10;
		int skipStart = bb.position();
		int skipEnd = Math.min(lineEnd, bb.limit());

		for (int i = lineStart; i < skipStart; i++) {
			sb.append(' ');
			if ((i & 0xf) == 7)
				sb.append(' ');
		}

		for (int i = skipStart; i < skipEnd; i++) {
			sb.append(printable(bb.get()));
			if ((i & 0xf) == 7)
				sb.append(' ');
		}

		for (int i = skipEnd; i < lineEnd; i++) {
			sb.append(' ');
			if ((i & 0xf) == 7)
				sb.append(' ');
		}
	}

	static void formatHexLine(StringBuilder sb, ByteBuffer bb) {
		int lineStart = lineOffset(bb);
		int lineEnd = lineStart + 0x10;
		int skipStart = bb.position();
		int skipEnd = Math.min(lineEnd, bb.limit());

		for (int i = lineStart; i < skipStart; i++) {
			sb.append("   ");
			if ((i & 0xf) == 7)
				sb.append(' ');
		}

		for (int i = skipStart; i < skipEnd; i++) {
			formatHexByte(sb, bb.get());
			sb.append(' ');
			if ((i & 0xf) == 7)
				sb.append(' ');
		}

		for (int i = skipEnd; i < lineEnd; i++) {
			sb.append("   ");
			if ((i & 0xf) == 7)
				sb.append(' ');
		}
	}

	static void formatLine(StringBuilder sb, ByteBuffer bb, int offsetWidth) {
		HexFormat.formatOffset(sb, offsetWidth, lineOffset(bb));
		sb.append(": ");
		int p = bb.position();
		formatHexLine(sb, bb);
		sb.append("| ");
		bb.position(p);
		formatAsciiLine(sb, bb);
	}

	static void formatOffset(StringBuilder sb, int width, int offset) {
		int pad = width - hexLength(offset);
		if (pad > 0)
			sb.append(ZEROES, 0, pad);
		sb.append(Integer.toHexString(offset));
	}

	public static String format(ByteBuffer bb) {
		StringBuilder sb = new StringBuilder(bb.remaining() * 4);
		format(sb, bb, bb.limit());
		return sb.toString();
	}

	public static void format(StringBuilder sb, ByteBuffer bb, int maxOffset) {
		ByteBuffer bb2 = bb.asReadOnlyBuffer();
		int l = bb2.limit();
		int offsetWidth = Math.max(2, hexLength((maxOffset - 1) & ~0xff));

		do {
			bb2.limit(Math.min(bb2.position() + 16, l));
			formatLine(sb, bb2, offsetWidth);
			sb.append(System.lineSeparator());
		} while (bb2.limit() < l);
	}
}
