package com.rocku.gateway.filter;

import java.util.List;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class StripInternalHeaders implements GlobalFilter, Ordered {
    private static final List<String> UNTRUSTED_HEADERS = List.of(
            "Authorization", "X-Admin-Token", "X-User-Id", "X-User-Roles", "X-Correlation-ID", "X-XSRF-TOKEN");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(headers -> UNTRUSTED_HEADERS.forEach(headers::remove))
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return -30;
    }
}