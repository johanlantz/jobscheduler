//  Copyright © Koa Health B.V. All rights reserved.
package com.example.jobscheduler_ctsd;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;


public class Logger {
    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Logger.class.getSimpleName());

    static class VerySimpleFormatter extends Formatter {
        private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

        @Override
        public String format(final LogRecord record) {
            return String.format(
                    "%1$s %2$-7s %3$-25s %4$4d   %5$s\n",
                    new SimpleDateFormat(PATTERN).format(
                            new Date(record.getMillis())),
                    record.getLevel().getName(), record.getLoggerName(), Thread.currentThread().getId(), formatMessage(record));
        }
    }

    public static String getVersionName(Context context) {
        try{
            String versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try{
            int versionCode = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static boolean isFileHandlerActive() {
        Handler[] handlers = log.getParent().getHandlers();
        boolean fileHandlerActive = false;
        for (Handler h: handlers) {
            if (h instanceof FileHandler){
                fileHandlerActive = true;
            }
        }
        return fileHandlerActive;
    }

    public static void start(Context context) {
        Handler logHandler;
        log.getParent().setLevel(Level.FINE);
        final int MAX_LOG_COUNT = 1;

        File logDir = new File(context.getFilesDir() + File.separator + "my_logs/");
        logDir.mkdirs();
        //File logDir = new File(context.getFilesDir() + File.separator);

        //File file = new File(context.getFilesDir() + File.separator + "my_logs/logfile.txt");
        //if(file.exists()) {
        //    long l = file.length();
        //}

        if (!isFileHandlerActive()) {
            String filePath = logDir + "/logfile.txt";

            try {
                logHandler = new FileHandler(filePath, 100 * 1000000, MAX_LOG_COUNT, true);
                logHandler.setFormatter(new VerySimpleFormatter());
                log.getParent().addHandler(logHandler);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            log.info("******************logfile handler created******************");
            log.info("Product version: " + getVersionName(context) + " Build number: " + getVersionCode(context));
        }
    }
}
