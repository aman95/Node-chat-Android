package com.aman_kumar.nodechat.controllers;

/**
 * Created by aman on 27/03/16.
 */
import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.aman_kumar.nodechat.App;
import com.aman_kumar.nodechat.adapters.ChatDBAdapter;
import com.aman_kumar.nodechat.adapters.ChatRVAdapter;
import com.aman_kumar.nodechat.models.Chat;
import com.aman_kumar.nodechat.network.VolleySingleton;
import com.aman_kumar.nodechat.utils.SharedPrefs;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatController {
    private static ChatController mInstance = null;

    private static Context mContext;

    private static String BASE_URL = "http://192.168.31.223:3000";

    public static boolean IS_CHAT_ACTIVE = false;

    ChatDBAdapter chatDBAdapter;
    ChatRVAdapter chatRVAdapter;
    RecyclerView rvChatList;
    RequestQueue requestQueue;

    private ChatController() {
        chatDBAdapter = new ChatDBAdapter(mContext);
        requestQueue = VolleySingleton.getmInstance().getRequestQueue();
    }

    public static ChatController getmInstance() {
        mContext = App.getAppContext();
        if(mInstance == null) {
            mInstance = new ChatController();
        }
        return mInstance;
    }

    public void initChat() {
        if(SharedPrefs.getPrefs("chatID","").isEmpty()) {
            JSONObject body = new JSONObject();
            try {
                body.put("name",SharedPrefs.getPrefs("name",""));
                body.put("player_id",SharedPrefs.getPrefs("playerID",""));
                body.put("email",SharedPrefs.getPrefs("email",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL+"/apis/chat/user/create", body, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        SharedPrefs.setPrefs("chatID",response.getString("_id"));
                        Toast.makeText(mContext, "Registered: "+response.getString("_id"),Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext, "Unable to connect to server",Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*10,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void addDataToDB(String msg, int sentBy) {
        if(sentBy == 1) {
            chatDBAdapter.insertCustomerMsg(msg);
        } else {
            chatDBAdapter.insertAdminMsg(msg);
        }
        if(IS_CHAT_ACTIVE) {
            Chat chat = new Chat(msg, sentBy, null);
            chatRVAdapter.addData(chat, rvChatList);
//                refreshRV(chatRVAdapter);
        }
    }

    public void setChatAdapter(RecyclerView rvChatList) {
        this.rvChatList = rvChatList;
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.setStackFromEnd(true);
        rvChatList.setLayoutManager(llm);
        chatRVAdapter = new ChatRVAdapter(chatDBAdapter.getAllChats());
        this.rvChatList.setAdapter(chatRVAdapter);
    }

    public void sendAndSaveChat(String msg) {
        JSONObject body = new JSONObject();
        try {
            body.put("chatMsg",msg);
            body.put("player_id",SharedPrefs.getPrefs("playerID",""));
            body.put("chatID",SharedPrefs.getPrefs("chatID",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL+"/apis/chat/user/msg", body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")) {
                        Toast.makeText(mContext, "Sent",Toast.LENGTH_SHORT).show();
                        addDataToDB(response.getJSONObject("data").getString("msg"),1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Unable to connect to server",Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*10,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }
}
