package com.redcare.githubsearch.service;

import com.redcare.githubsearch.client.GitHubClient;
import com.redcare.githubsearch.search.dto.GitHubRepoDto;
import com.redcare.githubsearch.search.dto.ScoredRepoDto;
import com.redcare.githubsearch.search.scorer.PopularityScorer;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class RepositoryService {

  private final GitHubClient client;
  private final PopularityScorer scorer;

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
    scoredRepos.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
    log.info("Returning {} scored repos", scoredRepos.size());
    return scoredRepos;
  }
}
