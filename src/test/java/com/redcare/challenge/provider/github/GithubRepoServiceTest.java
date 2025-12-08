package com.redcare.challenge.provider.github;

import com.redcare.challenge.client.GithubRepositoryExchange;
import com.redcare.challenge.client.model.GithubVersionControlList;
import com.redcare.challenge.client.model.VersionControl;
import com.redcare.challenge.domain.github.GithubRepoWithScoreList;
import com.redcare.challenge.mapper.GitHubRepoWithScoreMapper;
import com.redcare.challenge.provider.algorithm.ScoringPopularityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class GithubRepoServiceTest {

    String language = "javascript";
    LocalDate createdDate = LocalDate.now();
    VersionControl gitRepo1;
    VersionControl gitRepo2;

    @MockitoBean
    GithubRepositoryExchange githubRepositoryExchange;

    @MockitoBean
    ScoringPopularityService scoringPopularityService;

    GitHubRepoWithScoreMapper gitHubRepoWithScoreMapper;

    GithubRepoService githubRepoService;

    @BeforeEach
    public void setup(){

        gitHubRepoWithScoreMapper = new GitHubRepoWithScoreMapper();

        githubRepoService = new GithubRepoService(
                githubRepositoryExchange,
                scoringPopularityService,
                gitHubRepoWithScoreMapper);

        gitRepo1 = new VersionControl(
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

        gitRepo2 = new VersionControl(
                "hello-algo1",
                "krahets/hello-algo1",
                "javascript",
                "https://github.com/krahets/hello-algo1",
                "hello-algo1",
                "2022-11-05T11:08:34Z",
                "2025-12-08T00:53:49Z",
                21L,
                31L
        );


        when(githubRepositoryExchange.fetchGithubRepositories(any(), eq(2L), eq(1L)))
                .thenReturn(ResponseEntity.ok(new GithubVersionControlList(2,List.of(gitRepo1, gitRepo2))));

        when(scoringPopularityService.calculateScore(gitRepo1)).thenReturn(10.0);
        when(scoringPopularityService.calculateScore(gitRepo2)).thenReturn(20.0);
    }



    @Test
    public void shouldGetGithubRepoWithScoreList(){
        GithubRepoWithScoreList githubRepoWithScoreList = githubRepoService.getGithubRepoWithScoreList(language, createdDate,2, 1);

        Assertions.assertEquals(2, githubRepoWithScoreList.total());
        Assertions.assertEquals(2, githubRepoWithScoreList.items().size());


        assertThat(List.of(
                gitHubRepoWithScoreMapper.mapToGithubRepoWithScore(gitRepo1, scoringPopularityService.calculateScore(gitRepo1)),
                gitHubRepoWithScoreMapper.mapToGithubRepoWithScore(gitRepo2, scoringPopularityService.calculateScore(gitRepo2))))
                .containsExactlyInAnyOrderElementsOf(githubRepoWithScoreList.items());
    }

}
