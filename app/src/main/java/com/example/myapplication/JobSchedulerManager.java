package com.example.myapplication;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import org.json.JSONObject;

import java.util.List;
import java.util.logging.Logger;

public class JobSchedulerManager {

    private static final Logger log = Logger.getLogger(JobSchedulerManager.class.getSimpleName());
    private static final int SENSOR_JOB = 1099;

    private static BatterySensor batterySensor;

    public static void scheduleJobs(Context context) {
        log.info("Scheduling background jobs.");
        if(!(getPendingJobs(context)).isEmpty()) {
            log.info("JobScheduler already has jobs scheduled, they will be rescheduled by the system.");
        }
        com.example.myapplication.Logger.start(context);
        batterySensor = BatterySensor.getSensor(context);
        batterySensor.start();
        scheduleSensorJob(context);
    }

    public static List<JobInfo> getPendingJobs(Context context) {
        return ((JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE)).getAllPendingJobs();
    }

    public static void cancelJobs(Context context) {
        log.info("Stoppping all scheduled jobs");
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
    }

    public static void scheduleSensorJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int pollPeriod = 3600; 
        JobInfo jobInfo;
        jobInfo = new JobInfo.Builder(SENSOR_JOB,
                new ComponentName(context, JobSchedulerService.class)).setPeriodic(pollPeriod * 1000)
                        .setPersisted(true).setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE).build();
        
        int result = jobScheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS) {
            log.info("JobScheduler returned RESULT_SUCCESS when scheduling SENSOR_JOB with pollPeriod = "
                    + pollPeriod);
        } else {
            log.severe("JobScheduler returned RESULT_FAILURE when scheduling SENSOR_JOB with pollPeriod = "
                    + pollPeriod);
        }
    }
}
