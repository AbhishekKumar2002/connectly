package com.connectly_backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.connectly_backend.config.JwtService;

import org.springframework.transaction.annotation.Transactional;

@Service 
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                    .findByEmailIgnoreCase(request.email().trim())
                    .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid Email or Password"
                    ));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid Email or Password"
            );
        }

        String token = jwtService.generateToken(
            user.getId(), user.getEmail(), user.getUsername());


        return new LoginResponse(
            token,
            "Bearer",
            user.getId(),
            user.getUsername()
        );
    }
}
