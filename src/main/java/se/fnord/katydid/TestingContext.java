package se.fnord.katydid;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

public interface TestingContext {
	byte read();

	long read(int elementWidth);

	void write(int elementWidth, long value);

	ComparisonStatus compareTo(DataTester dataTester, int pass);

	void addFailure(String message, Object... formatArgs);
}
