package com.example.armageddon.streetcounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class GaleriaSImg extends AppCompatActivity {
    DBSQLite dbsqLite;
    TableLayout tb;
    ScrollView scrolled;
    ArrayList<String> paths;
    ArrayList<String> images;
    ArrayList<Boolean> whoChecked;
    String nameStreet2;
    String timestamp;
    int id_pom;
    @SuppressLint({"ResourceAsColor", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria_simg);
        int id=getIntent().getExtras().getInt("idPom");
        id_pom=id;
        String nameS2= getIntent().getExtras().get("nameStreet").toString();
        nameStreet2=nameS2;
        String tmstmp=getIntent().getExtras().get("createD").toString();
        timestamp=tmstmp;
        String nameS= getIntent().getExtras().get("nameStreet2").toString();
        dbsqLite=new DBSQLite(this);
        tb=(TableLayout)findViewById(R.id.images2);
        scrolled=(ScrollView)findViewById(R.id.scrolled4);
        paths=new ArrayList<String>();
        images=new ArrayList<String>();
        whoChecked=new ArrayList<Boolean>();

        Cursor cur=dbsqLite.returnSelectedImages(nameS);
        int counter=0;
        if(cur.getCount()>0) {
            while (cur.moveToNext()) {
                images.add(cur.getString(0));
                paths.add(cur.getString(1));
            }

            for (int i = 0; i < paths.size(); i++) {
                whoChecked.add(false);
            }
            while (counter < paths.size()) {
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);
                for (int i = 0; i < 4; i++) {
                    if (counter < paths.size()) {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeFile(paths.get(counter), options);

                        ImageView image = new ImageView(this);
                        image.setImageBitmap(bitmap);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
                        image.setLayoutParams(layoutParams);

                        LinearLayout parent = new LinearLayout(this);

                        parent.setOrientation(LinearLayout.VERTICAL);
                        parent.setPadding(60, 0, 20, 0);
                        LinearLayout layout2 = new LinearLayout(this);

                        //layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout2.setOrientation(LinearLayout.HORIZONTAL);
                        layout2.setGravity(Gravity.CENTER);
                        FloatingActionButton fab = new FloatingActionButton(this);
                        fab.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                        fab.setBackgroundTintList(ColorStateList.valueOf(R.color.colorPrimary));
                        fab.setSize(android.support.design.widget.FloatingActionButton.SIZE_MINI);
                        fab.setFocusable(true);
                        fab.setPadding(100, 0, 10, 0);
                        final int finalCounter = counter;
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), SelectedImage.class);
                                intent.putExtra("myImage", paths.get(finalCounter));
                                intent.putExtra("whoGallery",2);
                                intent.putExtra("idPom",id_pom);
                                intent.putExtra("nameStreet",nameStreet2);
                                intent.putExtra("createD",timestamp);
                                startActivity(intent);
                            }
                        });

                        AppCompatCheckBox acb = new AppCompatCheckBox(this);

                        acb.setId(counter);
                        acb.setTextSize(32f);
                        ColorStateList colorStateList = new ColorStateList(
                                new int[][]{

                                        new int[]{-android.R.attr.state_enabled}, //disabled
                                        new int[]{android.R.attr.state_enabled} //enabled
                                },
                                new int[]{

                                        Color.RED //disabled
                                        , Color.WHITE //enabled

                                }
                        );

                        acb.setSupportButtonTintList(colorStateList);
                        final int finalCounter2 = counter;
                        acb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                whoChecked.set(finalCounter2, isChecked);
                            }
                        });

                        layout2.addView(fab);
                        layout2.addView(acb);

                        parent.addView(image);

                        parent.addView(layout2);
                        row.addView(parent);
                        counter++;
                    } else
                        break;
                }
                tb.addView(row);
            }
        }
    }

    public void returnMainMenu(View view) {
        Intent inten=new Intent(GaleriaSImg.this,SelectedTimes.class);
        inten.putExtra("idPom",id_pom);
        inten.putExtra("nameStreet",nameStreet2);
        inten.putExtra("nameStreet2",nameStreet2);
        inten.putExtra("createD",timestamp);
        startActivity(inten);
    }
    public void deleteImages(View view) {
        final ArrayList<String> delImg=new ArrayList<String>();
        final ArrayList<String> delPaths=new ArrayList<String>();

        int counter=0;
        for(int i=0;i<paths.size();i++){
            if(whoChecked.get(i)==true)
                delImg.add(images.get(i));
            delPaths.add(paths.get(i));
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Czy na pewno chcesz usunąć zdjęcia?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(int i=0;i<delImg.size();i++) {
                            if (dbsqLite.deleteImages(delImg.get(i),delPaths.get(i))) {
                                Toast.makeText(getApplicationContext(), "Pomyśnie zdjęcia zostały usunięte", Toast.LENGTH_LONG).show();
                                delImg.clear();
                                delPaths.clear();
                                tb.removeAllViews();
                                paths.clear();
                                images.clear();
                                whoChecked.clear();
                                finish();
                                startActivity(getIntent());
                            } else {
                                Toast.makeText(getApplicationContext(), "Błąd usuwania", Toast.LENGTH_LONG).show();
                                break;
                            }
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
