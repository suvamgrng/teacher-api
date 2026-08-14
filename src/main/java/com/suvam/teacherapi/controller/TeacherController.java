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

    @PostMapping()
    public ResponseEntity<Teacher> addTeacher(@RequestBody Teacher teacher) {
        return ResponseEntity.ok(service.addTeacher(teacher));
    }

    @GetMapping()
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(service.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable long id) {
        return ResponseEntity.ok(service.getTeacher(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable long id,
                                                 @RequestBody Teacher teacher) {
        return ResponseEntity.ok(service.updateTeacher(id, teacher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Teacher> deleteTeacher(@PathVariable long id) {
        return ResponseEntity.ok(service.deleteTeacher(id));
    }
}
