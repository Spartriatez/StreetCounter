package com.example.armageddon.streetcounter;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectedTimes extends AppCompatActivity {
    DBSQLite dbsqLite;
    TableLayout tb;
    ArrayList<String> timers;
    ArrayList<Integer> ids;
    String nameStreet;
    String nameStreet2;
    String timestamp;
    int id_pom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_times);
        timers=new ArrayList<String>();
        ids=new ArrayList<Integer>();
        dbsqLite=new DBSQLite(this);
        tb=(TableLayout)findViewById(R.id.TimerVal);
        float dl=42f;
        float dl2=32f;
        int id=getIntent().getExtras().getInt("idPom");
        id_pom=id;
        String nameS= getIntent().getExtras().get("nameStreet").toString();
        nameStreet=nameS;
        nameStreet2=nameStreet;
        String tmstmp=getIntent().getExtras().get("createD").toString();
        timestamp=tmstmp;
        Cursor cur=dbsqLite.returnTimes(id);
        Log.e("num",String.valueOf(cur.getCount()));
        if(cur.getCount()>0) {
            while (cur.moveToNext()) {
                ids.add(cur.getInt(0));
                timers.add(cur.getString(1));
            }
            Log.e("times",String.valueOf(timers));
            for(int i=0;i<ids.size()+1;i++) {
                if(i==0) {
                    TableRow row = new TableRow(this);
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    row.setLayoutParams(lp);

                    LinearLayout layout2 = new LinearLayout(this);

                    //layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout2.setOrientation(LinearLayout.HORIZONTAL);
                    layout2.setGravity(Gravity.CENTER);
                    TextView tx1 = new TextView(this);
                    TextView tx2 = new TextView(this);
                    TextView tx3 = new TextView(this);
                    TextView tx4 = new TextView(this);

                    tx1.setText(String.valueOf(id));
                    tx1.setTextColor(Color.WHITE);
                    tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl);
                    tx1.setPadding(10, 0, 50, 0);
                    tx2.setText(nameS);
                    tx2.setTextColor(Color.WHITE);
                    tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl);
                    tx2.setPadding(10, 0, 50, 0);
                    tx3.setText(tmstmp);
                    tx3.setTextColor(Color.WHITE);
                    tx3.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl);
                    tx3.setPadding(10, 0, 50, 0);
                    tx4.setPadding(200,0,0,0);
                    layout2.addView(tx1);
                    layout2.addView(tx2);
                    layout2.addView(tx3);
                    layout2.addView(tx4);
                    row.addView(layout2);
                    tb.addView(row);
                } else{
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
                    tx1.setText("   ");
                    tx1.setTextColor(Color.WHITE);
                    tx1.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl2);
                    tx1.setPadding(40, 30, 50, 0);
                    tx2.setText(String.valueOf(ids.get(i-1)));
                    tx2.setTextColor(Color.WHITE);
                    tx2.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl2);
                    tx2.setPadding(200, 30, 200, 0);
                    tx3.setText(timers.get(i-1));
                    tx3.setTextColor(Color.WHITE);
                    tx3.setTextSize(TypedValue.COMPLEX_UNIT_SP, dl2);
                    tx3.setPadding(0, 30, 0, 0);
                    tx4.setText("       ");
                    layout2.addView(tx1);
                    layout2.addView(tx2);
                    layout2.addView(tx3);
                    layout2.addView(tx4);
                    row.addView(layout2);
                    tb.addView(row);
                }
            }
        }

    }

    public void returnMeasure(View view) {
        Intent intent=new Intent(this,Measure_list.class);
        startActivity(intent);
    }

    public void getGallerySImg(View view) {
        Intent i=new Intent(this,GaleriaSImg.class);
        i.putExtra("nameStreet2",nameStreet);
        i.putExtra("idPom",id_pom);
        i.putExtra("nameStreet",nameStreet2);
        i.putExtra("createD",timestamp);

        startActivity(i);
    }
}
