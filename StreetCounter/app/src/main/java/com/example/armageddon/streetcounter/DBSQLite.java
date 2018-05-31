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
    public static final String TABLE_NAME4="awp_czasy";
    public DBSQLite(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database","onCreate");
        String sql = "create table IF NOT EXISTS " + TABLE_NAME1 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,nazwa TEXT NOT NULL, datetime DEFAULT CURRENT_TIMESTAMP)";
        String sql2 = "create table IF NOT EXISTS " + TABLE_NAME2 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,id_skrzyz INTEGER NOT NULL,datetime DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (id_skrzyz) REFERENCES awp_skrzyzowania(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        String sql4 = "create table IF NOT EXISTS " + TABLE_NAME4 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,id_pom INTEGER NOT NULL,time TEXT, FOREIGN KEY (id_pom) REFERENCES awp_pomiary(id) ON DELETE CASCADE ON UPDATE CASCADE)";

        String sql3 = "create table IF NOT EXISTS " + TABLE_NAME3 + "(id INTEGER PRIMARY KEY AUTOINCREMENT,id_skrzyz INTEGER NOT NULL,img TEXT,path TEXT,datetime DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (id_skrzyz) REFERENCES awp_skrzyzowania(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        SQLiteStatement stmt=db.compileStatement(sql);
        stmt.execute();
        stmt=db.compileStatement(sql2);
        stmt.execute();
        stmt=db.compileStatement(sql3);
        stmt.execute();
        stmt=db.compileStatement(sql4);
        stmt.execute();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sq="Drop TABLE IF EXISTS "+TABLE_NAME1;
        String sq2="Drop TABLE IF EXISTS "+TABLE_NAME2;
        String sq3="Drop TABLE IF EXISTS "+TABLE_NAME3;
        String sq4="Drop TABLE IF EXISTS "+TABLE_NAME4;
        SQLiteStatement stmt=db.compileStatement(sq);
        stmt.execute();
        stmt=db.compileStatement(sq2);
        stmt.execute();
        stmt=db.compileStatement(sq3);
        stmt.execute();
        stmt=db.compileStatement(sq4);
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

    public boolean createVal(String name){
        int id=returnIdProj(name);
        String sq = "Insert into " + TABLE_NAME2 + "(id_skrzyz) Values(?)";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            SQLiteStatement statement = db.compileStatement(sq);
            statement.bindLong(1, id);
            statement.execute();
            return true;
        }catch (Exception e) {
            Log.w("Exception:", e);
            return false;
        }
    }


    public int returnIdPom(String name){
        int id=returnIdProj(name);
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select id from "+TABLE_NAME2+" where id="+id;
        data=db.rawQuery(sql,null);
        while (data.moveToNext()){
            return data.getInt(0);
        }
        return 0;
    }

    public boolean insertValues(List<String> arr,String name){
        int id=returnIdPom(name);
        String sq = "Insert into " + TABLE_NAME4 + "(id_pom,time) Values(?,?)";
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
    public int isAllAdded(List<String> arr,String name){
        int errno=0;
        if(createVal(name)==true){
            if(insertValues(arr,name))
                errno=0;
            else
                errno=2;
        }
        else
            errno=1;
        return errno;
    }

    public Cursor returnVals(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select t1.id,t2.nazwa, t1.datetime from "+TABLE_NAME2+" t1  join "+TABLE_NAME1+" t2 on t1.id_skrzyz=t2.id";

        data=db.rawQuery(sql,null);
        return data;
    }

    public Cursor returnTimes(int id){
        Log.e("id_param",String.valueOf(id));
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        /*String sql2="select id_pom,count(id) as num from "+TABLE_NAME4+" where id_pom="+id+" group by id_pom;";
        data=db.rawQuery(sql2,null);

        Log.e("id_pommmm",String.valueOf(data.getInt(0)));
        Log.e("counter",String.valueOf(data.getInt(1)));*/

        String sql = "select id,time from "+TABLE_NAME4+" where id_pom="+id+";";

        data=db.rawQuery(sql,null);
        return data;
    }
    //id_pom INTEGER NOT NULL,img TEXT,path TEXT
    public boolean saveImages(String name,String nameImg,String path){
        int id=returnIdProj(name);
        String sq = "Insert into " + TABLE_NAME3 + "(id_skrzyz,img,path) Values(?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            SQLiteStatement statement = db.compileStatement(sq);
            statement.bindLong(1, id);
            statement.bindString(2, nameImg);
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
        String sql = "select t1.img,t1.path, t1.id_skrzyz,t2.nazwa from "+TABLE_NAME3+" t1  join "+TABLE_NAME1+" t2 on t1.id_skrzyz=t2.id order by t1.id_skrzyz";

        data=db.rawQuery(sql,null);
        return data;
    }

    public Cursor returnSelectedImages(String name){
        int id=returnIdProj(name);
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor data=null;
        String sql = "select img,path from "+TABLE_NAME3+" where id_skrzyz="+id;
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

    public boolean deleteVal(String name){

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String sq = "Delete from " + TABLE_NAME2 + " where id_skrzyz= (select id from "+TABLE_NAME1+" where nazwa='"+name+"')";
            SQLiteStatement statement = db.compileStatement(sq);
            statement.execute();

            return true;
        } catch (Exception e) {
            Log.w("Exception:", e);
            return false;
        }
    }
}