package com.aman_kumar.nodechat.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.aman_kumar.nodechat.models.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 27/03/16.
 */
public class ChatDBAdapter {

    private Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public ChatDBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insertCustomerMsg(String msg) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.MESSAGE,msg.trim());
        contentValues.put(DBHelper.SENT_BY, 1);
        long id = db.insert(DBHelper.TABLE_NAME,DBHelper.IS_DELIVERED,contentValues);
        return id;
    }
    public long insertAdminMsg(String msg) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.MESSAGE,msg.trim());
        contentValues.put(DBHelper.SENT_BY, 0);
        long id = db.insert(DBHelper.TABLE_NAME,DBHelper.IS_DELIVERED,contentValues);
        return id;
    }

    public List<Chat> getAllChats() {
        String[] columnNames = {DBHelper.MESSAGE,DBHelper.SENT_BY,DBHelper.TIMESTAMP};
        Cursor cursor = db.query(DBHelper.TABLE_NAME,columnNames,null,null,null,null,null);
        List<Chat> chatList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            Chat chat = new Chat(
                    cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGE)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.SENT_BY)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.TIMESTAMP))
            );
//            Log.d("ChatDBAdapter:--",cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGE)));
            chatList.add(chat);
        }
        cursor.close();
        return chatList;
    }


    static class DBHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "nodeChat";
        private static final String TABLE_NAME = "chatTable";
        private static final String UID = "_id";
        private static final String MESSAGE = "msg";
        private static final String TIMESTAMP = "sentOn";
        private static final String SENT_BY = "sentBy";
        private static final String IS_DELIVERED = "isDelivered";

        private String QUERY = "CREATE TABLE "+TABLE_NAME+"("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+MESSAGE+" TEXT NOT NULL,"+SENT_BY+" INT NOT NULL, "+TIMESTAMP+" DATETIME DEFAULT CURRENT_TIMESTAMP, "+IS_DELIVERED+" INT);";

        private static final int DATABASE_VERSION = 3;

        Context context;

//        SentBy = 1 -> Customer sent the msg
//        SentBy = 0 -> Admin sent the msg

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
//            Toast.makeText(context, "Contructor called", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            Toast.makeText(context, "DB onCreate called", Toast.LENGTH_SHORT).show();
            try {
                db.execSQL(QUERY);
//                Toast.makeText(context, "DB created", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(context, "Oops Table is not created...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            Toast.makeText(context, "DB onUpgrade called", Toast.LENGTH_SHORT).show();
            try {
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            onCreate(db);
        }
    }

}
