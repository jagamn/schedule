package com.task.schedule.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.task.schedule.api.dto.TaskJob;
import com.task.schedule.domain.SchedulingJob;
import com.task.schedule.listener.JobExceptionDealListener;

@Component("quartzManager")
public class QuartzManager {
	
	private static Logger logger = LoggerFactory.getLogger(QuartzManager.class);
	
	@Autowired
	Scheduler scheduler;

	public void enableCronSchedule(List<TaskJob> list) {
		if (null == list || list.size() == 0) {
			return;
		}
		for (TaskJob task : list) {
			SchedulingJob job = new SchedulingJob();
			job.setJobId(task.getJobId());
			job.setJobName(task.getJobName());
			job.setJobGroup(task.getJobGroup());
			job.setCronExpression(task.getJobCronExpression());
			try {
				String className = task.getJobClass().trim();
				Class clazz = Class.forName(className);
				job.setStateFulljobExecuteClass(clazz);
			}
			catch (Exception e) {
				logger.error("异常 : ", e);
				continue;
			}
			JobDataMap paramsMap = new JobDataMap();
			paramsMap.put("jobName", task.getJobName());
			paramsMap.put("jobGroup", task.getJobGroup());
			paramsMap.put("paramMap", task.getParam());
			this.enableCronSchedule(job, paramsMap, true);

			logger.info("系统结束初始化任务：[ jobId=" + task.getJobId() + "  jobName=" + task.getJobName() + "]");
		}
	}

	public void enableCronScheduleNow(List<TaskJob> list) {
		if (null == list || list.size() == 0) {
			return;
		}
		for (TaskJob task : list) {
			SchedulingJob job = new SchedulingJob();
			job.setJobId(task.getJobId());
			job.setJobName(task.getJobName());
			job.setJobGroup(task.getJobGroup());
			job.setCronExpression(task.getJobCronExpression());
			try {
				String className = task.getJobClass().trim();
				Class clazz = Class.forName(className);
				job.setStateFulljobExecuteClass(clazz);
			}
			catch (Exception e) {
				logger.error("异常 : ", e);
				continue;
			}
			JobDataMap paramsMap = new JobDataMap();
			paramsMap.put("jobName", task.getJobName());
			paramsMap.put("jobGroup", task.getJobGroup());
			paramsMap.put("paramMap", task.getParam());
			this.enableCronScheduleNow(job, paramsMap);

			logger.info("系统启动定时任务：[ jobId=" + task.getJobId() + "  jobName=" + task.getJobName() + "]");
		}
	}
	
	public TriggerState getJobTrigerStatus(String jobId, String jobGroupId) {
		if (StringUtils.isAnyEmpty(jobId, jobGroupId)) {
			return null;
		}
		jobId += "Trigger";
		TriggerState state = null;
		try {
			state = scheduler.getTriggerState(new TriggerKey(jobId, jobGroupId));
		}
		catch (SchedulerException e) {
			logger.error("异常 : ", e);
		}
		return state;
	}

	/**
	 * 启动一个自定义的job
	 * 
	 * @param schedulingJob 自定义的job    
	 * @param paramsMap 传递给job执行的数据    
	 * @param isStateFull 是否是一个同步定时任务，true：同步，false：异步   
	 * @return 成功则返回true，否则返回false
	 */
	public boolean enableCronSchedule(SchedulingJob schedulingJob, JobDataMap paramsMap, boolean isStateFull) {
		if (null == schedulingJob) {
			return false;
		}
		try {
			TriggerKey triggerKey = new TriggerKey(schedulingJob.getTriggerName(), schedulingJob.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 如果不存在该trigger则创建一个
			if (null == trigger) {
				JobDetail jobDetail = null;
				if (isStateFull) {
					jobDetail = JobBuilder.newJob(schedulingJob.getStateFulljobExecuteClass()).withIdentity(schedulingJob.getJobId(), schedulingJob.getJobGroup()).build();
				}
				else {
					jobDetail = JobBuilder.newJob(schedulingJob.getJobExecuteClass()).withIdentity(schedulingJob.getJobId(), schedulingJob.getJobGroup()).build();
				}
				JobDataMap datamap = jobDetail.getJobDataMap();
				datamap.putAll(paramsMap);
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulingJob.getCronExpression());
				trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				Matcher<JobKey> matcher = KeyMatcher.keyEquals(new JobKey(schedulingJob.getJobId(), schedulingJob.getJobGroup()));
				scheduler.getListenerManager().addJobListener(new JobExceptionDealListener(schedulingJob.getJobId()), matcher);
				scheduler.scheduleJob(jobDetail, trigger);
			}
			else {
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulingJob.getCronExpression());
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				Matcher<JobKey> matcher = KeyMatcher.keyEquals(new JobKey(schedulingJob.getJobId(), schedulingJob.getJobGroup()));
				scheduler.getListenerManager().addJobListener(new JobExceptionDealListener(schedulingJob.getJobId()), matcher);
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		}
		catch (Exception e) {
			logger.error("创建job异常 : ", e);
			return false;
		}
		return true;
	}

	/**
	 * 立即启动一个自定义的job
	 * 
	 * @param schedulingJob 自定义的job     
	 * @param paramsMap 传递给job执行的数据     
	 * @return 成功则返回true，否则返回false
	 */
	public boolean enableCronScheduleNow(SchedulingJob schedulingJob, JobDataMap paramsMap) {
		if (schedulingJob == null) {
			return false;
		}
		try {
			TriggerKey triggerKey = new TriggerKey(schedulingJob.getTriggerName(), schedulingJob.getJobGroup());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 如果不存在该trigger则创建一个
			if (null == trigger) {
				JobDetail jobDetail = null;
				jobDetail = JobBuilder.newJob(schedulingJob.getStateFulljobExecuteClass()).withIdentity(schedulingJob.getJobId(), schedulingJob.getJobGroup()).build();

				JobDataMap datamap = jobDetail.getJobDataMap();
				datamap.putAll(paramsMap);
				// identify job with name, group
				SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().forJob(jobDetail).build();
				Matcher<JobKey> matcher = KeyMatcher.keyEquals(new JobKey(schedulingJob.getJobId(), schedulingJob.getJobGroup()));
				scheduler.getListenerManager().addJobListener(new JobExceptionDealListener(schedulingJob.getJobId()), matcher);
				scheduler.scheduleJob(jobDetail, simpleTrigger);
			}
			else {
				this.disableSchedule(schedulingJob.getJobId(), schedulingJob.getJobGroup());
				JobDetail jobDetail = null;
				jobDetail = JobBuilder.newJob(schedulingJob.getStateFulljobExecuteClass()).withIdentity(schedulingJob.getJobId(), schedulingJob.getJobGroup()).build();
				JobDataMap datamap = jobDetail.getJobDataMap();
				datamap.putAll(paramsMap);
				// identify job with name, group
				SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().forJob(jobDetail).build();
				Matcher<JobKey> matcher = KeyMatcher.keyEquals(new JobKey(schedulingJob.getJobId(), schedulingJob.getJobGroup()));
				scheduler.getListenerManager().addJobListener(new JobExceptionDealListener(schedulingJob.getJobId()), matcher);
				scheduler.scheduleJob(jobDetail, simpleTrigger);
				while (true) {
					TriggerState status = getJobTrigerStatus(schedulingJob.getJobId(), schedulingJob.getJobGroup());
					if (status != null && TriggerState.NONE.name().equals(status.name())) {
						logger.info("立即执行任务线程结束，重新启动原定时任务");
						this.enableCronSchedule(schedulingJob, paramsMap, true);
						break;
					}
					logger.info("等待立即执行任务线程结束.........");
					TimeUnit.SECONDS.sleep(5);
				}
			}
		}
		catch (Exception e) {
			logger.error("创建job异常 : ", e);
			return false;
		}
		return true;
	}

	/**
	 * 禁用一个job
	 * 
	 * @param jobId 需要被禁用的job的ID     
	 * @param jobGroupId 需要被警用的jobGroupId
	 * @return 成功则返回true，否则返回false
	 */
	public boolean disableSchedule(String jobId, String jobGroupId) {
		if (StringUtils.isAnyEmpty(jobId, jobGroupId)) {
			return false;
		}
		try {
			Trigger trigger = getJobTrigger(jobId, jobGroupId);
			if (null != trigger) {
				JobKey jobKey = new JobKey(jobId, jobGroupId);
				scheduler.deleteJob(jobKey);
				scheduler.getListenerManager().removeJobListener(jobId);
			}
		}
		catch (SchedulerException e) {
			logger.error("异常 : ", e);
			return false;
		}
		logger.info("系统关闭任务：[ jobId=" + jobId + "  jobGroup=" + jobGroupId + "]");
		return true;
	}

	/**
	 * 得到job的详细信息
	 * 
	 * @param jobId job的ID
	 * @param jobGroupId job的组ID     
	 * @return job的详细信息,如果job不存在则返回null
	 */
	public JobDetail getJobDetail(String jobId, String jobGroupId) {
		if (StringUtils.isAnyEmpty(jobId, jobGroupId)) {
			return null;
		}
		try {
			JobKey jobKey = new JobKey(jobId, jobGroupId);
			return scheduler.getJobDetail(jobKey);
		}
		catch (SchedulerException e) {
			logger.error("异常 : ", e);
			return null;
		}
	}

	/**
	 * 得到job对应的Trigger
	 * 
	 * @param jobId job的ID   
	 * @param jobGroupId job的组ID    
	 * @return job的Trigger,如果Trigger不存在则返回null
	 */
	public Trigger getJobTrigger(String jobId, String jobGroupId) {
		if (StringUtils.isAnyEmpty(jobId, jobGroupId)) {
			return null;
		}
		jobId += "Trigger";
		try {
			return scheduler.getTrigger(new TriggerKey(jobId, jobGroupId));
		}
		catch (SchedulerException e) {
			logger.error("异常 : ", e);
			return null;
		}
	}

}