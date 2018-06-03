package com.example.anthony.travisano_boces1;

/**
 * Created by Anthony on 9/28/2017.
 * Purpose: Represents teacher as an object
 * Date: 11/12/17
 */

public class Teacher {
    private String teacherId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNum;
    private byte[] image;

    public Teacher() {//default constructor
    }

    public Teacher(String firstName, String lastName,
                   String email, String phoneNum){
        //overloaded constructor
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPhoneNum(phoneNum);
    }

    public Teacher(String first, String last, String id) {
        this.setFirstName(first);
        this.setLastName(last);
        this.setTeacherId(id);
    }

    public Teacher(String firstName, String lastName,
                   String email, String phoneNum, byte[] image){
        //overloaded constructor
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPhoneNum(phoneNum);
        this.setImage(image);
    }

    public String toString() {
        return getTeacherId();
    }

    //returns teacher's first and last name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    //CHANGED TEACHER ID TO STRING INSTEAD OF INT

    /**** Accessors and Mutators ****/
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
