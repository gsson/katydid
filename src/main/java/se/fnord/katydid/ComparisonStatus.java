package se.fnord.katydid;

public enum ComparisonStatus {
	CONTINUE(true), SKIP(CONTINUE, false), ABORT(false);

	private final ComparisonStatus propagate;
	private final boolean shouldContinue;

	private ComparisonStatus(ComparisonStatus propagate, boolean shouldContinue) {
		this.propagate = propagate;
		this.shouldContinue = shouldContinue;
	}

	ComparisonStatus(boolean shouldContinue) {
		this.propagate = this;
		this.shouldContinue = shouldContinue;
	}

	public ComparisonStatus propagate() {
		return propagate;
	}

	public boolean shouldContinue() {
		return shouldContinue;
	}

	public static ComparisonStatus worst(ComparisonStatus a, ComparisonStatus b) {
		if (a.ordinal() > b.ordinal())
			return a;
		return b;
	}
}
