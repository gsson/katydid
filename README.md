KATYDID
=======

(KATYDID Asserts That Your Data Is Deluxe)

[![Build Status](https://travis-ci.org/gsson/katydid.svg?branch=master)](https://travis-ci.org/gsson/katydid)

KATYDID is a tool for testing serialization and deserialization of messages. [wanders](https://github.com/wanders) came up with the most excellent name (thanks!)

It's main features are readable message definitions and decent error messages. It's designed for easy composability and reuse.

Javadocs are [here](http://gsson.github.io/katydid/javadoc/)
Test coverage is [here](http://gsson.github.io/katydid/jacoco/)

License
-------

This software is licensed under the [ISC](http://opensource.org/licenses/ISC) license:

    Copyright (c) 2013 Henrik Gustafsson <henrik.gustafsson@fnord.se>

    Permission to use, copy, modify, and distribute this software for any
    purpose with or without fee is hereby granted, provided that the above
    copyright notice and this permission notice appear in all copies.

    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.


Example
-------

Below is simple test for checking a data block containing a 64 bit magic value, a 32 bit length indicator and a variable-sized block of byte data like so:

    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                           Magic ...                           |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                              ...                              |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                      Message Length                           |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    | Data ...
    +-+-+-+-+-+-+-+-+

The test assumes these function exists:

    public static byte[] asBytes(Number ... values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++)
            bytes[i] = values[i].byteValue();
        return bytes;
    }

	public ByteBuffer createTestData() {
		final ByteBuffer actual = ByteBuffer.allocate(16);
		actual.putInt(16);
		actual.putLong(0x0123456789abcdefL);
		actual.put(asBytes(1, 2, 3, 4));
		actual.flip();
		return actual;
	}

With that, we can test the data like this:

	@Test
	public void testBlockHeader1() {
		final DataTester expected = struct(
				h64(0x0123456789abcdefL),
				u32(16),
				u8(1, 2, 3, 4)
		);

		assertExact(expected, createTestData());
	}

It would be nice to describe the elements somehow though:

	@Test
	public void testBlockHeader2() {
		final DataTester expected = struct("block",
				h64("magic", 0x0123456789abcdefL),
				u32("length", 16),
				u8("data", 1, 2, 3, 4)
		);

		assertExact(expected, createTestData());
	}

This will allow for additional readability, and the error messages when the assertion fails will be decorated with the names used.

If there are several blocks that need to be tested, perhaps with additional magic values, the testing functions can be extracted for extra readability:

	public static DataTester magic(long value) {
		return h64("magic", value);
	}

	public static DataTester length(int length) {
		return u32("length", length);
	}

	public static DataTester data(Number ... values) {
		return u8("data", values);
	}

	public static DataTester block(DataTester ... testers) {
		return struct("block", testers);
	}

	@Test
	public void testBlockHeader3() {
		final DataTester expected = struct(
			magic(0x0123456789abcdefL),
			length(16),
			data(1, 2, 3, 4)
		);

		assertExact(expected, createTestData());
	}

If all blocks that should be tested follow exactly the same pattern, the header creation can be extracted as well:

	public static final int HEADER_LENGTH = 8 + 4;

	public static DataTester header(int dataLength) {
		return struct("header",
			magic(0x0123456789abcdefL),
			length(HEADER_LENGTH + dataLength));
	}

	public static DataTester blockForData(Number ... data) {
		final DataTester body = data(data);
		final DataTester header = header(body.length());
		return block(header, body);
	}

	@Test
	public void testBlockHeader4() {
		assertExact(blockForData(1, 2, 3, 4), createTestData());
	}

Be careful when extracting and wrapping though; it's easy to engineer a test that becomes hard to read due to excessive levels of indirection.

An executable version can be found in the `src/examples` folder as `se.fnord.katydid.examples.SimpleTests`

Error messages
--------------

Add this failing test to `se.fnord.katydid.examples.SimpleTests`:

	public static ByteBuffer createInvalidTestData() {
		final ByteBuffer actual = ByteBuffer.allocate(16);
		actual.putLong(0xfedcba9876543210L);
		actual.putInt(17);
		actual.put(asBytes(2, 3, 4, 5));
		actual.flip();
		return actual;
	}

	@Test
	public void testInvalidData() {
		assertExact(blockForData(1, 2, 3, 4), createInvalidTestData());
	}

It will fail in the following way:

    java.lang.AssertionError: Test failed:
    * block.header.magic at 00-07: Value differs: 0123456789abcdef != fedcba9876543210
    * block.header.length at 08-0b: Value differs: 16 != 17
    * block.data[0] at 0c: Value differs: 1 != 2
    * block.data[1] at 0d: Value differs: 2 != 3
    * block.data[2] at 0e: Value differs: 3 != 4
    * block.data[3] at 0f: Value differs: 4 != 5
    Expected bytes:
    00: 01 23 45 67 89 ab cd ef  00 00 00 10 01 02 03 04 | .#Eg.... ........
    Actual bytes:
    00: fe dc ba 98 76 54 32 10  00 00 00 11 02 03 04 05 | ....vT2. ........

        at se.fnord.katydid.internal.TestingContext.assertSuccess(TestingContext.java:82)
        at se.fnord.katydid.DataAsserts.assertExact(DataAsserts.java:30)
        at se.fnord.katydid.examples.SimpleTests.testInvalidData(SimpleTests.java:108)


Looking at the exception thrown by the `se.fnord.katydid.examples.TestDiameter.failingCCRTest()` the various test failures and their locations become obvious:

    java.lang.AssertionError: Test failed:
    * 180 bytes data expected, was 192 bytes
    * Credit-Control.commandHeader.length at 01-03: Value differs: 180 != 192
    * Credit-Control.avps.Class.avpHead.length at a9-ab: Value differs: 13 != 25
    * Credit-Control.avps.Class.pad[0] at b1: Value differs: 00 != 2d
    Expected bytes:
    00: 01 00 00 b4 c0 00 01 10  00 00 00 04 00 00 00 00 | ........ ........
    10: 00 00 00 00 00 00 01 07  40 00 00 1a 70 63 65 66 | ........ @...pcef
    20: 31 2e 66 6e 6f 72 64 2e  73 65 3b 30 3b 30 00 00 | 1.fnord. se;0;0..
    30: 00 00 01 08 40 00 00 16  70 63 65 66 31 2e 66 6e | ....@... pcef1.fn
    40: 6f 72 64 2e 73 65 00 00  00 00 01 28 40 00 00 10 | ord.se.. ...(@...
    50: 66 6e 6f 72 64 2e 73 65  00 00 01 25 40 00 00 10 | fnord.se ...%@...
    60: 66 6e 6f 72 64 2e 73 65  00 00 01 03 40 00 00 0c | fnord.se ....@...
    70: 00 00 00 00 00 00 01 cd  40 00 00 18 36 2e 33 32 | ........ @...6.32
    80: 32 36 30 40 33 67 70 70  2e 6f 72 67 00 00 01 a0 | 260@3gpp .org....
    90: 40 00 00 0c 00 00 00 01  00 00 01 9f 40 00 00 0c | @....... ....@...
    a0: 00 00 00 00 00 00 00 19  40 00 00 0d 63 6c 61 73 | ........ @...clas
    b0: 73 00 00 00                                      | s...
    Actual bytes:
    00: 01 00 00 c0 c0 00 01 10  00 00 00 04 00 00 00 00 | ........ ........
    10: 00 00 00 00 00 00 01 07  40 00 00 1a 70 63 65 66 | ........ @...pcef
    20: 31 2e 66 6e 6f 72 64 2e  73 65 3b 30 3b 30 00 00 | 1.fnord. se;0;0..
    30: 00 00 01 08 40 00 00 16  70 63 65 66 31 2e 66 6e | ....@... pcef1.fn
    40: 6f 72 64 2e 73 65 00 00  00 00 01 28 40 00 00 10 | ord.se.. ...(@...
    50: 66 6e 6f 72 64 2e 73 65  00 00 01 25 40 00 00 10 | fnord.se ...%@...
    60: 66 6e 6f 72 64 2e 73 65  00 00 01 03 40 00 00 0c | fnord.se ....@...
    70: 00 00 00 00 00 00 01 cd  40 00 00 18 36 2e 33 32 | ........ @...6.32
    80: 32 36 30 40 33 67 70 70  2e 6f 72 67 00 00 01 a0 | 260@3gpp .org....
    90: 40 00 00 0c 00 00 00 01  00 00 01 9f 40 00 00 0c | @....... ....@...
    a0: 00 00 00 00 00 00 00 19  40 00 00 19 63 6c 61 73 | ........ @...clas
    b0: 73 2d 63 6c 61 73 73 2d  63 6c 61 73 73 00 00 00 | s-class- class...

        at se.fnord.katydid.internal.TestingContext.assertSuccess(TestingContext.java:82)
        at se.fnord.katydid.DataAsserts.assertExact(DataAsserts.java:30)
        at se.fnord.katydid.examples.TestDiameter.failingCCRTest(TestDiameter.java:95)
