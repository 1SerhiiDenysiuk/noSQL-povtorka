package com.lab5.denysiuk.strategy;

import com.lab5.denysiuk.service.impl.RedisStrategySendImpl;

public class RedisStrategySend extends AbstractStrategy {
    public RedisStrategySend() {
        super(new RedisStrategySendImpl());
    }
}