package com.suvam.teacherapi.controller;

import com.suvam.teacherapi.model.Teacher;
import com.suvam.teacherapi.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity.ok(service.addTeacher(teacher));
    }

    @GetMapping("/")
    public ResponseEntity<List<Teacher>> getALlTeachers() {
        return ResponseEntity.ok(service.getAllTeachers());
    }
}
