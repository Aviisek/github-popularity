package com.redcare.challenge.mapper;

import com.redcare.challenge.client.model.VersionControl;
import com.redcare.challenge.domain.github.GithubRepoWithScore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Service
public class GitHubRepoWithScoreMapper {

    public GithubRepoWithScore mapToGithubRepoWithScore(VersionControl githubRepo,
                                                        Function<VersionControl, Double> popularityFn){
        return new GithubRepoWithScore(
                githubRepo.name(),
                githubRepo.fullName(),
                githubRepo.language(),
                githubRepo.htmlUrl(),
                githubRepo.description(),
                parseDate(githubRepo.createdAt()),
                parseDate(githubRepo.updatedAt()),
                githubRepo.forksCount(),
                githubRepo.stargazersCount(),
                popularityFn.apply(githubRepo)
        );

    }


    private LocalDateTime parseDate(String date){
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));

    }
}
