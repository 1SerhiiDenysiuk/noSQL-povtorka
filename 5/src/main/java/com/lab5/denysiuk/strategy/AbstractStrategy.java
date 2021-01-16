package com.lab5.denysiuk.strategy;

import com.lab5.denysiuk.service.SendDataBehaviour;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class AbstractStrategy {
    private SendDataBehaviour sendBehaviour;

    protected AbstractStrategy(SendDataBehaviour sendBehaviour) {
        this.sendBehaviour = sendBehaviour;
    }


    public void fetchFromAndTransfer(String url) {
        try {
            URL data = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) data.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            int count = 1;
            int startRaw = 1;
            int maxCount = 1000;
            int endRaw = 0;
            JSONArray jsonArray;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
                if (count == maxCount) {
                    jsonArray = new JSONArray(response.toString() + ']');
                    endRaw = endRaw + count;
                    startRaw = startRaw + count;
                    sendBehaviour.sendAndLog(jsonArray, startRaw, endRaw);
                    count = 0;
                }
                count += 1;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SendDataBehaviour getSendBehaviour() {
        return sendBehaviour;
    }

    public void setSendBehaviour(SendDataBehaviour sendBehaviour) {
        this.sendBehaviour = sendBehaviour;
    }
}
