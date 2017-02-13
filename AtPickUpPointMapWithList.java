package com.example.chetakdriverapp;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AtPickUpPointMapWithList extends Fragment implements OnClickListener
{

	Context context;
	private MapView m;
	private GoogleMap map=null;
    private Marker mycurrent_location_marker=null;
   
    
    private Location mLastLocation;
    private LatLng MyPoint;
    private TextView destination;
   
    private Button nextbutton;
    private Animation animShow,animHide;
 
	private String shipment_no=null;
    private String imei_no=null;
    private Marker driver_marker=null; 
    private  LatLng current_location,destination_location;
  
    private  static final String MyPREFERENCES ="MyPrefs" ;
    private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	
	private boolean zoomcontrol=false;
	
	
	     private SharedPreferences Activity_log;
	     private SharedPreferences.Editor activity_log_editor;
	     
	     private SharedPreferences Activity_log_time;
	     private SharedPreferences.Editor activity_log_time_editor;
	     
	     private SharedPreferences sharedPref=null;
	     private  static String retrievedshipmentno=null;
	     private  static String retrievedimeino =null;
	
   
	public AtPickUpPointMapWithList(Context c)
    {
    	
    	 context=c;
    	
    	
    	 writestateToInternalStorage();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflat and return the layout
		View v = inflater.inflate(R.layout.atpickuppoint, container, false);
		m = (MapView) v.findViewById(R.id.atpickuppointmap);
		m.onCreate(savedInstanceState);
		map=m.getMap();
		
		    sharedPref = context.getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
		    retrievedshipmentno = sharedPref.getString("shipmentno", "000");
		    retrievedimeino = sharedPref.getString("imeino", "000");
	
	    destination=(TextView)v.findViewById(R.id.to00);
	
        nextbutton=(Button)v.findViewById(R.id.next300);
        
        nextbutton.setOnClickListener(this);
       
        initAnimation();
        
        nextbutton.startAnimation(animShow);
	     
	     
	    map.setOnMarkerClickListener(new OnMarkerClickListener() {
	            public boolean onMarkerClick(Marker arg0) 
	            {
	                return true;
	            }
	        });
	    
	   // map.setMyLocationEnabled(true);
	    
	    
	    View zoomControls  = v.findViewById(0x1);
	     
	    if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
	        
	        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();
	        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,65,getResources().getDisplayMetrics());
	        params.setMargins(margin, margin, margin, margin);
	        zoomControls.setLayoutParams(params);
	        
	      } 
     
         (map.getUiSettings()).setZoomControlsEnabled(true);
    
		
		return v;
	}
	
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) 
	    {
	       super.onActivityCreated(savedInstanceState);
	      
	       sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
           double latitute=0.0;
           double longitute=0.0;
		try {
			latitute = Double.parseDouble(sharedpreferences.getString("customer_lat", "0.0"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
		try {
			longitute = Double.parseDouble(sharedpreferences.getString("customer_longi", "0.0"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           
           
           
           Circle circle = map.addCircle(new CircleOptions()
           .center(new LatLng(latitute, longitute))
           .radius(200)
           .strokeWidth(2)
           .strokeColor(Color.parseColor("#64b5f6"))
           .fillColor(Color.parseColor("#3264b5f6")))   ;
        
           destination.setText("At PickUp ");
           destination_location=new LatLng(latitute,longitute);
     	   mycurrent_location_marker= map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.newtruck)).position(destination_location).title("PickUp Point"));
	       CameraPosition cameraPosition = new CameraPosition.Builder().target(destination_location).zoom(15.0f).build();
           CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
           map.moveCamera(cameraUpdate);
 
	    }
	
	 
	 
	
	
	@Override
	public void onResume() 
	 {
		super.onResume();
		m.onResume();
		LocalBroadcastManager.getInstance(context).registerReceiver(myMessageReceiver, new IntentFilter("UI_UPDATE_BROADCAST"));
	  }
	
	@Override
	public void onPause() 
	{
		super.onPause();
		m.onPause();
		LocalBroadcastManager.getInstance(context).unregisterReceiver(myMessageReceiver);
    }
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		m.onDestroy();
	}
	
	@Override
	public void onLowMemory() 
	{
		super.onLowMemory();
		m.onLowMemory();
	}

    // this is receiver to receive data from the background service to update destination remaining distance and time 
	 private BroadcastReceiver myMessageReceiver = new BroadcastReceiver() 
     {
    	 //Marker driver_marker;
         @Override
         public void onReceive(Context context, Intent intent) {
             //Do Something With Received Data
        	 System.out.println("Receiving broadcast from the service");
             double longitute = intent.getDoubleExtra("longitute",0.0);
             double latitute = intent.getDoubleExtra("latitute",0.0);
             
             final LatLng MyPoint = new LatLng(latitute,longitute);	
           /*  if(driver_marker!=null)
		      {
            	 driver_marker.remove();
		      }*/
             if(mycurrent_location_marker!=null)
             {
            	 mycurrent_location_marker.remove();
             }
            mycurrent_location_marker= map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.newtruck)).position(MyPoint).title("My Current Location"));
	        CameraPosition cameraPosition = new CameraPosition.Builder().target(MyPoint).zoom(15.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            map.moveCamera(cameraUpdate);
           // map.setMyLocationEnabled(true);
	      
            }
       };


  private void initAnimation()
  {
  animShow=AnimationUtils.loadAnimation( context, R.anim.view_show);
  animHide=AnimationUtils.loadAnimation( context, R.anim.view_hide);
  }
  
 
 

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	
	
	
	 
	Intent mynext=new Intent(context,ToDestinationPoint.class);
	//mynext.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
	
	// changes on 23/12/2016
    mynext.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	context.startActivity(mynext);
	
	// no need to hit as the same hit is from the ToDestinationpointMap.java dated 24/1/17
    hitserver("begintrip",retrievedshipmentno,retrievedimeino);
	getActivity().finish();
	
}




     public class hitdata
	 {
		public String IMEI_NO;
	 }
    
  
    // this hit to send the msg that vehicle has reached the destination point 
    private void hitserver(String msg,String shipmentno,String imeino)
	{
	
		hitmessage hit=new hitmessage(imeino,shipmentno,msg);
		
		Thread t=new Thread(hit);
		t.start();
		
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("server hitted");
		
	}
	
	class hitmessage implements Runnable
	{

		private URL url=null;
		private HttpURLConnection con=null ;
		private String imei_no;
		private String shipment_no;
		private String message;
		
		
		 hitmessage(String imei, String shipmentno,String msg)
		{
			imei_no=imei;
			shipment_no=shipmentno;
			message=msg;
		}
		@Override
		public void run() 
		{
			
			
			//String ss="new";
	         final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+message+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
	      // final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+"waitingforload"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
	        
	        try {
				  url = new URL(request);
				  con = (HttpURLConnection) url.openConnection();
				  if(con.getResponseMessage()!=null)
				  System.out.println("IMEI_NO+SHIPMENT_NO DELIVERED SUCCESFULLY");
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
	
	
	
	  
	  
	// this is to store current stage to the memory to maintain current session  	 
	   private void writestateToInternalStorage( ) 
	   {
	        byte b=4;
	                   
	        try 
		    {
				        
				FileOutputStream fos1 = context.openFileOutput("status.txt", Context.MODE_PRIVATE);
				fos1.write(b);
				fos1.close();
				        
		    } 
				        
	        catch (Exception e) 
			 {
				Log.e("Error_In_status_saveToInternalStorage_view3", e.getMessage());
				      
		     }
	   }
	   
	   
	  
	   public boolean isOnline() 
		 {
			    ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo netInfo = cm.getActiveNetworkInfo();
			    return netInfo != null && netInfo.isConnectedOrConnecting();
	     }
		    		

}
