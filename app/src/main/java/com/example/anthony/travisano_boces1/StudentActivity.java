package com.example.anthony.travisano_boces1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
Author: Anthony Travisano
Purpose: User can add or remove all students from the database.
    Information is entered by editTexts and one spinner with the teacher's names.
    Student information is displayed in the ListView.
    Date: 10/5/17
*/

public class StudentActivity extends AppCompatActivity {

    protected DBHelper dbHelper;
    private ArrayList<Student> sList;
    private ArrayList<Teacher> tList;
    private CustomStudentAdapter adapt;
    private ImageButton btnImage;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "StudentActivity";
    private Uri selectedImageUri;
    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private Spinner spinTeachers;
    private CustomSpinnerAdapter spinnerAdapter;
    private EditText year;
    private Button btnAdd;
    private Button btnClear;
    private ListView lv2;

    private Student oldStud;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //referencing fields
        firstName = (EditText) findViewById(R.id.txtFirstName1);
        lastName = (EditText) findViewById(R.id.txtLastName1);
        age = (EditText) findViewById(R.id.txtAge);
        spinTeachers = (Spinner) findViewById(R.id.spinnerTeachers);
        year = (EditText) findViewById(R.id.txtYear);
        btnImage = (ImageButton) findViewById(R.id.btnImageStudent);
        btnAdd = (Button) findViewById(R.id.btnAddStudent);
        btnClear = (Button) findViewById(R.id.btnClearStudent);

        dbHelper = new DBHelper(this);
        sList = dbHelper.getAllStudents();
        tList = dbHelper.getAllTeachers();

        adapt = new CustomStudentAdapter(this, sList, tList);

        lv2 = (ListView) findViewById(R.id.listView2);
        lv2.setAdapter(new CustomStudentAdapter(this, sList, tList));

        loadSpinnerData();
        noTeacherMessage();

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");      //images directory
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //adds selected student to fields
                editStudent(position);
                update = true;
                btnAdd.setText("Done");
                btnClear.setText("Delete");
            }
        });

        /*The button has two functions update and delete**/
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update)
                {
                    updateStudent(oldStud);
                    update=false;
                    btnAdd.setText("Add Student");
                    btnClear.setText("Clear Students");
                }
                else {
                    addStudent();
                }
            }
        });

        /*The button can delete one student or delete all students*/
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update)
                {
                    confirmDeleteMessage();
                }
                else {
                    confirmDeleteAllMessage();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //update lists with new data
        sList = dbHelper.getAllStudents();
        tList = dbHelper.getAllTeachers();
        adapt = new CustomStudentAdapter(this, sList, tList);
        lv2.setAdapter(new CustomStudentAdapter(this, sList, tList));
        loadSpinnerData();
    }

    //finds selected student in spinner and sets it as selected
    private int findTeacherPosition() {
        for(int i =0; i < tList.size(); i++) {
            if(tList.get(i).getTeacherId().equals(oldStud.getTeacherId())) {
                return i;
            }
        }
        return 0;
    }

    //adds selected student to fields
    private void editStudent(int position) {
        Object s = lv2.getItemAtPosition(position);
        oldStud = (Student) s;
        Toast.makeText(getApplicationContext(), "Editing: " + oldStud.getFirstName(), Toast.LENGTH_SHORT).show();
        btnImage.setImageBitmap(Utils.getImage(oldStud.getImage()));
        firstName.setText(oldStud.getFirstName());
        lastName.setText(oldStud.getLastName());
        spinTeachers.setSelection(findTeacherPosition());
        age.setText(oldStud.getAge());
        year.setText(oldStud.getYear());
    }

    //Button click event for adding a new spinTeachers
    public void addStudent() {
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String a = age.getText().toString();
        //saves the teacher's id
        String t;
        try {
            t = spinTeachers.getSelectedItem().toString();
        }
        catch(Exception e) {
            //in case there are no teachers in spinner
            t = "";
        }
        String y = year.getText().toString();

        if(first.isEmpty() || last.isEmpty() ||  a.isEmpty()  || y.isEmpty())
            //If any fields are empty, the user will be notified
            Toast.makeText(getApplicationContext(), "Please Enter In All Information.", Toast.LENGTH_LONG).show();
        else {
            Student student;
            if(selectedImageUri == null) {
                Bitmap bitmap = ((BitmapDrawable)btnImage.getDrawable()).getBitmap();
                student = new Student(first, last, a, t, y, Utils.getImageBytes(bitmap));
            }
            else
            {
                student = new Student(first, last, a, t, y, saveImage(selectedImageUri));
            }
            //adds spinTeachers to database
            dbHelper.addStudent(student);
            clearFields();
            resetAdapter();
        }
        hideSoftKeyboard();
    }

    //updates cureent student that user edited
    public void updateStudent(Student stud) {
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String a = age.getText().toString();
        //saves the teacher's id
        String t;
        try {
            t = spinTeachers.getSelectedItem().toString();
        }
        catch(Exception e) {
            //in case there are no teachers in spinner
            t = "";
        }
        String y = year.getText().toString();

        if(first.isEmpty() || last.isEmpty() ||  a.isEmpty()  || y.isEmpty())
            //If any fields are empty, the user will be notified
            Toast.makeText(getApplicationContext(), "Please Enter In All Information.", Toast.LENGTH_LONG).show();
        else {
            Bitmap imgBitmap = ((BitmapDrawable)btnImage.getDrawable()).getBitmap();
            //adds spinTeachers to database
            Student s = new Student(first, last, a, t, y, Utils.getImageBytes(imgBitmap));
            s.setStudentId(stud.getStudentId());
            dbHelper.updateStudent(s);
            hideSoftKeyboard();
            clearFields();
            resetAdapter();
        }
    }

    //deletes student from database
    public void deleteStudent() {
        Student s = new Student();
        s.setStudentId(oldStud.getStudentId());
        dbHelper.deleteStudent(s);
        clearFields();
        resetAdapter();
        hideSoftKeyboard();
    }

    //Button click event for deleting all teachers
    public void clearStudents () {
        dbHelper.clearStudents(sList);
        adapt.notifyDataSetChanged();
        hideSoftKeyboard();
    }

    private void loadSpinnerData() {
        //gets all data for dropdown box
        List<Teacher>teacherList = dbHelper.getAllTeachers();
        spinnerAdapter = new CustomSpinnerAdapter(this,R.layout.spinner_row_view, teacherList);
        spinTeachers.setAdapter(spinnerAdapter);
    }

    //clears all fields
    private void clearFields() {
        firstName.setText("");
        lastName.setText("");
        age.setText("");
        year.setText("");
    }

    //reset adapter and listview with new values
    private void resetAdapter() {
        sList = dbHelper.getAllStudents();
        adapt = new CustomStudentAdapter(this, sList, tList);
        lv2.setAdapter(adapt);
        //add the task and set a notification of changes
        adapt.notifyDataSetChanged();
    }

    //hides soft keyboard from screen
    private void hideSoftKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //pop up asking making asking if they are sure they want to delete student
    public void confirmDeleteMessage() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete Student")
                .setMessage("Are you sure you want to delete this student?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteStudent();
                        update = false;
                        Toast.makeText(getApplicationContext(), "Student Deleted", Toast.LENGTH_SHORT).show();
                        btnAdd.setText("Add Student");
                        btnClear.setText("Clear Students");
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        update = true;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //pop up asking making asking if they are sure they want to delete all students from data base
    public void confirmDeleteAllMessage() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete All Students")
                .setMessage("Are you sure you want to delete all students?  This information CANNOT be recovered.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        clearStudents();
                        update = false;
                        Toast.makeText(getApplicationContext(), "All Students Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //displays a message if no teachers are no database
    public void noTeacherMessage() {
        if(tList.size() == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(StudentActivity.this).create();
            alertDialog.setTitle("No Teachers");
            alertDialog.setMessage("You must add a teacher before you can add a student");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(StudentActivity.this, AdminActivity.class));
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    //gets and sets picture from phone files
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    btnImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    // Save the
    byte[] saveImage(Uri selectedImageUri) {

        try {
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            return Utils.getBytes(iStream);
        } catch (IOException ioe) {
            Log.e(TAG, "<saveImageInDB> Error : " + ioe.getLocalizedMessage());
            dbHelper.close();
            return null;
        }

    }

}
