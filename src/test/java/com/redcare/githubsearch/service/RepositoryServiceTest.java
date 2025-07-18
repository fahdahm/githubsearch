package com.redcare.githubsearch.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.redcare.githubsearch.client.GitHubClient;
import com.redcare.githubsearch.search.dto.GitHubRepoDto;
import com.redcare.githubsearch.search.dto.ScoredRepoDto;
import com.redcare.githubsearch.search.scorer.PopularityScorer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RepositoryServiceTest {
  private GitHubClient mockClient;
  private PopularityScorer mockScorer;
  private RepositoryService service;

  @BeforeEach
  void setUp() {
    mockClient = mock(GitHubClient.class);
    mockScorer = mock(PopularityScorer.class);
    service = new RepositoryService(mockClient, mockScorer);
  }

  @Test
  void testFindAndScore_ReturnsSortedList() {
    GitHubRepoDto repo1 = mock(GitHubRepoDto.class);
    GitHubRepoDto repo2 = mock(GitHubRepoDto.class);

    when(repo1.getId()).thenReturn(1L);
    when(repo1.getName()).thenReturn("Repo1");
    when(repo1.getHtmlUrl()).thenReturn("url1");
    when(repo2.getId()).thenReturn(2L);
    when(repo2.getName()).thenReturn("Repo2");
    when(repo2.getHtmlUrl()).thenReturn("url2");
    when(mockClient.searchRepositories(anyString(), anyString(), anyInt(), anyInt()))
        .thenReturn(List.of(repo1, repo2));
    when(mockScorer.score(repo1)).thenReturn(1.0);
    when(mockScorer.score(repo2)).thenReturn(2.0);

    List<ScoredRepoDto> result = service.findAndScore("java", "2023-01-01", 0, 10);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("Repo2", result.get(0).getName());
  }
}
