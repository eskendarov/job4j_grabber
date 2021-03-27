package ru.job4j;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ResourceBundle;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * AlertRabbit выводит сообщение с указанным интервалом времени.
 *
 * @author Enver Eskendarov
 * @version 1.0 28/03/2021
 */
public class AlertRabbit {

    public static void main(String[] args) {
        final ResourceBundle res = ResourceBundle.getBundle("rabbit");
        final int value = Integer.parseInt(res.getString("rabbit.interval"));
        try {
            final Scheduler scheduler = StdSchedulerFactory
                    .getDefaultScheduler();
            scheduler.start();
            final JobDetail job = newJob(Rabbit.class).build();
            final SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(value)
                    .repeatForever();
            final Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        @Override
        public void execute(JobExecutionContext context)
                throws JobExecutionException {
            System.out.printf("Rabbit runs here ... [%s]%s",
                    context.getFireTime(), System.lineSeparator());
        }
    }
}