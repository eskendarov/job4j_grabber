package ru.job4j;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * AlertRabbit записывает сообщение в БД с указанным интервалом времени.
 *
 * @author Enver Eskendarov
 * @version 1.0 28/03/2021
 */
public class AlertRabbit {


    public static void main(String[] args) {
        final ResourceBundle res = ResourceBundle.getBundle("grabber");
        final int value = Integer.parseInt(res.getString("rabbit.interval"));
        try (Connection connection = DriverManager.getConnection(
                res.getString("url"),
                res.getString("username"),
                res.getString("password"))
        ) {
            final Scheduler sch = StdSchedulerFactory.getDefaultScheduler();
            sch.start();
            final JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            final JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            final SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(value)
                    .repeatForever();
            final Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            sch.scheduleJob(job, trigger);
            Thread.sleep(10000);
            sch.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            final Connection connection = (Connection) context.getJobDetail()
                    .getJobDataMap().get("connection");
            try (Statement statement = connection.createStatement()) {
                statement.execute("insert into rabbit(created_date)"
                        + " values(current_timestamp)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
