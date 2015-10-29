package com.android.xang.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class aboutapp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutapp);
        overridePendingTransition(R.anim.bottom_to_top, R.anim.bottom_to_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end oncreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            overridePendingTransition(R.anim.top_to_bottom,R.anim.top_to_bottom);
        }//end if

        return super.onOptionsItemSelected(item);
    }
}
