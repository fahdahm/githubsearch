package com.redcare.githubsearch.search.scorer;

import com.redcare.githubsearch.config.GitHubProperties;
import com.redcare.githubsearch.search.dto.GitHubRepoDto;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    long daysSinceUpdate = ChronoUnit.DAYS.between(updatedAt, ZonedDateTime.now());
    double recencyScore =
        Math.max(0, recencyWindowDays - daysSinceUpdate) / (double) recencyWindowDays;
    double score =
        wStars * gitHubRepoDto.getStargazersCount()
            + wForks * gitHubRepoDto.getForksCount()
            + wRecency * recencyScore;
    log.debug(
        "Score for repo {}: {} (stars: {}, forks: {}, recencyScore: {})",
        gitHubRepoDto,
        score,
        gitHubRepoDto.getStargazersCount(),
        gitHubRepoDto.getForksCount(),
        recencyScore);
    return score;
  }
}
