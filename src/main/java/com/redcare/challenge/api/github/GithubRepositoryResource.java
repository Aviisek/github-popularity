package com.redcare.challenge.api.github;

import com.redcare.challenge.domain.github.GithubRepoWithScoreList;
import com.redcare.challenge.provider.github.GithubRepoService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/github")
@Tag(name="Github repository api", description = "An api to return github repositories along with popularity score")
public class GithubRepositoryResource {

    private final GithubRepoService githubRepoService;

    public GithubRepositoryResource(GithubRepoService githubRepoService) {
        this.githubRepoService = githubRepoService;
    }

    @GetMapping(value = "/search/repositories", produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<GithubRepoWithScoreList> getRepositoriesWithScore(
            @Parameter(description = "language of the code in the repositories", example = "Java")
            @RequestParam(required = true) String language,

            @Parameter(description = "time of earliest creation date", example = "2025-06-24")
            @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid LocalDate createdDate,

            @Parameter(description = "size of the repo list to fetch with max 100 allowed by github api")
            @RequestParam(required = false, defaultValue = "30") long perPage,

            @Parameter(description="current page number")
            @RequestParam(required = false, defaultValue = "1") long pageNumber
            ){

        GithubRepoWithScoreList githubRepoWithScoreList = githubRepoService.getGithubRepoWithScoreList(language,
                createdDate, perPage, pageNumber);
        return ResponseEntity.ok(githubRepoWithScoreList);
    }


}
