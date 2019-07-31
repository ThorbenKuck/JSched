package com.github.thorbenkuck.schedule;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class JobRunnerContext implements JobContext {

	protected final Map<Object, Object> valueMap = new HashMap<>();
	protected boolean valid = true;
	protected boolean running = true;
	protected long millisDelay;
	protected final CompletableFuture completableFuture = new CompletableFuture();

	@Override
	public void put(Object key, Object value) {
		valueMap.put(key, value);
	}

	@Override
	public <T> Optional<T> get(Object key, Class<T> type) {
		return Optional.ofNullable(getNullable(key, type));
	}

	@Override
	public Optional<Object> get(Object key) {
		return Optional.ofNullable(getNullable(key));
	}

	@Override
	public <T> T getNullable(Object key, Class<T> type) {
		Object object = valueMap.get(key);
		if(object == null || !type.isAssignableFrom(object.getClass())) {
			return null;
		}

		return (T) object;
	}

	@Override
	public Object getNullable(Object key) {
		return valueMap.get(key);
	}

	@Override
	public void invalidate() {
		valid = false;
		running = false;
		completableFuture.cancel(true);
	}

	@Override
	public void complete() {
		running = false;
		completableFuture.complete(null);
	}

	@Override
	public void complete(Object object) {
		running = false;
		completableFuture.complete(object);
	}

	@Override
	public void delay(long amount, TimeUnit timeUnit) {
		millisDelay = timeUnit.toMillis(amount);
	}

	@Override
	public long getDelay() {
		return millisDelay;
	}

	@Override
	public boolean isValid() {
		return valid && !completableFuture.isCancelled();
	}

	@Override
	public boolean isRunning() {
		return running && !completableFuture.isDone();
	}
}
