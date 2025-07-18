package com.redcare.githubsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GithubSearchApplication {

  public static void main(String[] args) {
    SpringApplication.run(GithubSearchApplication.class, args);
  }
}
