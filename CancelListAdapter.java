package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CancelListAdapter extends BaseAdapter {
	
    	
	Context context;
		//ArrayList<Transit_record> s=new ArrayList<Transit_record>(10);
	private  ImageView driver_image;
	  
    private FileInputStream fis=null;
    private	Bitmap pics=null;
 
    private FileInputStream fos=null;
    
	private ArrayList<CancelTransit_record> transit=new ArrayList<CancelTransit_record>(10);
		
		public CancelListAdapter(Context con ,ArrayList<CancelTransit_record> list)
    	{
    		context=con;
    		transit=list;
    		
    		try {
    			fos = context.openFileInput("desiredFilename1.png");
    		    } 
    		catch (FileNotFoundException e1) 
    		{
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		
    		if(fos!=null)
    		{
            
    		  pics=BitmapFactory.decodeStream(fos);
             try {
      			   fos.close();
      		     } 
             
             catch (IOException e) 
             {
      			
      			e.printStackTrace();
      		  }
             
            // driver_image.setImageBitmap(pics);
             
    		}
    	}
    	
    	@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return transit.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return transit.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View custome_layout=convertView;
			CancelTransit_record record_holder=transit.get(position);

			if(convertView==null)
			{
		     LayoutInflater  Myinflate= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
		     custome_layout=(LinearLayout)Myinflate.inflate(R.layout.shipment_canceal, null);
			}
			

			driver_image=(ImageView)custome_layout.findViewById(R.id.vehicle_image_cancel);
			
			driver_image.setImageBitmap(pics);
			
			
			TextView myshipment=(TextView)custome_layout.findViewById(R.id.shipmentno1);
			myshipment.setText(String.valueOf(record_holder.getshipmentNo()));
			
			TextView vehicleno=(TextView)custome_layout.findViewById(R.id.cancel_vehicle_no);
			vehicleno.setText(String.valueOf(record_holder.getvehicleno()));
			
			
			TextView date_time=(TextView)custome_layout.findViewById(R.id.shipment_cancel_dt);
			date_time.setText(record_holder.getDate_time());
			
			TextView truck_type=(TextView)custome_layout.findViewById(R.id.vehicleno);
			truck_type.setText(record_holder.getTruck_type());
			
	        TextView from=(TextView)custome_layout.findViewById(R.id.fromshipment);
			from.setText(record_holder.getFrom());
		    TextView to=(TextView)custome_layout.findViewById(R.id.toshipment);
			to.setText(record_holder.getTo());
			
			TextView remarks=(TextView)custome_layout.findViewById(R.id.remarks);
			remarks.setText(record_holder.getremarks());
			return custome_layout;
		
		}
    	
   
  
}

class CancelTransit_record
{
	
	String date_time=null;
	String truck_type=null;
	String to=null;
	String from=null;
    int shipmentNo=0;
    String remarks=null;
    String vehicleno=null;
	
     public CancelTransit_record(String date_time,String truck_type,String to,String from,int shipment,String remarks,String vehicleno)
	{
		this.date_time=date_time;
		this.truck_type=truck_type;
		this.to=to;
		this.from=from;
		shipmentNo=shipment;
		this.remarks=remarks;
		this.vehicleno=vehicleno;
	}
     
     
     public String getvehicleno() {
 		return vehicleno;
 	}
	
	public String getDate_time() {
		return date_time;
	}
	public String getTruck_type() {
		return truck_type;
	}
	public String getTo() {
		return to;
	}
	public String getFrom() {
		return from;
	}
	
	public int getshipmentNo() {
		return shipmentNo;
	}
	
    
	public String getremarks() {
		return remarks;
	}

}
