package com.example.anthony.travisano_boces1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

/*
Author: Anthony Travisano
Purpose: an activity that allows admin access functions such as adding/deleting teachers,
    students, and tasks.  They can also generate a pdf report for the working students.
Date: 12/5/17
 */

public class AdminActivity extends AppCompatActivity {

    private CardView cdStudent, cdTeacher, cdTask, cdReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);

        cdStudent = (CardView) findViewById(R.id.cardStudent);
        cdTeacher = (CardView) findViewById(R.id.cardTeacher);
        cdTask = (CardView) findViewById(R.id.cardTask);
        cdReport = (CardView) findViewById(R.id.cardReport);

        cdStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, StudentActivity.class));
            }
        });

        cdTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, TeacherActivity.class));
            }
        });

        cdTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, TaskActivity.class));
            }
        });

        cdReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, ReportActivity.class));
            }
        });
    }
}
