package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

public class Defer implements DataTester {

	private final DataTester delegate;

	public Defer(DataTester delegate) {
		this.delegate = delegate;
	}

	@Override
	public int passCount() {
		return delegate.passCount() + 1;
	}

	@Override
	public ComparisonStatus compareTo(int pass, TestingContext context) {
		return delegate.compareTo(pass - 1, context);
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
