package com.github.thorbenkuck.schedule;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SchedulerTest {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		Integer integer = (Integer) Scheduler.access()
				.schedule(new TestJob())
				.withInitialDelay(10, TimeUnit.SECONDS)
				.withDelay(10, TimeUnit.SECONDS)
				.completable()
				.get();

		System.out.println("Finished with " + integer);
	}

	private static class TestJob implements Job {

		private int count = 0;
		private long lastExecution = System.currentTimeMillis();

		@Override
		public void execute(JobContext jobContext) {
			++count;
			System.out.println(count);
			if(count == 6) {
				jobContext.complete(count);
			}

			long current = System.currentTimeMillis();
			long delaySinceLast = current - lastExecution;
			lastExecution = current;
			System.out.println("Seconds since last execution" + TimeUnit.MILLISECONDS.toSeconds(delaySinceLast));

		}
	}

}
