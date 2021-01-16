package com.lab5.denysiuk.strategy;

import com.lab5.denysiuk.service.impl.EventHubStrategySendImpl;

public class EventHubStrategySend extends AbstractStrategy {
    public EventHubStrategySend() {
        super(new EventHubStrategySendImpl());
    }
}