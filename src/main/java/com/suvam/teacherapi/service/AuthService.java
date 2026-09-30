package com.suvam.teacherapi.service;

import com.suvam.teacherapi.dto.LoginRequestDTO;
import com.suvam.teacherapi.dto.LoginResponseDTO;
import com.suvam.teacherapi.dto.RegisterRequestDTO;
import com.suvam.teacherapi.exception.DuplicateUsernameException;
import com.suvam.teacherapi.model.Users;
import com.suvam.teacherapi.repository.UsersRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class AuthService {

    private final UsersRepo repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthService(
            UsersRepo repo,
            PasswordEncoder encoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.repo = repo;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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

    public LoginResponseDTO login(LoginRequestDTO request) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.username(),
                                    request.password()
                            )
                    );

            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();

            String role = userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_NONE");

            Map<String, Object> claims = Map.of("role", role);

            String token = jwtService.generateToken(claims, userDetails);

            return new LoginResponseDTO(token);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(
                    "Invalid username or password"
            );
        }
    }
}
