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
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
/*
Author: Anthony Travisano
Purpose: User can add or remove Teacher(s) from the database.
        Information is entered by editTexts and displayed in the ListView.
Date: 10/5/17
 */


public class TeacherActivity extends AppCompatActivity {

    protected DBHelper dbHelper;
    private ArrayList<Teacher> tList;
    private CustomTeacherAdapter adapt;
    private ImageButton btnImage;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "TeacherActivity";
    private Uri selectedImageUri;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private Button btnAdd;
    private Button btnClear;
    private ListView lv1;
    //represents if user is updating teacher
    private boolean update;
    private Teacher oldTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        //referencing fields
        btnImage = (ImageButton) findViewById(R.id.btnImageTeacher);
        firstName = (EditText) findViewById(R.id.txtFirstName);
        lastName = (EditText) findViewById(R.id.txtLastName);
        email = (EditText) findViewById(R.id.txtEmail);
        phone = (EditText) findViewById(R.id.txtPhone);
        btnAdd = (Button) findViewById(R.id.btnAddTeacher);
        btnClear = (Button) findViewById(R.id.btnClearTeacher);

        dbHelper = new DBHelper(this);
        tList = dbHelper.getAllTeachers();
        adapt = new CustomTeacherAdapter(this, tList);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");      //images directory
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

        lv1 = (ListView) findViewById(R.id.listView1);
        lv1.setAdapter(adapt);
        //Tapping on a teacher in the listview will allow the user to edit
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object t = lv1.getItemAtPosition(position);
                oldTeacher = (Teacher)t;
                Toast.makeText(getApplicationContext(), "Editing: " + oldTeacher.getFirstName(), Toast.LENGTH_SHORT).show();
                btnImage.setImageBitmap(Utils.getImage(oldTeacher.getImage()));
                firstName.setText(oldTeacher.getFirstName());
                lastName.setText(oldTeacher.getLastName());
                email.setText(oldTeacher.getEmail());
                phone.setText(oldTeacher.getPhoneNum());
                update = true;
                btnAdd.setText("Done");
                btnClear.setText("Delete");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update)
                {
                    updateTeacher(oldTeacher);
                    update=false;
                    btnAdd.setText("Add Teacher");
                    btnClear.setText("Clear Teachers");
                }
                else {
                    addTeacherNow();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update)
                {
                    deleteMessage();
                }
                else {
                    clearAllMessage();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        tList = dbHelper.getAllTeachers();
        adapt = new CustomTeacherAdapter(this, tList);
        lv1.setAdapter(adapt);
    }

    //Button click event for adding a new teacher
    public void addTeacherNow() {
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String e = email.getText().toString();
        String p = phone.getText().toString();

        if(first.isEmpty() || last.isEmpty() || e.isEmpty() || p.isEmpty())
            //If any fields are empty, the user will be notified
            Toast.makeText(getApplicationContext(), "Please Enter In All Information.", Toast.LENGTH_LONG).show();
        else {
            Teacher teacher;
            if(selectedImageUri == null) {
                Bitmap bitmap = ((BitmapDrawable)btnImage.getDrawable()).getBitmap();
                teacher = new Teacher(first, last, e, p, Utils.getImageBytes(bitmap));
            }
            else
            {
                teacher = new Teacher(first, last, e, p, saveImage(selectedImageUri));
            }
            //adds teacher to database
            dbHelper.addTeacher(teacher);
            clearFields();
            resetAdapter();
        }
        hideSoftKeyboard();
    }

    //passes in old teacher
    public void updateTeacher(Teacher oldTeach) {
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String e = email.getText().toString();
        String p = phone.getText().toString();

        if(first.isEmpty() || last.isEmpty() || e.isEmpty() || p.isEmpty())
            //If any fields are empty, the user will be notified
            Toast.makeText(getApplicationContext(), "Please Enter In All Information.", Toast.LENGTH_LONG).show();
        else {
            Bitmap imgBitmap = ((BitmapDrawable)btnImage.getDrawable()).getBitmap();
            //updates teacher in database
            Teacher teacher = new Teacher(first, last, e, p, Utils.getImageBytes(imgBitmap));
            teacher.setTeacherId(oldTeach.getTeacherId());
            dbHelper.updateTeacher(teacher);
            clearFields();
            resetAdapter();
            hideSoftKeyboard();
        }
    }

    public void deleteTeacher() {
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String e = email.getText().toString();
        String p = phone.getText().toString();
        Teacher teacher = new Teacher(first, last, e, p);
        teacher.setTeacherId(oldTeacher.getTeacherId());
        dbHelper.deleteTeacher(teacher);
        //updates students still in database to new teacher id
        dbHelper.updateStudentTeacherId(teacher);
        clearFields();
        resetAdapter();
        hideSoftKeyboard();
    }

    //Button click event for deleting all teachers
    public void clearTeachers () {
        dbHelper.clearTeachers(tList);
        //updates students still in database to new teacher id
        dbHelper.updateStudentTeacherIds();
        adapt.notifyDataSetChanged();
        hideSoftKeyboard();
    }

    //clears all fields
    private void clearFields() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        phone.setText("");
    }

    //resets adapter everytime database is changed
    private void resetAdapter() {
        tList = dbHelper.getAllTeachers();
        adapt = new CustomTeacherAdapter(this, tList);
        lv1 = (ListView) findViewById(R.id.listView1);
        lv1.setAdapter(adapt);
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

    //popup asking if their sure they want to delete teacher
    public void deleteMessage() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete Teacher")
                .setMessage("Are you sure you want to delete this teacher?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteTeacher();
                        update = false;
                        Toast.makeText(getApplicationContext(), "Teacher Deleted", Toast.LENGTH_LONG).show();
                        btnAdd.setText("Add Teacher");
                        btnClear.setText("Clear Teachers");
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
    //message before deleting all
    private void clearAllMessage() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete Teachers")
                .setMessage("Are you sure you want to delete all teachers? This information CANNOT be recovered.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    clearTeachers();
                    Toast.makeText(getApplicationContext(), "All Teachers Deleted", Toast.LENGTH_SHORT).show();
                    update=false;
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //confirms picture selection
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

    // Save the image by converting it to bytes
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
