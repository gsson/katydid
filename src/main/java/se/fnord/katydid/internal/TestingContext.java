package se.fnord.katydid.internal;

import java.nio.ByteBuffer;
import java.util.*;

public class TestingContext {
	private final ByteBuffer buffer;
	private final int startPosition;
	private final Deque<String> names;

	public TestingContext(ByteBuffer buffer) {
		this.buffer = buffer;
		this.startPosition = buffer.position();
		this.names = new ArrayDeque<>();
	}

	int globalPosition(int localPosition) {
		return this.startPosition + localPosition;
	}

	public ByteBuffer buffer() {
		return buffer;
	}

	public void down(String name) {
		names.addLast(names.isEmpty() ? name : (name() + "." + name));
	}

	public void up() {
		names.pollLast();
	}

	public String name() {
		return names.peekLast();
	}
}
