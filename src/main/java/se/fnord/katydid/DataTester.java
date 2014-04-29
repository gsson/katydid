package se.fnord.katydid;

import java.nio.ByteBuffer;

public interface DataTester extends NameFormatter {
	int passCount();

	ComparisonStatus compareItem(TestingContext context, int pass, int itemIndex);

	int length();

	int lengthOfItem(int itemIndex);

	int itemCount();

	void toBuffer(ByteBuffer bb);

	String name();
}
