package se.fnord.katydid;

public enum ComparisonStatus {
	EQUAL, NOT_EQUAL, ERROR;
	public static ComparisonStatus worst(ComparisonStatus a, ComparisonStatus b) {
		if (a.ordinal() > b.ordinal())
			return a;
		return b;
	}
}
