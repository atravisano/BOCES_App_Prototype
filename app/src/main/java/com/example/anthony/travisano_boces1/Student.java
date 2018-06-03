package com.example.anthony.travisano_boces1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anthony on 9/28/2017.
 * Purpose: Represents a student as an object
 */

public class Student implements Parcelable{

    //member attributes
    private int studentId;
    private String teacherId;
    private String firstName;
    private String lastName;
    private String age;
    private String year;
    private byte[] image;

    public Student() { //default constructor
    }

    public Student(String firstName, String lastName, String age, String teacherId, String year)
    {
        //overloaded constructor
        this.setStudentId(studentId);
        this.setTeacherId(teacherId);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.setYear(year);
    }
    public Student(String firstName, String lastName, String age, String teacherId, String year, byte[] image)
    {
        //overloaded constructor
        this.setStudentId(studentId);
        this.setTeacherId(teacherId);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setAge(age);
        this.setYear(year);
        this.setImage(image);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**** Accessors and Mutators ****/
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    //**********Parcelable Code
    protected Student(Parcel in) {
        studentId = in.readInt();
        teacherId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        age = in.readString();
        year = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(studentId);
        dest.writeString(teacherId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(age);
        dest.writeString(year);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
