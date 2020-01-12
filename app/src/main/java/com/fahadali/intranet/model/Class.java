package com.fahadali.intranet.model;


import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.ArrayList;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class Class {

    private int id;
    private String title;
    private ArrayList<Course> courses;

    public ArrayList<Course> getCoursesOfTheDay(int dayOfWeek) {
         ArrayList<Course> todaysCourses = new ArrayList<>();

        for (Course course: courses) {
            for(Lesson lesson: course.getLessons()) {
                if(lesson.getWeekday().getValue() == dayOfWeek+1) { //TODO: Remove the -3.
                    Course c = new Course();
                    ArrayList<Lesson> ls = new ArrayList<>();
                    ls.add(lesson);
                    c.setLessons(ls);
                    c.setId(course.getId());
                    c.setTeacher(course.getTeacher());
                    c.setTitle(course.getTitle());
                    todaysCourses.add(c);
                }
            }

        }

        return todaysCourses;
    }
}

