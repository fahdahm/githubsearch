package com.redcare.githubsearch.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.redcare.githubsearch.GithubSearchApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    classes = GithubSearchApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RepositoryControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void searchRepos_returnsOkWithResults() throws Exception {
    mockMvc
        .perform(
            get("/v1/api/github/search")
                .param("language", "java")
                .param("createdAfter", "2025-01-01")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void searchRepos_returnsBadRequestForInvalidParams() throws Exception {
    mockMvc
        .perform(
            get("/v1/api/github/search")
                .param("page", "-1")
                .param("size", "0")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
