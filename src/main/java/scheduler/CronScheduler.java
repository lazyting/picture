package main.java.scheduler;

import main.java.constant.ProjectConstant;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by Lei on 2019/10/19.
 */
public class CronScheduler {

    public static void task() throws SchedulerException {
        // Initiate a Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // Retrieve a scheduler from schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();

        // Initiate JobDetail with job name, job group, and executable job class
        JobDetail jobDetail = JobBuilder.newJob(GetPicJob.class)
                .withIdentity("job1", "group1")
                .build();
        // Initiate CronTrigger with its name and group name
        // 每5秒运行一次job
        CronScheduleBuilder cronScheduler = CronScheduleBuilder.cronSchedule(ProjectConstant.cron);
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
                .withIdentity("GetPicTrigger", "PicGroup")
                .withSchedule(cronScheduler)
                .startNow();
        Trigger trigger = triggerBuilder.build();
        // schedule a job with JobDetail and Trigger
        scheduler.scheduleJob(jobDetail, trigger);
        // start the scheduler
        scheduler.start();


    }
}
