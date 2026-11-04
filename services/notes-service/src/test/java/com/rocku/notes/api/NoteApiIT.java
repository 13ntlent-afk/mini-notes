package com.rocku.notes.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class NoteApiIT {
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("mini_notes_test")
            .withUsername("notes_test")
            .withPassword("notes_test_password");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("DB_SCHEMA", () -> "public");
        // No users table here, so let Hibernate build notes without the FK.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> "http://127.0.0.1:1/.well-known/jwks.json");
        registry.add("app.security.issuer", () -> "https://auth.test");
        registry.add("app.security.audience", () -> "mini-notes-api");
    }

    @Test
    void noteIsPersistedAndVisibleOnlyToItsOwner() throws Exception {
        var ownerId = 1L;
        var otherUserId = 2L;
        var ownerJwt = jwt().jwt(token -> token.claim("uid", ownerId.toString())
                .claim("scope", "notes:read notes:write"));
        var otherJwt = jwt().jwt(token -> token.claim("uid", otherUserId.toString())
                .claim("scope", "notes:read notes:write"));

        var result = mockMvc.perform(post("/notes")
                        .with(ownerJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new NotePayload("Private", "Only mine"))))
                .andExpect(status().isCreated())
                .andReturn();
        var noteId = objectMapper.readTree(result.getResponse().getContentAsByteArray()).get("id").asText();

        mockMvc.perform(get("/notes").with(otherJwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        mockMvc.perform(get("/notes/{id}", noteId).with(otherJwt))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    @Test
    void unauthenticatedRequestsReturnProblemJson() throws Exception {
        mockMvc.perform(get("/notes"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }

    private record NotePayload(String title, String body) {}
}