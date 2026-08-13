package com.suvam.teacherapi.controller;

import com.suvam.teacherapi.model.Teacher;
import com.suvam.teacherapi.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> addTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity.ok(service.addTeacher(teacher));
    }
}
