package com.suvam.teacherapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TeacherRequestDTO(
        @NotBlank(message = "You should fill up your first name")
        String firstName,

        @NotBlank(message = "You should fill up your last name")
        String lastName,

        @NotBlank(message = "You should fill up your email")
        @Email(message = "Invalid email. Please enter correct email")
        String email,

        @NotBlank(message = "You should fill up your department")
        String department,

        @NotBlank(message = "You should fill up your subject")
        String subject
) {}