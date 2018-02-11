package com.task.schedule.service;

import java.util.List;

import org.quartz.Trigger.TriggerState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.schedule.api.IScheduleService;
import com.task.schedule.api.dto.TaskJob;

@Service("scheduleService")
public class ScheduleServiceImpl implements IScheduleService {

	@Autowired
	private QuartzManager quartzManager;

	@Override
	public void enableTaskList(List<TaskJob> taskJobs) {
		if (null == taskJobs || taskJobs.size() == 0) {
			return;
		}
		quartzManager.enableCronSchedule(taskJobs);
	}

	@Override
	public void disableTaskList(List<TaskJob> taskJobs) {
		if (null == taskJobs || taskJobs.size() == 0) {
			return;
		}
		for (TaskJob taskJob : taskJobs) {
			quartzManager.disableSchedule(taskJob.getJobId(), taskJob.getJobGroup());
		}
	}

	@Override
	public void doTaskListNow(List<TaskJob> taskJobs) {
		if (null == taskJobs || taskJobs.size() == 0) {
			return;
		}
		quartzManager.enableCronScheduleNow(taskJobs);
	}

	@Override
	public List<TaskJob> getTaskJobRunStatus(List<TaskJob> taskJobs) {
		if (null == taskJobs || taskJobs.size() == 0) {
			return null;
		}
		TriggerState status = null;
		for (TaskJob job : taskJobs) {
			status = quartzManager.getJobTrigerStatus(job.getJobId(), job.getJobGroup());
			job.setJobStatus(status.name());
		}
		return taskJobs;
	}
}
