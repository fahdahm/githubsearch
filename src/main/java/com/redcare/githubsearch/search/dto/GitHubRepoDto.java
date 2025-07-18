package com.redcare.githubsearch.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubRepoDto {
  private long id;
  private String name;

  @JsonProperty("html_url")
  private String htmlUrl;

  @JsonProperty("stargazers_count")
  private int stargazersCount;

  @JsonProperty("forks_count")
  private int forksCount;

  @JsonProperty("updated_at")
  private ZonedDateTime updatedAt;
}
