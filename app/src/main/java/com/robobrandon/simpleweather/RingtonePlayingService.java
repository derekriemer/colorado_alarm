package com.robobrandon.simpleweather;

import android.app.Notification;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class RingtonePlayingService extends Service {
    MediaPlayer media_song;
    int startId;
    boolean isRunning;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //make .build() to work with this version
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("Local service", "Received start id " + startId + ":" + intent);
        //fetch the extra_string from alarm_on/off values
        String state = intent.getExtras().getString("extra");
        Log.e("Ringtone extra is ", state);
        //fetch the weather sound choice integer values
        Integer weather_sound_choice = intent.getExtras().getInt("weather_choice");

        //set up notification service
        NotificationManager notify_manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //set up an intent that goes to the name activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
        //set up pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);
        //make the notification parameters.
        //there is a bug here
        // .setSmallIcon(R.mipmap.logo)
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("An alarm is going off.")
                .setContentText("Click me.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .build();
        //this converts the extra strings from intent to start id-s values 0 and 1
        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }
        //if there is no music playing and the user pressed alarm on
        //music should start playing
        if (!this.isRunning && startId == 1) {
            Log.e("there is no music", "and we want to start");
            this.isRunning = true;
            this.startId = 0;
            //set up notification call command
            notify_manager.notify(0, notification_popup);
            //assert weather_sound_choice != null;
            //play the sound depending on the last choice id
//            if (weather_sound_choice == 0){
//                //play a random sound
//                int minimumum_number = 1;
//                int maximum_number = 3;
//                Random random_number = new Random();
//                int weather_number = random_number.nextInt(maximum_number - minimumum_number + 1)
//                        + minimumum_number;
//                if (weather_number == 1){
//                    media_song = MediaPlayer.create(this, R.raw.rain);
//                    //start the ringtone.
//                    media_song.start();
//                }
//                else if (weather_number == 2){
//                    media_song = MediaPlayer.create(this, R.raw.snow);
//                    //start the ringtone.
//                    media_song.start();
//                }
//                else if (weather_number == 3){
//                    media_song = MediaPlayer.create(this, R.raw.sun);
//                    //start the ringtone.
//                    media_song.start();
//                }
//            }
//            else if (weather_sound_choice == 1){
//                media_song = MediaPlayer.create(this, R.raw.rain);
//                //start the ringtone.
//                media_song.start();
//            }
//            else if (weather_sound_choice == 2){
//                media_song = MediaPlayer.create(this, R.raw.snow);
//                //start the ringtone.
//                media_song.start();
//            }
//            else if (weather_sound_choice == 3){
//                media_song = MediaPlayer.create(this, R.raw.sun);
//                //start the ringtone.
//                media_song.start();
//            }
//            else {
//                media_song = MediaPlayer.create(this, R.raw.sun);
//                //start the ringtone.
//                media_song.start();
//            }
        }
        //if music playing and the user presses alarm off
        //music should stop playing
        else if (this.isRunning && startId == 0) {
            Log.e("there is music", "and we want to end");
            //stop the ringtone.
            media_song.stop();
            media_song.reset();
            this.isRunning = false;
            this.startId = 0;
        }
        //if the user presses random buttons
        //bug proof the app.
        //if there is no music and user presses alarm off - do nothing.
        else if (!this.isRunning && startId == 0) {
            Log.e("there is no music", "and we want to end");
            this.isRunning = false;
            this.startId = 0;
        }
        //if there is music playing and presses alarm on - do nothing.
        else if (this.isRunning && startId == 1) {
            Log.e("there is music", "and we want to start");
            this.isRunning = true;
            this.startId = 1;
        }
        //catch odd events/
        else {
            Log.e("Somehow we are here", "and we should never reach this");
        }
        //create an instance of the media player
        //media_song = MediaPlayer.create(this, R.raw.storm);
        //media_song.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //tell the user that we stopped
        Log.e("On destroy called.","Check.");
        super.onDestroy();
        this.isRunning = false;
    }
}
