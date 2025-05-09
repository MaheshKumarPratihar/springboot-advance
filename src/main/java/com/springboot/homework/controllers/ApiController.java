package com.springboot.homework.controllers;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/*
 * For prometheus actuator endpoint
 *
 */
@RestController
public class ApiController {

    private final Counter apiCallCounter;

    public ApiController(MeterRegistry meterRegistry) {
        this.apiCallCounter = meterRegistry.counter("custom.metrics.api.calls", "endpoint", "/hello-world");
    }

    @GetMapping("/hello-world")
    public String handleApiRequest() {
        apiCallCounter.increment();
        return "Hello, world!";
    }
}

