package com.fahadali.intranet.model;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class Attendance {

    private int id;
    private int lessonId;
    private Lesson lesson;
    private int studentId;
    private String timestamp;
    private int checkType;
    private String tagId;
    private String deviceId;
    private boolean possibleFraud;

}
