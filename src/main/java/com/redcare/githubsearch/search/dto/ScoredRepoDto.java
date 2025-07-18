package com.redcare.githubsearch.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScoredRepoDto {
  private long id;
  private String name;
  private String url;
  private double score;
}
