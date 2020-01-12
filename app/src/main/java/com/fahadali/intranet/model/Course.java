package com.fahadali.intranet.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Course {

    private int id;
    private String title;
    private ArrayList<Lesson> lessons;
    private Teacher teacher;
}


