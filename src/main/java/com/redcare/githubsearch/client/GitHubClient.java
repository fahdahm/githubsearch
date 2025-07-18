package com.redcare.githubsearch.client;

import com.redcare.githubsearch.config.GitHubProperties;
import com.redcare.githubsearch.search.dto.GitHubRepoDto;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Component
public class GitHubClient {

  private final WebClient webClient;
  private final String baseUrl;
  private final String token;

  public GitHubClient(GitHubProperties props) {
    this.baseUrl = props.getApi().getBaseUrl();
    this.token = props.getApi().getToken();
    this.webClient =
        WebClient.builder()
            .baseUrl(this.baseUrl)
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .defaultHeader(
                HttpHeaders.AUTHORIZATION,
                (token != null && !token.isEmpty()) ? "Bearer " + token : "")
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs(
                        configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                    .build())
            .build();
  }

  public List<GitHubRepoDto> searchRepositories(
      String language, String createdAfter, int page, int size) {
    String query = "language:" + language + "+created:>" + createdAfter;
    try {
      SearchResponse response =
          webClient
              .get()
              .uri(
                  uriBuilder ->
                      uriBuilder
                          .path("/search/repositories")
                          .queryParam("q", query)
                          .queryParam("sort", "stars")
                          .queryParam("order", "desc")
                          .queryParam("page", page + 1)
                          .queryParam("per_page", size)
                          .build())
              .retrieve()
              .bodyToMono(SearchResponse.class)
              .timeout(Duration.ofSeconds(10))
              .block();
      return (response != null && response.getItems() != null)
          ? response.getItems()
          : Collections.emptyList();
    } catch (WebClientResponseException e) {
      log.error("GitHub API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
    } catch (Exception e) {
      log.error("Error calling GitHub API", e);
    }
    return Collections.emptyList();
  }

  @Setter
  @Getter
  private static class SearchResponse {
    private List<GitHubRepoDto> items;
  }
}
