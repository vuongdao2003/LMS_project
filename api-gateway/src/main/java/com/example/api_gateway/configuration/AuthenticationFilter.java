package com.example.api_gateway.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthenticationFilter started");
        //Get token
        List<String> authHeader =  exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if(CollectionUtils.isEmpty(authHeader)){

        }
        //Verify token

        //Delegate identity service



        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
