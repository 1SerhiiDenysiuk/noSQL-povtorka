package com.lab5.denysiuk.service;

import org.json.JSONArray;

public interface SendDataBehaviour {
    void sendAndLog(JSONArray jsonArray, int startRaw, int endRaw);
}
