package com.example.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener{

    private TimePicker picker;

    private TextView sleepTime;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker = findViewById(R.id.pickSleepTime);

        sleepTime = findViewById(R.id.sleepTime);

        picker.setOnTimeChangedListener(this);

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
        if (hour + 4 < 12) {
            low_label = "AM";
        }
        if (hour + 6 < 12) {
            med_low_label = "AM";
        }
        if (hour + 7 < 12) {
            med_label = "AM";
        }
        if (hour + 9 < 12) {
            high_label = "AM";
        }


        //Handle midnight
        if (hour + 4 == 24) {
            low_hour += 11;
        }

        if (hour + 6 == 24) {
            med_low_hour += 12;
        }

        if (hour + 7 == 24) {
            med_hour += 11;
        }

        if (hour + 9 == 24) {
            high_hour += 12;
        }


        //Handle noon
        if (hour + 4 == 12 && minute < 30) {
            low_hour += 12;
        }

        if (hour + 6 == 12) {
            med_low_hour += 12;
        }

        if (hour + 7 == 12 && minute < 30) {
            med_hour += 12;
        }

        if (hour + 9 == 12) {
            high_hour += 12;
        }



        int low_minute, med_minute;

        low_minute = med_minute = minute + 30;

        if (minute >= 30) {
            //always add one hour
            low_hour += 1;
            med_hour += 1;

            if (low_hour == 12) {
                low_label = "AM";
            }
            if (med_hour == 12) {
                med_label = "AM";
            }

            low_minute = med_minute = (minute + 30) % 60;

        }

        String low_text = low_hour + ":" + String.format("%02d", low_minute) + " " + low_label + "  (4.5 hours)";
        String med_low_text = med_low_hour + ":" + String.format("%02d", minute) + " " + med_low_label + "  (6 hours)";
        String med_text = med_hour + ":" + String.format("%02d", med_minute) + " " + med_label + "  (7.5 hours)";
        String high_text = high_hour + ":" + String.format("%02d", minute) + " " + high_label + "  (9 hours)";


        sleepTime.setText(low_text + "\n" + med_low_text + "\n" + med_text + "\n" + high_text);
    }




}
