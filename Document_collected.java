package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.chetakdriverapp.ToPickUpPointMap.hitmessage;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Document_collected extends Activity implements OnClickListener{
	
	private Button confirm;
	private Button request_document;
	private Intent starttrip;
	
	private int Mygr=0;
	private int MyInvoice=0;
	private int MyStateTo=0;
	
	private LinearLayout firstbox;
	private LinearLayout secondbox;
	private LinearLayout thirdbox;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gr_detail);
	    
		writestateToInternalStorage();
	    
		confirm=(Button)findViewById(R.id.confirmed);
		confirm.setVisibility(View.INVISIBLE);
		confirm.setOnClickListener(this);
		
		request_document=(Button)findViewById(R.id.request);
		request_document.setOnClickListener(this);
	
		starttrip=new Intent(this,AtPickUpPointActivity.class);
		firstbox=(LinearLayout)findViewById(R.id.first);
	    firstbox.setVisibility(View.INVISIBLE);
		
		secondbox=(LinearLayout)findViewById(R.id.second);
		secondbox.setVisibility(View.INVISIBLE);
		
		thirdbox=(LinearLayout)findViewById(R.id.third);
		thirdbox.setVisibility(View.INVISIBLE);
	    
		
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		
		
		case R.id.request:
	        	                 {
	        	                	 
	        	                	// hitserver();
	        	                	 
	        	                	mycallable hitting=new mycallable();
	        	         	    	Thread localthread=new Thread(hitting);
	        	         	    	localthread.start();
	        	         	    	try 
	        	         	    	{
	        	         				localthread.join();
	        	         			} 
	        	         	    	catch (InterruptedException e) 
	        	         	    	{
	        	         				// TODO Auto-generated catch block
	        	         				e.printStackTrace();
	        	         			 }
	        	         	    	
	        	         	    	
	        	         	     	
	        	         		    	Mygr=hitting.getGr_ok();
	        	         		    	MyInvoice=hitting.getInvoice_ok();
	        	         		    	MyStateTo=hitting.getStateTo_ok();
	        	         	    
	        	         		    	
	        	         		    	if(Mygr==1)
	        	         		    	firstbox.setVisibility(View.VISIBLE);	
	        	         		    	
	        	         		    	if(MyInvoice==1)
	        	         		        secondbox.setVisibility(View.VISIBLE);	
		        	         		    		
	        	         		    	
	        	         		    	
	        	         		    	if(MyStateTo==1)
	        	         		    	thirdbox.setVisibility(View.VISIBLE);	
		        	         		    		
	        	         		    	if(Mygr==1&&MyInvoice==1&&MyStateTo==1)
	        	         		    	{
	        	         		    	 confirm.setVisibility(View.VISIBLE);
	        	         		    	 request_document.setVisibility(View.INVISIBLE);
	        	         		    	}
	       			                    break;
			
			
		                         }
	        	                 
		
		case R.id.confirmed:
			
		                       {
		                    	   
		                    	 
			
		                    		try 
		                    		 {
										startActivity(starttrip);
									  
		                    		  } 
		                    		
		                    		 catch (Exception e) 
		                    		  {
									 	// TODO Auto-generated catch block
										Toast.makeText(this,"Data not available",Toast.LENGTH_SHORT).show();
									  }
		                    		
		                 		    hitserver();
		                    		
		                    		finish();
				                	
		 			
		 			                 break;
			
			
		                         }
		
		
	
		
		}
	
		
	}
	
	
	
	private  class mycallable implements Runnable
	{
		private volatile String IMEI_NO;
		private volatile int  Gr_ok=0;
	    private volatile int  Invoice_ok=0;
	    private volatile int  StateTo_ok=0;
	    private final TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	   
		
		public int getGr_ok() {
			return Gr_ok;
		}



		public int getInvoice_ok() {
			return Invoice_ok;
		}



		public int getStateTo_ok() {
			return StateTo_ok;
		}


		
	   public mycallable()
		{
		   
		  IMEI_NO=telephonyManager.getDeviceId();	
		}
		
	 
	  
		public void run()
		{
			
			try {
				
				   Log.i("ready to hit the server", " ....");
				 
				  // final String request="http://192.168.0.204/online/mobilelocation.php?msg="+ht.IMEI_NO+"%23"+"ok";
				   final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+IMEI_NO+"%23"+"request_document";	
				        JSONObject jsonResponse;    
				        Log.d("request for second page", ""+request);
			        	
			        	URL url = new URL(request);
			            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			          
			            System.out.println("data uploades for second page");
			            
			            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			            String local=null;
						try 
						{
							local = br.readLine();
							 Log.i("Json data received..longi,lati,adddress",local);
						 } 
						
						catch (Exception e) 
						{ 
							
							 
						}
							
						
						
						 try {
							 jsonResponse = new JSONObject(local);
							
							 JSONArray jsonMainNode=jsonResponse.optJSONArray("document");
							
							
							int lengthJsonArr=0;
							
							try {
								  lengthJsonArr = jsonMainNode.length();
							    } 
							
							 catch (Exception e1) 
							  {
								// TODO Auto-generated catch block
								   e1.printStackTrace();
							   }
							
							for(int i=0;i<lengthJsonArr;i++)
							{
								
							 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							
							 try {
								 Invoice_ok=Integer.parseInt(jsonChildNode.optString("invoice").toString());
								
							     } catch (Exception e) 
							     {
								// TODO Auto-generated catch block
								    e.printStackTrace();
							     }
							 
							 try {
								 Gr_ok=Integer.parseInt(jsonChildNode.optString("gr").toString());
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							 
							 
							 try {
								 StateTo_ok=Integer.parseInt(jsonChildNode.optString("statefrom").toString());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							 
							 
							}
							
							 
						 }
						 catch(JSONException js)
						 {
							 js.printStackTrace();
						 }
			              
			           br.close();
			           con.disconnect();
			           
				
		
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		}
	}
	
	
	private void writestateToInternalStorage() 
	{
		   byte b=10;
	        try {
	       
	        FileOutputStream fos = openFileOutput("status.txt", Context.MODE_PRIVATE);
	   
	        
	        fos.write(b);
	        //image.compress(Bitmap.CompressFormat.PNG, 100, fos);
	      
	        fos.close();
	        
	            } 
	        catch (Exception e) 
	        {
	        Log.e("Error_In_status_saveToInternalStorage_view4", e.getMessage());
	      
	        }
	 }
	
	
	 private void hitserver()
		{
			SharedPreferences sharedPref =getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
			String retrievedshipmentno = sharedPref.getString("shipmentno", "000");
			String retrievedimeino = sharedPref.getString("imeino", "000");
			hitmessage hit=new hitmessage(retrievedimeino,retrievedshipmentno);
			
			Thread t=new Thread(hit);
			t.start();
			
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("document submitted hit");
			
		}
	
	
	private class hitmessage implements Runnable
	{

		private URL url=null;
		private HttpURLConnection con=null ;
		private String imei_no;
		private String shipment_no;
		
		
		
		 hitmessage(String imei, String shipmentno)
		{
			imei_no=imei;
			shipment_no=shipmentno;
		}
		@Override
		public void run() 
		{
			
			
	        final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+"documentcollected"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
			try {
				  url = new URL(request);
				  con = (HttpURLConnection) url.openConnection();
				  if(con.getResponseMessage()!=null)
				  System.out.println("IMEI_NO+SHIPMENT_NO document collected time submitted SUCCESFULLY");
				  else
			      System.out.println("Error in IMEI_NO+SHIPMENT_NO ");
		         
			    }
		          
			    catch (Exception e) 
		        {
					  // TODO Auto-generated catch block
				   System.out.println("Error in IMEI_NO+SHIPMENT_NO ");
				}
			         
			} 
	
    }
	   
}
