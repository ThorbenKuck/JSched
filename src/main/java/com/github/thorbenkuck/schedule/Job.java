package com.github.thorbenkuck.schedule;

public interface Job {

	void execute(JobContext jobContext);

	default void error(JobContext jobContext, Throwable throwable) {
	}

}
