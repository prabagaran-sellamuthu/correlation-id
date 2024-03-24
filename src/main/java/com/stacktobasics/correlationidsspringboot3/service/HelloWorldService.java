package com.stacktobasics.correlationidsspringboot3.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class HelloWorldService {

    private final RestTemplate restTemplate;

    public HelloWorldService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String sayHello() {
        log.info("Returning hello from service");
        return "hello";
    }

}
