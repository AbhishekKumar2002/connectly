package com.connectly_backend.user;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column (nullable = false, unique = true, length = 255)
    private String email;

    @Column (nullable = false)
    private String password;

    @Column(length = 160)
    private String bio;

    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User(){}

    @PrePersist 
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    } 

    public Long getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
