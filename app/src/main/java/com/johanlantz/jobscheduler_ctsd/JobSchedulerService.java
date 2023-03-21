package com.johanlantz.jobscheduler_ctsd;
import android.app.job.JobParameters;
import android.app.job.JobService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class JobSchedulerService extends JobService {
    private static final Logger log = Logger.getLogger(JobSchedulerService.class.getSimpleName());
    public static final int DEFAULT_SENSOR_POLL_DURATION = 5;
    private ScheduledExecutorService scheduler;

    private static int sensorJobCounter = 0;

    public JobSchedulerService() {
        scheduler = Executors.newScheduledThreadPool(2);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        com.johanlantz.jobscheduler_ctsd.Logger.configure(getApplicationContext());
        log.info("Creating new JobSchedulerService instance");
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        log.info("Destroying JobSchedulerService instance");
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        scheduler.schedule(() -> startSensorJob(params), 0, TimeUnit.SECONDS);
        return true;
    }

    private void startSensorJob(final JobParameters params) {
        log.info("JobScheduler is starting (" + params.getJobId() + ") (sensorJobCounter=" + ++sensorJobCounter + ")");
        
        // Here we manually collect data from sensors during 5 seconds
        BatterySensor.getSensor(getApplicationContext()).poll("poll");

        scheduler.schedule(() -> {
            log.info("JobScheduler signals a finished job");
            jobFinished(params, false);
        }, DEFAULT_SENSOR_POLL_DURATION, TimeUnit.SECONDS);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        log.warning("Warning: Job " + params.getJobId() + " stopped by system");
        return false;
    }
}
