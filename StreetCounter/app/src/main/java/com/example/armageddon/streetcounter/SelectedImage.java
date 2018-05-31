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
    int whoGallery;
    String nameStreet2;
    String timestamp;
    int id_pom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_image);
        img=(ImageView)findViewById(R.id.selectImg);
        String path=getIntent().getExtras().get("myImage").toString();
        whoGallery=getIntent().getExtras().getInt("whoGallery");
        if(whoGallery==2){
            int id=getIntent().getExtras().getInt("idPom");
            id_pom=id;
            String nameS= getIntent().getExtras().get("nameStreet").toString();
            nameStreet2=nameS;
            String tmstmp=getIntent().getExtras().get("createD").toString();
            timestamp=tmstmp;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        img.setImageBitmap(bitmap);
    }

    public void getGallery(View view) {
        if(whoGallery==1) {
            Intent intent = new Intent(this, GalleryImages.class);
            startActivity(intent);
        }else if(whoGallery==2){
            Intent intent = new Intent(this, GaleriaSImg.class);
            intent.putExtra("idPom",id_pom);
            intent.putExtra("nameStreet",nameStreet2);
            intent.putExtra("nameStreet2",nameStreet2);
            intent.putExtra("createD",timestamp);
            startActivity(intent);
        }
    }
}
