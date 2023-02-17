# jobscheduler

The intention with this project is to reproduce a potential CTS issue described [here](https://issuetracker.google.com/issues/262750721).

Basically the suspicion is that after an app is updated, the application might not continue to run on some device unless the vendor proprietary "Autostart" option is enabled in Settings. This has been tested mainly on a Xiaomi device for now.

This limited app uses the JobScheduler to check the battery level once per hour. It also subscribes to various battery related broadcasts. The information is stored in a logfile that can be emailed using the mail icon in the app.

