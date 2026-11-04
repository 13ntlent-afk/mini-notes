package com.rocku.gateway.filter;

import com.rocku.gateway.security.BffSessionAttributes;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenRelay implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getSession().flatMap(session -> {
            var token = session.<String>getAttribute(BffSessionAttributes.ACCESS_TOKEN);
            ServerHttpRequest request = exchange.getRequest().mutate().headers(headers -> {
                headers.remove(HttpHeaders.AUTHORIZATION);
                if (token != null) {
                    headers.setBearerAuth(token);
                }
            }).build();
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    @Override
    public int getOrder() {
        return -10;
    }
}