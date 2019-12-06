package com.fahadali.intranet.model;

import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Subject {

    private int id;
    private String title;
    private String note;
    private ArrayList<Class> classes;


    public Subject(int id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Class> classes) {
        this.classes = classes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
