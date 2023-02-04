package com.dfrolod.tindergeo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import java.net.http.HttpClient;
import java.time.Duration;

@org.springframework.context.annotation.Configuration
@ComponentScan
public class Configuration {
    @Bean
    public HttpClient httpClientWithTimeout(){
         return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }
}
