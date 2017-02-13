package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class TransitDetailNext extends Activity {

	private TextView date_time;
	private TextView truck_type;

	private TextView time;
	private TextView from;
	private TextView to;
	private TextView driver_name;
	private TextView  mobile_no;
	private TextView  shipment_no;
	private TextView  eta;
	private TextView truckno;
	private TextView freightbill;
	
	private TextView podsubmitted;
	private TextView atdestination;
	private TextView tripstarted;
	private TextView documentsubmitted;
	private TextView atorigin;
	    
	    private  ImageView driver_image;
	 
	    private	Bitmap pics=null;
	    private FileInputStream fis=null;
	    
	    private ImageView imagepod;
	    private ImageView imagedestination;
	    private ImageView imagestarttrip;
	    private ImageView imagedoc;
	    private ImageView imageatpick;
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	   
		setContentView(R.layout.delivereddetail);
		date_time=(TextView)findViewById(R.id.ddship_date_time);
		truck_type=(TextView)findViewById(R.id.ddtruck_type);
		
		time=(TextView)findViewById(R.id.ddtravel_time);
		from=(TextView)findViewById(R.id.ddorigin);
		to=(TextView)findViewById(R.id.dddestination);
        shipment_no=(TextView)findViewById(R.id.ddshipment_no);
		eta=(TextView)findViewById(R.id.ddeta);
		truckno=(TextView)findViewById(R.id.ddtruck_no);
		freightbill=(TextView)findViewById(R.id.ddfreight_bill);
		
		podsubmitted=(TextView)findViewById(R.id.pod_submitted_deleivered);
		atdestination=(TextView)findViewById(R.id.reached_destination_delivered);
		tripstarted=(TextView)findViewById(R.id.mystart_trip_delivered);
		documentsubmitted=(TextView)findViewById(R.id.collectgr_document_delivered);
		atorigin=(TextView)findViewById(R.id.at_origin_delivered);
		
		imagepod=(ImageView)findViewById(R.id.imagepod);
		imagedestination=(ImageView)findViewById(R.id.imagedestination);
		imagestarttrip=(ImageView)findViewById(R.id.imagestarttrip);
		imagedoc=(ImageView)findViewById(R.id.imagedoc);
		imageatpick=(ImageView)findViewById(R.id.imageatorigin);
		
		
		try {
			fis = openFileInput("desiredFilename1.png");
		    } 
		catch (FileNotFoundException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(fis!=null)
		{
        
		  pics=BitmapFactory.decodeStream(fis);
         try {
  			   fis.close();
  		     } 
         
         catch (IOException e) 
         {
  			
  			e.printStackTrace();
  		  }
         
		}
		
		
		
		imagepod.setImageBitmap(pics);
		imagedestination.setImageBitmap(pics);
		imagestarttrip.setImageBitmap(pics);
		imagedoc.setImageBitmap(pics);
		imageatpick.setImageBitmap(pics);
	
		
		ActionBar actionBar = getActionBar();
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
		actionBar.setCustomView(R.layout.deliverdetailheading);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
		        | ActionBar.DISPLAY_SHOW_HOME); 
		
	   actionBar.setDisplayShowCustomEnabled(true);
	   actionBar.setDisplayHomeAsUpEnabled(false);
	   actionBar.setHomeButtonEnabled(true);
	   getActionBar().setDisplayShowHomeEnabled(true);
       actionBar.setIcon(R.drawable.newback);
   
           
           final  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
           
           String shipmentid=getIntent().getStringExtra("shipmentid");
           String url ="http://121.241.125.91/cc/mavyn/online/driverwiseupdate.php?msg=delivereddatadetail&shipmentid="+shipmentid+"&imeino="+telephonyManager.getDeviceId();
   		   new GrabURL().execute(url);
		   
	    }
	
	private class GrabURL extends AsyncTask<String, Void, String> 
    {
    	  private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    	  private static final int WAIT_TIMEOUT = 30 * 1000;
    	  private final HttpURLConnection httpconnection = null;
    	  private String content =  null;
    	  private boolean error = false;
    	  private ProgressDialog dialog = new ProgressDialog(TransitDetailNext.this);
    	  private int resCode = -1;
    	  
    	  private Transit_record  temprary_record;
    	
    	  protected void onPreExecute() {
    	   dialog.setMessage("Getting your data... Please wait...");
    	   dialog.show();
    	  }

    	 
    	  protected String doInBackground(String... urls) {

    	   String URL = null;
    	   BufferedReader bufferinput =null;
    	   String local=null;
    	   HttpURLConnection con=null;
    	   
    	   try
    	   {
    		  URL url = new URL(urls[0]);   
              con = (HttpURLConnection) url.openConnection();
              JSONObject jsonResponse; 
              System.out.println("data uploades");
              bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
           
			try {
				local = bufferinput.readLine();
				 Log.i("Json data received..",local);
			     } 
			 catch (Exception e) 
			  { 
				
		       Toast.makeText(getApplicationContext(),"Error in time reading",Toast.LENGTH_LONG).show();
			  }

    	   }
    	   catch(Exception e)
    	   {
    		   
    	   }
    	
    	   try 
    	   {
			bufferinput.close();
			con.disconnect();
		   } 
    	   
    	   catch (IOException e) 
    	   {
		    e.printStackTrace();
			System.out.println("Reading Buffer closing error");
		   }
  
    	return local; 
   }

    	  protected void onCancelled() 
    	  {
    	   dialog.dismiss();
    	   Toast toast = Toast.makeText(TransitDetailNext.this,"Error connecting to Server", Toast.LENGTH_LONG);
    	   toast.setGravity(Gravity.TOP, 25, 400);
    	   toast.show();
    	   }

    	  protected void onPostExecute(String content) 
    	  {
    	   dialog.dismiss();
    	   Toast toast;
    	     if (error) 
    	      {
    	      toast = Toast.makeText(TransitDetailNext.this,content, Toast.LENGTH_LONG);
    	      toast.setGravity(Gravity.TOP, 25, 400);
    	      toast.show();
    	      
    	      }
    	      else 
    	     {
    	       displayjsondata(content);
    	     }
    	   }

       }

    	 private void displayjsondata(String response){

    		 JSONObject responseObj; 
    		 
    		 JSONObject shipmenthistory; 
    		 
    		 


    	  try {
    		  
    		  
    	   responseObj = new JSONObject(response); 
    	   JSONArray countryListObj = responseObj.getJSONArray("delivereddatadetail");
    	  
    	   for (int i=0; i<countryListObj.length(); i++)
    	   {

    		   JSONObject jsonChildNode=countryListObj.getJSONObject(i);
    	    
    		   
    		    String  shipmentNo=jsonChildNode.optString("shipmentno").toString();
    			String  date_time1=jsonChildNode.optString("date_time");
    			
    			String  truck_type1=jsonChildNode.optString("truck_type");
    			String truck_no=jsonChildNode.optString("truckno");
    		    
    			String  from1=jsonChildNode.optString("from");
    			String  to1= jsonChildNode.optString("to");
    			
    		    
    		    String time1=jsonChildNode.optString("time");
    		    String distance1=jsonChildNode.optString("distance");
    		    
    		    String eta1=jsonChildNode.optString("ETA");
    		    String bill1=jsonChildNode.optString("freightbill");
    		    
    		    String atorg=jsonChildNode.optString("freightbill");
    		    String cltdocument=jsonChildNode.optString("freightbill");
    		    String stattrip=jsonChildNode.optString("freightbill");
    		    String rchddestination=jsonChildNode.optString("freightbill");
    		    String podsbmitd=jsonChildNode.optString("freightbill");
    		   
    		   
    	
    	    date_time.setText(date_time1);
    	    
    	    truck_type.setText(truck_type1);
    		
    	  
    		
    	    time.setText(time1);
    		
    	    from.setText(from1);
    		
    	    to.setText(to1);
    	  
    		
    	    shipment_no.setText(shipmentNo);
    		
    	    eta.setText(eta1);
    	    
    	    truckno.setText(truck_no);
    	    
    	    freightbill.setText(bill1);
    	
    	   }

    	  } 
    	  catch (JSONException e) 
    	  {
    	   e.printStackTrace();
    	  }
    	  
    	
    	  
    	  try {
    		  
    		  
    	   shipmenthistory = new JSONObject(response); 
       	   JSONArray countryListObj = shipmenthistory.getJSONArray("shipmenthistory");
       	  
       	   for (int i=0; i<countryListObj.length(); i++)
       	   {

       		   JSONObject jsonChildNode=countryListObj.getJSONObject(i);
       	    
       		   
       		    String  stage=jsonChildNode.optString("stage_name").toString();
       			String  date_time=jsonChildNode.optString("date_and_time");
       			
       			
       		   
       		   if(i==0)
       	
       			podsubmitted.setText(date_time);
       	    
       	      
       		 
       		   if(i==1)
       	
       			atdestination.setText(date_time);
       	    
       		   
       		 
       		   if(i==2)
       	
       			tripstarted.setText(date_time);
       	    
       		   
       		 
       		   if(i==3)
       	
       			documentsubmitted.setText(date_time);
       	    
       		   
       		 
       		   if(i==4)
       	
       			atorigin.setText(date_time);
       	    
       		
       	     
       	
       	   }

       	  } 
       	  catch (JSONException e) 
       	  {
       	   e.printStackTrace();
       	  }
    	  
    	  
    	  
    	  
    }
    	 
    	 
   
    class Transit_record
    {
    	 public String getShipmentno() {
			return shipmentno;
		}
		public String getDate_time() {
			return date_time;
		}
		public String getTruckNo() {
			return truckno;
		}
		public String getTruck_type() {
			return truck_type;
		}
		public String getFrom() {
			return from;
		}
		public String getTo() {
			return to;
		}
		public String getETA() {
			return ETA;
		}
		public String getDistance() {
			return distance;
		}
		public String getTime() {
			return time;
		}
		
		public String getfreightbill() {
			return freightbill;
		}
		
	
	   	
    	 
    	 public String getTruckno() {
			return truckno;
		}
		public String getFreightbill() {
			return freightbill;
		}
		
		
		 private String shipmentno=null;   	
    	 private String date_time=null;
    	 private String truckno=null;
    	 private String truck_type=null;
    	 private String from=null;
    	 private String to=null;
    	 private String distance=null;
    	 private String time=null;
    	 private String ETA=null;
    	 private String freightbill=null;
    	
    	
    } 
    	   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                   // Log.i(TAG, "You have forgotten to specify the parentActivityName in the AndroidManifest!");
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
