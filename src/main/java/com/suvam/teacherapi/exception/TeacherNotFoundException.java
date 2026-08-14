package com.suvam.teacherapi.exception;

public class TeacherNotFoundException extends RuntimeException {

    public TeacherNotFoundException(String message) {
        super(message);
    }
}
