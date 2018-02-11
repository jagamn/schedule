

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.task.schedule.api.IScheduleService;
import com.task.schedule.api.dto.TaskJob;

public class TaskTest {
	
	public static void main(String[] args) {
		ReferenceConfig<IScheduleService> reference = new ReferenceConfig<IScheduleService>();
		reference.setInterface(IScheduleService.class);
		ApplicationConfig application = new ApplicationConfig();
		application.setName("scheduleService");
		application.setRegistry(new RegistryConfig("zookeeper://192.168.23.139:2181"));
		reference.setApplication(application);
		reference.setTimeout(500000);
		reference.setRetries(0);
		ReferenceConfigCache cache = ReferenceConfigCache.getCache();
		IScheduleService pservice = cache.get(reference);
		List<TaskJob> taskJobs = new ArrayList<TaskJob>();
		TaskJob taskJob = new TaskJob();
		taskJob.setJobId("0001");
		taskJob.setJobName("测试任务");
		taskJob.setJobCronExpression("13 0/1 * * * ?");
		taskJob.setJobClass("com.task.schedule.job.JobExample");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("test", "0123456789");
		taskJob.setParam(param);
		taskJobs.add(taskJob);
		pservice.enableTaskList(taskJobs);
	}

}
