# jobscheduler

The intention with this project is to reproduce a potential CTS issue described [here](https://issuetracker.google.com/issues/262750721).

Basically the suspicion is that after an app is updated, the application might not continue to run on some devices unless the vendor proprietary "Autostart" option is enabled in Settings. This has been tested mainly on a Xiaomi device for now.

This limited app uses the JobScheduler to check the battery level once per hour. It also subscribes to various battery related broadcasts. The information is stored in a logfile that can be emailed using the mail icon in the app.

## Scenarios to check:

### Device reboot
- Press the button to start the JobScheduler
- Confirm the button now says the JobScheduler is running
- Reboot the device
- Start app to ensure the button still says that the JobScheduler is running. If it says "Start JobScheduler" the CTC condition is not met.

### App update
- Press the button to start the JobScheduler
- Confirm the button now says the JobScheduler is running
- Publish an update of the app on Google Play.
- Wait long enough to ensure the phone has updated the app.
- Start app to ensure the button still says that the JobScheduler is still running. If it says "Start JobScheduler" the CTC condition is not met.


