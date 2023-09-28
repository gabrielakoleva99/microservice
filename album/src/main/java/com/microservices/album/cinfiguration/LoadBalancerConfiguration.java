package com.microservices.album.cinfiguration;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClient(name = "AlbumService")
public class LoadBalancerConfiguration {
}
