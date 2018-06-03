package com.example.anthony.travisano_boces1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/*
Author: Anthony Travisano
Purpose: Creates a splash screen with animation of the logo and buttons
    The buttons take you to either the student login or admin login
Date: 12/13/17
 */

public class SplashActivity extends AppCompatActivity {
    private Button btnStudentLogin;
    private Button btnAdminLogin;
    private ImageView imageLogo;
    private Animation fromRight;
    private Animation fromLeft;
    private Animation fromTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnStudentLogin= (Button) findViewById(R.id.btnSplashStudent);
        btnAdminLogin= (Button) findViewById(R.id.btnSplashAdmin);
        imageLogo = (ImageView) findViewById(R.id.imageSplash);

        fromRight = AnimationUtils.loadAnimation(this, R.anim.from_right);
        fromLeft = AnimationUtils.loadAnimation(this, R.anim.from_left);
        fromTop = AnimationUtils.loadAnimation(this, R.anim.from_top);


        imageLogo.setAnimation(fromTop);
        btnStudentLogin.setAnimation(fromRight);
        btnAdminLogin.setAnimation(fromLeft);

        btnStudentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, LoginStudentActivity.class));
            }
        });
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashActivity.this, AdminLoginActivity.class));
            }
        });

    }
}
