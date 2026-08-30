package com.suvam.teacherapi.service;

import com.suvam.teacherapi.dto.RegisterRequestDTO;
import com.suvam.teacherapi.exception.DuplicateUsernameException;
import com.suvam.teacherapi.model.Users;
import com.suvam.teacherapi.repository.UsersRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UsersRepo repo;
    private final PasswordEncoder encoder;

    public AuthService(UsersRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void register(RegisterRequestDTO request) {
        if (repo.existsByUsername(request.username())) {
            throw new DuplicateUsernameException("Username '" + request.username() + "' is already taken");
        }
        Users user = new Users();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setRole("TEACHER");

        repo.save(user);
    }

}
