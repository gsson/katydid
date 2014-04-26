package se.fnord.katydid;

import se.fnord.katydid.internal.TestingContext;

import java.nio.ByteBuffer;

public interface DataTester {
	int passCount();

	ComparisonStatus compareTo(int pass, TestingContext context);

	int length();

	int lengthOfItem(int itemIndex);

	int itemCount();

	String formatName(TestingContext context, int index);

	void toBuffer(ByteBuffer bb);
}
