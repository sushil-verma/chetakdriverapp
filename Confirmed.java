package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.google.gson.Gson;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Confirmed extends Fragment
{
   private Context context;
   private TextView shipment;
   private TextView customer_name;
   private TextView customer_no;
   private TextView PickUpAddress;
   private TextView destination;
   private TextView TT;
   private TextView DeliveryDate;
   private TextView distance;
   private TextView freightbill;
   private  SharedPreferences orderrecord;
   private SharedPreferences sharedPref;
   private String teststring=null;
   private static String shipment_value=null;
   private SharedPreferences Activity_log;
   private SharedPreferences Activity_log_time;
   private ListView stagelist;
   private MyStageAdapter stageadapter;
   
   private  ArrayList <Stage>  mydata=new ArrayList <Stage> (5);  

  
   public Confirmed(Context c)
	{
       context=c;
    }
   
 
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       mydata.removeAll(mydata);
       downloadstagedata();
   }
   
   
 
   
   private void downloadstagedata() {
     
	   
	   sharedPref = getActivity().getSharedPreferences("Orderfile", Context.MODE_PRIVATE); // from MyAlert.java
       
       shipment_value=sharedPref.getString("shipment", "000");
	   
	   DownloadStagedata runnable=new DownloadStagedata(shipment_value);
	   
	   Thread  tt=new Thread(runnable);
	   tt.start();
       
	   try 
	   {
		 tt.join();
  	   
	   } catch (InterruptedException e) 
  	   {
		// TODO Auto-generated catch block
		   e.printStackTrace();
	  }

   }
	
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_confirem_new_version, null);
  
     
        // saved data in OrderAccept.java
        orderrecord = getActivity().getSharedPreferences("orderdetail", Context.MODE_PRIVATE);	// from OrderAccept.java
      
        
        
        if(!shipment_value.equalsIgnoreCase(" "))
        {
        
        	 
            if(!shipment_value.equalsIgnoreCase("000"))
            {
            
    
        shipment=(TextView)root.findViewById(R.id.confirmeshipment);
        customer_name=(TextView)root.findViewById(R.id.customername);
        customer_no=(TextView)root.findViewById(R.id.no);
        PickUpAddress=(TextView)root.findViewById(R.id.pickupaddressconfirmed);
        destination=(TextView)root.findViewById(R.id.destinationconfirmed);
        TT=(TextView)root.findViewById(R.id.tdatetimeconfirmed);
        DeliveryDate=(TextView)root.findViewById(R.id.deliverydt);
        freightbill=(TextView)root.findViewById(R.id.Freight);
        shipment.setText("Shipment No :-"+shipment_value);
        
        stagelist=(ListView)root.findViewById(R.id.stages);
        stagelist.setAdapter(stageadapter);
        
       
              	
        }
            
            else
            	 root = (ViewGroup) inflater.inflate(R.layout.no_confirmed_booking, null);
       }
        
        
        else
        	 root = (ViewGroup) inflater.inflate(R.layout.no_confirmed_booking, null);
        
        
        
       
   
        return root;
    }
 
   
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
       super.onActivityCreated(savedInstanceState);
       

        final TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
       
        String url ="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+telephonyManager.getDeviceId()+"%23"+"accept";
	    new GrabURL().execute(url);
     
      
    }
    
    
    private class GrabURL extends AsyncTask<String, Void, String> 
    {
    	  
    	
    	  private boolean error = false;
    	  private Transit_record  temprary_record;
    	
    	  protected void onPreExecute() 
    	  {
    	 
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
				
		       Toast.makeText(getActivity(),"Error in time reading",Toast.LENGTH_LONG).show();
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
    	 
    	   }

    	  protected void onPostExecute(String content) 
    	  {
    
    	       displayjsondata(content);
    	     
    	   }

       }
	
	
	private void displayjsondata(String response){

  	  JSONObject responseObj = null; 
  	  Gson gson = new Gson();

  	  try {

  	   responseObj = new JSONObject(response); 
  	   JSONArray countryListObj = responseObj.getJSONArray("order");
  	  
  	   for (int i=0; i<countryListObj.length(); i++)
  	   {

  	    String countryInfo = countryListObj.getJSONObject(i).toString();
  	    //create java object from the JSON object
  	    Transit_record jsondata = gson.fromJson(countryInfo, Transit_record.class);
  	
  	    
  	    Editor editor = orderrecord.edit();
  	    editor.putString("c_name", jsondata.getCustomer_name());
  	    editor.putString("c_address",jsondata.getAddress());
  	    editor.putString("c_mobime", jsondata.getMobile());
  	    editor.putString("ttime", jsondata.getTransittime());
  	    editor.putString("tdate", jsondata.getTransitdate());
  	    editor.putString("km", jsondata.getKm());
  	    editor.putString("rate",jsondata.getRate());
  	    editor.putString("eta", jsondata.getEta());
  	    editor.commit();
  	    
  	    
  	  
      customer_name.setText(jsondata.getCustomer_name());
      customer_no.setText(jsondata.getMobile());
      PickUpAddress.setText(jsondata.getAddress());
      destination.setText(sharedPref.getString("destination","   "));
      DeliveryDate.setText(jsondata.getEta());
      TT.setText(jsondata.getTransitdate()+"#"+jsondata.getTransittime());
      freightbill.setText(jsondata.getRate());

 
  	   }

  	  } 
  	  catch (JSONException e) 
  	  {
  	   e.printStackTrace();
  	  }
  }
  	 
  	 
 
  class Transit_record
  {
  	
	 	
  	 public String getCustomer_name() {
			return customername;
		}
		public String getAddress() {
			return address;
		}
		public String getMobile() {
			return mobileno;
		}
		public String getTransittime() {
			return transittime;
		}
		public String getTransitdate() {
			return transitdate;
		}
		public String getKm() {
			return km;
		}
		public String getRate() {
			return rate;
		}
		
		public String getEta() {
			return eta;
		}
		
		 private String customername=null;   	
		 private String address=null;
  	     private String mobileno=null;
  	     private String transittime=null;
  	     private String transitdate=null;
  	     private String km=null;
  	     private String rate=null;
         private String eta=null;
  	
  }
  
  
  private class DownloadStagedata implements Runnable 
  {
  	  
  	
	  private String shipment_id=null;
  	  private boolean error = false;
  	  
  	public DownloadStagedata(String ss)
  	{
  		shipment_id=ss;
  	}
  	
  	
   @Override
  	 public void run() {

  	   String URL = null;
  	   BufferedReader bufferinput =null;
  	   String local=null;
  	   HttpURLConnection con=null;
  	   
  	   try
  	   {
  		    URL url = new URL("http://121.241.125.91/cc/mavyn/online/shipmenthistoryapp.php?msg=shipmenthistory&shipmentid="+shipment_id);   
            con = (HttpURLConnection) url.openConnection();
             bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
         
			try {
				 local = bufferinput.readLine();
				 Log.i("Json data received..",local);
			     } 
			 catch (Exception e) 
			  { 
				
		        Toast.makeText(getActivity(),"Error in time reading",Toast.LENGTH_LONG).show();
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

  	   
  	 JSONObject responseObj = null; 
 	
	  try 
	  {

	   responseObj = new JSONObject(local); 
	   JSONArray countryListObj = responseObj.getJSONArray("shipmenthistory");
	  
	   for (int i=0; i<countryListObj.length(); i++)
	   {

	  
		  JSONObject temperyobjent = countryListObj.getJSONObject(i);
		   
		   String headingmsg = temperyobjent.getString("stage_name").toString();
		   String timedate = temperyobjent.getString("date_and_time").toString();
	    
		    mydata.add(new Stage(headingmsg,timedate));
      }

	  } 
	  catch (JSONException e) 
	  {
	   e.printStackTrace();
	  }
	  
	  stageadapter=new MyStageAdapter(context,mydata);
	     
	   }
 
  }

 }
