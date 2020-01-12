package com.fahadali.intranet.model;

import lombok.Data;

@Data
public class Weekday {

    private int id;
    private String day;
    private int value;


    public static final byte Sunday = 0;
    public static final byte Monday = 1;
    public static final byte Tuesday = 2;
    public static final byte Wednesday = 3;
    public static final byte Thursday = 4;
    public static final byte Friday = 5;
    public static final byte Saturday = 6;
}
