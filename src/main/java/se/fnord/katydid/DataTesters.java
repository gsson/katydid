package se.fnord.katydid;

import se.fnord.katydid.internal.*;

import java.nio.charset.Charset;
import java.util.Arrays;

public class DataTesters {
	private static final Charset UTF8 = Charset.forName("utf-8");
	public static DataTester u8(String name, Number... values) {
		return new Int(name, Int.IntFormat.UNSIGNED, 1, values);
	}

	public static DataTester u8(Number... values) {
		return u8("u8", values);
	}

	public static DataTester s8(String name, Number... values) {
		return new Int(name, Int.IntFormat.SIGNED, 1, values);
	}

	public static DataTester s8(Number... values) {
		return s8("s8", values);
	}

	public static DataTester h8(String name, Number... values) {
		return new Int(name, Int.IntFormat.HEX, 1, values);
	}

	public static DataTester h8(Number... values) {
		return h8("h8", values);
	}

	public static DataTester u16(String name, Number... values) {
		return new Int(name, Int.IntFormat.UNSIGNED, 2, values);
	}

	public static DataTester u16(Number... values) {
		return u16("u16", values);
	}

	public static DataTester s16(String name, Number... values) {
		return new Int(name, Int.IntFormat.SIGNED, 2, values);
	}

	public static DataTester s16(Number... values) {
		return s16("s16", values);
	}

	public static DataTester h16(String name, Number... values) {
		return new Int(name, Int.IntFormat.HEX, 2, values);
	}

	public static DataTester h16(Number... values) {
		return h16("h16", values);
	}

	public static DataTester u24(String name, Number... values) {
		return new Int(name, Int.IntFormat.UNSIGNED, 3, values);
	}

	public static DataTester u24(Number... values) {
		return u24("u24", values);
	}

	public static DataTester s24(String name, Number... values) {
		return new Int(name, Int.IntFormat.SIGNED, 3, values);
	}

	public static DataTester s24(Number... values) {
		return s24("s24", values);
	}

	public static DataTester h24(String name, Number... values) {
		return new Int(name, Int.IntFormat.HEX, 3, values);
	}

	public static DataTester h24(Number... values) {
		return h24("h24", values);
	}

	public static DataTester u32(String name, Number... values) {
		return new Int(name, Int.IntFormat.UNSIGNED, 4, values);
	}

	public static DataTester u32(Number... values) {
		return u32("u32", values);
	}

	public static DataTester s32(String name, Number... values) {
		return new Int(name, Int.IntFormat.SIGNED, 4, values);
	}

	public static DataTester s32(Number... values) {
		return s32("s32", values);
	}

	public static DataTester h32(String name, Number... values) {
		return new Int(name, Int.IntFormat.HEX, 4, values);
	}

	public static DataTester h32(Number... values) {
		return h32("h32", values);
	}

	public static DataTester u64(String name, Number... values) {
		return new Int(name, Int.IntFormat.UNSIGNED, 8, values);
	}

	public static DataTester u64(Number... values) {
		return u64("u64", values);
	}

	public static DataTester s64(String name, Number... values) {
		return new Int(name, Int.IntFormat.SIGNED, 8, values);
	}

	public static DataTester s64(Number... values) {
		return s64("s64", values);
	}

	public static DataTester h64(String name, Number... values) {
		return new Int(name, Int.IntFormat.HEX, 8, values);
	}

	public static DataTester h64(Number... values) {
		return h64("h64", values);
	}

	public static DataTester struct(String name, DataTester... elements) {
		return new Struct(name, elements);
	}

	public static DataTester struct(DataTester... elements) {
		return struct("struct", elements);
	}

	public static DataTester utf8(String name, String value) {
		return new Bytes(name, value.getBytes(UTF8));
	}

	public static DataTester utf8(String value) {
		return utf8("utf8", value);
	}

	public static DataTester bytes(String name, byte... bytes) {
		return new Bytes(name, bytes);
	}

	public static DataTester bytes(byte... bytes) {
		return bytes("bytes", bytes);
	}

	public static DataTester skip(String name, int size) {
		return new Skip(name, size);
	}

	public static DataTester skip(int size) {
		return skip("skip", size);
	}

	public static DataTester zero(String name, int count) {
		byte[] v = new byte[count];
		Arrays.fill(v, (byte) 0);
		return bytes(name, v);
	}

	public static DataTester zero(int count) {
		return zero("zero", count);
	}

	public static DataTester defer(DataTester tester) {
		return new Defer(tester);
	}

	public static DataTester fatal(DataTester tester) {
		return new Fatal(tester);
	}
}
