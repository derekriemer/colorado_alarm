package com.robobrandon.simpleweather;

/*
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        AlarmActivity inst = AlarmActivity.instance();
        inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
*/
import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("We are in the receiver", "Yes!");

        //fetch extra string from the intent
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("What is the key?", "get_your_string");

        //fetch the extra longs from the intent
        //tells the app which value the user picked from the drop down spinner
        Integer get_weather_choice = intent.getExtras().getInt("weather_choice");

        Log.e("Weather choice is ", get_weather_choice.toString());

        //create an intent to this ringtone service
        Intent service_intent = new Intent(context,RingtonePlayingService.class);

        //pass the extra string from alarmreceiver to the ringtoneplaying service
        service_intent.putExtra("extra", get_your_string);
        //pass the extra integer from the receiver to the ringtone playing service
        service_intent.putExtra("weather_choice", get_weather_choice);

        //start the ringtone service
        context.startService(service_intent);
    }
}
