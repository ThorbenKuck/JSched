package com.github.thorbenkuck.schedule;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface JobContext {

	void put(Object key, Object value);

	<T> Optional<T> get(Object key, Class<T> type);

	Optional<Object> get(Object key);

	<T> T getNullable(Object key, Class<T> type);

	Object getNullable(Object key);

	void invalidate();

	void complete();

	void complete(Object object);

	void delay(long amount, TimeUnit timeUnit);

	long getDelay();

	boolean isValid();

	boolean isRunning();

}
