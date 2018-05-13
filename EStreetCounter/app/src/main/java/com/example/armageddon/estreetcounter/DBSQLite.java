package com.example.armageddon.estreetcounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBSQLite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "datastreet.db";
    public static final String TABLE_NAME1="awp_skrzyzowania";
    public static final String TABLE_NAME2="awp_pomiary";
    public static final String TABLE_NAME3="awp_zdjecia";
    public DBSQLite(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database","onCreate");
        String sql = "create table IF NOT EXISTS " + TABLE_NAME1 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,nazwa TEXT NOT NULL, datetime DEFAULT CURRENT_TIMESTAMP)";
        String sql2 = "create table IF NOT EXISTS " + TABLE_NAME2 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,id_skrzyz INTEGER NOT NULL,timer NUMEIC,datetime DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (id_skrzyz) REFERENCES awp_skrzyzowania(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        String sql3 = "create table IF NOT EXISTS " + TABLE_NAME3 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,id_pom INTEGER NOT NULL,img TEXT,path BLOB,datetime DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (id_pom) REFERENCES awp_pomiary(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        SQLiteStatement stmt=db.compileStatement(sql);
        stmt.execute();
        stmt=db.compileStatement(sql2);
        stmt.execute();
        stmt=db.compileStatement(sql3);
        stmt.execute();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sq="Drop TABLE IF EXISTS "+TABLE_NAME1;
        String sq2="Drop TABLE IF EXISTS "+TABLE_NAME2;
        String sq3="Drop TABLE IF EXISTS "+TABLE_NAME3;
        SQLiteStatement stmt=db.compileStatement(sq);
        stmt.execute();
        stmt=db.compileStatement(sq2);
        stmt.execute();
        stmt=db.compileStatement(sq3);
        stmt.execute();
        onCreate(db);
    }
}