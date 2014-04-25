package se.fnord.katydid.examples;
import java.nio.ByteBuffer;

import static se.fnord.katydid.DataTesters.*;
import org.junit.Test;
import se.fnord.katydid.DataAsserts;
import se.fnord.katydid.DataTester;

public class TestDiameter {
	private static int AVP_SIZE = 8;
	private static int AVP_VENDOR_SIZE = 12;
	private static final int HEADER_SIZE = 20;

	private static int REQ = 0x80;
	private static int PXY = 0x40;

	private static int V = 0x80;
	private static int M = 0x40;

	private static final String SESSION_ID = "pcef1.fnord.se;0;0";
	private static final String ORIGIN_HOST = "pcef1.fnord.se";
	private static final String REALM = "fnord.se";


	private DataTester padFor(int length) {
		switch (length & 3) {
		case 0:
			return skip("pad", 0);
		case 1:
			return skip("pad", 3);
		case 2:
			return skip("pad", 2);
		case 3:
			return skip("pad", 1);
		}
		throw new IllegalStateException(Integer.toString(length));
	}

	private DataTester commandHeader(int flags, int code, int application, int avpLength) {
		return struct("commandHeader",
				u8("version", 1),
				defer(u24("length", avpLength + HEADER_SIZE)), // Check the message length field after all other values have been checked.
				u8("flags", flags), u24("commandCode", code),
				u32("application", application),
				skip("hopByHop", 4), // Skip validation of Hop-By-Hop and End-To-End values
				skip("endToEnd", 4));
	}

	private DataTester avpHead(int vendorId, int code, int flags, int valueLength) {
		if (vendorId > 0)
			return struct("avpHead", u32("code", code), u8("flags", flags), u24("length", valueLength + AVP_VENDOR_SIZE), u32("vendorId", vendorId));
		return struct("avpHead", u32("code", code), u8("flags", flags), u24("length", valueLength + AVP_SIZE));
	}

	protected DataTester avp(String name, int vendorId, int code, int flags, DataTester value) {
		return struct(name, avpHead(vendorId, code, flags, value.length()), value, padFor(value.length()));
	}

	protected DataTester command(String name, int flags, int app, int code, DataTester ...avps) {
		DataTester avpList = struct("avps", avps);
		return struct(name,
				commandHeader(flags, code, app, avpList.length()),
				avpList
		);
	}


	@Test
	public void failingCCRTest() {
		DataTester correctClassCode = command("Credit-Control", PXY | REQ, 4, 272,
				avp("Session-Id", 0, 263, M, utf8(SESSION_ID)),
				avp("Origin-Host", 0, 264, M, utf8(ORIGIN_HOST)),
				avp("Origin-Realm", 0, 296, M, utf8(REALM)),
				avp("Destination-Realm", 0, 293, M, utf8(REALM)),
				avp("Auth-Application-Id", 0, 259, M, u32(0)),
				avp("Service-Context-Id", 0, 461, M, utf8("6.32260@3gpp.org")),
				avp("CC-Request-Type", 0, 416, M, u32(1)),
				avp("CC-Request-Number", 0, 415, M, u32(0)),
				avp("Class", 0, 25, M, utf8("class"))
		);

		DataTester incorrectClassCode = command("Credit-Control", PXY | REQ, 4, 272,
				avp("Session-Id", 0, 263, M, utf8(SESSION_ID)),
				avp("Origin-Host", 0, 264, M, utf8(ORIGIN_HOST)),
				avp("Origin-Realm", 0, 296, M, utf8(REALM)),
				avp("Destination-Realm", 0, 293, M, utf8(REALM)),
				avp("Auth-Application-Id", 0, 259, M, u32(0)),
				avp("Service-Context-Id", 0, 461, M, utf8("6.32260@3gpp.org")),
				avp("CC-Request-Type", 0, 416, M, u32(1)),
				avp("CC-Request-Number", 0, 415, M, u32(0)),
				avp("Class", 0, 26, M, utf8("class"))
		);

		ByteBuffer buffer = ByteBuffer.allocate(incorrectClassCode.length());
		incorrectClassCode.toBuffer(buffer);
		buffer.flip();

		DataAsserts.assertEquals(correctClassCode, buffer);
	}
}
