package com.zu_min.playground.quarkus.extension.runtime;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * サンプルサービス。
 */
@ApplicationScoped
public class MyService {
    public String getMessage() {
        return "こんにちは！";
    }
}
