package com.stacktobasics.correlationidsspringboot3.api;

import com.stacktobasics.correlationidsspringboot3.service.HelloWorldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class HelloWorldController {

    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        log.info("Someone called the /hello endpoint");
        return ResponseEntity.ok(helloWorldService.sayHello());
    }

}
