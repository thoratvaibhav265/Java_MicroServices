package com.vaibhav.userapp.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vaibhav.userapp.entities.User;
import com.vaibhav.userapp.exception.NoSuchElementException;
import com.vaibhav.userapp.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTests {
    @Mock
    private UserRepository repository;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository);
    }

    @Test
    void saveUserReturnsPersistedUserWithAssignedId() {
        User submitted = User.builder().name("Ada").email("ada@example.com").build();
        User saved = User.builder().userId(42L).name("Ada").email("ada@example.com").build();
        when(repository.save(submitted)).thenReturn(saved);

        assertThat(service.saveUser(submitted)).isSameAs(saved);
    }

    @Test
    void getAllUserReturnsRepositoryResults() {
        User first = User.builder().userId(1L).build();
        User second = User.builder().userId(2L).build();
        when(repository.findAll()).thenReturn(List.of(first, second));

        assertThat(service.getAllUser()).containsExactly(first, second);
    }

    @Test
    void getUserReturnsMatchingUser() {
        User saved = User.builder().userId(42L).build();
        when(repository.findById(42L)).thenReturn(Optional.of(saved));

        assertThat(service.getUser(42L)).isSameAs(saved);
    }

    @Test
    void getUserThrowsDomainExceptionWhenIdIsMissing() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUser(42L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not available with ID:42");
    }
}
