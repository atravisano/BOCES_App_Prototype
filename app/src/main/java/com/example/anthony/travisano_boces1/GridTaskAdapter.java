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
 * Purpose: setups layout for gridview of tasks, containing an image and title
 * Date: 12/4/17
 */

public class GridTaskAdapter extends BaseAdapter{
    private static ArrayList<Task> tasks;
    private LayoutInflater mInflater;

    public GridTaskAdapter(Context c,ArrayList<Task> results) {
        tasks = results;
        mInflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return tasks.size();
    }

    public Object getItem(int position) {
        return tasks.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder task;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.gridview_task, null);
            task = new ViewHolder();
            task.image = (ImageView) convertView.findViewById(R.id.gridImage);
            task.text = (TextView) convertView.findViewById(R.id.gridTaskName);
            convertView.setTag(task);
        }
        else {
            task = (ViewHolder) convertView.getTag();
        }

        task.image.setImageBitmap(Utils.getImage(tasks.get(position).getImage()));
        task.text.setText(tasks.get(position).getTaskName());

        return convertView;
    }

    private class ViewHolder {
        //picture of task
        ImageView image;
        //label of task
        TextView text;

    }
}
