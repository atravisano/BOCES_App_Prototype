package com.example.anthony.travisano_boces1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
Author: Anthony Travisano
Purpose: user(admin) must type in correct password to continue to next activity
Date: 12/13/17
 */

public class AdminLoginActivity extends AppCompatActivity {

    private EditText txtPassword;
    private Button btnLogin;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnAdminLogin);

        //password for admin to enter
        password = "admin1234";
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPasswordCorrect()) {
                    startActivity(new Intent(AdminLoginActivity.this, AdminActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isPasswordCorrect() {
        return txtPassword.getText().toString().equals(password);
    }
}
