package com.task.schedule.api;

import java.util.List;

import com.task.schedule.api.dto.TaskJob;

/**
 * Function: 定时任务操作接口. <br/>
 * Date: 2018年2月11日 上午11:37:17 <br/>
 *
 * @author jianghm
 */
public interface IScheduleService {
	
	/**
	 * enableTaskList:新增/启动定时任务. <br/>
	 *
	 * @param taskJobs
	 */
	void enableTaskList(List<TaskJob> taskJobs);
	
	/**
	 * disableTaskList:停止定时任务. <br/>
	 *
	 * @param taskJobs
	 */
	void disableTaskList(List<TaskJob> taskJobs);
	
	/**
	 * doTaskListNow:立即新增/启动定时任务. <br/>
	 *
	 * @param taskJobs
	 */
	void doTaskListNow(List<TaskJob> taskJobs);
	
	/**
	 * getTaskJobRunStatus:获取定时任务运行状态. <br/>
	 *
	 * @param taskJobs
	 * @return
	 */
	List<TaskJob> getTaskJobRunStatus(List<TaskJob> taskJobs);

}

