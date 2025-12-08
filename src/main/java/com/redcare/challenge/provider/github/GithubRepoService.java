package com.redcare.challenge.provider.github;

import com.redcare.challenge.client.GithubRepositoryExchange;
import com.redcare.challenge.client.model.GithubVersionControlList;
import com.redcare.challenge.domain.github.GithubRepoWithScore;
import com.redcare.challenge.domain.github.GithubRepoWithScoreList;
import com.redcare.challenge.mapper.GitHubRepoWithScoreMapper;
import com.redcare.challenge.provider.algorithm.ScoringPopularityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class GithubRepoService {

    private final GithubRepositoryExchange githubRepositoryExchange;
    private final ScoringPopularityService scoringPopularityService;
    private final GitHubRepoWithScoreMapper gitHubRepoWithScoreMapper;

    public GithubRepoService(GithubRepositoryExchange githubRepositoryExchange,
                             ScoringPopularityService scoringPopularityService,
                             GitHubRepoWithScoreMapper gitHubRepoWithScoreMapper) {
        this.githubRepositoryExchange = githubRepositoryExchange;
        this.scoringPopularityService = scoringPopularityService;
        this.gitHubRepoWithScoreMapper = gitHubRepoWithScoreMapper;
    }


    public GithubRepoWithScoreList getGithubRepoWithScoreList(String language, LocalDate createdDate, long perPage, long pageNumber) {
        //String queryToFetch = "created:<2011-01-01 language:java";
        String queryToFetch = GithubQuery
                .builder()
                .createdDate(createdDate)
                .language(language)
                .build()
                .buildQueryString();

        GithubVersionControlList githubVersionControlList;

        try {
            //we can also iteratively fetch all the pages(max 1000 limit by github api)
            // but to present currently fetching single page
            ResponseEntity<GithubVersionControlList> githubVersionControlListResponseEntity =
                    githubRepositoryExchange.fetchGithubRepositories(queryToFetch, perPage, pageNumber);
             githubVersionControlList = githubVersionControlListResponseEntity.getBody();

        } catch (HttpClientErrorException.TooManyRequests | HttpClientErrorException.Forbidden ex){
                throw new ResponseStatusException(HttpStatusCode.valueOf(429),
                        getExceptionMessageFromResponse(ex.getResponseHeaders()));
        } catch (HttpClientErrorException ex) {
            throw new ResponseStatusException( ex.getStatusCode(), ex.getMessage());
        }

        if(githubVersionControlList == null){
            return new GithubRepoWithScoreList(0, List.of(), pageNumber, 0L);
        }

        List<GithubRepoWithScore> githubRepoWithScores = githubVersionControlList
                .items()
                .stream()
                .map(githubRepo ->
                        gitHubRepoWithScoreMapper.mapToGithubRepoWithScore(
                                githubRepo,
                                scoringPopularityService.calculateScore(githubRepo)
                        )
                ).toList();

        //github api only allows max 1000 records for any query
        // and 100 max per page, even though totalCount could be very large.
        long maxPageAvailable = Math.min(1000, githubVersionControlList.totalCount()) / perPage;
        long maxPageAvailableMod = Math.min(1000, githubVersionControlList.totalCount()) % perPage;
        if(maxPageAvailableMod > 0) {
            maxPageAvailable = 1+ maxPageAvailable;
        }

        return new GithubRepoWithScoreList(githubVersionControlList.totalCount(), githubRepoWithScores, pageNumber, maxPageAvailable);

    }


    private String getExceptionMessageFromResponse(HttpHeaders headers){
      String message = "Please try again later";
        if(headers == null) {
            return message;
        }
        if(headers.getContentLength() > 0){
            List<String> retry = headers.get("retry-after");
            String retryAfter = retry != null && !retry.isEmpty() ? retry.getFirst() : null;

            if(retryAfter != null){
                message = "Please retry after "+ retry.getFirst() +" seconds";
            }
        }

        return message;
    }

}
