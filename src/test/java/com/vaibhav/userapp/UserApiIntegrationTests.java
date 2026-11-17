package com.vaibhav.userapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.vaibhav.userapp.entities.User;
import com.vaibhav.userapp.repositories.UserRepository;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:userapp_tests",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@Transactional
class UserApiIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Test
    void createsUserWithGeneratedIdAndPersistsSubmittedFields() throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("""
                {"userId":-1,"name":"Ada","email":"ada@example.com","about":"Engineer"}
                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.name").value("Ada"))
                .andExpect(jsonPath("$.email").value("ada@example.com"))
                .andExpect(jsonPath("$.about").value("Engineer"));

        assertThat(repository.findAll()).singleElement().satisfies(user -> {
            assertThat(user.getUserId()).isPositive();
            assertThat(user.getName()).isEqualTo("Ada");
            assertThat(user.getEmail()).isEqualTo("ada@example.com");
            assertThat(user.getAbout()).isEqualTo("Engineer");
        });
    }

    @Test
    void retrievesUserById() throws Exception {
        User user = repository.saveAndFlush(User.builder()
                .name("Ada").email("ada@example.com").about("Engineer").build());

        mvc.perform(get("/users/{id}", user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(is(user.getUserId()), Long.class))
                .andExpect(jsonPath("$.name").value("Ada"))
                .andExpect(jsonPath("$.email").value("ada@example.com"))
                .andExpect(jsonPath("$.about").value("Engineer"))
                .andExpect(jsonPath("$.ratings.length()").value(0));
    }

    @Test
    void listsAllPersistedUsers() throws Exception {
        repository.saveAndFlush(User.builder().name("Ada").email("ada@example.com").build());
        repository.saveAndFlush(User.builder().name("Grace").email("grace@example.com").build());

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(content().json("""
                        [{"name":"Ada","email":"ada@example.com"},
                         {"name":"Grace","email":"grace@example.com"}]
                        """));
    }

    @Test
    void listsNoUsersAsAnEmptyArray() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void returnsNotFoundForUnknownId() throws Exception {
        mvc.perform(get("/users/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not available with ID:10"))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value("404 NOT_FOUND"));
    }

    @Test
    void rejectsNonNumericId() throws Exception {
        mvc.perform(get("/users/not-a-number")).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "ada@gmail", "ada@yahoo"})
    void returnsMessageForInvalidEmail(String email) throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("""
                {"name":"Ada","email":"%s"}
                """.formatted(email)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email is incorrect"))
                .andExpect(jsonPath("$.success").value(false));

        assertThat(repository.count()).isZero();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ada@gmail.com", "ada@yahoo.com"})
    void acceptsValidEmail(String email) throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("""
                {"name":"Ada","email":"%s"}
                """.formatted(email)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"name\":\" \",\"email\":\"ada@example.com\"}",
            "{\"email\":\"ada@example.com\"}",
            "{\"name\":\"Ada\",\"email\":\" \"}",
            "{\"name\":\"Ada\"}",
            "{"
    })
    void rejectsInvalidCreateRequestsWithoutPersistingUsers(String body) throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());

        assertThat(repository.count()).isZero();
    }
}
