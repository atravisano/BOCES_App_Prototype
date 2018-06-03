package com.example.anthony.travisano_boces1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Anthony Travisano
 * Purpose: displays a picture followed by text for each task in the listview
 * Date: 11/4/17
 */

public class CustomTaskAdapter extends BaseAdapter {
    private ArrayList<Task> tasks;
    private LayoutInflater mInflater;
    //represents what layout was passed in
    private int selectedLayout;

    public CustomTaskAdapter(Context context, int selectedLayout, ArrayList<Task> tasks) {
        mInflater = LayoutInflater.from(context);
        this.tasks = tasks;
        this.selectedLayout = selectedLayout;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder task;
        if(convertView == null) {
            convertView = mInflater.inflate(selectedLayout, null, true);
            task = new ViewHolder();
            task.txtTask = (TextView) convertView.findViewById(R.id.txt);
            task.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(task);
        }
        else {
            task = (ViewHolder) convertView.getTag();
        }

        task.txtTask.setText(tasks.get(position).getTaskName());
        task.img.setImageBitmap(Utils.getImage(tasks.get(position).getImage()));

    return convertView;
    }

    private class ViewHolder {
        TextView txtTask;
        ImageView img;
    }

}

