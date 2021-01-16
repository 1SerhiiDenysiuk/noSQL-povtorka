package com.lab5.denysiuk.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lab5.denysiuk.service.SendDataBehaviour;
import com.microsoft.azure.eventhubs.ConnectionStringBuilder;
import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventhubs.EventHubClient;
import com.microsoft.azure.eventhubs.EventHubException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.lab5.denysiuk.StrategyKeys.StrategyKeys.*;

@Service
public class EventHubStrategySendImpl implements SendDataBehaviour {

    private static final Logger logger = LoggerFactory.getLogger(EventHubStrategySendImpl.class);

    private EventHubClient EventHubCli;

    public EventHubStrategySendImpl() {
        final ConnectionStringBuilder connStr = new ConnectionStringBuilder()
                .setNamespaceName(EVENTHUB_NAME_SPACE)// namespace
                .setEventHubName(EVENTHUB_NAME)// hub name
                .setSasKeyName(SAS_KEY_NAME) // connection string–primary key
                .setSasKey(SAS_KEY); // primary key

        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
        try {
            this.EventHubCli = EventHubClient.createSync(connStr.toString(), executor);
        } catch (EventHubException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAndLog(JSONArray jsonArray, int startRaw, int endRaw) {
        final Gson gsonBuilder = new GsonBuilder().create();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonItem = (JSONObject) jsonArray.get(i);
            logger.info("Document index: {}", i);
            byte[] loadBytes = gsonBuilder.toJson(jsonItem).getBytes(Charset.defaultCharset());
            EventData sendEvent = EventData.create(loadBytes);

            try {
                EventHubCli.sendSync(sendEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
