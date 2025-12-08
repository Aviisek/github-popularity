package com.redcare.challenge.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Configuration
public class HttpClient {

    @Value("${github.api.base-url}")
    private String githubBaseUrl;

    @Bean
    GithubRepositoryExchange githubRepositoryService() {
        JsonMapper jsonMapper = JsonMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();

        RestClient restClient = RestClient.builder().baseUrl(githubBaseUrl)
                .configureMessageConverters(converters -> {
                    converters.addCustomConverter(new JacksonJsonHttpMessageConverter(jsonMapper));
                })
                .requestFactory(configureClientHttpRequestFactory())
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(GithubRepositoryExchange.class);
    }

    private ClientHttpRequestFactory configureClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(Duration.ofMillis(60000));
        httpComponentsClientHttpRequestFactory.setReadTimeout(Duration.ofMillis(60000));

        return httpComponentsClientHttpRequestFactory;
    }
}
