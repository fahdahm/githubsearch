package com.redcare.githubsearch.search.scorer;

import com.redcare.githubsearch.search.dto.GitHubRepoDto;

/** Interface for scoring the popularity of a GitHub repository. */
public interface PopularityScorer {
  /**
   * Calculates a popularity score for the given GitHub repository.
   *
   * @param repo the repository to score
   * @return the computed popularity score
   */
  double score(GitHubRepoDto repo);
}
