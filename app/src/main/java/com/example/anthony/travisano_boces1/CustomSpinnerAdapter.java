package com.example.anthony.travisano_boces1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Author: Anthony Travisano
 * Purpose: provides layout of Spinner.
 *  Displays the Teacher's picture and name.
 * Created by Anthony on 10/8/2017.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<Teacher> {
    private List<Teacher> list;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, List<Teacher> t) {
        super(context, textViewResourceId, t);
        inflater = ((Activity) context).getLayoutInflater();
        list = t;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null) {
            row = inflater.inflate(R.layout.list_single, parent, false);
            holder.image = (ImageView) row.findViewById(R.id.img);
            holder.name = (TextView) row.findViewById(R.id.txt);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }
        //first name and last name
        if(list.get(position).getImage() != null) {
            //gets image if available
            holder.image.setImageBitmap(Utils.getImage(list.get(position).getImage()));
        }
        holder.name.setText(list.get(position).getFullName());

        return row;
    }

    private class ViewHolder {
        ImageView image;
        TextView name;
    }
}
