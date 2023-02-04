package com.dfrolod.tindergeo.utils;

import com.dfrolod.tindergeo.dto.MatchProfileResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TinderApiImpl {
    private final String HOST = "https://api.gotinder.com";
    private final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.114 YaBrowser/22.9.1.1110 (beta) Yowser/2.5 Safari/537.36";
    private final ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    //TODO: Авторизацию вынести в другой класс (TinderApiClient)
    @Value("${tinder.auth.x-auth-token}")
    public String token;



    public final HttpClient httpClient;

    public TinderApiImpl(@Qualifier("httpClientWithTimeout") HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private void setDefaultHeaders(HttpRequest.Builder httpRequestBuilder){
        httpRequestBuilder.header("user-agent", USER_AGENT).header("x-auth-token", token);
    }

    public int getDistanceToUser(String userHash){
        var requestBuilder = HttpRequest.newBuilder();
        setDefaultHeaders(requestBuilder);
        requestBuilder.uri(URI.create(String.format("%s/user/%s", HOST, userHash))).GET();
        var responseFuture = httpClient.sendAsync(requestBuilder.build(), HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return MAPPER.readValue(body, new TypeReference<MatchProfileResult>() {});
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }).thenApply(matchProfileResult -> matchProfileResult.getResults().getDistanceMi());

        return responseFuture.join();
    }

    public void getMatches(){

    }

    private void getMatchesPaginate(int page, int perPage){
        var url = URI.create(String.format("%s/v2/matches?locale=ru&count=%i&message=%i&is_tinder_u=false", HOST, perPage, page));
    }

}
