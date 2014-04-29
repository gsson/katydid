package se.fnord.katydid;

public interface NameFormatter {
	String formatChild(int itemIndex, DataTester child);
	String formatItem(String name, int itemIndex);
}
