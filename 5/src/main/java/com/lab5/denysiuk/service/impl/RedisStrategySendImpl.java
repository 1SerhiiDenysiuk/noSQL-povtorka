package com.lab5.denysiuk.service.impl;

import com.lab5.denysiuk.service.SendDataBehaviour;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.time.Instant;
import java.util.Map;

import static com.lab5.denysiuk.StrategyKeys.StrategyKeys.*;

@Service
public class RedisStrategySendImpl implements SendDataBehaviour {
    private static final Logger logger = LoggerFactory.getLogger(RedisStrategySendImpl.class);
    private static final boolean USE_SSL = true;
    private static String fileName;

    private final Jedis jedis;

    public RedisStrategySendImpl() {
        logger.info("Max records number: {}", MAX_RECORDS_NUMBER);
        logger.info("Host: {}", REDIS_HOST);
        logger.info("Port: {}", REDIS_PORT);
        logger.info("Key: {}", REDIS_KEY);
        logger.info("Catalog name: {}", CATALOG_NAME);

        JedisShardInfo jedisShardInfo = new JedisShardInfo(REDIS_HOST, REDIS_PORT, USE_SSL);
        jedisShardInfo.setPassword(REDIS_KEY);
        this.jedis = new Jedis(jedisShardInfo);
    }

    public void sendAndLog(JSONArray jsonArray, int startRaw, int endRaw) {
        jedis.hset(CATALOG_NAME, "File", "None");
        Map<String, String> redisData = jedis.hgetAll(CATALOG_NAME);

        assignTempFileName();
        if (checkIfFileExist(jedis)) {
            logger.info("LENGTH: {}", jsonArray.length());
            jedis.set("Raws", startRaw + ":" + endRaw);
            showData(jsonArray.length(), jsonArray, jedis, redisData);
        }
    }

    public void showData(int count, JSONArray jsonArray, Jedis jedis, Map<String, String> map) {
        jedis.hset(CATALOG_NAME, "Raws", String.valueOf(count));
        if (jsonArray.length() != MAX_RECORDS_NUMBER) {
            jedis.hset(CATALOG_NAME, fileName, jsonArray.toString());
            jedis.hset(CATALOG_NAME, "File", fileName);

            jedis.hset(CATALOG_NAME, "Status", "Finished");
        } else {
            logger.info("Raws from file {}: {}", fileName, jedis.hget(CATALOG_NAME, fileName));
            jedis.hset(CATALOG_NAME, "Raws", "" + count);
            jedis.hset(CATALOG_NAME, "Status", "Completed");
            jedis.hset(CATALOG_NAME, "Info", "First attempt to input this file");
            logger.info(map.get("Status"));
            jedis.close();
        }
    }

    public boolean checkIfFileExist(Jedis jedis) {
        Map<String, String> map = jedis.hgetAll(CATALOG_NAME);
        String name = map.get("File");
        String status = map.get("Status");

        if (!name.equals(fileName)) {
            jedis.hset(CATALOG_NAME, "File", fileName);
        } else {
            if (status.equals("Not Finished")) {
                jedis.hset(CATALOG_NAME, "Info", "Retry to input this file");
                logger.info("Such file '{}' : already exists. Application stop", fileName);
                return false;
            }
        }
        return true;
    }

    private static void assignTempFileName() {
        fileName = "FILE_" + Instant.now();
    }
}
