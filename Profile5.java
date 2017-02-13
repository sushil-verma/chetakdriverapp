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

public class Profile5 extends Fragment
{
    Context context;
    
    TextView name;
  	TextView email;
  	TextView mobile;
  	ImageView driver_image;
  	
  	private String Imagelinkurl;
    private String Image_url_Received=null;
    boolean result=false;
	
     
    FileInputStream fis=null;
    private	Bitmap pics=null;
  
    Download_image asynk_task = new Download_image();
    Imageurl_download_thread imageurl_with_data;
    ImageView dfltpics;
    
    RoundImage roundedImage;

   public Profile5(Context c)
	{
	   
	   context=c;
	 

	}
	
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.myprofilecontent2, null);
        
         /*name=(TextView)root.findViewById(R.id.myprofilename);
         mobile=(TextView)root.findViewById(R.id.myprofilemobile);*/
        // driver_image=(ImageView)root.findViewById(R.id.driverimage);
         
       
 
         if(isOnline())
		 {    
	       download_img_url_with_data();
		 }  
	      
 	    else
 	    	
 	    	Toast.makeText(getActivity(),"No Interner Available..", Toast.LENGTH_SHORT).show();
   
        return root;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
       super.onActivityCreated(savedInstanceState);
    
      
    }
    
    public void download_img_url_with_data() 
   	{
   	          // progressbar.setVisibility(View.VISIBLE);
   		     
   		       TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
   		       Imagelinkurl="http://121.241.125.91/cc/Mobileimage?msg="+telephonyManager.getDeviceId();    //+"%23"+latitude+"%23"+longitude+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date())+"%23"+telephonyManager.getLine1Number();
   		       imageurl_with_data=new Imageurl_download_thread(Imagelinkurl);
   		       Thread t= new Thread(imageurl_with_data);
   		       t.start();
   		       try 
   		       {
   				t.join();
   			   } catch (InterruptedException e) {
   				
   				   //Toast.makeText(this,"Error in downloading",Toast.LENGTH_LONG).show();
   			  }
   		      
   		       Image_url_Received=imageurl_with_data.getValue(); 
   		
   		       asynk_task.execute(Image_url_Received);
        }
       
    
    
    
    class Imageurl_download_thread implements Runnable {
        private volatile String imageurl_received_local; 
        
        private volatile String  DriverName;
      
        private volatile String  Mobileno;
       
        public final String localurl;
        JSONObject jsonResponse;
       
        
        public String getMobileno() {
    		return Mobileno;
    	}
        
        public String getValue() {
   	       return imageurl_received_local;
   	     }

      
  	public String getDriverName() {
  		return DriverName;
  	}

   

      
        public Imageurl_download_thread(final String localString)
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
  						
  						 Toast.makeText(getActivity(),"Error in image Downloading",Toast.LENGTH_LONG).show();
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
  						imageurl_received_local=jsonChildNode.optString("imageurl").toString();
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
  	    			  Toast.makeText(getActivity()," Server Error ",Toast.LENGTH_LONG).show();
  	    		  }
  	        }

  	 
  	 }
    
    private class Download_image extends AsyncTask<String, Integer, Bitmap>{
        Bitmap bitmap = null;
      
        @Override
        protected void onPreExecute() {
        	
           /* progressbar.setProgress(0);
            progressbar.setMax(100);*/
            int progressbarstatus = 0;
        };
        
        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                bitmap = Hit_image(url[0]);
  
               }catch(Exception e){
                 Log.d("Background Task",e.toString());
              }
            
             return bitmap;
        }
 
        @Override
        protected void onPostExecute(Bitmap result) {
            
        /*	roundedImage = new RoundImage(result);
 
        	driver_image.setImageDrawable(roundedImage);*/
        	
            name.setText(imageurl_with_data.getDriverName());
		       
		     mobile.setText(imageurl_with_data.getMobileno());
            
   
        }
    
    }
    
  
    
    private Bitmap Hit_image(String strUrl) throws IOException{
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
    
    public boolean isOnline() 
    {
    	
	    ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	    
	  
    }
    

}
