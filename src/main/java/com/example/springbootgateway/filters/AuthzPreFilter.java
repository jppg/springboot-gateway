package com.example.springbootgateway.filters;

import com.example.springbootgateway.model.Authz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AuthzPreFilter implements GlobalFilter {

    @Autowired
    WebClient.Builder webClientBuilder;

    private String baseUrl = "http://localhost:8080";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("Global Pre Filter (AuthZ) executed");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        final String token = this.getAuthHeader(request);


        return webClientBuilder.baseUrl(baseUrl)
                .build()
                .get()
                .uri("/authz")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, cr -> Mono.error(new RuntimeException("Bad token")))
                .bodyToMono(Authz.class)
                .doOnNext(
                       r -> {
                           System.out.println("Allow: " + r.allow);
                           if(!r.allow) {
                               throw new RuntimeException("Unauthorized access");
                           }
                       }
                )
                .then(chain.filter(exchange));
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders()
                .getOrEmpty("Authorization")
                .get(0)
                .substring(7);
    }
}

