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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// The splash Screen reads the value from the file and launches the activity that is last stopped by mistake
public class SplasScreen extends Activity {

	private final int SPLASH_DISPLAY_LENGTH = 1000;
	private Intent first_activity;
	private Intent second_activity;// not used
	private Intent third_activity;
	private Intent forth_activity;
	private Intent fifth_activity;
	private Intent sixth_activity;
	private Intent seventh_activity;
	private Intent eighth_activity;
	private Intent ninth_activity;
	private Intent tenth_activity;
	int status_value=0;
	private  Intent myservice;
	private boolean isGPSEnabled=true;
	private  Hit_image hitimage;
	private ProgressBar myprogressbar;
	
	   /* this is for writing 0 to the file for the first time when app is stopped between to journey
	    and it is open after a long time . so need to write o the the Rom to start the app
	    from startin*/
	
	  
	  	private String Imagelinkurl=null;
	    private String Image_url_Received=null;
	    private String previous_url_Received=null;
	    private String saved_url =null;
	    boolean result=false;
		private FileInputStream fis=null;
	    private	Bitmap pics=null;
	    private ImageView dfltpics;
	    private static volatile int x=1;
	    private   Imageurl_download_thread imageurl_with_data;
	    private SharedPreferences driver_data;
	    private SharedPreferences.Editor editor;
	    private static volatile boolean profile_pics_changed=false;
	    
	   
    	
	 @Override
	public void onPause()
	{
	   super.onPause();
	   //finish();
	}
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	
		/*ActionBar action=getActionBar();
		action.hide();
		*/
		
		driver_data=getSharedPreferences("driverdate",MODE_PRIVATE);
		
	    editor=driver_data.edit();
		
	    first_activity = new Intent(this,FirstActivity.class);
		
	    myprogressbar=(ProgressBar)findViewById(R.id.progressbar);
	    myprogressbar.setVisibility(View.VISIBLE);
		
	    third_activity = new Intent(this,ToPickUpPoint.class);
		third_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		forth_activity = new Intent(this,AtPickUpPointActivity.class);
		forth_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		
		fifth_activity = new Intent(this,ToDestinationPoint.class);
		fifth_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		

		sixth_activity = new Intent(this,PODokNOTok.class);
		sixth_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		
		
		seventh_activity = new Intent(this,OpenCameraActivity.class);
		eighth_activity = new Intent(this,OpenGallaryActivity.class);
	   
		ninth_activity = new Intent(this,LastActivity.class);
		ninth_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		
		tenth_activity = new Intent(this,Document_collected.class);
		tenth_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		
	
		  if(!isOnline())
		{
			Toast.makeText(this, "No Network Connection",Toast.LENGTH_LONG).show();
			finish();
		}
		
		
		  
		else  
			
			if(!isGpson())
			{
				startActivity(new Intent(this,GpsAlert.class));
				finish();
			}
		  
		  else
			  
			  
		  {		  
		
		
	// calling this function to write 0 to the status file so as to start the app from zero.
    //   writestateToInternalStorage();
		
    // Starting of background service 
	 
	  myservice=new Intent(this,Myservice.class);

	   try 
	    {
		   startService(myservice);
		} 
	    
	    catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this,"Service double starting problem",Toast.LENGTH_LONG).show();
		}

	    try 
	    {
			status_value=ReadstatusFromInternalStorage();
			//status_value=3;
		} 
	    
	    catch (FileNotFoundException e) 
	    {
			// TODO Auto-generated catch block
			//Toast.makeText(this,"reading staus problem",Toast.LENGTH_SHORT).show();
	    	//status_value=3;
		}
		
		
		 
		new Handler().postDelayed(new Runnable(){
			

			 
	            @Override
	            public void run() {
	                /* Create an Intent that will start the Menu-Activity. */
	            	
	            	 if(isOnline())
	    	 		 {    
	    	        
	    	         	 download_img_url_with_data();
	    	         
	    	 		 }  
	                
	                switch(status_value)
	                {
	                
	                case 3:
	                	startActivity(third_activity);
		                finish();
		                break;
	                
	                case 4:
	                	
	                	startActivity(forth_activity);
		                finish();
		                break;
		            
	                case 5:
	                	
	                	startActivity(fifth_activity);
		                finish();
		                break;    
	                	
                    case 6:
	                	
	                	startActivity(sixth_activity);
		                finish();
		                break; 
		                
                    case 7:
	                	startActivity(seventh_activity);
		                finish();
		                break; 
		                
		                
                    case 8:
                       startActivity(eighth_activity);
                       finish();
                       break; 
                       
                    case 9:
                        startActivity(ninth_activity);
                        finish();
                        break; 
                        
                        
                    case 10:
                        startActivity(tenth_activity);
                        finish();
                        break; 
	                
                    default:
	                	
	                	startActivity(first_activity);
		                finish();
		                break;        
	                
	                }
	     
	            	 // for testing purpose
	                 //	startActivity(forth_activity);
	                  //  finish();
	                
	                myprogressbar.setVisibility(View.INVISIBLE);

	            }
	        }, SPLASH_DISPLAY_LENGTH);
		
		  }
	
	  }
	
	
	
	public void download_img_url_with_data() 
   	{
   	         
   		     
   		       TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
   		     //Imagelinkurl="http://121.241.125.91/cc/mavyn/online/mobileimage?msg=869071021163101";//+telephonyManager.getDeviceId();   
   		       
   		       Imagelinkurl="http://121.241.125.91/cc/mavyn/online/mobileimage?msg="+telephonyManager.getDeviceId();   
   		       
   		       imageurl_with_data=new Imageurl_download_thread(Imagelinkurl);
   		       Thread t= new Thread(imageurl_with_data);
   		       t.start();
   		       try 
   		       { //
   				t.join();
   			   } catch (InterruptedException e) {
   				
   				   //Toast.makeText(this,"Error in downloading",Toast.LENGTH_LONG).show();
   			  }
   		      
   		       Image_url_Received=imageurl_with_data.getValue(); 
   		       
   		    
   		       if(Image_url_Received!=null)
   		       {
   		       
   		       if(profile_pics_changed)
   		       {
   		    	  
   		    	       hitimage=new Hit_image(Image_url_Received);
   		    	       Thread tt= new Thread(hitimage);
   		               tt.start();
   		               try 
   		               { //
   				        tt.join();
   			           } 
   		              catch (InterruptedException e) 
   		              {
   				
   				        Toast.makeText(this,"Error in downloading or writing image",Toast.LENGTH_SHORT).show();
   			          }
   		          
   		
   		       
   		       }
   		       }
   		       
   	}
	
	
	
	class Imageurl_download_thread implements Runnable {
        private volatile String imageurl_received_local=null; 
        
        private volatile String  f_name;
        private volatile String  l_name;
      
        private volatile String  Mobile;
       
        public final String localurl;
        JSONObject jsonResponse;
       
        
        public String getMobileno() {
    		return Mobile;
    	}
        
        public String getValue() {
   	       return imageurl_received_local;
   	     }

      
     	public String getDriverName() 
     	{
  		    return f_name;
  	    }
  	
     	public String getDriverName_last()
     	{
  		  return l_name;
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
  						
  						 //Toast.makeText(getActivity(),"Error in image Downloading",Toast.LENGTH_LONG).show();
  					 }
  						
  					 System.out.println("the recived url"+local);
  					 
  					 if(local!=null)
  					 {
  						 
  					
  					 
  					 try {
  						 jsonResponse = new JSONObject(local);
  						
  						 JSONArray jsonMainNode=jsonResponse.optJSONArray("chetak");
  				
  						JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
  						imageurl_received_local=jsonChildNode.optString("imageurl").toString();
  						f_name=jsonChildNode.optString("firstname").toString();
  						l_name=jsonChildNode.optString("lastname").toString();
  						Mobile= jsonChildNode.optString("mobileno").toString();
  			
  					
  					   } catch (JSONException e) {
  						
  						  e.printStackTrace();
  						 
  					    } 
  					 
  					 if(imageurl_received_local!=null)
  					 { 
  					 
  					 previous_url_Received=ReadulrFromInternalStorage();
  					 
  					 if(previous_url_Received==null)
  					 {
  					 
  					   writeUrl_addressToInternalStorage(imageurl_received_local);
  					   profile_pics_changed=true;
  					 
  					 }
  					 
  					 else
  					 {
  						 saved_url=ReadulrFromInternalStorage();
  						 if(!saved_url.equals(imageurl_received_local))
  						 {
  							 writeUrl_addressToInternalStorage(imageurl_received_local); 
  							 
  							 profile_pics_changed=true;
  						 }
  						 
  						 
  					 }
  					 
  					editor.putString("first_name", f_name);
  		            editor.putString("last_name", l_name);
  		            editor.putString("mobile_no",Mobile);
  		            editor.commit();
  				  }
  					
  					 
  	    	    }
  					
  			       }
  	    		  catch(Exception e)
  	    		  {
  	    			 // Toast.makeText(getActivity()," Server Error ",Toast.LENGTH_LONG).show();
  	    		  }
  	        }

  	 
  	 }
	

    class Hit_image implements Runnable
    {
        Bitmap bitmap=null;
        InputStream iStream = null;
        String strUrl=null;
        String text=null;
        
        public Hit_image(String myurl)
        {
        	strUrl=myurl;
        }
      
        
        @Override
		public void run() 
        {
        	
        	
        	  text=strUrl;
        	  
        	  text=strUrl;
        	  try{
                  URL url = new URL(strUrl);
                  
                  HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
       
               
                  //urlConnection.connect();
       
                  try {
					iStream = urlConnection.getInputStream();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                  
                  if(iStream!=null)
       
                  bitmap = BitmapFactory.decodeStream(iStream);
                  
                  saveImageToInternalStorage(bitmap);
                  iStream.close();
                 
                  Log.i("image saved","image savaed");
       
              }
        	  catch(Exception e)
        	  {
                  Log.d("Exception while downloading url", e.toString());
              }
  
      }
    }
   	
	
	
	 private int ReadstatusFromInternalStorage() throws FileNotFoundException 
	    {
         
	    	int status=0;
	    	FileInputStream fos = openFileInput("status.txt");
	        
	        try {
	        	status=fos.read();
				fos.close();
			    } 
	        
	            catch (IOException e) 
			    
			    {
				
			    	e.printStackTrace();
		        }
	        
	        return status;
	     }
	 
	 
	 
	 
	 public void saveImageToInternalStorage(Bitmap image) 
	     {

	        try {
	        	
	        	
	       
	        FileOutputStream fos = openFileOutput("desiredFilename1.png", Context.MODE_PRIVATE);
	        image.compress(Bitmap.CompressFormat.PNG, 100, fos);
	      
	        fos.close();
	        
	        } catch (Exception e) {
	        Log.e("saveToInternalStorage()", e.getMessage());
	      
	        }
	     }
	    
	    
	   
	 
	    boolean isOnline() 
	    {
		    ConnectivityManager cm =
		                      (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    return netInfo != null && netInfo.isConnectedOrConnecting();
		}
	   
	   
	   boolean isGpson()
	   {
		   LocationManager  locationManager=(LocationManager)this.getSystemService(LOCATION_SERVICE);
		   isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		   return isGPSEnabled;
	   }
	   


	   private void writestateToInternalStorage() 
		{
	            byte b=0;
		        try {
		       
		        FileOutputStream fos = openFileOutput("status.txt", Context.MODE_PRIVATE);
		   
		        fos.write(b);
		        fos.close();
		        
		            } 
		        catch (Exception e) 
		        {
		        Log.e("Error_In_status_saveToInternalStorage_view3", e.getMessage());
		      
		        }
		 }
	   
	   
	   private void writeUrl_addressToInternalStorage(String localurl) 
	 		{
	 	            String test=localurl;
	 		        try {
	 		       
	 		        FileOutputStream fos = openFileOutput("url1.txt", Context.MODE_PRIVATE);
	 		   
	 		        fos.write(test.getBytes());
	 		        fos.close();
	 		        
	 		            } 
	 		        catch (Exception e) 
	 		        {
	 		        Log.e("Error_In_status_saveToInternalStorage_view3", e.getMessage());
	 		      
	 		        }
	 		 }
	   
	   
	   private String ReadulrFromInternalStorage() 
	    {
        
	    	 String urllocal=null;
	    	 StringBuffer buffer = new StringBuffer();
	    	 
	    	 try{

	    	    FileInputStream fis = openFileInput("url1.txt");
	    	    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
	    	    if (fis!=null)                          
	    	      urllocal = reader.readLine();
	    	    	
	              fis.close();
	              
	    	 }catch(IOException e)
	    	 {
	    		 e.printStackTrace();
	    		 return null;
	    	 }
	              
	              return urllocal;
	    }


}
