package com.github.rafaelfernandes.gateway.filter;

import com.github.rafaelfernandes.gateway.exception.UnauthorizedException;
import com.github.rafaelfernandes.gateway.service.ValidateTokenService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticatorFilter extends AbstractGatewayFilterFactory<AuthenticatorFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private ValidateTokenService validateToken;

    public AuthenticatorFilter() {
        super(Config.class);

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = null;
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey("Authorization")) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                String token = exchange.getRequest().getHeaders().get("Authorization").get(0);

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                try {

                    if (!validateToken.validate(token, config.getRole())) {
                        throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                    }

                } catch (Exception e) {
                    System.out.println("Error in AuthenticationFilter: " + e.getMessage());
                    throw new UnauthorizedException(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
                }
            }
            return chain.filter(exchange.mutate().request(request).build());
        });
    }

    @Data
    public static class Config {
        private String role;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        // we need this to use shortcuts in the application.yml
        return Arrays.asList("role");
    }

}
