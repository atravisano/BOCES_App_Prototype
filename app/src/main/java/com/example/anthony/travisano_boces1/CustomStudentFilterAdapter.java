package com.example.anthony.travisano_boces1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anthony on 10/25/2017.
 * Purpose: Filters the students by teacher's id
 *  Formats items in listview with an image followed by the text the user entered
 * Date: 12/5/17
 */

public class CustomStudentFilterAdapter extends BaseAdapter implements Filterable {
    private static ArrayList<Student> studentList;
    private ArrayList<Student> studentFilterList;
    private StudentFilter studFilter;
    private LayoutInflater mInflater;

    public CustomStudentFilterAdapter(Context context, ArrayList<Student> results) {
        studentList = results;
        studentFilterList = results;
        mInflater = LayoutInflater.from(context);
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
            convertView = mInflater.inflate(R.layout.list_single, null);
            student = new ViewHolder();
            student.image = (ImageView) convertView.findViewById(R.id.img);
            student.txtName = (TextView) convertView.findViewById(R.id.txt);
            convertView.setTag(student);
        }
        else {
            student = (ViewHolder) convertView.getTag();
        }

        student.image.setImageBitmap(Utils.getImage(studentList.get(position).getImage()));
        student.txtName.setText(studentList.get(position).getFullName());
        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView txtName;

    }

    public Filter getFilter() {
        if(studFilter == null) {
            studFilter = new StudentFilter();
        }
        return studFilter;
    }

    //creates a new student list filtered by teacher id
    private class StudentFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String teachId = constraint.toString();
            FilterResults results = new FilterResults();

            ArrayList<Student> filterList = new ArrayList<Student>();
            for (int i = 0; i < studentFilterList.size(); i++) {
                //only students with same teacher id go in studentFilterList
                if (studentFilterList.get(i).getTeacherId().equals(teachId)) {
                    Student stud = studentFilterList.get(i);
                    filterList.add(stud);
                    }
            }

            results.count = filterList.size();
            results.values = filterList;

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            studentList = (ArrayList<Student>) results.values;
            notifyDataSetChanged();
        }
    }

}
