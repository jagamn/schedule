package com.task.schedule.api.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * Function: 定时任务对象. <br/>
 * Date: 2018年2月11日 上午11:39:05 <br/>
 *
 * @author jianghm
 */
public class TaskJob implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 任务的Id，一般为所定义Bean的ID
	 */
	private String jobId;
	
	/**
	 * 任务的描述
	 */
	private String jobName;
	
	/**
	 * 定时任务class
	 */
	private String jobClass;
	
	/**
	 * 定时任务运行时间表达式
	 */
	private String jobCronExpression;
	
	/**
	 * 定时任务状态
	 */
	private String jobStatus;
	
	/**
	 * 任务所属组的名称
	 */
	private String jobGroup;
	
	/**
	 * 定时任务执行结果
	 */
	private String result;
	
	/**
	 * 其他参数
	 */
	private Map<String, Object> param;

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

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}

	public String getJobCronExpression() {
		return jobCronExpression;
	}

	public void setJobCronExpression(String jobCronExpression) {
		this.jobCronExpression = jobCronExpression;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "TaskJob [jobId=" + jobId + ", jobName=" + jobName + ", jobClass=" + jobClass + ", jobCronExpression=" + jobCronExpression + ", jobStatus=" + jobStatus
				+ ", jobGroup=" + jobGroup + ", result=" + result + ", param=" + param + "]";
	}

}
