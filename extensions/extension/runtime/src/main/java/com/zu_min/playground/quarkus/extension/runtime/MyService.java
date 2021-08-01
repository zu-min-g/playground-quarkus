package com.zu_min.playground.quarkus.extension.runtime;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyService {
    public String getMessage() {
        return "こんにちは！";
    }
}
