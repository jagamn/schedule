package com.task.schedule.job;

import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Function: 定时任务模板. <br/>
 * date: 2018年2月11日 下午2:46:07 <br/>
 *
 * @author jianghm
 */
@DisallowConcurrentExecution
@Service
public class JobExample implements Job {

	private Logger logger = LoggerFactory.getLogger(JobExample.class);

	@Override
	public void execute(JobExecutionContext paramJobExecutionContext) throws JobExecutionException {
		logger.info("-----定时任务开始-------------------------------------");
		JobDataMap dataMap = paramJobExecutionContext.getJobDetail().getJobDataMap();
		Map<String, Object> paramMap = (Map<String, Object>) dataMap.get("paramMap");
		logger.info(paramMap.toString());
		logger.info("-----定时任务结束-------------------------------------");
	}

}
