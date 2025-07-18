package com.redcare.githubsearch.service;

import com.redcare.githubsearch.client.GitHubClient;
import com.redcare.githubsearch.search.dto.GitHubRepoDto;
import com.redcare.githubsearch.search.dto.ScoredRepoDto;
import com.redcare.githubsearch.search.scorer.PopularityScorer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service for searching GitHub repositories and scoring them using a PopularityScorer. */
@Slf4j
@Service
@AllArgsConstructor
public class RepositoryService {

  private final GitHubClient client;
  private final PopularityScorer scorer;

  /**
   * Finds GitHub repositories using the given filters and scores them.
   *
   * @param language programming language filter
   * @param createdAfter filter for repositories created after this date
   * @param page pagination page number (0-based)
   * @param size number of results per page
   * @return list of scored repositories, sorted by score descending
   */
  public List<ScoredRepoDto> findAndScore(
      String language, String createdAfter, int page, int size) {
    log.info(
        "Searching GitHub repos with language={}, createdAfter={}, page={}, size={}",
        language,
        createdAfter,
        page,
        size);

    List<GitHubRepoDto> repos = client.searchRepositories(language, createdAfter, page, size);
    List<ScoredRepoDto> scoredRepos = new ArrayList<>();
    for (GitHubRepoDto r : repos) {
      log.debug("Repo Found: {}", r);
      scoredRepos.add(new ScoredRepoDto(r.getId(), r.getName(), r.getHtmlUrl(), scorer.score(r)));
    }
    var sortedScoredRepos =
        scoredRepos.parallelStream()
            .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
            .collect(Collectors.toList());
    log.info("Returning {} scored repos", scoredRepos.size());
    return sortedScoredRepos;
  }
}
