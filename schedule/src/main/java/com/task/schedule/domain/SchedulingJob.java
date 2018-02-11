package com.task.schedule.domain;

import org.quartz.Job;
import org.quartz.Scheduler;

public class SchedulingJob {

	/**
	 * 任务的Id，一般为所定义Bean的ID
	 */
	private String jobId;
	
	/**
	 * 任务的描述
	 */
	private String jobName;
	
	/**
	 * 任务所属组的名称
	 */
	private String jobGroup;
	
	/**
	 * 定时任务运行时间表达式
	 */
	private String cronExpression;
	
	/**
	 * 同步的执行类，需要从StatefulMethodInvokingJob继承
	 */
	private Class<Job> stateFulljobExecuteClass;
	
	/**
	 * 异步的执行类，需要从MethodInvokingJob继承
	 */
	private Class<Job> jobExecuteClass;

	/**
	 * 得到该job的Trigger名字
	 * 
	 * @return
	 */
	public String getTriggerName() {
		return this.getJobId() + "Trigger";
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		if (null == jobGroup) {
			jobGroup = Scheduler.DEFAULT_GROUP;
		}
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Class<Job> getStateFulljobExecuteClass() {
		return stateFulljobExecuteClass;
	}

	public Class<Job> getJobExecuteClass() {
		return jobExecuteClass;
	}

	public void setJobExecuteClass(Class<Job> jobExecuteClass) {
		this.jobExecuteClass = jobExecuteClass;
	}

	public void setStateFulljobExecuteClass(Class<Job> stateFulljobExecuteClass) {
		this.stateFulljobExecuteClass = stateFulljobExecuteClass;
	}

}
