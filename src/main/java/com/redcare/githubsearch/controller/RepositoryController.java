package com.redcare.githubsearch.controller;

import com.redcare.githubsearch.config.GitHubProperties;
import com.redcare.githubsearch.search.dto.ScoredRepoDto;
import com.redcare.githubsearch.service.RepositoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/v1/api/github")
@Tag(name = "Search", description = "Github Search endpoints")
public class RepositoryController {

  private final RepositoryService service;
  private final GitHubProperties props;

  @Operation(summary = "Get filtered and scored repositories")
  @GetMapping("/search")
  public ResponseEntity<?> searchRepos(
      @RequestParam(required = false) String language,
      @RequestParam(required = false) String createdAfter,
      @RequestParam(required = false) @Min(0) Integer page,
      @RequestParam(required = false) @Min(1) Integer size) {

    var lang = (language == null) ? props.getApi().getDefaultLanguage() : language.toLowerCase();
    var date = (createdAfter == null) ? props.getApi().getDefaultCreatedAfter() : createdAfter;
    int pageVal = (page == null) ? 0 : page;
    int sizeVal = (size == null) ? 10 : size;

    if (pageVal < 0) {
      throw new ConstraintViolationException("page must be >= 0", Collections.emptySet());
    }
    if (sizeVal < 1) {
      throw new ConstraintViolationException("size must be >= 1", Collections.emptySet());
    }

    List<ScoredRepoDto> list = service.findAndScore(lang, date, pageVal, sizeVal);
    if (list.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(list);
  }
}
