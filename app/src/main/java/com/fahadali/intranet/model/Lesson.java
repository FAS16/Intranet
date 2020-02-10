package com.fahadali.intranet.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class Lesson {

    private int id;
    private String startTime;
    private String endTime;
    private Weekday weekday;
    private Room classRoom;
    private Course course;

    public Date getStartTimeDate(){
        Date date = null;
        String pattern = "HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
             date = simpleDateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public Date getEndTimeDate(){
        Date date = null;
        String pattern = "HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            date = simpleDateFormat.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }




}
