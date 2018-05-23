package com.example.armageddon.streetcounter;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        String sql2 = "create table IF NOT EXISTS " + TABLE_NAME2 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,id_skrzyz INTEGER NOT NULL,timer TEXT,datetime DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (id_skrzyz) REFERENCES awp_skrzyzowania(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        String sql3 = "create table IF NOT EXISTS " + TABLE_NAME3 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,id_pom INTEGER NOT NULL,img TEXT,path TEXT,datetime DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (id_pom) REFERENCES awp_skrzyzowania(id) ON DELETE CASCADE ON UPDATE CASCADE)";
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
    public Cursor getAllData()
    {   SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select nazwa from "+TABLE_NAME1;
        data=db.rawQuery(sql,null);
        return data;
    }

    public boolean checkProj(String name)
    {   SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select nazwa from "+TABLE_NAME1+" where nazwa='"+name+"'";
        data=db.rawQuery(sql,null);
        if(data.getCount()==0)
            return true;
        else
            return false;
    }

    public boolean insertProject(String name) {
        String sq = "Insert into " + TABLE_NAME1 + "(nazwa) Values(?)";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            SQLiteStatement statement = db.compileStatement(sq);
            statement.bindString(1, name);
            statement.execute();
            return true;
        } catch (Exception e) {
            Log.w("Exception:", e);
            return false;
        }
    }

    public int returnIdProj(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select id from "+TABLE_NAME1+" where nazwa='"+name+"'";
        data=db.rawQuery(sql,null);
        while (data.moveToNext()){
            return data.getInt(0);
        }
        return 0;
    }

    public Cursor returnAllImagesFromProj(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select path from "+TABLE_NAME3+" where id_pom="+id;
        data=db.rawQuery(sql,null);
        return data;
    }
    public boolean deleteProject(String name) {
        int id=returnIdProj(name);
        Cursor cur=returnAllImagesFromProj(id);
        if(cur.getCount()>0){
            while(cur.moveToNext()){
                String path=cur.getString(0);
                File myFile = new File(path);
                if(myFile.exists())
                    myFile.delete();
            }
        }

        String sq = "Delete from " + TABLE_NAME1 + " where nazwa=?";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            SQLiteStatement statement = db.compileStatement(sq);
            statement.bindString(1, name);
            statement.execute();
            return true;
        } catch (Exception e) {
            Log.w("Exception:", e);
            return false;
        }
    }

    public int returnLastIdProj(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select MAX(id) from "+TABLE_NAME1;
        data=db.rawQuery(sql,null);
        while (data.moveToNext()){
            return data.getInt(0);
        }
        return 0;
    }

    public boolean insertValues(List<String> arr){
        int id=returnLastIdProj();
        String sq = "Insert into " + TABLE_NAME2 + "(id_skrzyz,timer) Values(?,?)";
        int count=0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            SQLiteStatement statement = db.compileStatement(sq);
            for(int i=0;i<arr.size();i++) {
                statement.bindLong(1, id);
                statement.bindString(2,arr.get(i));
                statement.execute();
                count++;
            }
            if(count==arr.size())
                return true;
            else
                return false;
        } catch (Exception e) {
            Log.w("Exception:", e);
            return false;
        }

    }
    //id_pom INTEGER NOT NULL,img TEXT,path TEXT
    public boolean saveImages(String name,String path){
        int id=returnLastIdProj();
        String sq = "Insert into " + TABLE_NAME3 + "(id_pom,img,path) Values(?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            SQLiteStatement statement = db.compileStatement(sq);
            statement.bindLong(1, id);
            statement.bindString(2, name);
            statement.bindString(3, path);
            statement.execute();
            return true;
        } catch (Exception e) {
            Log.w("Exception:", e);
            return false;
        }
    }

    public Cursor returnImages(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select img,path from "+TABLE_NAME3;
        data=db.rawQuery(sql,null);
        return data;
    }


    public boolean deleteImages(String img, String paths){

        SQLiteDatabase db = this.getWritableDatabase();
        try {
                String sq = "Delete from " + TABLE_NAME3 + " where path='"+paths+"'";
                SQLiteStatement statement = db.compileStatement(sq);
                statement.execute();

                File myFile = new File(paths);
                if(myFile.exists())
                    myFile.delete();

                return true;
        } catch (Exception e) {
            Log.w("Exception:", e);
            return false;
        }
    }
}