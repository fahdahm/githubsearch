package com.redcare.githubsearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "github")
public class GitHubProperties {

  private Api api = new Api();
  private Scorer scorer = new Scorer();

  @Getter
  @Setter
  public static class Api {
    private String baseUrl;
    private String token;
    private String defaultLanguage;
    private String defaultCreatedAfter;
  }

  @Getter
  @Setter
  public static class Scorer {
    private double weightStars;
    private double weightForks;
    private double weightRecency;
    private int recencyWindowDays = 30;
  }
}
