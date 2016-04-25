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


public class MainActivity extends Activity {


    ImageView mImageView;
    TextView mTxtDegrees, mTxtWeather, mTxtError;

    //merging AlarmActivity into MainActivity
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static MainActivity inst;
    private TextView update_text;
    Context context;

    //merging AlarmActivity into MainActivity
    public static MainActivity instance() {
        return inst;
    }

    //merging AlarmActivity into MainActivity
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    MarsWeather helper = MarsWeather.getInstance();
    int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    int mainColor = Color.parseColor("#FF5722");
    SharedPreferences mSharedPref;


    final static String
            FLICKR_API_KEY = "49fe6498b87721f71a938787976088bd",
            IMAGES_API_ENDPOINT = "https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&sort=random&method=flickr.photos.search&" +
                    "tags=mars,planet,rover&tag_mode=all&api_key=",
            RECENT_API_ENDPOINT = "http://marsweather.ingenology.com/v1/latest/",

            SHARED_PREFS_IMG_KEY = "img",
            SHARED_PREFS_DAY_KEY = "day";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Inside", "on create.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout); //merging AlarmActivity into MainActivity
        this.context = this; //merging AlarmActivity into MainActivity

   /*     // Views setup
        mImageView = (ImageView) findViewById(R.id.main_bg);
        mTxtDegrees = (TextView) findViewById(R.id.degrees);
        mTxtWeather = (TextView) findViewById(R.id.weather);
        mTxtError = (TextView) findViewById(R.id.error);

        // Font
        mTxtDegrees.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf"));
        mTxtWeather.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf"));

        // SharedPreferences setup
        mSharedPref = getPreferences(Context.MODE_PRIVATE);
*/

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
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
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

    private void loadWeatherData() {

        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET, RECENT_API_ENDPOINT, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // if you want to debug: Log.v(getString(R.string.app_name), response.toString());
                        try {

                            String minTemp, maxTemp, atmo;
                            int avgTemp;

                            response = response.getJSONObject("report");

                            minTemp = response.getString("min_temp");
                            minTemp = minTemp.substring(0, minTemp.indexOf("."));
                            maxTemp = response.getString("max_temp");
                            maxTemp = maxTemp.substring(0, maxTemp.indexOf("."));

                            avgTemp = (Integer.parseInt(minTemp) + Integer.parseInt(maxTemp)) / 2;

                            atmo = response.getString("atmo_opacity");


                            mTxtDegrees.setText(avgTemp + "°");
                            mTxtWeather.setText(atmo);

                        } catch (Exception e) {
                            txtError(e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtError(error);
                    }
                });

        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
        // Weather data
        loadWeatherData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // This will tell to Volley to cancel all the pending requests
        helper.cancel();
    }

    private void txtError(Exception e) {
        mTxtError.setVisibility(View.VISIBLE);
        e.printStackTrace();
    }

    // UI Method
    public void setAlarm(View view){
        // Build our Intent
        Intent intent = new Intent(this, AlarmActivity.class);
        // Don't need to pass anything, just transition.
        startActivity(intent);
    }

}

//Commented out for merging in AlarmActivity
// Picture
       /* if (mSharedPref.getInt(SHARED_PREFS_DAY_KEY, 0) != today) {
            // search and load a random mars pict.
            try {
                searchRandomImage();
            } catch (Exception e) {
                // please remember to set your own Flickr API!
                // otherwise I won't be able to show
                // a random Mars picture
                imageError(e);
            }
        } else {
            // we already have a pict of the day: let's load it!
            loadImg(mSharedPref.getString(SHARED_PREFS_IMG_KEY, ""));
        }*/

    /**
     * Fetches a random picture of Mars, using Flickr APIs, and then displays it.
     * @throws Exception When a working API key is not provided.
     */
   /*
    private void searchRandomImage() throws Exception {
        if (FLICKR_API_KEY.equals(""))
            throw new Exception("You didn't provide a working Flickr API key!");

        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET, IMAGES_API_ENDPOINT+ FLICKR_API_KEY, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // if you want to debug: Log.v(getString(R.string.app_name), response.toString());

                        try {
                            JSONArray images = response.getJSONObject("photos").getJSONArray("photo");
                            int index = new Random().nextInt(images.length());

                            JSONObject imageItem = images.getJSONObject(index);

                            String imageUrl = "http://farm" + imageItem.getString("farm") +
                                    ".static.flickr.com/" + imageItem.getString("server") + "/" +
                                    imageItem.getString("id") + "_" + imageItem.getString("secret") + "_" + "c.jpg";

                            // store the pict of the day
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putInt(SHARED_PREFS_DAY_KEY, today);
                            editor.putString(SHARED_PREFS_IMG_KEY, imageUrl);
                            editor.commit();

                            // and finally load it
                            loadImg(imageUrl);

                        } catch (Exception e) {
                            imageError(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageError(error);
                    }
                });

        request.setPriority(Request.Priority.LOW);
        helper.add(request);

    }*/

    /**
     * Downloads and displays the picture using Volley.
     * @param imageUrl the URL of the picture.
     */
    /*
    private void loadImg(String imageUrl) {
        // Retrieves an image specified by the URL, and displays it in the UI
        ImageRequest request = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageError(error);
                    }
                });

        // we don't need to set the priority here;
        // ImageRequest already comes in with
        // priority set to LOW, that is exactly what we need.
        helper.add(request);
    }*/

    /**
     * Fetches and displays the weather data of Mars.
     */

/*
    private void imageError(Exception e) {
        mImageView.setBackgroundColor(mainColor);
        e.printStackTrace();
    }
*/
