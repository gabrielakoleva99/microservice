package com.microservices.song.configuration;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClient(name = "SongListService")
public class LoadBalancerConfiguration {
}
