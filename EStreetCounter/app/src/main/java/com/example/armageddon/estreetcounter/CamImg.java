package com.example.armageddon.estreetcounter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

public class CamImg extends AppCompatActivity {
    FloatingActionButton fbtn;
    ImageView imageView;
    private Bitmap bitmap;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_img);

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
    }

    public void getMainMenu(View view) {
        Intent inten=new Intent(CamImg.this,MainMenu.class);
        inten.putExtra("dane2",text);
        startActivity(inten);
    }
}
