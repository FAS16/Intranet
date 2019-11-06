package com.fahadali.intranet.model;

import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalTime;

public class Course {

    private String startTime; // Change to Time Object
    private String endTime; // Change to Time Object
    private String courseTitle;
    private String courseNote;

    public Course(String startTime, String endTime, String courseTitle, String courseNote) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.courseTitle = courseTitle;
        this.courseNote = courseNote;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseNote() {
        return courseNote;
    }

    public void setCourseNote(String courseNote) {
        this.courseNote = courseNote;
    }
}
