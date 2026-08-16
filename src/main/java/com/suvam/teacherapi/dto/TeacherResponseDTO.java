package com.suvam.teacherapi.dto;

public record TeacherResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department,
        String subject
) {}