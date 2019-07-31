package com.github.thorbenkuck.schedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Scheduler {

	private static final Scheduler DEFAULT_SCHEDULER = new Scheduler();
	private final ExecutorService dispatcher = Executors.newCachedThreadPool();

	public static Scheduler access() {
		return DEFAULT_SCHEDULER;
	}

	public SchedulerBuilder schedule(Job job) {
		return new SchedulerBuilder(job);
	}

	public class SchedulerBuilder {

		private final JobRunner jobRunner;

		private SchedulerBuilder(Job job) {
			this.jobRunner = new JobRunner(job);
		}

		public SchedulerBuilder withDelay(long amount, TimeUnit timeUnit) {
			jobRunner.millisDelay = timeUnit.toMillis(amount);

			return this;
		}

		public SchedulerBuilder withInitialDelay(long amount, TimeUnit timeUnit) {
			jobRunner.initialDelay = timeUnit.toMillis(amount);

			return this;
		}

		public Future completable() {
			dispatcher.execute(jobRunner);

			return jobRunner.getFuture();
		}

		public void now() {
			dispatcher.execute(jobRunner);
		}
	}
}
