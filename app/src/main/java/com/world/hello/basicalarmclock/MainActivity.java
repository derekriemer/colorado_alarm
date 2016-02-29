package com.world.hello.basicalarmclock;

import android.os.Bundle;
import android.view.Menu;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.app.Activity;
import android.view.Window;

import com.alarmclock.R;

import java.util.Calendar;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Calendar t = Calendar.getInstance();
        t.add(Calendar.SECOND, 15);

        Intent i = new Intent(this, AlarmSound.class);
        PendingIntent pending = PendingIntent.getActivity(this,1235, i, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, t.getTimeInMillis(), pending);
        //startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

