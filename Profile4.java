package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Profile4 extends Fragment
{
    Context context;
    
    private Intent intent;
   
    TextView name;
  	TextView email;
  	TextView mobile;
  	ImageView driver_image;
  	
  	private String Imagelinkurl;
    private String ImageReceived=null;
    boolean result=false;
	
     
    FileInputStream fis=null;
    private	Bitmap pics=null;
    private DownloadTask downloadTask;
    
    private downloaddata imageurl;
  

   public Profile4(Context c)
	{
	   //URL url=new URL();
	   context=c;
	   downloadTask = new DownloadTask();

	}
	
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.myprofilecontent2, null);
        
      /*   name=(TextView)root.findViewById(R.id.myprofilename);
         mobile=(TextView)root.findViewById(R.id.myprofilemobile);*/
       //  driver_image=(ImageView)root.findViewById(R.id.driverimage);
         
 
 	    if(isOnline())
 		 {    
 	    	downloadTask.execute();// url for data of driver pending from dinesh
 		 } 
 	    
 	    else
 	    	
 	    	Toast.makeText(getActivity(),"No Interner Available..", Toast.LENGTH_SHORT).show();
   
        return root;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
       super.onActivityCreated(savedInstanceState);
       //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
      /* String url ="http://121.241.125.91/cc/mavyn/online/ownerwiseupdate.php?msg=profile";
       GrabURL Excecute=new GrabURL(context);
	   Excecute.execute(url);*/
       
       
       
       
      
    }
    
   /* public void downloadImage() 
	{
	          // progressbar.setVisibility(View.VISIBLE);
		      
		       TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		       Imagelinkurl="http://121.241.125.91/cc/Mobileimage?msg="+telephonyManager.getDeviceId();    //+"%23"+latitude+"%23"+longitude+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date())+"%23"+telephonyManager.getLine1Number();
		       imageurl=new downloaddata(Imagelinkurl);
		       Thread t= new Thread(imageurl);
		       t.start();
		       try 
		       {
				t.join();
			   } catch (InterruptedException e) {
				
				  // Toast.makeText(this,"Error in downloading",Toast.LENGTH_LONG).show();
			  }
		      
		       ImageReceived=imageurl.getValue(); 
		    		      
		       downloadTask.execute(ImageReceived);
     }*/
	
  
    public boolean isOnline() 
    {
    	
	    ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	    
	  
    }
    
   
    private class DownloadTask extends AsyncTask<Void, Void, Bitmap>{
        Bitmap bitmap = null;
        
        TelephonyManager telephonyManager=null;
	   
      
        @Override
        protected void onPreExecute() {
        	
        	telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            Imagelinkurl="http://121.241.125.91/cc/Mobileimage?msg="+telephonyManager.getDeviceId();  // this is url for the driver image
        };
        
        @Override
        protected Bitmap doInBackground(Void... params) {
            try{
               // bitmap = downloadUrl(url[0]);
            	
            	bitmap = downloadUrl(Imagelinkurl);
  
               }catch(Exception e){
                 Log.d("Background Task",e.toString());
              }
            
             return bitmap;
        }
 
        @Override
        protected void onPostExecute(Bitmap result) 
       {
            
        	driver_image.setImageBitmap(result);
        	
        	 downloaddata data=new downloaddata("http://121.241.125.91/cc/Mobileimage?msg="+telephonyManager.getDeviceId());
        	 Thread t= new Thread(data);
		       t.start();
		       try 
		       {
				t.join();
			   } catch (InterruptedException e) {
				
				  // Toast.makeText(this,"Error in downloading",Toast.LENGTH_LONG).show();
			  }
		       
		       
		      
		       
        }
    
    }
    
    private Bitmap downloadUrl(String strUrl) throws IOException{
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);
            
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
 
         
            urlConnection.connect();
 
            iStream = urlConnection.getInputStream();
 
            bitmap = BitmapFactory.decodeStream(iStream);
            
           // saveImageToInternalStorage(bitmap);
            
            Log.i("image saved","image savaed");
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
        }
       
        return bitmap;
    }
    
  
    
    class downloaddata implements Runnable {
       
        private volatile String  DriverName;
      
        private volatile String  Mobileno;
    
        public final String localurl;
       
        JSONObject jsonResponse;
       
        
        
      
  	public String getDriverName() 
  	{
  		return DriverName;
  	}
  	
 	
  	public String getMobile() 
  	{
  		return Mobileno;
  	}
  	
  
        public downloaddata(final String localString)
           {
      	      localurl=localString;
           }
  	   
           @Override
  	     public void run() {
  	    	 try {
  					URL myurl=new URL(localurl);
  					  HttpURLConnection connection_4_image_url=(HttpURLConnection)myurl.openConnection();
  					  if (connection_4_image_url.getResponseCode() != 200) {
  		                    throw new RuntimeException("HTTP error code : "+ connection_4_image_url.getResponseCode());
  		              }
  		         
  					 BufferedReader br = new BufferedReader(new InputStreamReader((connection_4_image_url.getInputStream())));
  					 String local=null;
  					try {
  						local = br.readLine();
  					 } catch (Exception e) {
  						
  						// Toast.makeText(getApplicationContext(),"Error in image Downloading",Toast.LENGTH_LONG).show();
  					 }
  						
  					 System.out.println("the recived url"+local);
  					 
  					 try {
  						 jsonResponse = new JSONObject(local);
  						
  						 JSONArray jsonMainNode=jsonResponse.optJSONArray("chetak");
  						
  						/*********** Process each JSON Node ************/
  						int lengthJsonArr = jsonMainNode.length();
  						for(int i=0;i<lengthJsonArr;i++)
  						{
  							/****** Get Object for each JSON node.***********/
  						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
  						
  						 DriverName=jsonChildNode.optString("DriverName").toString();
  						 Mobileno= jsonChildNode.optString("Mobileno").toString();
  						
  						
  						 
  						}
  					} catch (JSONException e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  						System.out.println("JSON Error");
  					} 
  					// imageurl_received_local=local;
  			       }
  	    		  catch(Exception e)
  	    		  {
  	    			 // Toast.makeText(getApplicationContext()," Server Error ",Toast.LENGTH_LONG).show();
  	    		  }
  	        }

  	    
  	 }   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
	
 
    private class GrabURL extends AsyncTask<String, Void, String> 
	{
	       Context localcontext;
	       private ProgressDialog dialog ;
	       private String local;
			      
		   public GrabURL(Context c)
		   {
			   localcontext=c;  
		   }
		
		  protected void onPreExecute() 
		  {
		   dialog = new ProgressDialog(localcontext);  
		   dialog.setMessage("Getting your data... Please wait...");
		   dialog.show();
		  }

		 
		  protected String doInBackground(String... urls) 
		  {

	        BufferedReader bufferinput =null;
		    HttpURLConnection con=null;
		   
		   try
		   {
			 
			  URL url = new URL(urls[0]);   
			  con = (HttpURLConnection) url.openConnection();
	          bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
	           try 
	           {
				  local = bufferinput.readLine();
				  System.out.println("json data"+local);
			    } 
	            catch (Exception e) 
	            {
				 
				     System.out.println("json data not fetched");
			    }
		         
	              Log.i("Json data received..",local);
		    }
		   
		   catch(IOException e)
		   {
			   e.printStackTrace();
			   Log.i("Erro", "data downloading erro");
			   
		   }
		
		   try 
		   {
			 bufferinput.close();
		   } 
		   catch (IOException e) 
		   {
			// TODO Auto-generated catch block
			  e.printStackTrace();
		    }
		     con.disconnect();
		   
		   return local;
	}

		  protected void onCancelled() 
		  {
		   dialog.dismiss();
		   Toast toast = Toast.makeText(localcontext,"Error connecting to Server", Toast.LENGTH_LONG);
		   toast.setGravity(Gravity.TOP, 25, 400);
		   toast.show();
		   }
	
		  protected void onPostExecute(String content) 
		  {
		  // dialog.dismiss();
		 
		       displayCountryList(content);
		     
		       dialog.dismiss();
		  }

	   }

	// closing AsyncTask

		 private void displayCountryList(String response){

		  JSONObject responseObj; 


		  try {

		 
		   responseObj = new JSONObject(response); 
		   JSONArray countryListObj = responseObj.optJSONArray("profiledata");  //.getJSONArray("transit");
	       
		   int length=countryListObj.length();
		   
		   System.out.println("json array length="+length);
		  
		   for (int i=0; i<length; i++)
		   {
			   
			JSONObject jsonChildNode=countryListObj.getJSONObject(i);
			name.setText(jsonChildNode.optString("name"));
		    email.setText(jsonChildNode.optString("email"));
			mobile.setText(jsonChildNode.optString("mobile"));
			
			
		    }

		  } 
		  catch (JSONException e) 
		  {
		   e.printStackTrace();
		   System.out.println("Error in reading json object");
		  }
	
		 }
*/
}
