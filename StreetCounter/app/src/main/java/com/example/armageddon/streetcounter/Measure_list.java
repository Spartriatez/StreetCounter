package com.example.armageddon.streetcounter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Measure_list extends AppCompatActivity {
    DBSQLite dbsqLite;
    TableLayout tb;
    ArrayList<Integer> ids;
    ArrayList<String> namesS;
    ArrayList<String> tmstanp;
    ArrayList<Boolean> whoChecked;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "RestrictedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_list);
        dbsqLite=new DBSQLite(this);
        tb=(TableLayout)findViewById(R.id.nameStreets);
        ids=new ArrayList<Integer>();
        namesS=new ArrayList<String>();
        tmstanp=new ArrayList<String>();
        Cursor cur=dbsqLite.returnVals();
        whoChecked=new ArrayList<Boolean>();
        int counter=0;
        float dl=42f;
        float dl2=32f;
        if(cur.getCount()>0) {
            while (cur.moveToNext()){
                ids.add(cur.getInt(0));
                namesS.add(cur.getString(1));
                tmstanp.add(cur.getString(2));
            }
            for(int i=0;i<ids.size();i++){
                whoChecked.add(false);
            }
            Log.e("dane",String.valueOf(ids));
            for(int i=0;i<ids.size()+1;i++) {
                if(i==0){
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    row.setLayoutParams(lp);

                    LinearLayout layout2 = new LinearLayout(this);

                    //layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout2.setOrientation(LinearLayout.HORIZONTAL);
                    layout2.setGravity(Gravity.LEFT);
                    TextView tx1 = new TextView(this);
                    TextView tx2 = new TextView(this);
                    TextView tx3 = new TextView(this);
                    TextView tx4 = new TextView(this);

                    tx1.setText("Id");
                    tx1.setTextColor(Color.WHITE);
                    tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl);
                    tx1.setPadding(0, 0, 20, 0);
                    tx2.setText("Nazwa skrzyż.");
                    tx2.setTextColor(Color.WHITE);
                    tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl);
                    tx2.setPadding(0, 0, 50, 0);
                    tx3.setText("Timestamp proj.");
                    tx3.setTextColor(Color.WHITE);
                    tx3.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl);
                    tx3.setPadding(0, 0, 10, 0);
                    tx4.setPadding(0,0,0,0);
                    layout2.addView(tx1);
                    layout2.addView(tx2);
                    layout2.addView(tx3);
                    layout2.addView(tx4);
                    row.addView(layout2);
                    tb.addView(row);
                }else {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    row.setLayoutParams(lp);

                    LinearLayout layout2 = new LinearLayout(this);

                    //layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout2.setOrientation(LinearLayout.HORIZONTAL);
                    layout2.setGravity(Gravity.LEFT);
                    TextView tx1 = new TextView(this);
                    TextView tx2 = new TextView(this);
                    TextView tx3 = new TextView(this);
                    TextView tx4 = new TextView(this);
                    tx1.setText(String.valueOf(ids.get(i - 1)));
                    tx1.setTextColor(Color.WHITE);
                    tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl2);
                    tx1.setGravity(Gravity.CENTER);
                    tx1.setPadding(10, 0, 50, 0);
                    tx2.setText(namesS.get(i - 1));
                    tx2.setTextColor(Color.WHITE);
                    tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl2);
                    tx2.setPadding(50, 0, 100, 0);
                    tx2.setGravity(Gravity.CENTER);
                    tx3.setText(tmstanp.get(i - 1));
                    tx3.setTextColor(Color.WHITE);
                    tx3.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl2);
                    tx3.setPadding(000, 0, 0, 0);
                    tx3.setGravity(Gravity.CENTER);
                    tx4.setText("       ");

                    FloatingActionButton fab = new FloatingActionButton(this) ;
                    fab.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(R.color.colorPrimary));
                    fab.setSize(android.support.design.widget.FloatingActionButton.SIZE_MINI);
                    LinearLayout layout3 = new LinearLayout(this);

                    //layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout3.setOrientation(LinearLayout.HORIZONTAL);
                    layout3.setGravity(Gravity.CENTER);
                    //fab.setFocusable(true);
                    final int idPom=ids.get(i-1);
                    final String nameStreet=namesS.get(i-1);
                    final String createdData=tmstanp.get(i-1);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("jakie id",String.valueOf(idPom));
                            Intent intent=new Intent(getApplicationContext(),SelectedTimes.class);
                            intent.putExtra("idPom",idPom-1);
                            intent.putExtra("nameStreet",nameStreet);
                            intent.putExtra("createD",createdData);
                            startActivity(intent);
                        }
                    });

                    fab.setPadding(50,0,10,0);

                    AppCompatCheckBox acb = new AppCompatCheckBox(getApplicationContext());

                    acb.setId(counter);
                    acb.setTextSize(32f);
                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{

                                    new int[]{-android.R.attr.state_enabled}, //disabled
                                    new int[]{android.R.attr.state_enabled} //enabled
                            },
                            new int[] {

                                    Color.RED //disabled
                                    ,Color.WHITE //enabled

                            }
                    );

                    acb.setSupportButtonTintList(colorStateList);
                    final int finalCounter2 = i-1;
                    acb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            whoChecked.set(finalCounter2,isChecked);
                        }
                    });

                    acb.setPadding(20,20,0,0);
                    /*layout3.addView(fab);
                    layout3.addView(acb);*/
                    layout2.addView(tx1);
                    layout2.addView(tx2);
                    layout2.addView(tx3);
                    layout2.addView(tx4);
                    layout2.addView(fab);
                    layout2.addView(acb);
                    row.addView(layout2);
                    tb.addView(row);
                }
            }

        }else{
            Toast.makeText(this,"Brak danych",Toast.LENGTH_LONG).show();
        }

    }


    public void returnMainMenu(View view) {
        Intent intent=new Intent(this,MainMenu.class);
        startActivity(intent);
    }

    public void deleteProj(View view) {
        final ArrayList<String> delProj=new ArrayList<String>();
        for(int i=0;i<namesS.size();i++){
            if(whoChecked.get(i)==true)
                delProj.add(namesS.get(i));
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Czy na pewno chcesz usunąć pomiary?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Tak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for(int i=0;i<delProj.size();i++) {
                            if (dbsqLite.deleteVal(delProj.get(i))) {
                                Toast.makeText(getApplicationContext(), "Pomyśnie pomiary zostały usunięte", Toast.LENGTH_LONG).show();
                                delProj.clear();
                                ids.clear();
                                tmstanp.clear();
                                namesS.clear();
                                tb.removeAllViews();
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
