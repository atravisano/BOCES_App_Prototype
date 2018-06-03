package com.example.anthony.travisano_boces1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/*
Author: Anthony Travisano
Purpose: User can choose their name filtered by the teacher spinner
    After selecting student and task, they can go to the working activity
Date: 11/9/17
 */

public class LoginStudentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Parcelable{

    private Spinner spinTeachLogin;
    private ListView listStuds;
    private TextView txtName;
    private Button btnWork;
    private DBHelper dbHelper;
    private ArrayList<Student> studs;
    private ArrayList<Teacher> teachers;

    private CustomSpinnerAdapter tAdapter;
    private CustomStudentFilterAdapter sAdapter;

    private Student studentSelected;

    public LoginStudentActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinTeachLogin = (Spinner) findViewById(R.id.spinnerLogin);
        listStuds = (ListView) findViewById(R.id.listViewLogin);
        txtName = (TextView) findViewById(R.id.txtLoginName);
        btnWork = (Button) findViewById(R.id.btnWork);

        dbHelper = new DBHelper(this);
        teachers = dbHelper.getAllTeachers();
        studs = dbHelper.getAllStudents();
        //checks to see if students arent assigned a teacher
        checkTeachers();
        //only loads associated students when teacher is in spinner
        if(spinTeachLogin.getSelectedItem() != null)
            studs = dbHelper.getAssociatedStudents(teachers.get(0).getTeacherId());
        tAdapter = new CustomSpinnerAdapter(this,R.layout.spinner_row_view, teachers);
        spinTeachLogin.setAdapter(tAdapter);
        spinTeachLogin.setOnItemSelectedListener(this);

        sAdapter = new CustomStudentFilterAdapter(this, studs);
        listStuds.setAdapter(sAdapter);
        listStuds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //read students name and display choice in textview
                studentSelected = (Student) listStuds.getItemAtPosition(position);
                txtName.setVisibility(View.VISIBLE);
                String loginConfirmation = "Hello, " + studentSelected.getFullName();
                txtName.setText(loginConfirmation);
            }
        });

        //button changes text and function on each click
        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(studentSelected != null) {
                    //pass selected student object to work activity
                    Intent i = new Intent(LoginStudentActivity.this, LoginTaskActivity.class);
                    i.putExtra("studentWork", studentSelected);
                    startActivity(i);
                }
                else
                    Toast.makeText(getApplicationContext(), "Please Choose Your Name", Toast.LENGTH_LONG).show();

            }

        });
    }

    protected void onRestart() {
       super.onRestart();
        teachers = dbHelper.getAllTeachers();
        studs = dbHelper.getAllStudents();
        //checks to see if students arent assigned a teacher
        checkTeachers();
        tAdapter = new CustomSpinnerAdapter(this,R.layout.spinner_row_view, teachers);
        spinTeachLogin.setAdapter(tAdapter);
        spinTeachLogin.setOnItemSelectedListener(this);
        sAdapter = new CustomStudentFilterAdapter(this, studs);
        listStuds.setAdapter(sAdapter);
    }

    //creates a "no teacher assigned" if a student doesn't have an assigned teacher
    private void checkTeachers() {
        for(Student s: studs) {
            if(s.getTeacherId().equals("-1")){
                //creates a dummy teacher for students that don't have an assigned teacher
                Teacher t = new Teacher("--No Teacher Assigned--", "","-1");
                t.setImage(null);
                //Adding picture bytes
                Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                t.setImage(stream.toByteArray());

                teachers.add(t);
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        //filters students by the teacher's id
        Teacher teacher = tAdapter.getItem(position);
        sAdapter.getFilter().filter(teacher.getTeacherId(), new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int i) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //******************Methods for Parcelable *************

    protected LoginStudentActivity(Parcel in) {
        studentSelected = (Student) in.readValue(Student.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(studentSelected);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LoginStudentActivity> CREATOR = new Parcelable.Creator<LoginStudentActivity>() {
        @Override
        public LoginStudentActivity createFromParcel(Parcel in) {
            return new LoginStudentActivity(in);
        }

        @Override
        public LoginStudentActivity[] newArray(int size) {
            return new LoginStudentActivity[size];
        }
    };
}
