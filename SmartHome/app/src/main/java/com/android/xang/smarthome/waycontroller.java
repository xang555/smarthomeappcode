package com.android.xang.smarthome;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class waycontroller extends AppCompatActivity {

    private String[] list={"ໄຟດອກທີ່ 1 ","ໄຟດອກທີ່ 2 "};
    private ListView listcontrol=null;
    private Socket socket;
    private String username=null;
    private ArrayList<Boolean> statlist=new ArrayList<Boolean>();

    //======================variable==========================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controlbedroom);

        overridePendingTransition(R.anim.bottom_to_top, R.anim.bottom_to_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listcontrol=(ListView)findViewById(R.id.list);

        Intent intent=getIntent();
        username=intent.getStringExtra(SmarthomeAdapter.KEYWHONAME);
        getSupportActionBar().setTitle(intent.getExtras().getString(SmarthomeAdapter.KEYNAME));
        socket=smarthome_control.socket;
        final adapter adp=new adapter();


        initsocket(); //init socket
        onrestore();
        for (int i=0;i<SmartHome_interface.PINWAY.length;i++){
            onsendcommand(SmartHome_interface.PINWAY[i]);
            reqsocket(SmartHome_interface.PINWAY[i]);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listcontrol.setAdapter(adp);
            }
        }, 2000);

    }//end oncreate

    public void switcher(int pin,boolean ischeck){

        if(ischeck){

            sendcommand(SmartHome_interface.COMMANDON,pin);

        }else {
            sendcommand(SmartHome_interface.COMMANDOFF, pin);

        }//end if else

    }//end switcher


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
            overridePendingTransition(R.anim.top_to_bottom,R.anim.top_to_bottom);
        }//end if

        return super.onOptionsItemSelected(item);
    }//end onoption

    public class adapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public Object getItem(int position) {
            return list[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v= LayoutInflater.from(waycontroller.this).inflate(R.layout.item_listview,parent,false);
            TextView lable=(TextView)v.findViewById(R.id.itemlablelist);
            final Switch aSwitch=(Switch)v.findViewById(R.id.ligthswith);


            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (aSwitch.isChecked()){
                        sendcommand(SmartHome_interface.COMMANDON,SmartHome_interface.PINWAY[position]);
                    }else{
                        sendcommand(SmartHome_interface.COMMANDOFF,SmartHome_interface.PINWAY[position]);
                    }

                }
            });

            try{
                aSwitch.setChecked(statlist.get(position));
            }catch (Exception e){
                Toast.makeText(waycontroller.this,"ກະລຸນາລອງໄຫມ",Toast.LENGTH_SHORT).show();
            }

            lable.setText(list[position]);
            return v;

        }//end getview

    }//end adapter class


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_to_bottom,R.anim.top_to_bottom);
    }//end back

    public void initsocket(){
        try{
            if (!socket.connected()){
                socket.connect();
            }

        }catch (Exception e){
            Log.e("ERROR", e.getMessage());
        }

    }//end method initsocket

    public  void sendcommand(String command,int Pincontrol){

        try {

            JSONObject obj=new JSONObject();
            obj.put(SmartHome_interface.PIN,Pincontrol);
            obj.put(SmartHome_interface.COMMAND, command);
            obj.put(SmartHome_interface.KEY_NAME,username);
            socket.emit(SmartHome_interface.SENDMESSAGE, obj);
        } catch (JSONException e) {
            Log.e("socket error", e.getMessage());
        }

    }//end setcommand


    private void onsendcommand(final int Pincontrol){


        socket.on(SmartHome_interface.SENDMESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                try {
                    JSONObject object=(JSONObject)args[0];
                    if(object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDON) && object.getInt(SmartHome_interface.PIN)==Pincontrol){

                       waycontroller.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(waycontroller.this, username + "ເປີດໄຟ", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else if (object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDOFF)&& object.getInt(SmartHome_interface.PIN)==Pincontrol){

                        waycontroller.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(waycontroller.this,username+"ປິດໄຟ",Toast.LENGTH_SHORT).show();

                            }

                        });

                    }//end if else


                } catch (JSONException e) {
                    Log.e("error in json",e.getMessage());
                }

            }//end call

        });//end on sendmessge


    }//end manager socket

    private void onrestore(){


        socket.on(SmartHome_interface.EVENT_RESTORE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object=(JSONObject)args[0];
                try{

                    if (object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDON)){
                        statlist.add(true);

                    }else if (object.getString(SmartHome_interface.COMMAND).equals(SmartHome_interface.COMMANDOFF)){

                        statlist.add(false);

                    }//end if else


                }catch (JSONException e){
                    e.printStackTrace();
                }



            }//end call

        });


    }//end onrestore

    public void reqsocket(int pincontrol){

        JSONObject obj=new JSONObject();
        try {
            obj.put(SmartHome_interface.PIN,pincontrol);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(SmartHome_interface.EVENT_RESTORE,obj);

    }//end reqsocket





}
