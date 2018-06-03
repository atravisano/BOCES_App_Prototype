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
 * Purpose: sets up and displays the teacher's information and image in the ListView on the add teacher screen
 * Date: 12/5/2017.
 */

public class CustomTeacherAdapter extends BaseAdapter{
    private static ArrayList<Teacher> teacherList;

    private LayoutInflater mInflater;

    public CustomTeacherAdapter(Context context, ArrayList<Teacher> results) {
        teacherList = results;
        mInflater = LayoutInflater.from(context);
    }


    public int getCount() {
        return teacherList.size();
    }

    public Object getItem(int position) {
        return teacherList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder teacher;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            teacher = new ViewHolder();
            teacher.image = (ImageView) convertView.findViewById(R.id.imgTeacher);
            teacher.txtName = (TextView) convertView.findViewById(R.id.first);
            teacher.txtEmail = (TextView) convertView.findViewById(R.id.email);
            teacher.txtPhone = (TextView) convertView.findViewById(R.id.phone);
            convertView.setTag(teacher);
        }
        else {
            teacher = (ViewHolder) convertView.getTag();
        }

        teacher.image.setImageBitmap(Utils.getImage(teacherList.get(position).getImage()));
        teacher.txtName.setText(teacherList.get(position).getFullName());
        teacher.txtEmail.setText(teacherList.get(position).getEmail());
        teacher.txtPhone.setText(teacherList.get(position).getPhoneNum());

        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView txtName;
        TextView txtEmail;
        TextView txtPhone;
    }



}
