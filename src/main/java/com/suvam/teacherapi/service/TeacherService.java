package com.suvam.teacherapi.service;

import com.suvam.teacherapi.model.Teacher;
import com.suvam.teacherapi.repository.TeacherRepo;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private final TeacherRepo repo;

    public TeacherService(TeacherRepo repo) {
        this.repo = repo;
    }

    public Teacher addTeacher(Teacher teacher) {
        return repo.save(teacher);
    }
}
