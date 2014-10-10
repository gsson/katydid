package se.fnord.katydid;

import se.fnord.katydid.internal.*;

import static java.nio.charset.StandardCharsets.*;
import static se.fnord.katydid.internal.Util.fromHex;

import java.util.Arrays;

/**
 * Factory-methods to create DataTester instances.
 */
public class DataTesters {

	/**
	 * Creates a tester for generating or verifying unsigned 8 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * To test for the sequence
	 * <pre>
	 *     01 02 03 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(u8("data", 1, 2, 3, 4), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 8 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u8(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.UNSIGNED, 1, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 8 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     u8("u8", values)
	 * </code>
	 *
	 * @see #u8(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 8 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u8(Number... values) {
		return u8("u8", values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 8 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * To test for the sequence
	 * <pre>
	 *     ff fe fd fc
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(s8("data", -1, -2, -3, -4), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to signed 8 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s8(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.SIGNED, 1, values);
	}


	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 8 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     s8("s8", values)
	 * </code>
	 *
	 * @see #s8(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to signed 8 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s8(Number... values) {
		return s8("s8", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 8 bit values.
	 * Values will be presented in hexadecimal in reports and failures.
	 * <p>
	 * To test for the sequence
	 * <pre>
	 *     01 02 03 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(h8("data", 0x01, 0x02, 0x03, 0x04), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 8 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h8(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.HEX, 1, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 8 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     h8("h8", values)
	 * </code>
	 *
	 * @see #h8(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 8 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h8(Number... values) {
		return h8("h8", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 16 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 02 02 03 03 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(u16("data", 257, 514, 771, 1028), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 16 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u16(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.UNSIGNED, 2, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 16 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     u16("u16", values)
	 * </code>
	 *
	 * @see #u16(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 16 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u16(Number... values) {
		return u16("u16", values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 16 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     ff ff ff fe ff fd ff fc
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(s16("data", -1, -2, -3, -4), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to signed 16 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s16(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.SIGNED, 2, values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 16 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     s16("s16", values)
	 * </code>
	 *
	 * @see #s16(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to signed 16 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s16(Number... values) {
		return s16("s16", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 16 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 02 02 03 03 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(h16("data", 0x0101, 0x0202, 0x0303, 0x0404), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 16 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h16(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.HEX, 2, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 16 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     h16("h16", values)
	 * </code>
	 *
	 * @see #h16(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 16 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h16(Number... values) {
		return h16("h16", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 24 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 01 02 02 02 03 03 03 04 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(u24("data", 65793, 131586, 197379, 263172), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 24 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u24(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.UNSIGNED, 3, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 24 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     u24("u24", values)
	 * </code>
	 *
	 * @see #u24(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 24 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u24(Number... values) {
		return u24("u24", values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 24 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     ff ff ff ff ff fe ff ff fd ff ff fc
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(s24("data", -1, -2, -3, -4), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to signed 24 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s24(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.SIGNED, 3, values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 24 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     s24("s24", values)
	 * </code>
	 *
	 * @see #s24(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to signed 24 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s24(Number... values) {
		return s24("s24", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 24 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 01 02 02 02 03 03 03 04 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(h24("data",<br>
	 *       0x010101, 0x020202,<br>
	 *       0x030303, 0x040404), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 24 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h24(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.HEX, 3, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 24 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     h24("h24", values)
	 * </code>
	 *
	 * @see #h24(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 24 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h24(Number... values) {
		return h24("h24", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 32 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 01 01 02 02 02 02 03 03 03 03 04 04 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(u32("data",<br>
	 *       16843009, 33686018,<br>
	 *       50529027, 67372036), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 32 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u32(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.UNSIGNED, 4, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 32 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     u32("u32", values)
	 * </code>
	 *
	 * @see #u32(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 32 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u32(Number... values) {
		return u32("u32", values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 32 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     ff ff ff ff ff ff ff fe ff ff ff fd ff ff ff fc
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(s32("data", -1, -2, -3, -4), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to signed 32 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s32(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.SIGNED, 4, values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 32 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     s32("s32", values)
	 * </code>
	 *
	 * @see #s32(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to signed 32 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s32(Number... values) {
		return s32("s32", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 32 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 01 01 02 02 02 02 03 03 03 03 04 04 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(h24("data",<br>
	 *       0x01010101, 0x02020202,<br>
	 *       0x03030303, 0x04040404), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 32 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h32(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.HEX, 4, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 32 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     h32("h32", values)
	 * </code>
	 *
	 * @see #h32(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 32 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h32(Number... values) {
		return h32("h32", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 64 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 01 01 01 01 01 01 02 02 02 02 02 02 02 02
	 *     03 03 03 03 03 03 03 03 04 04 04 04 04 04 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(u64("data",<br>
	 *        72340172838076673L, 144680345676153346L,<br>
	 *       217020518514230019L, 289360691352306692L), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 64 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u64(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.UNSIGNED, 8, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 64 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     u64("u64", values)
	 * </code>
	 *
	 * @see #u64(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 64 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester u64(Number... values) {
		return u64("u64", values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 64 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff fe
	 *     ff ff ff ff ff ff ff fd ff ff ff ff ff ff ff fc
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(s32("data", -1, -2, -3, -4), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to signed 64 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s64(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.SIGNED, 8, values);
	}

	/**
	 * Creates a tester for generating or verifying signed (two's-complement) 64 bit values.
	 * Values will be presented in decimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     s64("s64", values)
	 * </code>
	 *
	 * @see #s64(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to signed 64 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester s64(Number... values) {
		return s64("s64", values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 64 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Given a buffer in big endian format, to test for the sequence:
	 * <pre>
	 *     01 01 01 01 01 01 01 01 02 02 02 02 02 02 02 02
	 *     03 03 03 03 03 03 03 03 04 04 04 04 04 04 04 04
	 * </pre>
	 * The following code could be used:
	 * <p>
	 * <code>
	 *     assertExact(h24("data",<br>
	 *       0x0101010101010101L, 0x0202020202020202L,<br>
	 *       0x0303030303030303L, 0x0404040404040404L), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param values a list of values to generate or validate. Will be converted to unsigned 64 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h64(String name, Number... values) {
		return new IntTester(name, IntTester.IntFormat.HEX, 8, values);
	}

	/**
	 * Creates a tester for generating or verifying unsigned 64 bit values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <code>
	 *     h64("h64", values)
	 * </code>
	 *
	 * @see #h64(String, Number...)
	 * @param values a list of values to generate or validate. Will be converted to unsigned 64 bit values before testing.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester h64(Number... values) {
		return h64("h64", values);
	}

	/**
	 * Creates a tester for generating or verifying a collection of testers.
	 * Value names will be presented in a structured form like {@code structName.elementName} whereas the values themselves will be presented in the form specified by its tester.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Example:
	 * <code>
	 *     assertExact(struct("message",<br>
	 *     	u8("length", 15),<br>
	 *     	h16("code", 0x1010),<br>
	 *     	h32("propertyKey", 0x12345678),<br>
	 *     	s64("propertyValue", -1)), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param elements a list of testers to generate or validate. Each tester will be evaluated in the order they are specified.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester struct(String name, DataTester... elements) {
		return new StructTester(name, elements);
	}

	/**
	 * Creates a tester for generating or verifying a collection of testers.
	 * Value names will be presented in a structured form like {@code structName.elementName} whereas the values themselves will be presented in the form specified by its tester.
	 * Testing and generation endianness is determined by the byte order of the buffer.
	 * <p>
	 * Equivalent to calling
	 * <code>
	 *     struct("struct", elements)
	 * </code>
	 *
	 * @see #struct(String, DataTester...)
	 * @param elements a list of testers to generate or validate. Each tester will be evaluated in the order they are specified.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester struct(DataTester... elements) {
		return struct("struct", elements);
	}

	/**
	 * Creates a tester for generating or verifying a collection of testers.
	 * Value names will be presented in a list form like {@code structName[index]} whereas the values themselves will be presented in the form specified by its tester.
	 * <p>
	 * Example:
	 * <code>
	 *     assertExact(list("names",<br>
	 *     	struct("name", u8("length", 5), utf8("text", "name1")),<br>
	 *     	struct("name", u8("length", 5), utf8("text", "name2")),<br>
	 *     	struct("name", u8("length", 5), utf8("text", "name3")))), byteBuffer);
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param elements a list of testers to generate or validate. Each tester will be evaluated in the order they are specified.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester list(String name, DataTester... elements) {
		return new ListTester(name, elements);
	}

	/**
	 * Creates a tester for generating or verifying a collection of testers.
	 * Value names will be presented in a list form like {@code structName[index]} whereas the values themselves will be presented in the form specified by its tester.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     list("list", elements)
	 * </code>
	 *
	 * @see #list(String, DataTester...)
	 * @param elements a list of testers to generate or validate. Each tester will be evaluated in the order they are specified.
	 * @return the DataTester for generating or verifying the values
	 */
	public static DataTester list(DataTester... elements) {
		return list("list", elements);
	}

	/**
	 * Creates a tester for generating or verifying utf-8 encoded strings.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Example:
	 * <p>
	 * <code>
	 *     assertExact(utf8("name", "value"), byteBuffer);
	 * </code>
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     bytes(name, value.getBytes(UTF_8))
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param value a string whose utf-8 representation to generate or validate.
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester utf8(String name, String value) {
		return bytes(name, value.getBytes(UTF_8));
	}

	/**
	 * Creates a tester for generating or verifying utf-8 encoded strings.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     utf8("utf8", value)
	 * </code>
	 *
	 * @see #utf8(String, String)
	 * @param value a string whose utf-8 representation to generate or validate.
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester utf8(String value) {
		return utf8("utf8", value);
	}

	/**
	 * Creates a tester for generating or verifying a list of byte values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Example
	 * <p>
	 * <code>
	 *     bytes("something", "bytes".getBytes(UTF_16BE))
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param bytes an array of bytes to generate or validate
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester bytes(String name, byte... bytes) {
		return new BytesTester(name, bytes);
	}

	/**
	 * Creates a tester for generating or verifying a list of byte values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     bytes("bytes", bytes)
	 * </code>
	 * @see #bytes(String, byte...)
	 * @param bytes an array of bytes to generate or validate
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester bytes(byte... bytes) {
		return bytes("bytes", bytes);
	}

	/**
	 * Creates a tester for generating or verifying a list of byte values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Example
	 * <p>
	 * <code>
	 *     bytes("something", "010203aabb")
	 * </code>
	 *
	 * @param name the name of the tester.
	 * @param hexBytes a hex string representing the bytes to generate or validate
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester hex(String name, String hexBytes) {
		return new BytesTester(name, fromHex(hexBytes));
	}

	/**
	 * Creates a tester for generating or verifying a list of byte values.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     bytes("bytes", hexBytes)
	 * </code>
	 * @see #hex(String, String)
	 * @param hexBytes a hex string representing the bytes to generate or validate
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester hex(String hexBytes) {
		return hex("bytes", hexBytes);
	}

	/**
	 * Creates a tester for skipping a number of bytes in when testing.
	 * When generating, this tester will generate {@code size} zero-bytes.
	 * <p>
	 * Example
	 * <p>
	 * <code>
	 *     skip("unused", size)
	 * </code>
	 * @see #zero(String, int)
	 * @param name the name of the tester.
	 * @param size the number of bytes to skip
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester skip(String name, int size) {
		return new SkippingTester(name, size);
	}

	/**
	 * Creates a tester for skipping a number of bytes in when testing.
	 * When generating, this tester will generate {@code size} zero-bytes.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     skip("skip", bytes)
	 * </code>
	 * @see #skip(String, int)
	 * @param size the number of bytes to skip
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester skip(int size) {
		return skip("skip", size);
	}

	/**
	 * Creates a tester for generating or verifying a sequence of {@code count} of zero-bytes.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Example
	 * <p>
	 * <code>
	 *     zero("padding", 42)
	 * </code>
	 * @see #skip(String, int)
	 * @param name the name of the tester.
	 * @param count the number of bytes to skip
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester zero(String name, int count) {
		byte[] v = new byte[count];
		Arrays.fill(v, (byte) 0);
		return bytes(name, v);
	}

	/**
	 * Creates a tester for generating or verifying a sequence of {@code count} of zero-bytes.
	 * Values will be presented in hexadecimal form in reports and failures.
	 * <p>
	 * Equivalent to calling
	 * <p>
	 * <code>
	 *     zero("zero", count)
	 * </code>
	 * @see #zero(String, int)
	 * @param count the number of bytes to skip
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester zero(int count) {
		return zero("zero", count);
	}

	/**
	 * Modifies a tester to defer validation to a the pass after when it would ordinarily be executed.
	 * Has no effect on generation.
	 * @param tester the tester whose execution is to be deferred
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester defer(DataTester tester) {
		return new DeferringTesterModifier(tester);
	}

	/**
	 * Modifies a tester to emit a fatal error on a non-fatal validation failure, thus stopping the validation of any subsequent testers or validation passes.
	 * Has no effect on generation.
	 * @param tester the tester whose execution result is to be modified
	 * @return the DataTester for generating or verifying the value
	 */
	public static DataTester fatal(DataTester tester) {
		return new FatalTesterModifier(tester);
	}
}
