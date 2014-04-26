KATYDID
=======

(KATYDID Asserts That Your Data Is Deluxe)

KATYDID is a tool for testing serialization and deserialization of messages. [wanders](https://github.com/wanders) came up with the most excellent name (thanks!)

It's main features are readable message definitions and decent error messages. It's designed for easy composability and reuse.

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

Below is simple test for checking a block header containing a 32 bit length indicator, a 64 bit magic value and four bytes of data like so:

    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                      Message Length                           |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                           Magic ...                           |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    |                              ...                              |
    +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    | Data  |
    +-+-+-+-+

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
                u32(16),
                h64(0x0123456789abcdefL),
                u8(1, 2, 3, 4)
        );

        final ByteBuffer actual = createTestData();

        DataAsserts.assertEquals(expected, actual);
    }

It would be nice to describe the elements somehow though:

    @Test
    public void testBlockHeader2() {
        final DataTester expected = struct("blockHeader",
                u32("length", 16),
                h64("magic", 0x0123456789abcdefL),
                u8("data", 1, 2, 3, 4)
        );

        final ByteBuffer actual = createTestData();

        DataAsserts.assertEquals(expected, actual);
    }

This will allow for additional readability, and the error messages when the assertion fails will be decorated with the names used.

If there are several blocks that need to be tested, the values can be extracted for extra readability:

    public DataTester blockHeader(DataTester ... testers) {
        return struct("blockHeader", testers);
    }

	public DataTester length(int length) {
		return u32("length", length);
	}

    public DataTester magic(long value) {
        return h64("magic", value);
    }

	public DataTester data(Number ... values) {
		return u8("data", values);
	}

    @Test
    public void testBlockHeader3() {
        final DataTester expected = blockHeader(
                length(16),
                magic(0x0123456789abcdefL),
                data(1, 2, 3, 4)
        );

        final ByteBuffer actual = createTestData();

        DataAsserts.assertEquals(expected, actual);
    }

Normally the data is checked in the order it appears in the structure. Sometimes it might be useful to delay a check until after the following parts have been
checked. For example, checking the length field after the actual data has been checked is useful in order to figure out at what point the data is differs rather
than just finding out that the length is not as expected.

To defer a test to a later pass, wrap it with `defer()`:

	@Test
	public void testBlockHeader4() {
		final DataTester expected = blockHeader(
				defer(length(16)),
				magic(0x0123456789abcdefL),
				data(1, 2, 3, 4)
		);

		final ByteBuffer actual = createTestData();

		DataAsserts.assertExact(expected, actual);
	}


An executable version can be found in the `src/examples` folder as `se.fnord.katydid.examples.SimpleTests`
