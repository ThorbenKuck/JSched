package com.github.thorbenkuck.schedule;

import java.util.concurrent.Future;

class JobRunner implements Runnable {

	private final Job job;
	JobRunnerContext jobContext;
	long millisDelay = 0;
	long initialDelay = 0;

	JobRunner(Job job) {
		this(job, new JobRunnerContext());
	}

	JobRunner(Job job, JobRunnerContext jobContext) {
		this.job = job;
		this.jobContext = jobContext;
	}

	private void error(Throwable throwable) {
		jobContext.invalidate();
		job.error(jobContext, throwable);
		jobContext.completableFuture.completeExceptionally(throwable);
	}

	@Override
	public void run() {
		jobContext.millisDelay = millisDelay;
		try {
			Thread.sleep(initialDelay);
		} catch (InterruptedException e) {
			error(e);
		}
		while (jobContext.isRunning() && jobContext.isValid()) {
			try {
				job.execute(jobContext);
			} catch (Exception e) {
				error(e);
				return;
			}
			if (!jobContext.isRunning()) {
				return;
			}

			try {
				Thread.sleep(millisDelay);
			} catch (InterruptedException e) {
				error(e);
			}

			millisDelay = jobContext.getDelay();
		}
	}

	Future getFuture() {
		return jobContext.completableFuture;
	}
}
