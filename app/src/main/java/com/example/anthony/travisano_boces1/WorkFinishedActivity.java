package com.example.anthony.travisano_boces1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/*
Author: Anthony Travisano
Purpose: Display final results when student clocks out
    Displays task completed, time, and complement
    creates an animation that counts up the minutes
    plays drum roll sound when adding time worked and a plays a celebration sound at end of animation
Date: 11/11/17
 */

public class WorkFinishedActivity extends AppCompatActivity {

    private TextView txtTask;
    private TextView txtTime;
    private TextView txtComplement;
    private Button btnReturn;
    private MediaPlayer mpDrum;
    private MediaPlayer mpTaDa;
    private Animation enlargeText;

    private Work work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_finished);

        work = getIntent().getParcelableExtra("work");

        txtTask = (TextView) findViewById(R.id.txtTaskFinished);
        txtTime = (TextView) findViewById(R.id.txtTimeFinished);
        txtComplement = (TextView) findViewById(R.id.txtComplement);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        mpDrum = new MediaPlayer();
        mpDrum = MediaPlayer.create(this, R.raw.drum_roll);
        mpTaDa = new MediaPlayer();
        mpTaDa = MediaPlayer.create(this, R.raw.ta_da);
        enlargeText = AnimationUtils.loadAnimation(this, R.anim.enlarge_text);

        txtTask.setText(work.getTaskName());

        counterWorkTime();

        //gives user a randomly generated complement
        txtComplement.setText(randComplement());


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WorkFinishedActivity.this, LoginStudentActivity.class));
            }
        });
    }

    //animation of amount of time counting up
    private void counterWorkTime() {
        //00:00
        String[] time = work.getWorkTime().split("\\:");
        //time[0] represents minutes
        int mins;
        //time[1] represents seconds
        int secs;
        String unit;
        //check minutes
        if(time[0].equals("00")) {
            unit = "seconds";
            secs = Integer.parseInt(time[1]);
            counterAnimation(secs, unit);
        }
        else {
            unit = "minutes";
            mins = Integer.parseInt(time[0]);
            counterAnimation(mins, unit);
        }
    }

    //animation of the time a student worked
    private void counterAnimation(int limit, String clock) {
        //represents last animated string
        final String last = clock;
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, limit);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                //display new total each frame
                txtTime.setText(String.valueOf(animation.getAnimatedValue()) + " " + last);
            }
        });
        animator.setEvaluator(new TypeEvaluator<Integer>() {
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                return Math.round(startValue + (endValue - startValue) * fraction);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //drum roll ends and plays celebration sound
                mpDrum.stop();
                mpTaDa.start();
                //pulsing animation
                txtTime.setAnimation(enlargeText);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                //plays a drum roll
                mpDrum.start();
            }
        });
        animator.setDuration(2000);
        animator.start();
    }


    //randomly generates a complement for user
    private String randComplement() {
        Random rand = new Random();
        int num = rand.nextInt(6);
        String complement;

        switch(num) {
            case 0:
                complement = "Great Job!";
                break;
            case 1:
                complement = "Awesome Job!";
                break;
            case 2:
                complement = "Keep Up The Great Work!";
                break;
            case 3:
                complement = "You're Amazing!";
                break;
            case 4:
                complement = "Excelent Work!";
                break;
            case 5:
                complement = "Nice Work!";
                break;
            default:
                complement = "You're The Best!";
                break;
        }
        return complement;
    }
}
