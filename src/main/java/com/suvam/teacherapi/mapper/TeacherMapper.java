package com.suvam.teacherapi.mapper;

import com.suvam.teacherapi.dto.TeacherRequestDTO;
import com.suvam.teacherapi.dto.TeacherResponseDTO;
import com.suvam.teacherapi.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {

    // Convert incoming request DTO object to Teacher Entity
    public Teacher toEntity(TeacherRequestDTO requestDTO) {
        Teacher teacher = new Teacher();

        teacher.setFirstName(requestDTO.firstName());
        teacher.setLastName(requestDTO.lastName());
        teacher.setDepartment(requestDTO.department());
        teacher.setEmail(requestDTO.email());
        teacher.setSubject(requestDTO.subject());

        return teacher;
    }

    // Convert existed Teacher Entity to response DTO object
    public TeacherResponseDTO toResponseDTO(Teacher teacher) {
        return new TeacherResponseDTO(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getDepartment(),
                teacher.getSubject()
        );
    }
}
