package com.example.anthony.travisano_boces1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Anthony Travisano
 * Purpose: Sets up and displays the student's information in ListView on the add student screen.
 * Date: 10/6/17
 */

public class CustomStudentAdapter extends BaseAdapter {
    private static ArrayList<Student> studentList;
    private ArrayList<Teacher> teacherList;
    private LayoutInflater mInflater;

    public CustomStudentAdapter(Context context, ArrayList<Student> results, ArrayList<Teacher> teachers) {
        studentList = results;
        mInflater = LayoutInflater.from(context);
        teacherList = teachers;
    }

    public int getCount() {
        return studentList.size();
    }

    public Object getItem(int position) {
        return studentList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder student;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view2, null);
            student = new ViewHolder();
            student.image = (ImageView) convertView.findViewById(R.id.imgStudent);
            student.txtName = (TextView) convertView.findViewById(R.id.first);
            student.txtAge = (TextView) convertView.findViewById(R.id.age);
            student.txtTeacher = (TextView) convertView.findViewById(R.id.teacher);
            student.txtYear = (TextView) convertView.findViewById(R.id.year);
            convertView.setTag(student);
        }
        else {
            student = (ViewHolder) convertView.getTag();
        }

        student.image.setImageBitmap(Utils.getImage(studentList.get(position).getImage()));
        student.txtName.setText(studentList.get(position).getFullName());
        student.txtAge.setText(studentList.get(position).getAge());
        String id = studentList.get(position).getTeacherId();
        //displays teacher's full name
        student.txtTeacher.setText(getTeacherName(id));
        student.txtYear.setText(studentList.get(position).getYear());

        return convertView;
    }

    //Finds teachers id name and returns their full name
    private String getTeacherName(String id) {
        for (Teacher t: teacherList) {
            if(t.getTeacherId().equals(id))
            {
                return t.getFullName();
            }
        }
        return "No Teacher";
    }

    private class ViewHolder {
        ImageView image;
        TextView txtName;
        TextView txtAge;
        TextView txtTeacher;
        TextView txtYear;
    }

}