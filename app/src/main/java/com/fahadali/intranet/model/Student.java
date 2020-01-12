package com.fahadali.intranet.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class Student {

    private static final String TAG = "Student";
    private static Student instance;
    private int id;
    private String name;
    private String email;
    @SerializedName("class")
    private Class _class;
    private ArrayList<Attendance> attendances;

    private Student() {
        if(attendances == null) {
            attendances = new ArrayList<>();
        }

    }

    public static Student getInstance() {
        if(instance == null) {
            instance = new Student();
            Log.i(TAG, "getInstance: New user has been initialized");
        }

        return instance;
    }

    public void setStudent(Student student){
        setId(student.id);
        setName(student.name);
        setEmail(student.email);
        set_class(student._class);
        setAttendances(student.attendances);
    }


    public void nullify() {

        instance = null;
    }
}


