package com.redcare.challenge.mapper;

import com.redcare.challenge.client.model.VersionControl;
import com.redcare.challenge.domain.github.GithubRepoWithScore;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubRepoWithScoreMapperTest {

    @Test
    public void testGithubRepoMapper() {
        VersionControl githubVersionControlRepo = new VersionControl(
                "hello-algo",
                "krahets/hello-algo",
                "javascript",
                "https://github.com/krahets/hello-algo",
                "hello-algo",
                "2022-11-04T11:08:34Z",
                "2025-12-07T00:53:49Z",
                20L,
                30L
        );

        GithubRepoWithScore  githubRepoWithScore = new GithubRepoWithScore(
                githubVersionControlRepo.name(),
                githubVersionControlRepo.fullName(),
                githubVersionControlRepo.language(),
                githubVersionControlRepo.htmlUrl(),
                githubVersionControlRepo.description(),
                LocalDateTime.parse(githubVersionControlRepo.createdAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")),
                LocalDateTime.parse(githubVersionControlRepo.updatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")),
                githubVersionControlRepo.forksCount(),
                githubVersionControlRepo.stargazersCount(),
                20.0
        );

        GitHubRepoWithScoreMapper mapper = new GitHubRepoWithScoreMapper();
        GithubRepoWithScore mappedGithubRepoWithScore = mapper.mapToGithubRepoWithScore(githubVersionControlRepo, (repo) -> 20.0);

        assertEquals(githubRepoWithScore, mappedGithubRepoWithScore);

    }
}
