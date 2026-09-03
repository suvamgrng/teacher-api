package com.suvam.teacherapi.service;

import com.suvam.teacherapi.dto.LoginRequestDTO;
import com.suvam.teacherapi.dto.RegisterRequestDTO;
import com.suvam.teacherapi.exception.DuplicateUsernameException;
import com.suvam.teacherapi.model.Users;
import com.suvam.teacherapi.repository.UsersRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UsersRepo repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsersRepo repo, PasswordEncoder encoder, AuthenticationManager authenticationManager) {
        this.repo = repo;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    public void register(RegisterRequestDTO request) {
        if (repo.existsByUsername(request.username())) {
            throw new DuplicateUsernameException("Username '" + request.username() + "' is already taken");
        }
        Users user = new Users();
        user.setUsername(request.username());
        user.setPassword(encoder.encode(request.password()));
        user.setRole("ROLE_TEACHER");

        repo.save(user);
    }

    public void login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
