package com.example.anthony.travisano_boces1;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/*
Author: Anthony Travisano
Purpose: Allows user to enter a date range (start date - end date)
    user can choose if student clock ins should be continued
    This activity creates the pdf based off this information
Date: 12/7/17
 */

public class ReportActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Button btnPdf;
    private CustomReportAdapter adapter;
    private DBHelper dbHelper;
    private ExportToPdf pdf;
    //holds start/end date range in textview
    private EditText txtStartDate;
    private EditText txtEndDate;
    private DatePickerDialog datePickerDialog;

    //switch determines if report should include all student clock ins
    private Switch switchClockIn;

    //start
    private Date startDate;
    //end
    private Date endDate;

    private static final int PERMISSION_REQUEST_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        txtStartDate = (EditText) findViewById(R.id.txtStartDate);
        txtEndDate = (EditText) findViewById(R.id.txtEndDate);
        btnPdf = (Button) findViewById(R.id.btnGenPdf);
        switchClockIn = (Switch) findViewById(R.id.switchClockIn);

        dbHelper = new DBHelper(this);


        pdf = new ExportToPdf(this, dbHelper.getAllWork());

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //will create a pdf based on date range if the range is valid
                if (isValidDateRange()) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            showPdfPreview();
                        }
                    };
                    thread.start();
                }
            }
        });
        //format used when outputting user selection to textview
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFromDatePickerDialog(txtStartDate, simpleDateFormat);
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createToDatePickerDialog(txtEndDate, simpleDateFormat);
            }
        });
    }

    private void createFromDatePickerDialog(final EditText txtDate, final SimpleDateFormat simpleDateFormat) {
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR); // current year
        final int mMonth = c.get(Calendar.MONTH); // current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(ReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    //http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        //save chosen date to check later if range is valid
                        startDate = newDate.getTime();
                        // set day of month , month and year value in the edit text
                        txtDate.setText(simpleDateFormat.format(newDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
     }

    private void createToDatePickerDialog(final EditText txtDate, final SimpleDateFormat simpleDateFormat) {
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR); // current year
        final int mMonth = c.get(Calendar.MONTH); // current month
        final int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(ReportActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    //http://androidopentutorials.com/android-datepickerdialog-on-edittext-click-event/
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        //save chosen date to check later if range is valid
                        endDate = newDate.getTime();
                        // set day of month , month and year value in the edit text
                        txtDate.setText(simpleDateFormat.format(newDate.getTime()));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //checks if the start and end dates are possible
     private boolean isValidDateRange(){
        //date objects to check bounds and if user entered date
        if(txtStartDate.getText().toString().equals("") || txtEndDate.getText().toString().equals("") || !startDate.before(endDate)) {
            //if the end month, day, or year is in the future
            Toast.makeText(getApplicationContext(), "The chosen date range is not valid", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
     }

    protected void onRestart() {
        super.onRestart();
        dbHelper = new DBHelper(this);
    }

    //creates pdf if storage permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for Storage permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start creating pdf
                showToast("Storage permission was granted.");
                //create pdf
                createNewPdf();
            } else {
                // Permission request was denied.
                showToast("Storage permission request was denied.");
            }
        }
    }

    private void showPdfPreview() {
        // Check if the Storage permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, create pdf
            createNewPdf();
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }
    }

    //Requests Storage permission
    private void requestStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide a window to the user if the permission was not granted
            // Request the permission
            ActivityCompat.requestPermissions(ReportActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        } else {
            showToast("Permission is not available. Requesting permission.");
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);
        }
    }

    //creates the pdf file in in storage in the BOCES folder
    private void createNewPdf() {
        try {
            pdf.setClockIns(switchClockIn.isChecked());
            pdf.setStartDate(txtStartDate.getText().toString());
            pdf.setEndDate(txtEndDate.getText().toString());
            showToast("Creating Report...");
            pdf.createPdf();
            showToast("Report Created!");
            Log.d("ReportActivity", "Pdf finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //shortcut method that is able to run while multithreading
    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
