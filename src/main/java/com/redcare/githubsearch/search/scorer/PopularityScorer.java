package com.redcare.githubsearch.search.scorer;

import com.redcare.githubsearch.search.dto.GitHubRepoDto;

public interface PopularityScorer {
  double score(GitHubRepoDto repo);
}
