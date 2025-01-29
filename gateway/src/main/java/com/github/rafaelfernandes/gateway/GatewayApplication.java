package com.github.rafaelfernandes.gateway;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/login-api/v3/api-docs").and().method(HttpMethod.GET).uri("lb://LOGIN-API"))
				.route(r -> r.path("/customer-api/v3/api-docs").and().method(HttpMethod.GET).uri("lb://CUSTOMER-API"))
				.route(r -> r.path("/credit-card-api/v3/api-docs").and().method(HttpMethod.GET).uri("lb://CREDITCARD-API"))
				.route(r -> r.path("/payment-api/v3/api-docs").and().method(HttpMethod.GET).uri("lb://PAYMENT-API"))
				.build();
	}

	@Bean
	@Primary
	public WebClient webClient() {
		HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
	}

}
