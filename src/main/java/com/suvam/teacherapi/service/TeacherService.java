package com.suvam.teacherapi.service;

import com.suvam.teacherapi.dto.TeacherRequestDTO;
import com.suvam.teacherapi.dto.TeacherResponseDTO;
import com.suvam.teacherapi.exception.TeacherNotFoundException;
import com.suvam.teacherapi.mapper.TeacherMapper;
import com.suvam.teacherapi.model.Teacher;
import com.suvam.teacherapi.repository.TeacherRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepo repo;
    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherRepo repo, TeacherMapper teacherMapper) {
        this.repo = repo;
        this.teacherMapper = teacherMapper;
    }

    public TeacherResponseDTO addTeacher(TeacherRequestDTO request) {
       Teacher teacher = teacherMapper.toEntity(request);
       Teacher saveTeacher = repo.save(teacher);

        return teacherMapper.toResponseDTO(saveTeacher);
    }

    //Return ResponseDTO object to client
    public List<TeacherResponseDTO> getAllTeachers() {
        return repo.findAll()
                .stream()
                .map(teacherMapper:: toResponseDTO)
                .toList();
    }

    public Teacher getTeacher(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id " + id));
    }

    public Teacher updateTeacher(long id,
                                 Teacher teacher) {
       return repo.findById(id)
               .map(existTeacher -> {
                   teacher.setId(id);
                   return repo.save(teacher);
               })
               .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id " + id));
    }

    public Teacher deleteTeacher(long id) {
        return repo.findById(id)
                .map(existingTeacher -> {
                            repo.deleteById(id);
                            return existingTeacher;
                        }
                ).orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id " + id));
    }
}
