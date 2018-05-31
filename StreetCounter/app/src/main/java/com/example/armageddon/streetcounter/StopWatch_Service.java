package com.example.armageddon.streetcounter;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class StopWatch_Service  extends Service {
    private final IBinder mBinder = new LocalBinder();
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds,Hours;
    boolean mIsRunning;
    String savedString=null;

    /********/
    @Override
    public void onCreate() {
        handler = new Handler();
        mIsRunning=false;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        StopWatch_Service getService() {
            // Return this instance of LocalService so clients can call public methods
            return StopWatch_Service.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            if (!mIsRunning) {
                return;
            } else {
                MillisecondTime = SystemClock.uptimeMillis() - StartTime;

                UpdateTime = TimeBuff + MillisecondTime;
                Seconds = (int) (UpdateTime / 1000);

                Minutes = Seconds / 60;

                Seconds = Seconds % 60;

                Hours = Minutes / 60;
                MilliSeconds = (int) (UpdateTime % 1000);
                Intent i = new Intent("timer");
                i.putExtra("stopwatch", "" + String.format("%02d", Hours) + ":" + String.format("%02d", Minutes) + ":"
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
                i.putExtra("mIsRun",mIsRunning);
                sendBroadcast(i);


                handler.postDelayed(this, 0);
            }
        }
    };


    public void startTime(){
        mIsRunning=true;
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        Intent i = new Intent("isRunning");
        i.putExtra("mIsRun",mIsRunning);
        sendBroadcast(i);
    }
    public void stopTime(){
        mIsRunning=false;
        TimeBuff += MillisecondTime;
        handler.removeCallbacks(runnable);
        Intent i = new Intent("isRunning");
        i.putExtra("mIsRun",mIsRunning);
        sendBroadcast(i);
    }
    public void restartTime(){
        MillisecondTime = 0 ;
        StartTime = SystemClock.uptimeMillis();
        TimeBuff = 0 ;
        UpdateTime = 0 ;
        Seconds = 0 ;
        Minutes = 0 ;
        MilliSeconds = 0 ;

    }

    public boolean valueRunning(){
        return mIsRunning;
    }

}