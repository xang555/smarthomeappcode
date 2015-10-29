package com.android.xang.smarthome;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

/**
 * Created by xang- on 22/10/2015.
 */
public class SmarthomeAdapter extends RecyclerView.Adapter<SmarthomeAdapter.holder> {

    private static String name;
    private static Context context;
    private static String[] labletitle;
    private String stat=null;
    private int[] resid;
    public static final String KEYNAME="EXTRATITLE";
    public static final String KEYPIN="PIN";
    public static final String KEYSOCKET="socket";
    public static final String KEYWHONAME="name";

    //==================variable=====================//

    public SmarthomeAdapter(Context context,String[] lable,int[] resid,String name){
        this.context=context;
        this.labletitle=lable;
        this.resid=resid;
        this.name=name;
            }//end init method


    @Override
    public holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.smatehomeitem,parent,false);
        holder vh=new holder(view);

        return vh;
    }//end oncreatebind

    @Override
    public void onBindViewHolder(holder holder, int position) {

        holder.lable.setText(labletitle[position]);
        holder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), resid[position]));

    }//end onbind

    @Override
    public int getItemCount() {
        return labletitle.length;

    }//end itemcount

    public static class holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView lable=null;
        ImageView imageView=null;

        public holder(View itemView) {
          super(itemView);
        itemView.setOnClickListener(this);
        lable=(TextView)itemView.findViewById(R.id.lableitem);
        imageView=(ImageView)itemView.findViewById(R.id.imageitem);

        }//end holder contru

        @Override
        public void onClick(View v) {

            int id=getAdapterPosition();

            Intent intent=new Intent(context.getApplicationContext(),controlroom.class);
            intent.putExtra(KEYNAME,labletitle[id]);
            intent.putExtra(KEYWHONAME,name);
            if (id==0){
                Intent i=new Intent(context.getApplicationContext(),bedcontroller.class);
                i.putExtra(KEYNAME,labletitle[id]);
                i.putExtra(KEYWHONAME,name);

                context.startActivity(i);

            }else if (id==1){
                intent.putExtra(KEYPIN,SmartHome_interface.PINKITCHEN);
                context.startActivity(intent);

            }else if (id==2){
                intent.putExtra(KEYPIN,SmartHome_interface.PINCAR);
                context.startActivity(intent);
            }else if (id==3){
                intent.putExtra(KEYPIN,SmartHome_interface.PINSTORE);
                context.startActivity(intent);

            }else if (id==4){
                intent.putExtra(KEYPIN,SmartHome_interface.PINLIVING);
                context.startActivity(intent);
            }else if (id==5){

                intent.putExtra(KEYPIN,SmartHome_interface.PINDOOR);
                context.startActivity(intent);

            }else  if (id==6){
                Intent i=new Intent(context.getApplicationContext(),toiletcontroller.class);
                i.putExtra(KEYNAME,labletitle[id]);
                i.putExtra(KEYWHONAME,name);
                context.startActivity(i);

            }else if (id==7){
                Intent i=new Intent(context.getApplicationContext(),waycontroller.class);
                i.putExtra(KEYNAME,labletitle[id]);
                i.putExtra(KEYWHONAME,name);
                context.startActivity(i);
            }


        }//end onclick


    }//end class holder


}//end class adapter
