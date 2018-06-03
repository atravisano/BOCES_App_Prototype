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
Purpose: user can add, delete, or clear all tasks from the database.  Each task has an image and
text associated with it.
Date: 12/5/17
 */

public class TaskActivity extends AppCompatActivity {

    private EditText taskName;
    private ImageButton btnImageTask;
    private Button btnAdd;
    private Button btnClear;
    private ListView listView;
    private DBHelper dbHelper;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "TaskActivity";
    private Uri selectedImageUri;
    private CustomTaskAdapter adapt;
    private ArrayList<Task> tasks;

    private Task oldTask;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskName = (EditText) findViewById(R.id.txtTaskName);
        btnImageTask = (ImageButton) findViewById(R.id.btnImageTask);
        btnAdd = (Button) findViewById(R.id.btnAddTask);
        btnClear = (Button) findViewById(R.id.btnClearTasks);
        listView = (ListView) findViewById(R.id.lvTask);
        dbHelper = new DBHelper(this);
        tasks = dbHelper.getAllTasks();
        adapt = new CustomTaskAdapter(this, R.layout.custom_row_view4, tasks);
        listView.setAdapter(adapt);


        btnImageTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
        //Tapping on a task in the listview will allow the user to edit
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object task = listView.getItemAtPosition(position);
                oldTask = (Task)task;
                Toast.makeText(getApplicationContext(), "Editing Task: " + oldTask.getTaskName(), Toast.LENGTH_SHORT).show();
                taskName.setText(oldTask.getTaskName());
                btnImageTask.setImageBitmap(Utils.getImage(oldTask.getImage()));
                update = true;
                btnAdd.setText("Done");
                btnClear.setText("Delete");
            }
        });

        //adds tasks to database or updates task
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update)
                {
                    updateTask(oldTask);
                    update=false;
                    btnAdd.setText("Add Task");
                    btnClear.setText("Clear Tasks");
                }
                else {
                    addTask();
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
        dbHelper = new DBHelper(this);
        tasks = dbHelper.getAllTasks();
        adapt = new CustomTaskAdapter(this, R.layout.custom_row_view4, tasks);
        listView.setAdapter(adapt);
    }

    public void addTask() {
        String label = taskName.getText().toString();
        if(label.isEmpty())
            Toast.makeText(getApplicationContext(), "Please type in task name", Toast.LENGTH_LONG).show();
        else {
            Task task;
            //getting image
            if(selectedImageUri == null) {
                Bitmap bitmap = ((BitmapDrawable)btnImageTask.getDrawable()).getBitmap();
                task = new Task(label, Utils.getImageBytes(bitmap));
            } else {
                task = new Task(label, saveImage(selectedImageUri));
            }
            dbHelper.insertTask(task);
            //clear field
            taskName.setText("");
            tasks = dbHelper.getAllTasks();
            adapt = new CustomTaskAdapter(this, R.layout.custom_row_view4, tasks);
            listView = (ListView) findViewById(R.id.lvTask);
            listView.setAdapter(adapt);
            hideSoftKeyboard();
            adapt.notifyDataSetChanged();
        }
    }

    public void clearTasks() {
        dbHelper.clearTasks(tasks);
        adapt.notifyDataSetChanged();
    }

    public void updateTask(Task oldTask) {
        String label = taskName.getText().toString();
        if(label.isEmpty())
            Toast.makeText(getApplicationContext(), "Please type in task name", Toast.LENGTH_LONG).show();
        else {
            //cast current imageView to Bitmap
            Bitmap imgBitmap = ((BitmapDrawable)btnImageTask.getDrawable()).getBitmap();
            Task t = new Task(label, Utils.getImageBytes(imgBitmap));
            //update task id
            t.setId(oldTask.getId());
            dbHelper.updateTask(t);
            //clear field
            taskName.setText("");
            tasks = dbHelper.getAllTasks();
            adapt = new CustomTaskAdapter(this, R.layout.custom_row_view4, tasks);
            listView = (ListView) findViewById(R.id.lvTask);
            listView.setAdapter(adapt);
            hideSoftKeyboard();
            adapt.notifyDataSetChanged();
        }

    }

    public void deleteTask() {
        String name = taskName.getText().toString();
        Task t = new Task(name);
        t.setId(oldTask.getId());
        dbHelper.deleteTask(t);
        //clear field
        taskName.setText("");
        tasks = dbHelper.getAllTasks();
        adapt = new CustomTaskAdapter(this,R.layout.custom_row_view4, tasks);
        listView = (ListView) findViewById(R.id.lvTask);
        listView.setAdapter(adapt);
        hideSoftKeyboard();
        adapt.notifyDataSetChanged();
    }

    // Choose an image from Gallery
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");      //images directory
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    //confirms selection
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    btnImageTask.setImageURI(selectedImageUri);
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

    //popup asking if they are sure they want to delete task
    public void deleteMessage() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteTask();
                        update = false;
                        Toast.makeText(getApplicationContext(), "Task Deleted", Toast.LENGTH_LONG).show();
                        btnAdd.setText("Add Task");
                        btnClear.setText("Clear Tasks");
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
        builder.setTitle("Delete All Tasks")
                .setMessage("Are you sure you want to delete all tasks? This information CANNOT be recovered.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearTasks();
                        Toast.makeText(getApplicationContext(), "All Tasks Deleted", Toast.LENGTH_SHORT).show();
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

    //hides soft keyboard from screen
    private void hideSoftKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
