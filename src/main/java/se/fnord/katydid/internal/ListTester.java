package se.fnord.katydid.internal;

import se.fnord.katydid.DataTester;

public class ListTester extends CompositeTester {
	public ListTester(String name, DataTester... values) {
		super(name, values);
	}

	@Override
	public String formatChild(int itemIndex, DataTester child) {
		checkItemIndex(itemIndex);
		return "[" + itemIndex + "]";
	}
}
