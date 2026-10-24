package com.connectly_backend.user;

public record LoginResponse (
    String token,
    String tokenType,
    Long userId,
    String username
){}
