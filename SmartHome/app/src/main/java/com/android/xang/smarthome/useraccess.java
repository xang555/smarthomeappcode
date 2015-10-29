package com.android.xang.smarthome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class useraccess extends AppCompatActivity {

    private ListView useraccess;
    private Socket socket;
    private ArrayAdapter<String> adap;
    //=========variable================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useraccess);
        useraccess=(ListView)findViewById(R.id.listuseraccess);
        socket=smarthome_control.socket;
        adap=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
            useraccess.setAdapter(adap);

    }//end oncreate

    @Override
    protected void onStart() {
        super.onStart();
        socket.on(SmartHome_interface.EXTRASENNAME, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONArray array = (JSONArray) args[0];
                for (int i = 0; i < array.length(); i++) {
                    try {

                        JSONObject object = array.getJSONObject(i);
                        adap.add(object.getString(SmartHome_interface.KEY_NAME));
                        adap.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(SmartHome_interface.EXTRASENNAME, "ERROR socket");
                    }


                }//end for

            }
        });

        socket.emit(SmartHome_interface.EXTRASENNAME, "xang");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.off(SmartHome_interface.EXTRASENNAME);

    }




}//end class
