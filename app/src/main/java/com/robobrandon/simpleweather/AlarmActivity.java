package com.robobrandon.simpleweather;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;


public class AlarmActivity extends Activity {


    ImageView mImageView;
    TextView mTxtDegrees, mTxtWeather, mTxtError;

    //merging AlarmActivity into MainActivity
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static MainActivity inst;
    private TextView update_text;
    Context context;

    /**
     *
     * @return active AlarmActivity object to be used elsewhere
     */
    public static AlarmActivity instance() {
        return instance();
    }

    //merging AlarmActivity into MainActivity
    @Override
    public void onStart() {
        super.onStart();
//        inst = this;
    }

    /**
     * Instantiate widget and bind to system calendar data via the android alarm service
     *
     * @param savedInstanceState from previously running instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Inside", "on create.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout); //merging AlarmActivity into MainActivity
        this.context = this;

        //merging AlarmActivity into MainActivity
        //Initialize alarm manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Initialize timepicker
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        //Initialize text update box
        update_text = (TextView) findViewById(R.id.update_text);
        //Create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();
        //Create intent to the alarmreceiver class
        final Intent my_intent = new Intent(this.context, AlarmReceiver.class);

        //Initialize button on
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)//to make .getHour
            @Override
            public void onClick(View v) {
                //setting calendar with the hour and minute we picked on the time picker.
                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());


                //get the int values of the hour and minutes
                int hour = alarmTimePicker.getHour();
                int minute = alarmTimePicker.getMinute();

                //string converts int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                //to make time non-military
                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }
                //to make minutes look good (example '02' instead of '2')
                if (minute < 10) {
                    minute_string = "0" + String.valueOf(minute);
                }
                // Method that changes the updates text box.
                set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);
                //create a pending intent that delays the intent until the specified calendar time.
                pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                //set the alarm manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }
        });
        //Initialize button off
        Button alarm_off = (Button) findViewById(R.id.alarm_off);
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Method that changes the updates text box.
                set_alarm_text("Alarm off.");
                //cancel the alarm
                alarmManager.cancel(pendingIntent);

                //put extra sring into my_intent to tell the clock that you pressed the alarm off button.
                my_intent.putExtra("extra", "alarm off");

                //to prevent null pointer exception
                my_intent.putExtra("weather_choice", choose_weather_sound);

                //stop the ringtone. Sends signal to
                // AlarmReceiver that sends it to
                //RingtonePlayingService
                sendBroadcast(my_intent);
            }
        });
    }
    long choose_weather_sound;

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    public void setAlarmText(String alarmText) {
        update_text.setText(alarmText);
    }

    // UI Method
//    public void setAlarm(View view){
//        // Build our Intent
//        Intent intent = new Intent(this, MainActivity.class);
//        // Don't need to pass anything, just transition.
//        startActivity(intent);
//    }

}
