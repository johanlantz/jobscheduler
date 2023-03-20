package com.example.jobscheduler_ctsd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.util.logging.Logger;


public class BatterySensor  {

    private static final Logger log = Logger.getLogger(BatterySensor.class.getSimpleName());
    private static BatterySensor instance;
    private BroadcastReceiver receiver = null;
    private Context context;

    private BatterySensor(Context context) {
        this.context = context;
    }

    public static synchronized BatterySensor getSensor(final Context context) {
        if (instance == null) {
            instance = new BatterySensor(context);
            instance.start();
        }
        return instance;
    }

    public boolean start() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                log.info("Battery broadcast received: " + action);
                poll(action);
            }
        };
        context.registerReceiver(receiver, new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED"));
        context.registerReceiver(receiver, new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED"));
        context.registerReceiver(receiver, new IntentFilter("android.intent.action.ACTION_BATTERY_OKAY"));
        context.registerReceiver(receiver, new IntentFilter("android.intent.action.ACTION_BATTERY_LOW"));

        context.registerReceiver(receiver, new IntentFilter("android.os.action.DEVICE_IDLE_MODE_CHANGED"));
        context.registerReceiver(receiver, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
        log.info("Battery sensor started.");
        return true;
    }

    public void stop() {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public void poll(String action) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) / 100.0;
        log.info("BatteryLevel is:" + batteryLevel + " action:" + action);
    }
}
