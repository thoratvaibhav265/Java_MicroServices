package com.vaibhav.userapp.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

    @NotBlank
    private String name;

    @Email(regexp = "[^@]+@[^@.]+(?:\\.[^@.]+)+", message = "Email is incorrect")
    @NotBlank(message = "Email address is required.")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 1000)
    private String about;

    @Transient
    private List<Rating> ratings;

    public User() {
    }

    public User(Long userId, String name, String email, String about) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.about = about;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public static class UserBuilder {
        private Long userId;
        private String name;
        private String email;
        private String about;

        UserBuilder() {
        }

        public UserBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder about(String about) {
            this.about = about;
            return this;
        }

        public User build() {
            return new User(userId, name, email, about);
        }
    }
}
