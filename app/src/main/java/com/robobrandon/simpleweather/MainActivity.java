package com.robobrandon.simpleweather;

import android.app.PendingIntent;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlarmManager;

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

    RestHelper helper = RestHelper.getInstance();
    int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    int mainColor = Color.parseColor("#FF5722");
    SharedPreferences mSharedPref;


    final static String
            API_ENDPOINT = "http://colorado-alarm-api.herokuapp.com/forecasts?lat=-105.2705456&lon=40.0149856";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views setup
        mImageView = (ImageView) findViewById(R.id.main_bg);
        mTxtDegrees = (TextView) findViewById(R.id.degrees);
        mTxtWeather = (TextView) findViewById(R.id.weather);
        mTxtError = (TextView) findViewById(R.id.error);

        // Font
        mTxtDegrees.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf"));
        mTxtWeather.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf"));

        // SharedPreferences setup
        mSharedPref = getPreferences(Context.MODE_PRIVATE);

        // Weather data
        loadWeatherData();
//        scheduleWeatherCheck();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // This will tell to Volley to cancel all the pending requests
        helper.cancel();
    }



    private void loadImg() {

    }

    /**
     * Fetches and displays the weather data of Mars.
     */
    private void loadWeatherData() {

        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET, API_ENDPOINT, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // if you want to debug: Log.v(getString(R.string.app_name), response.toString());
                        try {
                            String precipType, temp, precipProbability, summary;
                            summary = response.getString("summary");
                            temp = response.getString("temperature");
                            precipType = response.getString("precip_type");
                            precipProbability= response.getString("precip_probability");


                            mTxtDegrees.setText(temp + "°");
                            mTxtWeather.setText(summary);
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

    }

    private void imageError(Exception e) {
        mImageView.setBackgroundColor(mainColor);
        e.printStackTrace();
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

    // Setup a recurring alarm every half hour
//    public void scheduleWeatherCheck() {
//        // Construct an intent that will execute the AlarmReceiver
//        Intent intent = new Intent(getApplicationContext(), WeatherPullReceiver.class);
//        // Create a PendingIntent to be triggered when the alarm goes off
//        final PendingIntent pIntent = PendingIntent.getBroadcast(this, WeatherPullReceiver.REQUEST_CODE,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Setup periodic alarm every 5 seconds
//        long firstMillis = System.currentTimeMillis(); // alarm is set right away
//        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
//                AlarmManager.INTERVAL_HALF_HOUR, pIntent);
//    }
//
//    public void cancelWeatherService() {
//        Intent intent = new Intent(getApplicationContext(), WeatherPullReceiver.class);
//        final PendingIntent pIntent = PendingIntent.getBroadcast(this, WeatherPullReceiver.REQUEST_CODE,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//        alarm.cancel(pIntent);
//    }

}
