package com.example.attendancerecorder;

public class Details {
    public String field;
    public String value;

    Details(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String toString() {
        return field+" "+value;
    }
}


