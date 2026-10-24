package com.connectly_backend.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest (
    @Size(min =3, max = 30)
    @Pattern(regexp = ".*\\S.*")
    String username,

    @Size(max = 160)
    String bio
){}
