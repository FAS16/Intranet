package com.fahadali.intranet.model;

import java.time.LocalDateTime;

public class Class {

    private int id;
    private String start;
    private String end;
    public boolean checkedIn;
    public boolean checkedOut;

    public Class(int id, String start, String end, boolean checkedIn, boolean checkedOut) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.checkedIn = checkedIn;
        this.checkedOut = checkedOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }
}
