package com.android.xang.smarthome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by xang- on 28/10/2015.
 */
public class banner extends Activity {

    private int reqcode=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner);
        final Intent intent=new Intent(this,login_controller.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                 startActivityForResult(intent,reqcode);
            }

        },3000);





    }//end oncreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==reqcode && resultCode==RESULT_CANCELED){
            finish();

        }//end if

    }//end onactivity


}//end class
