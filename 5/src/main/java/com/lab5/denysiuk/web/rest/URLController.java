package com.lab5.denysiuk.web.rest;

import com.lab5.denysiuk.StrategyManager;
import com.lab5.denysiuk.strategy.AbstractStrategy;
import com.lab5.denysiuk.web.rest.dto.RequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class URLController {
    private static final Logger logger = LoggerFactory.getLogger(URLController.class);

    private final StrategyManager strategyManager;

    @Autowired
    public URLController(StrategyManager strategyManager) {
        this.strategyManager = strategyManager;
    }

    @PostMapping("/url")
    public void addNewUrl(@RequestBody RequestDTO dto) {
        logger.info("Chosen strategy: {}", dto.getStrategy());
        logger.info("Dataset URL: {}", dto.getUrl());
        AbstractStrategy sendDataStrategy = this.strategyManager.getStrategy(dto.getStrategy());
        try {
            sendDataStrategy.fetchFromAndTransfer(dto.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}