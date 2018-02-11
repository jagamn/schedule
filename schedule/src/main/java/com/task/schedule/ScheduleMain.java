package com.task.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleMain {
	private static Logger logger = LoggerFactory.getLogger(ScheduleMain.class);

	public static void main(String[] args) {
		logger.info("ScheduleMain start!");
		com.alibaba.dubbo.container.Main.main(args);
		logger.info("ScheduleMain end!");
	}
}