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
 * Created by Anthony on 11/12/2017.
 * Purpose: displays all info about working student in listview
 */

public class CustomReportAdapter extends BaseAdapter{

    private static ArrayList<Work> workList;

    private LayoutInflater mInflater;

    public CustomReportAdapter(Context context, ArrayList<Work> results) {
        workList = results;
        mInflater = LayoutInflater.from(context);
    }


    public int getCount() {
        return workList.size();
    }

    public Object getItem(int position) {
        return workList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder work;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view3, null);
            work = new ViewHolder();
            work.name = (TextView) convertView.findViewById(R.id.name);
            work.start = (TextView) convertView.findViewById(R.id.timeStart);
            work.stop = (TextView) convertView.findViewById(R.id.timeStop);
            work.time = (TextView) convertView.findViewById(R.id.timeWorked);
            convertView.setTag(work);
        }
        else {
            work = (ViewHolder) convertView.getTag();
        }

        work.name.setText(workList.get(position).getStudentName());
        work.start.setText("Start: " + workList.get(position).getStartTime());
        work.stop.setText("Stop: " + workList.get(position).getStopTime());
        work.time.setText("Time Working: " + workList.get(position).getWorkTime());

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView start;
        TextView stop;
        TextView time;
    }
}
