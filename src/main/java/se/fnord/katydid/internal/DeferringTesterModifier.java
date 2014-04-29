package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

public class DeferringTesterModifier implements DataTester {

	private final DataTester delegate;

	public DeferringTesterModifier(DataTester delegate) {
		this.delegate = delegate;
	}

	@Override
	public int passCount() {
		return delegate.passCount() + 1;
	}

	@Override
	public ComparisonStatus compareItem(TestingContext context, int pass, int itemIndex) {
		return delegate.compareItem(context, pass - 1, itemIndex);
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
