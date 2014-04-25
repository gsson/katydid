package se.fnord.katydid;

import se.fnord.katydid.internal.TestingContext;

import java.nio.ByteBuffer;

public interface DataTester {
	int maxPass();

	void compareTo(int pass, TestingContext context);

	int length();

	String formatName(TestingContext context, int index);

	void toBuffer(ByteBuffer bb);
}
