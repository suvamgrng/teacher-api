package com.suvam.teacherapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachers")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "You should fill up your first name")
    private String firstName;

    @NotBlank(message = "You should fill up your last name")
    private String lastName;

    @NotBlank(message = "You should fill up your email")
    @Email(message = "Invalid email. Please enter correct email")
    private String email;

    @NotBlank(message = "You should fill up your department")
    private String department;

    @NotBlank(message = "You should fill up your subject")
    private String subject;
}
