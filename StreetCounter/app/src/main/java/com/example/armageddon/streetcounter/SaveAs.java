package com.example.armageddon.streetcounter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SaveAs extends AppCompatActivity {
    DBSQLite dbsqLite;
    EditText ed;
    TextView mEtLaps; //laps text view
    ScrollView mSvLaps;
    Spinner sp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_as);
        dbsqLite=new DBSQLite(this);

        mEtLaps = (TextView) findViewById(R.id.Resulted2);
        mSvLaps = (ScrollView) findViewById(R.id.scrolled2);
        sp1=(Spinner)findViewById(R.id.sp1);
        ed=(EditText)findViewById(R.id.ProjName);
        Cursor cur=dbsqLite.getAllData();
        ed.setText("");
        if(cur.getCount()==0){
            mEtLaps.append("---------brak projektów------------");
            mSvLaps.post(new Runnable() {
                @Override
                public void run() {
                    mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                }
            });
            List<String> list = new ArrayList<String>();
            list.add("----brak----");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(dataAdapter);
        }else{
            List<String> list = new ArrayList<String>();
            while (cur.moveToNext()){
                String name=cur.getString(0);
                mEtLaps.append(name+"\n");
                list.add(name);
            }
            mSvLaps.post(new Runnable() {
                @Override
                public void run() {
                    mSvLaps.smoothScrollTo(0, mEtLaps.getBottom());
                }
            });
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(dataAdapter);
            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String dane = adapterView.getSelectedItem().toString();
                    ed.setText(dane);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

    }

    public void getMainMenu(View view) {
        Intent inten=new Intent(SaveAs.this,MainMenu.class);
        startActivity(inten);
    }

    public void saveProject(View view) {
        String name=ed.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this,"Brak nazwy",Toast.LENGTH_LONG).show();
        }else if(!name.isEmpty() && dbsqLite.checkProj(name)==true){
                if(dbsqLite.insertProject(name)==true){
                    Toast.makeText(this,"Dodano dane",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                    ed.setText("");
                }else{
                    Toast.makeText(this,"Błąd bazy danych",Toast.LENGTH_LONG).show();
                }
        }else if(dbsqLite.checkProj(name)==false && !name.isEmpty()){
            Toast.makeText(this,"Projekt o takiej nazwie isnieje",Toast.LENGTH_LONG).show();
        }
    }

    public void deleteProj(View view) {
        String name=ed.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this,"Brak nazwy",Toast.LENGTH_LONG).show();
        }else if(!name.isEmpty() && dbsqLite.checkProj(name)==false){
            if(dbsqLite.deleteProject(name)==true){
                Toast.makeText(this,"usunięto dane",Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
                ed.setText("");
            }else{
                Toast.makeText(this,"Błąd bazy danych",Toast.LENGTH_LONG).show();
            }
        }
    }
}
