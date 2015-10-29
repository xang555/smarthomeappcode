package com.android.xang.smarthome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class smarthome_control extends AppCompatActivity {

   public static Socket socket;
    {
        try {
            socket= IO.socket(SmartHome_interface.Urlsocket);
        } catch (URISyntaxException e) {
            Log.e("Socket","Create Socket");
        }
    }
    private  SmarthomeAdapter adp;
    private TextView stat;
    private String username=null;
    private RecyclerView smarthomelist;
    private String[] lable={"ຫ້ອງນອນ","ເຮືອນຄົວ","ຫ້ອງໄວ້ລົດ","ສາງເຄື່ອງ","ຫ້ອງຮັບແຂກ","ປະຕູເຮືອນ","ຫ້ອງນໍ້າ","ທາງຍ່າງ"};
    private int[] resid={R.drawable.bedroom,R.drawable.cookroom,R.drawable.caroom,R.drawable.storeroom,R.drawable.liveingroom,R.drawable.door,R.drawable.toilet,R.drawable.way};
    //==========================variable==============================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smarthome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        smarthomelist=(RecyclerView)findViewById(R.id.smarthomelist);

        toolbar.setLogo(R.drawable.applogo);
        toolbar.setLogoDescription("NoulMaker");
        setSupportActionBar(toolbar);
        //get intent
        Intent intent=getIntent();
        username=intent.getStringExtra(login_controller.KEYUSERNAME);
         adp=new SmarthomeAdapter(this,lable,resid,username);
        smarthomelist.setLayoutManager(new GridLayoutManager(this,2));
        smarthomelist.setHasFixedSize(false);
        smarthomelist.setAdapter(adp);



        super.onResume();
        if (!socket.connected()){
            socket.connect();
        }

        try {
            JSONObject object=new JSONObject();
            object.put("name",username);
            socket.emit(SmartHome_interface.SENNAME,object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }//end oncreate


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();

    }//end resume


    @Override
    public void onBackPressed() {
       setResult(RESULT_CANCELED);
        super.onBackPressed();
    }


    public void restorestat(){

        socket.on(SmartHome_interface.EVEN_RESTT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            try{
                JSONArray array=(JSONArray)args[0];
                for (int i=0;i<array.length();i++){
                    JSONObject object=array.getJSONObject(i);

                }//end for

                adp.notifyDataSetChanged();

            }catch (JSONException e){
                Log.e("JSNERROR","error in jsonarray");
            }


            }

        });//end event restore

    }//end restorestst

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_smarthome_control,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.about){
            Intent intent=new Intent(smarthome_control.this,aboutapp.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}//end class
