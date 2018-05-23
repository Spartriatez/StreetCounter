package com.example.armageddon.streetcounter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamImg extends AppCompatActivity {
    DBSQLite dbsqLite;
    FloatingActionButton fbtn;
    ImageView imageView;
    private Bitmap bitmap;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_img);
        dbsqLite=new DBSQLite(this);
        fbtn=(FloatingActionButton)findViewById(R.id.cameraImage);
        imageView=(ImageView)findViewById(R.id.imageView);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            fbtn.setClickable(true);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode==0){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED ){
                fbtn.setClickable(true);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == 100 && resultCode == Activity.RESULT_OK){
            if(data.getData()!=null){}
            else{
                bitmap= (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
        }

    }
    /* @Override
     protected void onSaveInstanceState(Bundle outState){
         super.onSaveInstanceState(outState);
         outState.putString("timezone",text);
     }*/


    public void makePhoto(View view) {
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,100);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Czy chcesz zapisać obraz?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String s = getApplicationInfo().dataDir;
                        String pathImg=s+"/images";
                        File dir = new File(pathImg);
                        if(!dir.exists() && !dir.isDirectory()) {
                            dir.mkdir();
                        }

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss'Z'");
                        Date date = new Date();
                        String dateTime = dateFormat.format(date);
                        String imgName="img_"+dateTime+".jpg";
                        File file = new File(pathImg, imgName);
                        if (file.exists())
                            file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            Bitmap bm=getResizedBitmap(bitmap,1024,748);
                            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(dbsqLite.saveImages(imgName,pathImg+'/'+imgName)==true){
                            Toast.makeText(getApplicationContext(),"Utworzono Zdjęcie",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Błąd zapisu",Toast.LENGTH_LONG).show();

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

    public void getMainMenu(View view) {
        Intent inten=new Intent(CamImg.this,MainMenu.class);
        startActivity(inten);
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
}
