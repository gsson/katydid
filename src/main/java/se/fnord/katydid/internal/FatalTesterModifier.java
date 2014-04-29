package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;
import se.fnord.katydid.TestingContext;

import java.nio.ByteBuffer;

import static se.fnord.katydid.ComparisonStatus.*;

public class FatalTesterModifier implements DataTester {

	private final DataTester delegate;

	public FatalTesterModifier(DataTester delegate) {
		this.delegate = delegate;
	}

	@Override
	public int passCount() {
		return delegate.passCount();
	}

	@Override
	public ComparisonStatus compareItem(TestingContext context, int pass, int itemIndex) {
		final ComparisonStatus status = delegate.compareItem(context, pass, itemIndex);
		if (!status.shouldContinue())
			return ABORT;
		return status;
	}

	@Override
	public int length() {
		return delegate.length();
	}

	@Override
	public int lengthOfItem(int itemIndex) {
		return delegate.lengthOfItem(itemIndex);
	}

	@Override
	public int itemCount() {
		return delegate.itemCount();
	}

	@Override
	public void toBuffer(ByteBuffer bb) {
		delegate.toBuffer(bb);
	}

	@Override
	public String name() {
		return delegate.name();
	}

	@Override
	public String formatChild(int itemIndex, DataTester child) {
		return delegate.formatChild(itemIndex, child);
	}

	@Override
	public String formatItem(String name, int itemIndex) {
		return delegate.formatItem(name, itemIndex);
	}
}
