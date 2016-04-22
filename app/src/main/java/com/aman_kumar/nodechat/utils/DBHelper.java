package com.aman_kumar.nodechat.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by aman on 27/03/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "nodeChat";
    private static final String TABLE_NAME = "chatTable";
    private static final String UID = "_id";
    private static final String MESSAGE = "msg";
    private static final String SENT_BY = "sentBy";
    private static final String IS_DELIVERED = "isDelivered";

    private static final int DATABASE_VERSION = 1;

    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Toast.makeText(context, "Contructor called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context, "DB onCreate called", Toast.LENGTH_SHORT).show();
        try {
            db.execSQL("CREATE TABLE "+TABLE_NAME+"("+UID+" int NOT NULL AUTO_INCREMENT,"+MESSAGE+" text NOT NULL,"+SENT_BY+" int NOT NULL,PRIMARY KEY (_id),time DATETIME DEFAULT CURRENT_TIMESTAMP,"+IS_DELIVERED+" int);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context, "DB onUpgrade called", Toast.LENGTH_SHORT).show();
        try {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(db);
    }
}
