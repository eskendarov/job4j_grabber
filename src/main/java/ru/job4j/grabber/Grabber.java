package ru.job4j.grabber;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.html.Grab;
import ru.job4j.html.Parse;
import ru.job4j.html.SqlRuParse;
import ru.job4j.html.Store;

import java.util.ResourceBundle;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Grabber - записывает данные с сайта sql.ru/forum/job-offers/ в БД
 * с указанным интервалом времени.
 *
 * @author Enver Eskendarov
 * @version 1.0 10/05/2021
 */
public class Grabber implements Grab {

    private final ResourceBundle res = ResourceBundle.getBundle("grabber");

    public Store store() {
        return new PsqlStore(res);
    }

    public Scheduler scheduler() throws SchedulerException {
        final Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler)
            throws SchedulerException {
        final JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        final JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        final int interval = Integer.parseInt(res.getString("time"));
        final SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(interval)
                .repeatForever();
        final Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            final JobDataMap map = context.getJobDetail().getJobDataMap();
            final Store store = (Store) map.get("store");
            final Parse parse = (Parse) map.get("parse");
            parse.list("http://sql.ru/forum/job-offers/").forEach(
                    post -> store.save(parse.detail(post.getLink()))
            );
        }
    }


    public static void main(String[] args) throws Exception {
        final Grabber grab = new Grabber();
        final Scheduler scheduler = grab.scheduler();
        final Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
    }
}
