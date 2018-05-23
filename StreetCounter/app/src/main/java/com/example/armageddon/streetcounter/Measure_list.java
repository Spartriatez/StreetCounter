package com.example.armageddon.streetcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Measure_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_list);
    }

    public void getMenuImem(View view) {
        Intent intent=new Intent(this,MainMenu.class);
        startActivity(intent);
    }
}
