package com.example.anthony.travisano_boces1;

/**
 * Created by Anthony on 11/4/2017.
 * Purpose: Represents task as an object
 *  each task has an image and name
 */

public class Task {
    private String id;
    private String taskName;
    private byte[] image;

    public Task() {
    }

    public Task(String taskName) {
        this.setTaskName(taskName);
    }

    public Task(String taskName, byte[] pic) {
        this.setTaskName(taskName);
        this.setImage(pic);
    }

    /** Accessor and Mutators **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
