package com.connectly_backend.user;

public record MeResponse (
    Long id,
    String username,
    String email
){}
