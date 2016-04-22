package com.aman_kumar.nodechat;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.aman_kumar.nodechat.controllers.ChatController;
import com.aman_kumar.nodechat.utils.SharedPrefs;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aman on 21/03/16.
 */
public class App extends Application {

    private static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        OneSignal.startInit(this)
//                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
//                    @Override
//                    public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
////                        Toast.makeText(getAppContext(),  "isActive: "+isActive+"\nMessage: "+message+"\n Data: "+additionalData.toString(), Toast.LENGTH_SHORT).show();
//                        try {
//                            ChatController.getmInstance().addDataToDB(additionalData.getString("msg"), 0);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                })
                .init();
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.v("debug", "User:" + userId);
                if (registrationId != null) {
                    Log.v("debug", "registrationId:" + registrationId);
                    SharedPrefs.setPrefs("playerID",userId);
                    SharedPrefs.setPrefs("email", "nitin@gmail.com");
                    SharedPrefs.setPrefs("name", "Nitin Kumar");
                    ChatController.getmInstance().initChat();
                }

                //Toast.makeText(getAppContext(),  "Reg Id: "+registrationId+"\nuserID: "+userId, Toast.LENGTH_LONG).show();

            }
        });
        //OneSignal.enableInAppAlertNotification(true);

    }

    public static App getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
