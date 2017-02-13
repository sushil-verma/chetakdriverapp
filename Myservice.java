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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Myservice extends Service implements LocationListener{
	
	    private Context mycontext=this;
	    public static boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
	    boolean isPassiveEnabled=false; 
	    boolean canGetLocation = false;
	    public  Location location; 
	    public  volatile Location location_updates; 
	 //   public  int MIN_DISTANCE_CHANGE_FOR_UPDATES =1; 
	 //   public  int MIN_TIME_BW_UPDATES=1;
	 //   public  int TIME_UPDATE=1;
	    public  int TIME_UPDATE=5;
        public  int MIN_TIME_BW_UPDATES=5;
	    public  int MIN_DISTANCE_CHANGE_FOR_UPDATES =1000; 
	    protected LocationManager locationManager;
	    
	    public  int oldtime=-1;
	    mycallable callab;
	    Thread callabtask;
	    int x=1;
	 // private final IBinder mBinder = new LocalBinder();
	    Intent intent = new Intent("UI_UPDATE_BROADCAST");
	    Intent first_activity = new Intent("FIRST_ACTIVITY");
	    Intent fifth_activity = new Intent("FIFTH_UI_UPDATE_BROADCAST");
	    private double distance_received;
	    private String pickup_d_t;
	    private int datafrequency;
	    private static volatile String source=null;
	    private static volatile String destination=null;
	    private static volatile String rate=null;
	    private static volatile String kilometer=null;
	    private static volatile String Eta=null;
	    private static volatile String shipmentno=null;
	    private static volatile int cancelstatus=0;
	    private static volatile int mygrstatus=0;
	 
	    
	  //private int testvalue=1;
	    
	  
	   /* public class LocalBinder extends Binder 
	    {
	    	Myservice getService() 
	    	{
	          return Myservice.this;
	        }
	     }*/
	    
	    
	 
	
	    @Override
		public IBinder onBind(Intent intent) 
	    {
			
			return null;
		}

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) 
	 {
    
	    System.out.println("service started.......................");
	    getLocation(MIN_TIME_BW_UPDATES);
        isGPSEnabled=true;
        return android.app.Service.START_STICKY;
    }
	
	/* this is method to receive location detail such as longitute and latitute from the location service using GPS system. this is one and only method 
	 * which is called in the onStart service method
	 */
	
	public void getLocation(int SetTimeFrequency) {
		
		
		     Log.i("executing getLocation", " ");
	
		     locationManager=(LocationManager)this.getSystemService(LOCATION_SERVICE);
			
			 isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			 isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		 
			 isPassiveEnabled=locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
		       
		      int One_minute=1000*60;
		      long TimeFrequency=SetTimeFrequency*One_minute;
		 
	      try {
	          
	          if (!isGPSEnabled) 
	          { 
	        	 
	        	 
	          }
	     
	          else 
	          {
	        	  
	        	   
	        	   
	        	   if (isNetworkEnabled) 
		              {
		                  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,TimeFrequency, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		              	  if (locationManager!= null) 
		              	  location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		              }
		        	
		        	  if (isGPSEnabled) 
		              {
		        		  if (location == null)
			            	{
		              
		                      locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,TimeFrequency,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		                      if (locationManager != null) 
		                      location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			            	}       
		               }
	        	
	        	
	           }// closing else block
	          
	          }
	          catch (Exception e) { e.printStackTrace();}
	         Log.i("executed getLocation", " ");
	      
	} 
	
	
	/* As the name implies this is the method to send or upload the data received from the location manager to the server . this further call 
	 * mycallable thread that sends the data to the server .It is also responsible to receive data from the server and store
	 * that data to the local variables . It using json code for fetching data from the server.
	 *  
	 */
	
	 private void uploadGpsToServer(final Location location)
	 {
			
			 Log.i("start uploading data", " ");
	    	
				try{
					
				 final  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
					
					LocationDetail locationDetail=null;
					try {
						locationDetail = new LocationDetail();
						locationDetail.IMEI_NO=	telephonyManager.getDeviceId();	
						locationDetail.mobile=telephonyManager.getLine1Number();
						locationDetail.dateStr=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;
						locationDetail.TimeStr=new SimpleDateFormat("HH:mm:ss").format(new Date());
					    locationDetail.latitude=Double.toString(location.getLatitude());
					  	locationDetail.longitude=Double.toString(location.getLongitude());
						
					// test code to complete transit when reached manesar gurgaon
					//	locationDetail.latitude="28.383268";
					//	locationDetail.longitude="76.8948378";
						
				    //  test code to complete transit when reached rewari
					//	locationDetail.latitude="28.194685";
					//	locationDetail.longitude="76.720598";
						
						
				    //  test code to complete transit when reached naraina
					//	locationDetail.latitude="28.615764";
					//	locationDetail.longitude="77.136311";
						
						
						
						
					
					     } 
					
					     catch (Exception e1) 
					     {
					        e1.printStackTrace();
			             }
				
					if( isOnline())
					{
						
						
						    Log.i("this is online sending data", " ");
						    /* calling a new thread class named mycallable that starts sending or uploading data to the server and 
						     * also responsible to receive data from the server and store them to the variables declared
						     * 
						     */
					        callab=new mycallable(locationDetail);
					        callabtask=new Thread(callab);
							callabtask.start();
							callabtask.join();
							Log.i("data sent...", " ");
						    int alarm_signal=callab.getalarm(); 
							
							distance_received=callab.getdistance();    // Receiving distance from the service sent 
						 	pickup_d_t=callab.getTraverTime();        // Receiving time from the server
							datafrequency=callab.getdatafrequency();
							//oldtime=datafrequency;
						
							  
							try {
								
								   cancelstatus=callab.getCancelstatus();
						   	   
							   } catch (Exception e1)  { e1.printStackTrace();}
							
							
							if(cancelstatus==1)
							{
								
						
							   Intent first=new Intent(this,ShipCancealAlert.class);
							   first.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							   startActivity(first); 
					
							}
							
							/*try {
							
								mygrstatus=Integer.parseInt(callab.getgrstatus());
							   } 
							
							   catch (Exception e) 
							   {
								   // TODO Auto-generated catch block
								    e.printStackTrace();
							    }
							
							if(mygrstatus==1)
							{
								Intent grintent=new Intent(this,Document_collected.class);
								grintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(grintent);
							}*/
							
							
							
							
							
							//intent.putExtra("distance",distance_received);
							intent.putExtra("Distance", distance_received);
						    intent.putExtra("pickup_d_t",pickup_d_t);
							fifth_activity.putExtra("Distance",distance_received);
							fifth_activity.putExtra("pickup_d_t",pickup_d_t);
							    
						       if(alarm_signal==1)
							   {
							    source=callab.getSource();
								destination=callab.getDestination();
							    rate=callab.getRate();
							    kilometer=callab.getKilometer();
							    Eta=callab.getEta();
							    shipmentno=callab.geshipmentno();
								
							// Testing purpose only 
						        	
						       
							/*	String source="new york south africa maharastra";
							    String destination="new york south africa maharastra jammu kasmir";
							    String rate="6000000";
							    String kilometer="700km";
							    Eta="123";
							    shipmentno="12";
							    */
								 
						       
							
							    Intent intent = new Intent(this, MyAlert.class);
						        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						        intent.putExtra("source",source);
						        intent.putExtra("destination",destination);
						        intent.putExtra("rate",rate);
						        intent.putExtra("km",kilometer);
						        intent.putExtra("Eta",Eta);
						        
						        intent.putExtra("pickup_d_t", pickup_d_t);
						        intent.putExtra("shipment_no",shipmentno);
						        
						        
						        Intent intentdirect = new Intent(this, MyAlertDirect.class);
						        intentdirect.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						        intentdirect.putExtra("source",source);
						        intentdirect.putExtra("destination",destination);
						        intentdirect.putExtra("rate",rate);
						        intentdirect.putExtra("km",kilometer);
						        intentdirect.putExtra("Eta",Eta);
						        intentdirect.putExtra("pickup_d_t", pickup_d_t);
						        intentdirect.putExtra("shipment_no",shipmentno);
						        
						        startMusic();// this is music or alarm when an order is placed.
						        
						        
						        if(callab.getdirect_driver()==0)
						        	
						        startActivity(intent);
						     
						        else 
						        	
							    startActivity(intentdirect);
						

							  }
			          
					}	
				
		        }
				
				   catch (Exception e)
		           {
		            e.printStackTrace();
		            Log.i("locationmanager", "unable to get IMEI No");
		           }
				
			  } 
		
	
	
	/* this is method to check to the status of the network that is it is working or not
	 * 
	 */
	
 
	

	@Override
	public void onLocationChanged(Location loc) {
		
		
	   //  location_updates=loc;
	 //  uploadGpsToServer(location);
	     uploadGpsToServer(loc);
		 broadcastMessage(mycontext,loc.getLongitude(),loc.getLatitude());
	 	
		if(oldtime!=-1)
		{
			
	    	 TIME_UPDATE=oldtime;
	    	 getLocation(TIME_UPDATE);
	         x++;
	  
	    }
		
		System.out.println("hello brother");
		
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { getLocation(TIME_UPDATE);}

	@Override
	public void onProviderEnabled(String provider){}

	@Override
	public void onProviderDisabled(String provider){}
	
	//this is the thread class to send and receive data from the server. data receiced are time, alarm, and distance and data


   class mycallable implements Runnable
	{
		LocationDetail location=null;
	    private volatile int  Datafrequency=0;
	    private volatile int  alarm=0;
	    private volatile double  distance=0.0;
	    private volatile String  pickup_d_t=null;
	    private volatile String  source =null;
	    private volatile String  destination=null;
	    private volatile String  kilometer=null;
	    private volatile String  rate=null;
	    private volatile String  Eta=null;
	    private volatile String  shipmentno=null;
	    private volatile String  grstatus=null;
	    private  int  direct_driver=0;
	    private  int  is_shipment_cancelled=0;
	    
	  
	    
	    
	    public int getCancelstatus() {
			return is_shipment_cancelled;
		}
	   
	    public String getSource() {
			return source;
		}
	    
	    public String geshipmentno() {
			return shipmentno;
		}
	    
	    
	    public String getEta() {
			return Eta;
		}
	    
	    
	    public String getDestination() {
			return destination;
		}

		public String getKilometer() {
			return kilometer;
		}

		public String getRate() {
			return rate;
		}

	  public int getdatafrequency()
	  {
		  return Datafrequency;
	  }
	  
	  public String getTraverTime()
	  {
		  return pickup_d_t;
	  }
	  
	  
	  public int getalarm()
	  {
		  return alarm;
	  }
	  
	  public double getdistance()
	  {
		  return distance;
	  }
	  
	 
	  
	  public String getgrstatus()
	  {
		  return grstatus;
	  }
	
	  
	  mycallable(LocationDetail location)
	   {
		  this.location=location;	
	   }
	  
	  
	  
	    public int getdirect_driver(){
			return direct_driver;
		}
	    
	  
	 
		public void run()
		{
			
			try {
				
				   Log.i("ready to hit the server", " ....");
				 
				        final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+location.IMEI_NO+"%23"+location.latitude+"%23"+location.longitude+"%23"+location.dateStr+"%23"+location.TimeStr+"%23"+1;
				        JSONObject jsonResponse;    
				        String local=null;
				        URL url = new URL(request);
			            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			           
			            
			            
						  try 
						  {
							local = br.readLine();
							 
						  } 
						  catch (Exception e) 
						  { 
							
							 Toast.makeText(getApplicationContext(),"Error in time reading",Toast.LENGTH_LONG).show();
						  }
							
						
						 
						 try {
							 jsonResponse = new JSONObject(local);
							
							 JSONArray jsonMainNode=jsonResponse.optJSONArray("chetak");
							
							
							int lengthJsonArr = jsonMainNode.length();
							for(int i=0;i<lengthJsonArr;i++)
							{
								
							 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							 
							 Datafrequency=Integer.parseInt(jsonChildNode.optString("datafrequency").toString());
							 alarm=Integer.parseInt(jsonChildNode.optString("alarm").toString());
						
							 if(alarm==1)
						     {
						     
						         try {
							   	      source=jsonChildNode.optString("source").toString();
								      destination=jsonChildNode.optString("destination").toString();
								      rate=jsonChildNode.optString("rate").toString();
								      kilometer=jsonChildNode.optString("km").toString();
								      Eta=jsonChildNode.optString("eta").toString();
								      shipmentno=jsonChildNode.optString("shipmentno").toString();
								      grstatus=jsonChildNode.optString("gr_status").toString();
								      direct_driver=Integer.parseInt(jsonChildNode.optString("directdriver").toString());
								     
							         } 
						     
						           catch (Exception e) 
						           {
								   // TODO Auto-generated catch block
								   e.printStackTrace();
								   Log.i("Json data received is null ..",shipmentno);
							       }
						     
						     }
							 
							 // this try block is to catch exception when distance and pickup_d_t is not in double
							 try {
								   distance=Double.parseDouble(jsonChildNode.optString("distance").toString());
								 } 
							 
							      catch (Exception e1) 
							     {
							       e1.printStackTrace();
							     }
							 
							 
							 try {
								 
								  pickup_d_t=jsonChildNode.optString("transittime").toString();
							     } 
							     catch (Exception e) 
							     
							     {
								// TODO Auto-generated catch block
								 e.printStackTrace();
							     }
							 
							 
							 try 
							 {
							    is_shipment_cancelled=Integer.parseInt(jsonChildNode.optString("reset").toString());
							 } 
							 
							 catch (Exception e1) 
							  {
								// TODO Auto-generated catch block
								 e1.printStackTrace();
							  }
						   
					
							  Log.i("Json data received is is_shipment_cancelled .."," "+is_shipment_cancelled);
					
							}
							
							
						   
						    }
						 catch(JSONException js)
						 {
							 js.printStackTrace();
						 }
			              
			           br.close();
			           con.disconnect();
			           
				
		
			}
			
			catch (NumberFormatException e) 
			{
				e.printStackTrace();
			} 
			
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			
			
			      // For testing of app only
			    /*  Datafrequency=5;
			      alarm=1;
			      distance=0.1;
			      pickup_d_t=0.1;
			      source ="new delhi";
			      destination="kolkata";
			      kilometer="1000";
			      rate="123";
			      Eta="333";
			      shipmentno="123";
			      grstatus="0";
			      is_shipment_cancelled=0;	
			      direct_driver=0;*/
		}
	}
   
  
   /* sending broadcast message to start the music means alarm when 1 is received from the server as a part of all the response
   * 
   */
   
   public void startMusic() 
   {
		
	      Intent i = new Intent("super");
	      sendBroadcast(i);
		
   }
   
   /* this is class that contain all the detail to be sent to the server 
    * 
    */
	class LocationDetail
	{
		 String IMEI_NO;	
	     String longitude;
	     String latitude;
		 String dateStr;
		 String TimeStr;
		 String mobile;
	}

   /* this is broadcast method that broadcast three intents to the three different activity to perform action on the basis  of the 
    * value received from the receiver. It is called in onLocationChanged method of location service . sent repeatedly to all
    */
   
   private void broadcastMessage(Context context,double longi,double lati)
   {
       
       intent.putExtra("longitute",longi);
       intent.putExtra("latitute",lati);
      
       LocalBroadcastManager.getInstance(context).sendBroadcast(intent);  
       System.out.println("sending broadcast to the third_activity");
       
       first_activity.putExtra("longitute",longi);
       first_activity.putExtra("latitute",lati);
       LocalBroadcastManager.getInstance(context).sendBroadcast(first_activity);  
       System.out.println("sending broadcast to the first_activity");
       
       fifth_activity.putExtra("longitute", longi);
       fifth_activity.putExtra("latitute", lati);
       LocalBroadcastManager.getInstance(context).sendBroadcast(fifth_activity);  
       System.out.println("sending broadcast to the fifth_activity");
  } 
   
 
   
 /*  this is method to stop the service means when the on/off status button is switched off this method is called and hit the server
  *  with IMEI no and other detail along with  a Zero value to Indicate the server that the toggle button is switched off and in terms of driver the driver no longer 
  *  wants any order from the truck owner.
  *  
  * 
  */
   
  /* public void stop(Location location )
	{
		
		try{
			
			 final  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
				
			 final	LocationDetail  locationDetail = new LocationDetail();;
				try {
					
					locationDetail.IMEI_NO=	telephonyManager.getDeviceId();	
					locationDetail.mobile=telephonyManager.getLine1Number();
					locationDetail.dateStr=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;
					locationDetail.TimeStr=new SimpleDateFormat("HH:mm:ss").format(new Date());
					locationDetail.longitude=Double.toString(location.getLongitude());;
					locationDetail.latitude=Double.toString(location.getLatitude());
				    } 
				   catch (Exception e1) { e1.printStackTrace();}
			
				if( isOnline())
				{
	                 Log.i("this is online sending data", " ");
					 new Thread(new Runnable(){
						 @Override
						public void run()
						 {
							 final String request="http://192.168.0.204/online/mobilelocation.php?msg="+locationDetail.IMEI_NO+"%23"+locationDetail.latitude+"%23"+locationDetail.longitude+"%23"+locationDetail.dateStr+"%23"+locationDetail.TimeStr+"%23"+0;

							 try {
								 URL url = new URL(request);
								 HttpURLConnection con = (HttpURLConnection) url.openConnection();
								 String response=con.getResponseMessage(); 
								 System.out.println("response is...closing updates ="+response);
							     } catch (MalformedURLException e) 
							     {
					              e.printStackTrace();
							     } catch (IOException e) 
							    {
								
								   e.printStackTrace();
							    }
						}
					 }).start();
					 
					locationManager.removeUpdates(this);
					locationManager=null;
			        Log.i("switch 0ff...", " ");
			     }	
			
	        }
		
		
	             catch (Exception e)
	           {
	            e.printStackTrace();
	            Log.i("error in stoping process", "checke");
	          }
		
	}*/
   
   
   /* when the activity is destroyed the location service is closed  */
  
   @Override
   public void onDestroy() 
   {
            
	 //  stop(location);
	   
	   super.onDestroy();
	   locationManager.removeUpdates (this);
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
   
   public boolean isOnline() {
	    ConnectivityManager cm =
	                      (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
}
