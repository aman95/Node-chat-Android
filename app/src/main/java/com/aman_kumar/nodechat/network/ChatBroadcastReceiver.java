package com.aman_kumar.nodechat.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.aman_kumar.nodechat.controllers.ChatController;

import org.json.JSONObject;

/**
 * Created by aman on 08/04/16.
 */
public class ChatBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle dataBundle = intent.getBundleExtra("data");

        try {
            Log.i("OneSignalExample", "NotificationTable title: " + dataBundle.getString("title"));
            Log.i("OneSignalExample", "Is Your App Active: " + dataBundle.getBoolean("isActive"));
            Log.i("OneSignalExample", "data additionalData: " + dataBundle.getString("custom"));
            JSONObject customJSON = new JSONObject(dataBundle.getString("custom"));
            if (customJSON.has("a")) {
                JSONObject additionalData = customJSON.getJSONObject("a");
                if (additionalData.has("msg"))
                    ChatController.getmInstance().addDataToDB(additionalData.getString("msg"),0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Log.i("Broadcast Chat", "Bundle: "+dataBundle.toString());
    }
}
