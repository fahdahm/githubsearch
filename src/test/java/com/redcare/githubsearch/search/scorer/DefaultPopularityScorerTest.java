package com.redcare.githubsearch.search.scorer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.redcare.githubsearch.config.GitHubProperties;
import com.redcare.githubsearch.search.dto.GitHubRepoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultPopularityScorerTest {
  private DefaultPopularityScorer scorer;
  private GitHubProperties mockProps;
  private GitHubProperties.Scorer mockScorer;

  @BeforeEach
  void setUp() {
    mockProps = mock(GitHubProperties.class);
    mockScorer = mock(GitHubProperties.Scorer.class);

    when(mockProps.getScorer()).thenReturn(mockScorer);
    when(mockScorer.getWeightStars()).thenReturn(1.0);
    when(mockScorer.getWeightForks()).thenReturn(1.0);
    when(mockScorer.getWeightRecency()).thenReturn(2.0);
    when(mockScorer.getRecencyWindowDays()).thenReturn(30);

    scorer = new DefaultPopularityScorer(mockProps);
  }

  @Test
  void testScore_CalculatesCorrectly() {
    GitHubRepoDto repo = mock(GitHubRepoDto.class);
    when(repo.getStargazersCount()).thenReturn(10);
    when(repo.getForksCount()).thenReturn(5);
    when(repo.getUpdatedAt()).thenReturn(java.time.ZonedDateTime.now().minusDays(0));

    double score = scorer.score(repo);

    assertEquals(17.0, score);
  }

  @Test
  void testScore_NullRepo_ReturnsZero() {
    assertEquals(0.0, scorer.score(null));
  }

  @Test
  void testScore_NullUpdatedAt_ReturnsZero() {
    GitHubRepoDto repo = mock(GitHubRepoDto.class);
    when(repo.getUpdatedAt()).thenReturn(null);

    assertEquals(0.0, scorer.score(repo));
  }

  @Test
  void testScore_CustomRecencyWindow() {
    when(mockScorer.getRecencyWindowDays()).thenReturn(10);
    scorer = new DefaultPopularityScorer(mockProps);
    GitHubRepoDto repo = mock(GitHubRepoDto.class);
    when(repo.getStargazersCount()).thenReturn(2);
    when(repo.getForksCount()).thenReturn(3);
    when(repo.getUpdatedAt()).thenReturn(java.time.ZonedDateTime.now().minusDays(5));

    assertEquals(6.0, scorer.score(repo));
  }
}
