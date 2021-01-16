package com.lab5.denysiuk;

import com.lab5.denysiuk.strategy.AbstractStrategy;
import com.lab5.denysiuk.strategy.RedisStrategySend;
import com.lab5.denysiuk.strategy.EventHubStrategySend;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyManager {

    private final Map<String, AbstractStrategy> strategies;
    public StrategyManager() {
        this.strategies = new HashMap<>();
    }

    public AbstractStrategy getStrategy(String strategyName) {
        if (strategies.get(strategyName) == null) {
            createStrategy(strategyName);
        }
        return strategies.get(strategyName);
    }

    public void createStrategy(String strategyName){
        if (strategyName.equals("redis")) {
            strategies.put("redis", new RedisStrategySend());
        } else if (strategyName.equals("eventHub")) {
            strategies.put("eventHub", new EventHubStrategySend());
        }
    }

    public Collection<AbstractStrategy> getAllStrategies(){
        return strategies.values();
    }
    public Set<String> getAllNames() {
        return strategies.keySet();
    }
}