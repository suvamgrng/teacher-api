package com.suvam.teacherapi.service;

import com.suvam.teacherapi.dto.LoginRequestDTO;
import com.suvam.teacherapi.dto.RegisterRequestDTO;
import com.suvam.teacherapi.exception.DuplicateUsernameException;
import com.suvam.teacherapi.model.Users;
import com.suvam.teacherapi.repository.UsersRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UsersRepo repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository sessionContextRepository;
    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;

    public AuthService(UsersRepo repo, PasswordEncoder encoder, AuthenticationManager authenticationManager, SecurityContextRepository sessionContextRepository, SessionAuthenticationStrategy sessionAuthenticationStrategy) {
        this.repo = repo;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.sessionContextRepository = sessionContextRepository;
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
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

    public void login(
            LoginRequestDTO request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                            )
                    );

            // Regenerate the session ID to prevent session fixation
            sessionAuthenticationStrategy
                    .onAuthentication(
                            authentication,
                            httpRequest,
                            httpResponse
                    );

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            sessionContextRepository.saveContext(
                    context,
                    httpRequest,
                    httpResponse
            );

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(
                    "Invalid username or password"
            );
        }
    }
}
