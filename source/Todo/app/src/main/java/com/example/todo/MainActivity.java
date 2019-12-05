package com.example.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.provider.AlarmClock;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener, AdapterView.OnItemClickListener {

    private TimePicker picker;

    private ListView timeList;

    private ArrayList<String> times;

    private int[] times24;

    private ArrayAdapter<String> timeListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker = findViewById(R.id.pickSleepTime);

        //sleepTime = findViewById(R.id.sleepTime);

        times24 = new int[8];

        times = new ArrayList<>();

        timeList = findViewById(R.id.sleepList);

        timeListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);

        timeList.setAdapter(timeListAdapter);

        picker.setOnTimeChangedListener(this);

        timeList.setOnItemClickListener(this);


    }

    @Override
    public void onTimeChanged(TimePicker picker, int hour, int min) {
        int minute = min;
        int[] multiples = {4, 6, 7, 9};

        String low_label;
        String med_low_label;
        String med_label;
        String high_label;

        low_label = med_low_label = med_label = high_label = "PM";

        int low_hour = (hour + multiples[0]) % 12;
        int med_low_hour = (hour + multiples[1]) % 12;
        int med_hour = (hour + multiples[2]) % 12;
        int high_hour = (hour + multiples[3]) % 12;

        int low_hour_24 = (hour + multiples[0]) % 24;
        int med_low_hour_24 = (hour + multiples[1]) % 24;
        int med_hour_24 = (hour + multiples[2]) % 24;
        int high_hour_24 = (hour + multiples[3]) % 24;


        //Handle going into the AMs
        if (hour + 4 >= 24) {
            low_label = "AM";
        }
        if (hour + 6 >= 24) {
            med_low_label = "AM";
        }
        if (hour + 7 >= 24) {
            med_label = "AM";
        }
        if (hour + 9 >= 24) {
            high_label = "AM";
        }


        //Handle staying within the AM boundaries
        if (hour + 4 < 12 && minute < 30) {
            low_label = "AM";
        }
        if (hour + 6 < 12) {
            med_low_label = "AM";
        }
        if (hour + 7 < 12 && minute < 30) {
            med_label = "AM";
        }
        if (hour + 9 < 12) {
            high_label = "AM";
        }


        //Handle midnight
        if (hour + 4 == 24 && minute < 30) {
            low_hour = 12;
        } else if  (hour + 4 == 24) {
            low_hour = 0;
        }

        if (hour + 6 == 24) {
            med_low_hour = 12;
        }

        if (hour + 7 == 24 && minute < 30) {
            med_hour = 12;
        } else if (hour + 7 == 24) {
            med_hour = 0;
        }

        if (hour + 9 == 24) {
            high_hour = 12;
        }


        //Handle noon
        if (hour + 4 == 12 && minute < 30) {
            low_hour = 12;
        } else if (hour + 4 == 12 ) {
            low_hour = 11;
        }

        if (hour + 6 == 12) {
            med_low_hour = 12;
        }

        if (hour + 7 == 12 && minute < 30) {
            med_hour = 12;
        } else if (hour + 7 == 12) {
            med_hour = 11;
        }

        if (hour + 9 == 12) {
            high_hour = 12;
        }


        int low_minute, med_minute;

        low_minute = med_minute = minute + 30;

        if (minute >= 30) {
            //always add one hour
            low_hour += 1;
            med_hour += 1;
            low_hour_24 = (low_hour_24 + 1) % 24;
            med_hour_24 = (med_hour_24 + 1) % 24;

            if (hour + 5 == 24) {
                low_label = "AM";
            } else if (hour + 5 < 12) {
                low_label = "AM";
            }


            if (hour + 8 == 24) {
                med_label = "AM";
            } else if (hour + 8 < 12) {
                med_label = "AM";
            }

            low_minute = med_minute = (minute + 30) % 60;

        }

        String low_text = low_hour + ":" + String.format(Locale.US, "%02d", low_minute)
                + " " + low_label + "  (4.5 hours)";
        String med_low_text = med_low_hour + ":" + String.format(Locale.US, "%02d", minute)
                + " " + med_low_label + "  (6 hours)";
        String med_text = med_hour + ":" + String.format(Locale.US, "%02d", med_minute)
                + " " + med_label + "  (7.5 hours)";
        String high_text = high_hour + ":" + String.format(Locale.US, "%02d", minute)
                + " " + high_label + "  (9 hours)";

        timeListAdapter.clear();

        timeListAdapter.add(low_text);
        timeListAdapter.add(med_low_text);
        timeListAdapter.add(med_text);
        timeListAdapter.add(high_text);


        times24[0] = low_hour_24;
        times24[1] = low_minute;
        times24[2] = med_low_hour_24;
        times24[3] = times24[7] = minute;
        times24[4] = med_hour_24;
        times24[5] = med_minute;
        times24[6] = high_hour_24;


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder setAlarm = new AlertDialog.Builder(this)
                .setTitle("Set alarm?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent setAlarm = new Intent(AlarmClock.ACTION_SET_ALARM);

                        final int hour = times24[position * 2];
                        final int minute = times24[position * 2 + 1];

                        setAlarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        setAlarm.putExtra(AlarmClock.EXTRA_HOUR, hour);
                        setAlarm.putExtra(AlarmClock.EXTRA_MINUTES, minute);

                        setAlarm.putExtra(AlarmClock.EXTRA_MESSAGE, "Sleep well!");

                        startActivity(setAlarm);
                    }
                }).setNegativeButton(android.R.string.no, null);

        setAlarm.show();


    }






}
