package com.task.schedule.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.task.schedule.api.dto.TaskJob;

public class JobExceptionDealListener implements JobListener {

	private Logger logger = LoggerFactory.getLogger(JobExceptionDealListener.class);

	private String name;

	public JobExceptionDealListener(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext paramJobExecutionContext) {

	}

	@Override
	public void jobToBeExecuted(JobExecutionContext paramJobExecutionContext) {
		logger.info("==============================================================================");
		logger.info("job listener begin !");
		logger.info("+++++++++++++++++++++++++++++++" + this.name + "+++++++++++++++++++++++++++++++");
		logger.info("==============================================================================");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext paramJobExecutionContext, JobExecutionException paramJobExecutionException) {
		logger.info("==============================================================================");
		logger.info("job listener end !");
		TaskJob taskJob = new TaskJob();
		taskJob.setJobId(getName());
		if (null != paramJobExecutionException) {
			paramJobExecutionException.setUnscheduleAllTriggers(true);
			taskJob.setResult("exception");
		}
		else {
			taskJob.setResult("success");
		}
		logger.info(taskJob.toString());
		logger.info("==============================================================================");
	}

}
