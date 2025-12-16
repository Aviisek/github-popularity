package com.redcare.challenge.client;

import com.redcare.challenge.client.model.GithubVersionControlList;
import org.springframework.http.ResponseEntity;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import static org.springframework.web.client.HttpClientErrorException.TooManyRequests;
import static org.springframework.web.client.HttpClientErrorException.Forbidden;

// currently we are not using personal access token but using personal access token will make
// it scale per user instead. ConcurrencyLimit is set to 50 just not to spam github api as they recommends
// max 100 call concurrently.

@HttpExchange(url = "/search/repositories", accept = "application/vnd.github+json", headers = {"X-GitHub-Api-Version=2022-11-28"})
@ConcurrencyLimit(50)
public interface GithubRepositoryExchange {

    @GetExchange
    @Retryable(includes = {TooManyRequests.class, Forbidden.class},
            maxRetriesString = "${github.retry.maxRetries}",
            delayString = "${github.retry.delay}",
            multiplierString = "${github.retry.multiplier}",
            maxDelayString = "${github.retry.maxDelay}")
    ResponseEntity<GithubVersionControlList> fetchGithubRepositories(
            @RequestParam(required = false) String q,
            @RequestParam(required = false, name = "per_page", defaultValue = "30") long perPage,
            @RequestParam(required = false, name = "page", defaultValue = "1") long pageNumber);

}
