package com.example.anthony.travisano_boces1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anthony on 11/10/2017.
 * Purpose: Represents the work a student did and date/times
 */

public class Work implements Parcelable{
    private String workId;
    private int studentId;
    private String studentName;
    private String taskName;
    //time student started
    private String startTime;
    //time student stopped
    private String stopTime;
    //time worked on task
    private String workTime;
    //total minutes and seconds worked on task
    private int totalMinutes;
    private int totalSeconds;

    public Work() {
    }

    public Work(int studentId, String studentName, String taskName, String startTime, String stopTime, String workTime) {
        this.setStudentId(studentId);
        this.setStudentName(studentName);
        this.setTaskName(taskName);
        this.setStartTime(startTime);
        this.setStopTime(stopTime);
        this.setWorkTime(workTime);
    }

    public String getTotalWorkTime() {
        totalMinutes += totalSeconds/60;
        totalSeconds = totalSeconds%60;
        String time;
        if(totalSeconds < 10)
            time = totalMinutes + ":0" + totalSeconds;
        else
            time = totalMinutes + ":" + totalSeconds;

        return time;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    //Parcelable methods
    protected Work(Parcel in) {
        workId = in.readString();
        studentId = in.readInt();
        studentName = in.readString();
        taskName = in.readString();
        startTime = in.readString();
        stopTime = in.readString();
        workTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workId);
        dest.writeInt(studentId);
        dest.writeString(studentName);
        dest.writeString(taskName);
        dest.writeString(startTime);
        dest.writeString(stopTime);
        dest.writeString(workTime);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Work> CREATOR = new Parcelable.Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel in) {
            return new Work(in);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(int totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
}
