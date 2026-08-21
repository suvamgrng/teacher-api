package com.suvam.teacherapi.controller;

import com.suvam.teacherapi.dto.TeacherRequestDTO;
import com.suvam.teacherapi.dto.TeacherResponseDTO;
import com.suvam.teacherapi.model.Teacher;
import com.suvam.teacherapi.service.TeacherService;
import jakarta.validation.Valid;
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
    public ResponseEntity<TeacherResponseDTO> addTeacher(
            @Valid
            @RequestBody TeacherRequestDTO request) {
        return ResponseEntity.ok(service.addTeacher(request));
    }

    @GetMapping()
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        return ResponseEntity.ok(service.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacher(@PathVariable long id) {
        return ResponseEntity.ok(service.getTeacher(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(
            @PathVariable long id,
            @RequestBody TeacherRequestDTO request) {
        return ResponseEntity.ok(service.updateTeacher(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Teacher> deleteTeacher(@PathVariable long id) {
        return ResponseEntity.ok(service.deleteTeacher(id));
    }
}
