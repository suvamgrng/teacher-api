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

    public TeacherResponseDTO getTeacher(long id) {
        return repo.findById(id)
                .map(teacherMapper::toResponseDTO)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id " + id));
    }

    public TeacherResponseDTO updateTeacher(
            long id,
            TeacherRequestDTO request) {
       return repo.findById(id)
               .map(existTeacher -> {
                   teacherMapper.updateEntityFromDto(request, existTeacher); // apply new data to the FETCHED entity
                   Teacher updatedTeacher = repo.save(existTeacher); // existTeacher is now updated so it is saved to db
                   return teacherMapper.toResponseDTO(updatedTeacher); // convert to the correct return type HERE
               })
               .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id " + id));
    }

    public void deleteTeacher(long id) {
        Teacher teacher = repo.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with id " + id));
        repo.delete(teacher);
    }
}
