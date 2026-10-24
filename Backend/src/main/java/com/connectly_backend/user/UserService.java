package com.connectly_backend.user;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@Service 
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
    }

    @Transactional 
    public Long register(RegisterRequest request)
    {
        String username = request.username().trim();
        String email = request.email().trim();

        if(userRepository.existsByUsernameIgnoreCase(username))
        {
            throw new IllegalArgumentException("Username Already exist");
        }

        if(userRepository.existsByEmailIgnoreCase(email))
        {
            throw new IllegalArgumentException("Email Already exist");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));

        return userRepository.save(user).getId();
    }

    @Transactional 
    public MeResponse updateProfile(
        String currentEmail,
        UpdateProfileRequest request
    ){
        if(request.username() == null && request.bio() == null)
        {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Provide username or bio to update"
            );
        }

        User user = userRepository.findByEmailIgnoreCase(currentEmail).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if(request.username()!= null)
        {
            System.out.println(request);
            String newUsername = request.username().trim();

            if(newUsername.length() < 3 || newUsername.length() > 30)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username must be 3-30 characters");
            }

            if(!newUsername.equalsIgnoreCase(user.getUsername()) && userRepository.existsByUsernameIgnoreCase(newUsername)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username Already Exist");
            }

            user.setUsername(newUsername);
            System.out.println(newUsername);
            System.out.println(user);
        }

        if(request.bio() != null)
        {
            if(request.bio().length() > 160){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bio cannot exceed 160 characters");
            }

            user.setBio(request.bio());
        }

        userRepository.flush();

        User updatedUser = userRepository.findById(user.getId())
                .orElseThrow();

        System.out.println("Updated username: " + updatedUser.getUsername());

        return new MeResponse(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail()
);
    }



}
