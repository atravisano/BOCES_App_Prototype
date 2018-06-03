package com.example.anthony.travisano_boces1;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/*
Author: Anthony Travisano
Purpose: Prints the current time and time spent on task
    Used to update student with amount worked
Date: 11/12/17
 */

public class WorkActivity extends AppCompatActivity implements Parcelable{
    private TextView txtTimer;
    private TextView txtStudName;
    private TextView txtTask;

    private Chronometer cTimer;
    private Button btnFinished;

    private Timer timer;
    private MyTimerTask myTimerTask;
    private Student student;
    private String taskName;
    private DBHelper dbHelper;

    private String startTime;
    //used as the offset in the chronometer
    private long stopTime;

    private Work work;

    public WorkActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        student = getIntent().getParcelableExtra("studentWork");
        taskName = getIntent().getStringExtra("taskWork");

        txtStudName = (TextView) findViewById(R.id.txtWorkName);
        txtStudName.setText(student.getFullName());
        txtTimer = (TextView) findViewById(R.id.txtTime);
        txtTask = (TextView) findViewById(R.id.txtTask);
        txtTask.setText("is doing " + taskName);
        btnFinished = (Button) findViewById(R.id.btnFinish);
        cTimer = (Chronometer) findViewById(R.id.chronometerWork);

        stopTime = 0;

        startChronometer();

        dbHelper = new DBHelper(this);
        startWork();
        startTime = DateFormat.getDateTimeInstance().format(new Date());

        btnFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWork();
                cTimer.stop();
                reportWork();
                //pass selected student object to work activity
                Intent i = new Intent(WorkActivity.this, WorkFinishedActivity.class);
                i.putExtra("work", work);
                startActivity(i);
            }
        });
    }

    //inputs work into database if student accidentally hits back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopWork();
        cTimer.stop();
        reportWork();
        Toast.makeText(getApplicationContext(), "Your Work Was Saved", Toast.LENGTH_LONG).show();
    }

    private void reportWork() {
        work = new Work();
        //get working student name and id
        work.setStudentId(student.getStudentId());
        work.setStudentName(student.getFullName());
        //get task name
        work.setTaskName(taskName);
        //getting time started
        work.setStartTime(startTime);
        //get date time student finished task
        work.setStopTime(txtTimer.getText().toString());
        //Getting time student worked on task
        work.setWorkTime(cTimer.getText().toString());
        //set timer back to 0
        resetChronometer();
        //put all info into database
        dbHelper.addWorkTime(work);
    }

    //starts the chronometer timer
    private void startChronometer() {
        cTimer.setBase(SystemClock.elapsedRealtime() + stopTime);
        cTimer.start();
    }
    //resets the chronometer timer to 0
    private void resetChronometer() {
        cTimer.stop();
        cTimer.setBase(SystemClock.elapsedRealtime());
        stopTime=0;
    }

    //shows system date and time
    public void startWork() {
        txtTimer.setVisibility(View.VISIBLE);
        if(timer != null)
        {
            timer.cancel();
        }
        timer = new Timer();
        myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1, 1);
    }

    //stop updating date and time
    public void stopWork(){
        if(timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }

    //private inner class to handle current date and time
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            Calendar cal = Calendar.getInstance();
            //the locale "a" stands for am/pm
            //4 M's gives Month name
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy:HH:mm:ss a", Locale.US);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US);
            final String strDate = simpleDateFormat.format(cal.getTime());
            //saves date time when activity starts

            //Start thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtTimer.setVisibility(View.VISIBLE);
                    txtTimer.setGravity(Gravity.CENTER);
                    txtTimer.setText(strDate);
                }
            });
        }
    }

    protected WorkActivity(Parcel in) {
        work = (Work) in.readValue(Work.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeValue(work);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WorkActivity> CREATOR = new Parcelable.Creator<WorkActivity>() {
        @Override
        public WorkActivity createFromParcel(Parcel in) {
            return new WorkActivity(in);
        }

        @Override
        public WorkActivity[] newArray(int size) {
            return new WorkActivity[size];
        }
    };
}
