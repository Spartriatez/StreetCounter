package com.example.armageddon.streetcounter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class SelectedImage extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_image);
        img=(ImageView)findViewById(R.id.selectImg);
        String path=getIntent().getExtras().get("myImage").toString();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        img.setImageBitmap(bitmap);
    }

    public void getGallery(View view) {
        Intent intent=new Intent(this,GalleryImages.class);
        startActivity(intent);
    }
}
