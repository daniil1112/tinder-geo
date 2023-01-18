package com.dfrolod.tindergeo;

import com.dfrolod.tindergeo.dto.MatchProfileResult;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@SpringBootApplication
public class TinderGeoApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(TinderGeoApplication.class, args);
        makeRequest();
    }


    public static void makeRequest() throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("User-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.114 YaBrowser/22.9.1.1110 (beta) Yowser/2.5 Safari/537.36")
                .header("x-auth-token", "3fc16ac4-9d6e-4cbc-9c60-9af4a22e6123")
                .uri(URI.create("https://api.gotinder.com/user/62bb573435f6540100d0af05?locale=ru"))
                .GET()
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(e -> {
                            try {
                                var r = mapper.readValue(e, new TypeReference<MatchProfileResult>() {});
                                System.out.println(r.getResults().getDistanceMi());
                                System.out.println(e);
                            } catch (JsonProcessingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                );
    }

}
