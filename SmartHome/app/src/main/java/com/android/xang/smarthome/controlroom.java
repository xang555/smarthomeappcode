package com.android.xang.smarthome;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class controlroom extends AppCompatActivity {

    private TextView stat;
    private int Pincontrol;
    private String TAG="Socket";
    private ImageView imageligth;
    private ToggleButton onoff;
    private Socket socket=smarthome_control.socket;
    private String username="";
    //======================variable===================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controlroom);

        overridePendingTransition(R.anim.rigth_to_left, R.anim.rigth_to_left);
        imageligth=(ImageView)findViewById(R.id.imageligth);
        onoff=(ToggleButton)findViewById(R.id.onoff);
        stat=(TextView)findViewById(R.id.status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        getSupportActionBar().setTitle(intent.getExtras().getString(SmarthomeAdapter.KEYNAME));
        Pincontrol=intent.getExtras().getInt(SmarthomeAdapter.KEYPIN);
        username=intent.getStringExtra(SmarthomeAdapter.KEYWHONAME);

        initsocket(); //init socket


    }//end oncreate

    public void initsocket(){
        try{
            if (!socket.connected()){
                socket.connect();
            }
            socketmanager(socket);

            JSONObject obj=new JSONObject();
            obj.put(SmartHome_interface.PIN,Pincontrol);

            socket.emit(SmartHome_interface.EVENT_RESTORE,obj);

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }//end method


    public void onoffclick(View view){

    if (onoff.isChecked()){
        sendcommand(SmartHome_interface.COMMANDON);

    }else{
     sendcommand(SmartHome_interface.COMMANDOFF);

    }//end if else

    }//end onoffclick


    public void socketmanager(Socket socket){


        socket.on(SmartHome_interface.SENDMESSAGE, new Emitter.Listener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void call(Object... args) {

                try {
                    JSONObject object=(JSONObject)args[0];
                    if(object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDON) && object.getInt(SmartHome_interface.PIN)==Pincontrol){

                        controlroom.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stat.setText(username+" ເປີດໄຟ");
                                imageligth.setImageDrawable(getDrawable(R.drawable.bulbon));
                                onoff.setChecked(true);
                            }
                        });

                    }else if (object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDOFF)&& object.getInt(SmartHome_interface.PIN)==Pincontrol){

                        controlroom.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stat.setText(username+" ປິດໄຟ");
                                imageligth.setImageDrawable(getDrawable(R.drawable.bulboff));
                                onoff.setChecked(false);
                            }
                        });

                    }//end if else


                } catch (JSONException e) {
                    Log.e(TAG,e.getMessage());
                }


            }//end call

        }); //on message send

        socket.on(SmartHome_interface.EVENT_RESTORE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                final JSONObject object=(JSONObject)args[0];
                if (object!=null){

                    try {
                        if(object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDON)){

                            controlroom.this.runOnUiThread(new Runnable() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    try {
                                        stat.setText(object.getString(SmartHome_interface.KEY_NAME)+"ເປີດໄຟ");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    imageligth.setImageDrawable(getDrawable(R.drawable.bulbon));
                                    onoff.setChecked(true);
                                }
                            });

                        }else if (object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDOFF)){

                            controlroom.this.runOnUiThread(new Runnable() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void run() {
                                    try {
                                        stat.setText(object.getString(SmartHome_interface.KEY_NAME)+"ປິດໄຟ");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    imageligth.setImageDrawable(getDrawable(R.drawable.bulboff));
                                    onoff.setChecked(false);
                                }
                            });

                        }//end if else


                    } catch (JSONException e) {
                        Log.e(TAG,e.getMessage());
                    }


                }//end if


            }

        });//end restroe





    }//end socketmanager



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if (id==android.R.id.home){
            super.onBackPressed();
            overridePendingTransition(R.anim.left_to_rigth, R.anim.left_to_rigth);

            return true;
        }

        return super.onOptionsItemSelected(item);

    }//end optionselect

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_rigth,R.anim.left_to_rigth);
    }//end backpress


    public  void sendcommand(String command){

        try {
            JSONObject obj=new JSONObject();
            obj.put(SmartHome_interface.PIN,Pincontrol);
            obj.put(SmartHome_interface.COMMAND,command);
            obj.put(SmartHome_interface.KEY_NAME,username);
            socket.emit(SmartHome_interface.SENDMESSAGE, obj);
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }

    }//end setcommand



}//end class
