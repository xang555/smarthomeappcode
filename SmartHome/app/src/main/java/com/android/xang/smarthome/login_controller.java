package com.android.xang.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by xang- on 25/10/2015.
 */
public class login_controller extends AppCompatActivity {
    private EditText username=null;
    public static final String KEYUSERNAME="username";
    private CardView cardView;
    private int REQLONIN=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        cardView=(CardView)findViewById(R.id.view);
        username=(EditText)findViewById(R.id.username);

        //checkusername
        checkusername();


    }//end oncreate

    public void loginnclick(View view){

        Intent intent=new Intent(login_controller.this,smarthome_control.class);
        String user=username.getText().toString().trim();
        if (!user.equals("")){
            saveusername(user);
            intent.putExtra(KEYUSERNAME,user);
            startActivityForResult(intent,REQLONIN);

        }else {
            Snackbar.make(cardView,getResources().getString(R.string.userinformationlogin),Snackbar.LENGTH_LONG).show();
        }

    }//end loginclick


    private void saveusername(String username){

        SharedPreferences preferences=getSharedPreferences(KEYUSERNAME,MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(KEYUSERNAME,username);
        editor.commit();

    }//end saveuser

    private void checkusername(){

        SharedPreferences preferences=getSharedPreferences(KEYUSERNAME,MODE_PRIVATE);
        String username=preferences.getString(KEYUSERNAME,null);
        if (username!=null){
            Intent intent=new Intent(login_controller.this,smarthome_control.class);
            intent.putExtra(KEYUSERNAME,username);
            startActivityForResult(intent,REQLONIN);

        }else {
            return;
        }

    }//end checkusername

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQLONIN && resultCode==RESULT_CANCELED){
            finish();
            setResult(RESULT_CANCELED);
        }

    }//end activityresult





}//end class
