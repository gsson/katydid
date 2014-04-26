package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

import static se.fnord.katydid.ComparisonStatus.EQUAL;
import static se.fnord.katydid.ComparisonStatus.ERROR;

public class Fatal implements DataTester {

	private final DataTester delegate;

	public Fatal(DataTester delegate) {
		this.delegate = delegate;
	}

	@Override
	public int passCount() {
		return delegate.passCount();
	}

	@Override
	public ComparisonStatus compareTo(int pass, TestingContext context) {
		final ComparisonStatus status = delegate.compareTo(pass, context);
		if (status != EQUAL)
			return ERROR;
		return status;
	}

	@Override
	public int length() {
		return delegate.length();
	}

	@Override
	public String formatName(TestingContext context, int index) {
		return delegate.formatName(context, index);
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		delegate.toBuffer(bb);
	}
}
