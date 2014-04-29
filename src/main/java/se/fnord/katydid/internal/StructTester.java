package se.fnord.katydid.internal;

import se.fnord.katydid.DataTester;

public class StructTester extends CompositeTester {
	public StructTester(String name, DataTester... values) {
		super(name, values);
	}

	@Override
	public String formatChild(int itemIndex, DataTester child) {
			return "." + child.name();
	}
}
