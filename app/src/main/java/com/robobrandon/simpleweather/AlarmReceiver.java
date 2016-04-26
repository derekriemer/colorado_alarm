package com.robobrandon.simpleweather;
//resource <https://www.youtube.com/watch?v=KseXIsTLXaY&list=PL4uut9QecF3DLAacEoTctzeqTyvgzqYwA&index=2#t=1.8585>
// and Android Studio documentation.
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

        Log.e("Wheather choice is ", get_weather_choice.toString());

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

