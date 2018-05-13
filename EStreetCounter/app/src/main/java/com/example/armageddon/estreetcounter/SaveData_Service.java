package com.example.armageddon.estreetcounter;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

public class SaveData_Service extends Service implements Runnable {
    private Handler handler=new Handler();
    String text;
    ArrayList<String> dates=new ArrayList<String>();
    @Override
    public void onCreate() {
        super.onCreate();
        handler.postDelayed(this, 1000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(this);
    }
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        text = intent.getStringExtra("UserID");
        dates=intent.getStringArrayListExtra("arrDate");
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {
        Log.d("dane", String.valueOf(dates));
        Intent i = new Intent("textView");
        i.putExtra("getText",text);
        i.putStringArrayListExtra("getArr",dates);
        sendBroadcast(i);
        handler.postDelayed(this, 100);
    }
}