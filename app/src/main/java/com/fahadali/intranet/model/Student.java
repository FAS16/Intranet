package com.fahadali.intranet.model;

import java.util.ArrayList;

public class Student {

    private int id;
    private String name;
    private ArrayList<Subject> enrolledSubjects;


    public Student(int id, String name, ArrayList<Subject> enrolledSubjects) {
        this.id = id;
        this.name = name;
        this.enrolledSubjects = enrolledSubjects;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", enrolledSubjects=" + enrolledSubjects +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Subject> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public void setEnrolledSubjects(ArrayList<Subject> enrolledSubjects) {
        this.enrolledSubjects = enrolledSubjects;
    }
}


