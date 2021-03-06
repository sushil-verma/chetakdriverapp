package com.example.chetakdriverapp;

import java.io.BufferedReader;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

//import com.google.gson.Gson;

public class OrederReport extends Activity {

	private TextView custmer_name;
	private TextView address;
	private TextView mobile_no;
	private TextView transittime;
	private TextView transitdate;
	private TextView km;
	private TextView rate;
    private TextView eta;
	private  SharedPreferences orderrecord;
	/*private TextView  eta;
	private TextView booking_date;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	   /* String url ="http://121.241.125.91/cc/mavyn/online/ownerwiseupdate.php?msg=transitdetail&shipmentno="+1001;
		new GrabURL().execute(url);*/
		setContentView(R.layout.activity_order_detail_next);
		
		custmer_name=(TextView)findViewById(R.id.customer_name);
		address=(TextView)findViewById(R.id.customer_address);
		address.setMovementMethod(new ScrollingMovementMethod());
	    mobile_no=(TextView)findViewById(R.id.mobile);
	    transittime=(TextView)findViewById(R.id.ttime);
	    transitdate=(TextView)findViewById(R.id.tdate);
	    km=(TextView)findViewById(R.id.km);
	    rate=(TextView)findViewById(R.id.rate);
	   
	    eta=(TextView)findViewById(R.id.eta);
		
		ActionBar actionBar = getActionBar();
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		actionBar.setCustomView(R.layout.myswitch);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM| ActionBar.DISPLAY_SHOW_HOME); 
		
	    actionBar.setDisplayShowCustomEnabled(true);
	    actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.newback);
        orderrecord = getSharedPreferences("orderdetail", Context.MODE_PRIVATE);	
        
       /* Editor editor = orderrecord.edit();
	    editor.putString("c_name", jsondata.getCustomer_name());
	    editor.putString("c_address",jsondata.getAddress());
	    editor.putString("c_mobime", jsondata.getMobile());
	    editor.putString("ttime", jsondata.getTransittime());
	    editor.putString("tdate", jsondata.getTransitdate());
	    editor.putString("km", jsondata.getKm());
	    editor.putString("rate", jsondata.getRate());
	    editor.putString("trip", jsondata.getTip());
	    editor.commit();*/
	    
	    custmer_name.setText(orderrecord.getString("c_name", "null"));
	    address.setText(orderrecord.getString("c_address","null"));
	    mobile_no.setText(orderrecord.getString("c_mobime","null"));
	    transittime.setText(orderrecord.getString("ttime", "null"));
	    transitdate.setText(orderrecord.getString("tdate","null"));
	    km.setText(orderrecord.getString("km","0"));
	    rate.setText(orderrecord.getString("rate","null"));
	    eta.setText(orderrecord.getString("rate","null"));
        
		   
	    }
	
	/*private class GrabURL extends AsyncTask<String, Void, String> 
    {
    	  
    	
    	  private boolean error = false;
    	  private ProgressDialog dialog = new ProgressDialog(OrederReport.this);
    	 
    	  
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
    	   Toast toast = Toast.makeText(OrederReport.this,"Error connecting to Server", Toast.LENGTH_LONG);
    	   toast.setGravity(Gravity.TOP, 25, 400);
    	   toast.show();
    	   }

    	  protected void onPostExecute(String content) 
    	  {
    	   dialog.dismiss();
    	   Toast toast;
    	     if (error) 
    	      {
    	      toast = Toast.makeText(OrederReport.this,content, Toast.LENGTH_LONG);
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

    	  JSONObject responseObj = null; 
    	  Gson gson = new Gson();

    	  try {

    	   responseObj = new JSONObject(response); 
    	   JSONArray countryListObj = responseObj.getJSONArray("transit");
    	  
    	   for (int i=0; i<countryListObj.length(); i++)
    	   {

    	    String countryInfo = countryListObj.getJSONObject(i).toString();
    	    //create java object from the JSON object
    	    Transit_record jsondata = gson.fromJson(countryInfo, Transit_record.class);
    	
    	  //  custmer_name.setText(jsondata.getCustomer_name());
    	   // address.setText(jsondata.getAddress());
    	    mobile_no.setText(jsondata.getMobile());
    	    transittime.setText(jsondata.getTransittime());
    	    transitdate.setText(jsondata.getTransitdate());
    	    km.setText(jsondata.getKm());
    	    rate.setText(jsondata.getRate());
    	    tip.setText(jsondata.getTip());
    	    eta.setText(jsondata.getEta());
   
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
			return customer_name;
		}
		public String getAddress() {
			return address;
		}
		public String getMobile() {
			return mobile;
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
		public String getTip() {
			return tip;
		}
		public String getEta() {
			return eta;
		}
		
		 private String customer_name=null;   	
		 private String address=null;
    	 private String mobile=null;
    	 private String transittime=null;
    	 private String transitdate=null;
    	 private String km=null;
    	 private String rate=null;
    	 private String tip=null;
    	 private String eta=null;
    	
    } */
    	   
    
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
