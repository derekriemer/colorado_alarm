package com.robobrandon.simpleweather;

//resource <https://www.youtube.com/watch?v=KseXIsTLXaY&list=PL4uut9QecF3DLAacEoTctzeqTyvgzqYwA&index=2#t=1.8585>
// and Android Studio documentation.
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    //to make alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_time_picker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;


        //Initialize alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //Initialize timepicker
        alarm_time_picker = (TimePicker) findViewById(R.id.timePicker);
        //Initialize text update box
        update_text = (TextView) findViewById(R.id.update_text);
        //Create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();
        //Create intent to the alarmreceiver class
        final Intent my_intent = new Intent(this.context, AlarmReceiver.class);
        //create spinner in the main ui
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weather_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Initialize button on
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        //Create an onClick listener to start the alarm

        assert alarm_on != null;
        alarm_on.setOnClickListener(new View.OnClickListener() {
            //added Target Api, so that .getHour and .getMinute work.
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //setting calendar with the hour and minute we picked on the time picker.
                calendar.set(Calendar.HOUR_OF_DAY, alarm_time_picker.getHour());
                calendar.set(Calendar.MINUTE, alarm_time_picker.getMinute());


                //get the int values of the hour and minutes
                int hour = alarm_time_picker.getHour();
                int minute = alarm_time_picker.getMinute();

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

                //put in extra string into my_intent. Tells the clock that you pressed the alarm on button.
                my_intent.putExtra("extra", "alarm on");

                //put in an extra long into my intent that tells the clock that you want a certain value
                //from the spinner
                my_intent.putExtra("weather_choice", choose_weather_sound);


                //create a pending intent that delays the intent until the specified calendar time.
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });
        //Initialize button off
        Button alarm_off = (Button) findViewById(R.id.alarm_off);
        //Create an onClick listener to switch off the alarm
        assert alarm_off != null;
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Method that changes the updates text box.
                set_alarm_text("Alarm off.");
                //cancel the alarm
                alarm_manager.cancel(pending_intent);

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
   /* int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public void findPlace(View view) {
        try {
  */          //.setFilter(TYPE_FILTER_GEOCODE)
            //AutocompleteFilter TYPE_FILTER_GEOCODE;
            //PlaceAutocomplete
            /*The Place Autocomplete service is a web service that returns place
            predictions in response to an HTTP request. The request specifies a
            textual search string and optional geographic bounds. The service can
            be used to provide autocomplete functionality for text-based
            geographic searches, by returning places such as businesses,
            addresses and points of interest as a user types.*/
      /*      Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
*/
    // A place has been received; use requestCode to track the request.
 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("In onActivityResult - 1","Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                //Indicates the current status of the task.
                //Each status will be set only once during the lifetime of a task.
                // AsyncTask.Status -> does not work => made a suggested change.
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("In onActivityResult - 2", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        //outputing whatever id the user has selected
        Toast.makeText(parent.getContext(),"the spinner item is" + id, Toast.LENGTH_SHORT).show();
        choose_weather_sound = (int) id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}