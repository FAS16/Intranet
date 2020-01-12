package com.fahadali.intranet.model;



import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor @ToString @Data
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
}
