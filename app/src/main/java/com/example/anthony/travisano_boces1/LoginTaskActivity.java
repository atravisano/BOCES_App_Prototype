package com.example.anthony.travisano_boces1;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/*
Author: Anthony Travisano
Purpose: Allows student logging in to pick which task they will do
Date: 12/13/17
 */

public class LoginTaskActivity extends AppCompatActivity implements Parcelable{
    private GridView gridTasks;
    private Button btnBegin;
    private GridTaskAdapter adapter;
    private DBHelper dbHelper;
    private ArrayList<Task> taskList;
    private Task taskSelected;
    private Student stud;

    public LoginTaskActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_task);

        //saving current student selected
        stud = getIntent().getParcelableExtra("studentWork");

        gridTasks = (GridView) findViewById(R.id.gridviewTasks);
        btnBegin = (Button) findViewById(R.id.btnBegin);

        dbHelper = new DBHelper(this);
        taskList = dbHelper.getAllTasks();
        adapter = new GridTaskAdapter(this, taskList);
        gridTasks.setAdapter(adapter);

        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pass selected student and task name to work activity
                Intent i = new Intent(LoginTaskActivity.this, WorkActivity.class);
                i.putExtra("studentWork", stud);
                try {
                    i.putExtra("taskWork", taskSelected.getTaskName());
                } catch(Exception  e) {
                    i.putExtra("taskWork", "No Task Selected");
                }
                startActivity(i);            }
        });

        gridTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //have the background of the element change color
                gridTasks.getItemAtPosition(position);
                taskSelected = (Task) gridTasks.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), "touched " + taskSelected, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /******* methods for Parcelable ******/
    protected LoginTaskActivity(Parcel in) {
        stud = (Student) in.readValue(Student.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(stud);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoginTaskActivity> CREATOR = new Parcelable.Creator<LoginTaskActivity>() {
        @Override
        public LoginTaskActivity createFromParcel(Parcel in) {
            return new LoginTaskActivity(in);
        }

        @Override
        public LoginTaskActivity[] newArray(int size) {
            return new LoginTaskActivity[size];
        }
    };
}
