package com.redcare.challenge.api;

import com.redcare.challenge.domain.github.GithubRepoWithScoreList;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.ExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubRepositoryResourceIntegrationTest {

    private RestTestClient restTestClient;


    public static final MockWebServer server = new MockWebServer();

    @BeforeAll
    static void setServer() throws IOException {
        server.start();
    }

    @AfterAll
    static void tearDown()  {
        server.close();
    }

    @DynamicPropertySource
    static void overrideGithubApiBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", () ->"http://localhost:"+server.getPort());
    }

    @BeforeEach
    public void setup(WebApplicationContext context)  {
        restTestClient = RestTestClient.bindToApplicationContext(context)
                .configureMessageConverters(converters -> {
                    converters.addCustomConverter(new ByteArrayHttpMessageConverter());
                    converters.addCustomConverter(new JacksonJsonHttpMessageConverter(
                            JsonMapper.builder()
                                    .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                                    .build()));
                }).build();
    }

    @Test
    public void testGetGithubRepositoriesWithScore()  {

        server.enqueue(new MockResponse.Builder().code(200).body("""
                {
                  "total_count": 1,
                  "incomplete_results": false,
                  "items": [{
                              "id": 93558276,
                              "node_id": "MDEwOlJlcG9zaXRvcnk5MzU1ODI3Ng==",
                              "name": "awesome-design-systems",
                              "full_name": "alexpate/awesome-design-systems",
                              "html_url": "https://github.com/alexpate/awesome-design-systems",
                              "description": "💅🏻 ⚒ A collection of awesome design systems",
                              "fork": false,
                              "created_at": "2017-06-06T19:56:49Z",
                              "updated_at": "2025-12-06T21:03:03Z",
                              "pushed_at": "2025-07-21T12:10:32Z",
                              "git_url": "git://github.com/alexpate/awesome-design-systems.git",
                              "homepage": "https://git.io/design-systems",
                              "size": 454,
                              "stargazers_count": 18614,
                              "watchers_count": 18614,
                              "language": "java",
                              "forks_count": 1138,
                              "forks": 1138,
                              "watchers": 18614
                          }
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .build());



        EntityExchangeResult<GithubRepoWithScoreList> githubRepoWithScoreListEntityExchangeResult = restTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/v1/github/search/repositories")
                                .queryParam("language", "java")
                                .queryParam("createdDate", "2025-06-24")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(GithubRepoWithScoreList.class).returnResult();

        GithubRepoWithScoreList responseBody = githubRepoWithScoreListEntityExchangeResult.getResponseBody();

        assertEquals(1, responseBody.total());
        assertEquals(1, responseBody.items().size());

    }

    @Test
    public void shouldSucceedOnRetry()  {

        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .build());
        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .build());

        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .build());
        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .build());

        server.enqueue(new MockResponse.Builder().code(200).body("""
                {
                  "total_count": 1,
                  "incomplete_results": false,
                  "items": [{
                              "id": 93558276,
                              "node_id": "MDEwOlJlcG9zaXRvcnk5MzU1ODI3Ng==",
                              "name": "awesome-design-systems",
                              "full_name": "alexpate/awesome-design-systems",
                              "html_url": "https://github.com/alexpate/awesome-design-systems",
                              "description": "💅🏻 ⚒ A collection of awesome design systems",
                              "fork": false,
                              "created_at": "2017-06-06T19:56:49Z",
                              "updated_at": "2025-12-06T21:03:03Z",
                              "pushed_at": "2025-07-21T12:10:32Z",
                              "git_url": "git://github.com/alexpate/awesome-design-systems.git",
                              "homepage": "https://git.io/design-systems",
                              "size": 454,
                              "stargazers_count": 18614,
                              "watchers_count": 18614,
                              "language": "java",
                              "forks_count": 1138,
                              "forks": 1138,
                              "watchers": 18614
                          }
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .build());



        EntityExchangeResult<GithubRepoWithScoreList> githubRepoWithScoreListEntityExchangeResult = restTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/v1/github/search/repositories")
                                .queryParam("language", "java")
                                .queryParam("createdDate", "2025-06-24")
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(GithubRepoWithScoreList.class).returnResult();

        GithubRepoWithScoreList responseBody = githubRepoWithScoreListEntityExchangeResult.getResponseBody();

        assertEquals(1, responseBody.total());
        assertEquals(1, responseBody.items().size());

    }


    @Test
    public void shouldSendErrorMessageWithRetryMessage()  {

        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .addHeader("retry-after", "5")
                .build());
        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .addHeader("retry-after", "4")
                .build());
        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .addHeader("retry-after", "3")
                .build());
        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .addHeader("retry-after", "2")
                .build());

        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .addHeader("retry-after", "1")
                .build());
        server.enqueue(new MockResponse.Builder().code(429).body("""
                {
                  "total_count": 0,
                  "incomplete_results": false,
                  "items": [
                
                  ]
                }""")
                .addHeader("Content-Type", "application/json")
                .addHeader("retry-after", "1")
                .build());

        ExchangeResult exchangeResult = restTestClient.get().uri(uriBuilder ->
                        uriBuilder.path("/v1/github/search/repositories")
                                .queryParam("language", "java")
                                .queryParam("createdDate", "2025-06-24")
                                .build())
                .exchange()
                .expectStatus().is4xxClientError().returnResult();

        assertEquals(HttpStatusCode.valueOf(429),exchangeResult.getStatus());
        String s = new String(exchangeResult.getResponseBodyContent(), StandardCharsets.UTF_8);

        Assertions.assertThat(s).contains("Please retry after", "seconds");


    }

}
