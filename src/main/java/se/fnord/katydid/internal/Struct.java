package se.fnord.katydid.internal;

import se.fnord.katydid.ComparisonStatus;
import se.fnord.katydid.DataTester;

import java.nio.ByteBuffer;

public class Struct extends CompositeDataTester {
	public Struct(String name, DataTester... values) {
		super(name, values);
	}

	@Override
	public String formatChild(int itemIndex, DataTester child) {
			return "." + child.name();
	}
}
