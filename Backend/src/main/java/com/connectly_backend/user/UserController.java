
package com.connectly_backend.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(
            @Valid @RequestBody RegisterRequest request) {

        Long id = userService.register(request);

        return Map.of(
                "message", "User registered successfully",
                "userId", id
        );
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        return new MeResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }

    @PatchMapping("/me")
    public MeResponse updateProfile(
        Authentication authentication,
        @Valid @RequestBody UpdateProfileRequest request
    )
    {
        System.out.println(request.username());
        return userService.updateProfile(authentication.getName(), request);
    }

}
