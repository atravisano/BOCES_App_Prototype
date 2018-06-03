package com.example.anthony.travisano_boces1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Anthony Travisano
 * Purpose: The database for the student and teacher tables.
 *  Database also includes pictures for teachers, students, and tasks.
 *  Keeps a table of work times
 *  Operations include being able to add and delete(all) students and teachers.
 * Date: 9/28/2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    //Define the Database and Table
    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "teachersAndStudents";
    private static final String DATABASE_TABLE_TEACHERS = "teachers";
    private static final String DATABASE_TABLE_STUDENTS = "students";
    private static final String DATABASE_TABLE_WORK = "workTime";
    private static final String DATABASE_TABLE_TASKS = "tasks";

    //Define the column names for the  Teacher Table
    private static final String KEY_TEACHER_ID = "teacherID";
    private static final String KEY_TEACHER_IMAGE = "image";
    private static final String KEY_TEACHER_FIRST_NAME = "tFirstName";
    private static final String KEY_TEACHER_LAST_NAME = "tLastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";

    //Define the column names for the Student Table
    private static final String KEY_STUDENT_ID = "_id";
    private static final String KEY_STUDENT_IMAGE = "image";
    private static final String KEY_STUDENT_FIRST_NAME = "sFirstName";
    private static final String KEY_STUDENT_LAST_NAME = "sLastName";
    private static final String KEY_AGE = "Age";
    private static final String KEY_YEAR = "Year";
    //Student also uses the teacher's ID

    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "taskName";
    private static final String KEY_TASK_IMAGE = "image";

    //Define the column names for workTime table
    private static final String KEY_WORK_ID = "id";
    //stores student name and id
    private static final String KEY_WORK_STUDENT_NAME = "studentName";
    private static final String KEY_WORK_TASK_NAME = "taskName";
    private static final String KEY_START_WORK = "startTime";
    private static final String KEY_STOP_WORK = "stopTime";
    //amount of time student worked on task
    private static final String KEY_WORK_TIME = "workTime";

    private int teacherCount;
    private int studentCount;

    public DBHelper (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creates table for teacher
        String teacherTable = "CREATE TABLE " + DATABASE_TABLE_TEACHERS + "(" +
                KEY_TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TEACHER_FIRST_NAME +" TEXT, " + KEY_TEACHER_LAST_NAME + " TEXT, " +
                KEY_EMAIL + " TEXT, " + KEY_PHONE_NUMBER + " TEXT, " + KEY_TEACHER_IMAGE + " BLOB NOT NULL" + ")";
        //Creates table for student
        String studentTable = "CREATE TABLE " + DATABASE_TABLE_STUDENTS + "(" +
                KEY_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_STUDENT_FIRST_NAME +" TEXT, " + KEY_STUDENT_LAST_NAME + " TEXT, " +
                KEY_AGE + " INTEGER, " +KEY_TEACHER_ID + " INTEGER, " + KEY_YEAR +
                " TEXT, " + KEY_STUDENT_IMAGE + " BLOB NOT NULL" +")";
        //Creates table for tasks
        String taskTable = "CREATE TABLE " + DATABASE_TABLE_TASKS + "(" + KEY_TASK_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASK_NAME + " TEXT, " +
                KEY_TASK_IMAGE + " BLOB NOT NULL)";

        //create table for work times
        String workTable = "CREATE TABLE " + DATABASE_TABLE_WORK + "(" + KEY_WORK_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_STUDENT_ID + " INTEGER, " + KEY_WORK_STUDENT_NAME +
                " TEXT, " + KEY_WORK_TASK_NAME + " TEXT, " + KEY_START_WORK + " TEXT, " + KEY_STOP_WORK +
                " TEXT, " + KEY_WORK_TIME + " TEXT" +")";

        db.execSQL(teacherTable);
        db.execSQL(studentTable);
        db.execSQL(taskTable);
        db.execSQL(workTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TEACHERS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_WORK);

        //Create Table again
        onCreate(db);
    }

    //******DATABASE OPERATIONS: ADD, EDIT, DELETE
    //Adding new teacher
    public void addTeacher(Teacher teacher) {
        //Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Getting Teacher info
        //First name, last name, email, phone number
        values.put(KEY_TEACHER_FIRST_NAME, teacher.getFirstName());
        values.put(KEY_TEACHER_LAST_NAME, teacher.getLastName());
        values.put(KEY_EMAIL, teacher.getEmail());
        values.put(KEY_PHONE_NUMBER, teacher.getPhoneNum());
        values.put(KEY_TEACHER_IMAGE, teacher.getImage());

        //Insert the row in the teacher table
        db.insert(DATABASE_TABLE_TEACHERS, null, values);
        teacherCount++;
        //Close Database Connection
        db.close();
    }

    //add new students to list
    public void addStudent(Student student) {
        //Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Getting Student info
        //First name, last name, age, teacherID, Class/Year
        //adds values to the set
        values.put(KEY_STUDENT_FIRST_NAME, student.getFirstName());
        values.put(KEY_STUDENT_LAST_NAME, student.getLastName());
        values.put(KEY_AGE, student.getAge());
        values.put(KEY_TEACHER_ID, student.getTeacherId());
        values.put(KEY_YEAR, student.getYear());
        values.put(KEY_STUDENT_IMAGE, student.getImage());

        //Insert the row in the student table
        db.insert(DATABASE_TABLE_STUDENTS, null, values);
        studentCount++;
        //Close Database Connection
        db.close();
    }

    //returns a list of all the teachers from the table
    public ArrayList<Teacher> getAllTeachers() {
        //get all teachers on the list
        ArrayList<Teacher> teachList = new ArrayList<>();
        //Select all query from the table
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_TEACHERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //loop through teachers
        if(cursor.moveToFirst()) {
            do {
                Teacher teacher = new Teacher();
                teacher.setTeacherId(cursor.getString(0));
                teacher.setFirstName(cursor.getString(1));
                teacher.setLastName(cursor.getString(2));
                teacher.setEmail(cursor.getString(3));
                teacher.setPhoneNum(cursor.getString(4));
                teacher.setImage(cursor.getBlob(5));
                //adding teacher to list
                teachList.add(teacher);
            } while (cursor.moveToNext());
        }
        //return list of teachers from the table
        return teachList;
    }

    //returns a list of all the students from the table
    public ArrayList<Student> getAllStudents() {
        //get all students on the list
        ArrayList<Student> studList = new ArrayList<>();
        //select all query from the table
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_STUDENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        //Runs the provided SQL and returns a Cursor over the result set.
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through students
        if(cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setStudentId(cursor.getInt(0));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                student.setAge(cursor.getString(3));
                student.setTeacherId(cursor.getString(4));
                student.setYear(cursor.getString(5));
                student.setImage(cursor.getBlob(6));
                //adding student to array list
                studList.add(student);

            } while (cursor.moveToNext());
        }
        return studList;
    }



    //filters by studentID
    public ArrayList<Student> getAssociatedStudents(String teacherId) {
        //get all students on the list
        ArrayList<Student> studList = new ArrayList<>();
        //select all query from the table
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_STUDENTS + " WHERE " +
                KEY_TEACHER_ID +  "=" + teacherId;


        SQLiteDatabase db = this.getWritableDatabase();
        //Runs the provided SQL and returns a Cursor over the result set.
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through students
        if(cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setStudentId(cursor.getInt(0));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                student.setAge(cursor.getString(3));
                student.setTeacherId(cursor.getString(4));
                student.setYear(cursor.getString(5));
                student.setImage(cursor.getBlob(6));
                //adding student to array list
                studList.add(student);

            } while (cursor.moveToNext());
        }
        return studList;
    }

    //get all teachers on list and clear them
    public void clearTeachers(List<Teacher> list) {
        list.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_TEACHERS, null, new String[]{});
        db.close();
    }

    //get all students on list and clear them
    public void clearStudents(List<Student> list) {
        list.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_STUDENTS, null, new String[]{});
        db.close();
    }

    //removes all tasks from database
    public void clearTasks(List<Task> list) {
        list.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_TASKS, null, new String[]{});
        db.close();
    }

    //deletes a selected teacher from database
    public void deleteTeacher(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_TEACHERS,KEY_TEACHER_ID + " = ?", new String[]{String.valueOf(teacher.getTeacherId())});
    }

    //removes specific student from database
    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_STUDENTS,KEY_STUDENT_ID + " = ?", new String[]{String.valueOf(student.getStudentId())});

    }

    //removes task from database
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_TASKS,KEY_TASK_ID + " = ?", new String[]{String.valueOf(task.getId())});
    }

    //updates students' teacher id when that teacher is no longer in database
    public void updateStudentTeacherId(Teacher t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //set all student's teacher id to -1
        for (Student student: getAllStudents()) {
            if(student.getTeacherId().equals(t.getTeacherId())) {
                values.put(KEY_TEACHER_ID, -1);
                db.update(DATABASE_TABLE_STUDENTS, values, KEY_STUDENT_ID + " = ?", new String[]{String.valueOf(student.getStudentId())});
                break;
            }
        }
        db.close();
    }

    //updates all students' teacher id when teachers are no longer in database
    public void updateStudentTeacherIds() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //set all student's teacher id to -1
        for (Student student: getAllStudents()) {
            values.put(KEY_TEACHER_ID, -1);
            db.update(DATABASE_TABLE_STUDENTS, values, KEY_STUDENT_ID + " = ?", new String[]{String.valueOf(student.getStudentId())});
        }
        db.close();
    }

    //updates all of Teacher's values
    public void updateTeacher(Teacher teacher) {
        //updating row
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //First name, last name, email, phone number
        values.put(KEY_TEACHER_FIRST_NAME, teacher.getFirstName());
        values.put(KEY_TEACHER_LAST_NAME, teacher.getLastName());
        values.put(KEY_EMAIL, teacher.getEmail());
        values.put(KEY_PHONE_NUMBER, teacher.getPhoneNum());
        values.put(KEY_TEACHER_IMAGE, teacher.getImage());
        db.update(DATABASE_TABLE_TEACHERS, values, KEY_TEACHER_ID + " = ?", new String[]{String.valueOf(teacher.getTeacherId())});
        db.close();
    }

    //updates all of the Student's values
    public void updateStudent(Student student) {
        //updating row
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //First name, last name, age, teacherID, Class/Year
        values.put(KEY_STUDENT_FIRST_NAME, student.getFirstName());
        values.put(KEY_STUDENT_LAST_NAME, student.getLastName());
        values.put(KEY_AGE, student.getAge());
        values.put(KEY_TEACHER_ID, student.getTeacherId());
        values.put(KEY_YEAR, student.getYear());
        values.put(KEY_STUDENT_IMAGE, student.getImage());
        db.update(DATABASE_TABLE_STUDENTS, values, KEY_STUDENT_ID + " = ?", new String[]{String.valueOf(student.getStudentId())});
        db.close();
    }

    //updates task's name and picture
    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, task.getTaskName());
        values.put(KEY_TASK_IMAGE, task.getImage());
        db.update(DATABASE_TABLE_TASKS, values, KEY_TASK_ID + " = ?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    // Insert the image to the Sqlite DB
    public void insertTask(Task t) {
        //Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TASK_NAME, t.getTaskName());
        cv.put(KEY_TASK_IMAGE, t.getImage());
        db.insert(DATABASE_TABLE_TASKS, null, cv);
        db.close();
    }

    //adds all info about working student
    public void addWorkTime(Work work) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STUDENT_ID, work.getStudentId());
        cv.put(KEY_WORK_STUDENT_NAME, work.getStudentName());
        cv.put(KEY_WORK_TASK_NAME, work.getTaskName());
        cv.put(KEY_START_WORK, work.getStartTime());
        cv.put(KEY_STOP_WORK, work.getStopTime());
        cv.put(KEY_WORK_TIME, work.getWorkTime());
        db.insert(DATABASE_TABLE_WORK, null, cv);
        db.close();
    }

    //returns an arraylist of all the tasks in database
    public ArrayList<Task> getAllTasks() {
        //get all students on the list
        ArrayList<Task> taskList = new ArrayList<>();
        //select all query from the table
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        //Runs the provided SQL and returns a Cursor over the result set.
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through students
        if(cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getString(0));
                task.setTaskName(cursor.getString(1));
                task.setImage(cursor.getBlob(2));
                //adding student to array list
                taskList.add(task);

            } while (cursor.moveToNext());
        }
        return taskList;
    }

    //gets all work data from database and puts the in Work ArrayList
    public ArrayList<Work> getAllWork() {
        ArrayList<Work> workList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE_WORK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //loop through work
        if(cursor.moveToFirst()) {
            do {
                Work w = new Work();
                w.setWorkId(cursor.getString(0));
                w.setStudentId(cursor.getInt(1));
                w.setStudentName(cursor.getString(2));
                w.setTaskName(cursor.getString(3));
                w.setStartTime(cursor.getString(4));
                w.setStopTime(cursor.getString(5));
                w.setWorkTime(cursor.getString(6));
                workList.add(w);

            }while(cursor.moveToNext());
        }

        return workList;
    }


    // Get the image from SQLite DB
    public byte[] retreiveImageFromDB() {
        //Create and/or open a database that will be used for reading and writing.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.query(true, DATABASE_TABLE_TASKS, new String[]{KEY_TASK_IMAGE,},
                null, null, null, null,
                KEY_TASK_ID + " DESC", "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(KEY_TASK_IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }
}
