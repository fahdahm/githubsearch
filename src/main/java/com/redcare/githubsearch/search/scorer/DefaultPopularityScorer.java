package com.redcare.githubsearch.search.scorer;

import com.redcare.githubsearch.config.GitHubProperties;
import com.redcare.githubsearch.search.dto.GitHubRepoDto;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Default implementation of PopularityScorer that scores GitHub repositories based on stars, forks,
 * and recency.
 *
 * <p>The score is calculated as a weighted sum of stargazers, forks, and how recently the repo was
 * updated. Weights and recency window are configurable via GitHubProperties.
 */
@Slf4j
@Component
public class DefaultPopularityScorer implements PopularityScorer {

  private final double wStars, wForks, wRecency;
  private final int recencyWindowDays;

  public DefaultPopularityScorer(GitHubProperties props) {
    this.wStars = props.getScorer().getWeightStars();
    this.wForks = props.getScorer().getWeightForks();
    this.wRecency = props.getScorer().getWeightRecency();
    this.recencyWindowDays = props.getScorer().getRecencyWindowDays();
  }

  /**
   * Calculates the popularity score for a GitHub repository. Returns 0 if the input or its update
   * date is null. Otherwise, the score is: score = wStars * stars + wForks * forks + wRecency *
   * recencyScore where recencyScore decreases as the repo gets older, up to recencyWindowDays.
   *
   * @param gitHubRepoDto the repository to score
   * @return the computed popularity score
   */
  @Override
  public double score(GitHubRepoDto gitHubRepoDto) {
    if (gitHubRepoDto == null) {
      log.warn("GitHubRepoDto is null. Returning score 0");
      return 0.0;
    }

    ZonedDateTime updatedAt = gitHubRepoDto.getUpdatedAt();
    if (updatedAt == null) {
      log.warn("UpdatedAt is null for repo: {}. Returning score 0", gitHubRepoDto);
      return 0.0;
    }
    // Calculate days since last update
    long daysSinceUpdate = ChronoUnit.DAYS.between(updatedAt, ZonedDateTime.now());
    // Recency score is 1 if updated today, 0 if older than recencyWindowDays
    double recencyScore =
        Math.max(0, recencyWindowDays - daysSinceUpdate) / (double) recencyWindowDays;
    double finalScore =
        wStars * gitHubRepoDto.getStargazersCount()
            + wForks * gitHubRepoDto.getForksCount()
            + wRecency * recencyScore;
    log.debug(
        "Score for repo {}: {} (stars: {}, forks: {}, recencyScore: {})",
        gitHubRepoDto,
        finalScore,
        gitHubRepoDto.getStargazersCount(),
        gitHubRepoDto.getForksCount(),
        recencyScore);
    return finalScore;
  }
}
