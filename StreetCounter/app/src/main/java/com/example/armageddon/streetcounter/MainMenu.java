package com.example.armageddon.streetcounter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    DBSQLite dbsqLite;
    private StopWatch_Service mService;
    private BroadcastReceiver broadcastReceiver, broadcastReceiver2, bcSaved;
    TextView tx1;
    String val = "";
    boolean isRunning2;
    String projName = "";
    Button bt1, bt2, bt3, bt4;
    ArrayList<String> dates;
    TextView mEtLaps; //laps text view
    ScrollView mSvLaps;
    boolean mBound = false;
    private Bitmap bitmap;
    int mLapCounter = 1;
    FloatingActionButton fbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dates = new ArrayList<String>();
        dbsqLite = new DBSQLite(this);
        tx1 = (TextView) findViewById(R.id.StopWatch);
        bt1 = (Button) findViewById(R.id.start);
        bt2 = (Button) findViewById(R.id.stop);
        bt3 = (Button) findViewById(R.id.laps);
        tx1.setText("00:00:00:00");
        bt2.setEnabled(false);
        bt2.setTextColor(Color.RED);
        bt3.setEnabled(false);
        bt3.setTextColor(Color.RED);
        mEtLaps = (TextView) findViewById(R.id.saveResult);
        mEtLaps.setEnabled(false); //prevent the et_laps to be editable

        mSvLaps = (ScrollView) findViewById(R.id.scrolled);

        fbtn=(FloatingActionButton)findViewById(R.id.createImg);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            fbtn.setClickable(true);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }


        if (bcSaved == null) {
            bcSaved = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String text = intent.getExtras().get("getText").toString();
                    dates = intent.getStringArrayListExtra("getArr");
                    for (int i = 0; i < dates.size(); i++) {
                        mEtLaps.append("LAP " + String.valueOf(mLapCounter++)
                                + "   " + dates.get(i) + "\n");
                        mSvLaps.post(new Runnable() {
                            @Override
                            public void run() {
                                mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                            }
                        });
                    }
                    tx1.setText(text);
                }
            };
        }
        registerReceiver(bcSaved, new IntentFilter("textView"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveAs) {
            return true;
        } else if (id == R.id.saveMeasure) {
            return true;
        } else if (id == R.id.MeasureList)
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean isRunning = intent.getExtras().getBoolean("mIsRun");

                    isRunning2 = mService.valueRunning();
                    if (isRunning == true && isRunning2 == true) {
                        val = intent.getExtras().get("stopwatch").toString();
                        tx1.setText(val);
                        bt1.setEnabled(false);
                        bt1.setTextColor(Color.RED);
                        bt2.setEnabled(true);
                        bt2.setTextColor(Color.WHITE);
                        bt3.setEnabled(true);
                        bt3.setTextColor(Color.WHITE);
                    } else {
                        bt1.setEnabled(true);
                        bt1.setTextColor(Color.WHITE);
                        bt2.setEnabled(false);
                        bt2.setTextColor(Color.RED);
                        if (val != "") {
                            bt3.setEnabled(true);
                            bt3.setTextColor(Color.WHITE);
                        } else {
                            bt3.setEnabled(false);
                            bt3.setTextColor(Color.RED);
                        }
                    }
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("timer"));


        Intent intent = new Intent(this, SaveData_Service.class);
        stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, StopWatch_Service.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(cashbackReciver);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }*/
   @Override
   public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
       if(requestCode==0){
           if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED ){
               fbtn.setClickable(true);
           }
       }
   }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            //Log.e("dsds","fdfdfdgfgdgdffffffffffffffffffffffffff");
            if(data.getData()!=null){
                Log.e("dsds","fdfdfdgfgdgdffffffffffffffffffffffffff");
                bitmap= (Bitmap) data.getExtras().get("data");
                saveImg();
            }
            else{
                bitmap= (Bitmap) data.getExtras().get("data");
                //imageView.setImageBitmap(bitmap);
            }
        }

    }
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            StopWatch_Service.LocalBinder binder = (StopWatch_Service.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void reseted(View view) {
        tx1.setText("00:00:00:00");
        if (mBound) {
            mService.restartTime();
        }
        mEtLaps.setText("");
        dates.clear();
        mLapCounter = 1;
    }

    public void stoped(View view) {

        if (mBound) {
            mService.stopTime();
            bt1.setEnabled(true);
            bt1.setTextColor(Color.WHITE);
            bt2.setEnabled(false);
            bt2.setTextColor(Color.RED);
            if (val != "") {
                bt3.setEnabled(true);
                bt3.setTextColor(Color.WHITE);
            } else {
                bt3.setEnabled(false);
                bt3.setTextColor(Color.RED);
            }
        }
    }

    public void started(View view) {
        if (mBound) {
            mService.startTime();
            bt1.setEnabled(false);
            bt1.setTextColor(Color.RED);
            bt2.setEnabled(true);
            bt2.setTextColor(Color.WHITE);
            bt3.setEnabled(true);
            bt3.setTextColor(Color.WHITE);
        }
    }

    public void lap(View view) {
        String dane = tx1.getText().toString();
        dates.add(dane);
        mEtLaps.append("LAP " + String.valueOf(mLapCounter++)
                + "   " + dane + "\n");
        mSvLaps.post(new Runnable() {
            @Override
            public void run() {
                mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
            }
        });
        if (isRunning2 == false) {
            bt3.setEnabled(false);
            bt3.setTextColor(Color.RED);
        }
        val = "";
        tx1.setText("00:00:00:00");
        if (mBound) {
            mService.restartTime();
        }
    }


    /*public void GetCamera(View view) {
        Cursor cur = dbsqLite.getAllData();
        if (cur.getCount() > 0) {
            String dane = tx1.getText().toString();
            Intent intent = new Intent();
            intent.setClass(this, SaveData_Service.class);
            intent.putExtra("TextView", dane);
            intent.putStringArrayListExtra("arrDate", dates);
            startService(intent);

            Intent inten = new Intent(MainMenu.this, CamImg.class);
            startActivity(inten);
        } else
            Toast.makeText(this, "Utwórz projekt", Toast.LENGTH_LONG).show();
    }*/

    public void saveAs(MenuItem item) {
        String dane = tx1.getText().toString();
        Intent intent = new Intent();
        intent.setClass(this, SaveData_Service.class);
        intent.putExtra("TextView", dane);
        intent.putStringArrayListExtra("arrDate", dates);
        startService(intent);
        Intent i = new Intent(MainMenu.this, SaveAs.class);
        startActivity(i);
    }

    @SuppressLint("WrongViewCast")
    public void saveList(MenuItem item) {
        Cursor cur = dbsqLite.getAllData();
        if (dates.isEmpty()) {
            Toast.makeText(this, "Nie masz żadnych pomiarów", Toast.LENGTH_LONG).show();
        } else if (cur.getCount() == 0) {
            Toast.makeText(this, "Utwórz projekt", Toast.LENGTH_LONG).show();
        } else if (!dates.isEmpty() && cur.getCount() != 0) {

            LinearLayout parent = new LinearLayout(this);

            parent.setOrientation(LinearLayout.VERTICAL);

            Spinner msp = new Spinner(this);
            final TextView txAB = new TextView(this);
            txAB.setGravity(Gravity.CENTER);
            List<String> list = new ArrayList<String>();
            while (cur.moveToNext()) {
                String name = cur.getString(0);
                list.add(name);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            msp.setAdapter(dataAdapter);
            msp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String dane = adapterView.getSelectedItem().toString();
                    projName = dane;
                    txAB.setText(dane);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            parent.addView(msp);
            parent.addView(txAB);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Czy chcesz zapisać pomiary?");
            builder1.setView(parent);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int res = dbsqLite.isAllAdded(dates, projName);
                            if (res == 0) {
                                Toast.makeText(getApplicationContext(), "Dodano pomiary", Toast.LENGTH_LONG).show();
                                dates.clear();
                                mEtLaps.setText("");
                                mSvLaps.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                                    }
                                });
                                mLapCounter = 1;
                            } else if (res == 1) {
                                Toast.makeText(getApplicationContext(), "Błąd tworzenia pomiarów", Toast.LENGTH_LONG).show();
                            } else if (res == 2) {
                                Toast.makeText(getApplicationContext(), "Błąd zapisu czasów", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    public void showList(MenuItem item) {
        String dane = tx1.getText().toString();
        Intent intent = new Intent();
        intent.setClass(this, SaveData_Service.class);
        intent.putExtra("TextView", dane);
        intent.putStringArrayListExtra("arrDate", dates);
        startService(intent);
        Intent inten = new Intent(this, Measure_list.class);
        startActivity(inten);
    }

    public void getGallery(View view) {
        String dane = tx1.getText().toString();
        Intent intent = new Intent();
        intent.setClass(this, SaveData_Service.class);
        intent.putExtra("TextView", dane);
        intent.putStringArrayListExtra("arrDate", dates);
        startService(intent);
        Intent i = new Intent(MainMenu.this, GalleryImages.class);
        startActivity(i);
    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }

    public void makeImage(View view) throws InterruptedException {
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);

        Thread.sleep(3000);
    }

    public void saveImg(){
        Cursor cur = dbsqLite.getAllData();
        LinearLayout parent = new LinearLayout(this);

        parent.setOrientation(LinearLayout.VERTICAL);

        Spinner msp = new Spinner(this);
        final TextView txAB = new TextView(this);
        txAB.setGravity(Gravity.CENTER);
        List<String> list = new ArrayList<String>();
        while (cur.moveToNext()) {
            String name = cur.getString(0);
            list.add(name);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msp.setAdapter(dataAdapter);
        msp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String dane = adapterView.getSelectedItem().toString();
                projName = dane;
                txAB.setText(dane);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        parent.addView(msp);
        parent.addView(txAB);

        if(bitmap!=null) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Czy chcesz zapisać obraz?");
            builder1.setView(parent);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Tak",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String s = getApplicationInfo().dataDir;
                            String pathImg = s + "/images";
                            File dir = new File(pathImg);
                            if (!dir.exists() && !dir.isDirectory()) {
                                dir.mkdir();
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss'Z'");
                            Date date = new Date();
                            String dateTime = dateFormat.format(date);
                            String imgName = "img_" + dateTime + ".jpg";
                            File file = new File(pathImg, imgName);
                            if (file.exists())
                                file.delete();
                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                Bitmap bm = getResizedBitmap(bitmap, 1024, 748);
                                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (dbsqLite.saveImages(projName, imgName, pathImg + '/' + imgName) == true) {
                                Toast.makeText(getApplicationContext(), "Utworzono Zdjęcie", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Błąd zapisu", Toast.LENGTH_LONG).show();

                            }
                        }

                    });

            builder1.setNegativeButton(
                    "Nie",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}