package com.example.chetakdriverapp;

import java.io.FileOutputStream;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PodokFisrtFregmnt extends Fragment implements OnClickListener
{

	    Context context;
	    private Button pod_ok;
	    private Button pod_not_ok;
	    private Button open_camera;
	    private Button open_galary;
	    private LinearLayout show_camera;
	    private int podstatus=0;
	    private Animation animShow,animHide;
		private MapView m;
		private GoogleMap map=null;
		private Marker mycurrent_location_marker=null;
		private  View myLocationButton;
	
		    
	    private Location mLastLocation;
		private LatLng MyPoint;
		private boolean pod_ok_selection_button=true;
		private boolean pod_not_ok_selection_button=true;
		
		
		
    
    public PodokFisrtFregmnt(Context c)
    {
    	
    	 context=c;
    
         writestateToInternalStorage();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflat and return the layout
		View v = inflater.inflate(R.layout.activity_sixth, container, false);
		
	    	m = (MapView) v.findViewById(R.id.sixthpagemap);
		    m.onCreate(savedInstanceState);
		    map=m.getMap();
		    initAnimation();
		
		   // writestateToInternalStorage();
	        pod_ok=(Button)v.findViewById(R.id.pod_ok);
	        pod_not_ok=(Button)v.findViewById(R.id.pod_not_ok);
	        open_camera=(Button)v.findViewById(R.id.camera);
	        open_galary=(Button)v.findViewById(R.id.memory);
	        show_camera=(LinearLayout)v.findViewById(R.id.sub_menu);
	        show_camera.setVisibility(View.INVISIBLE);
	        pod_ok.setOnClickListener(this);
		    pod_not_ok.setOnClickListener(this);
		    open_camera.setOnClickListener(this);
		    open_galary.setOnClickListener(this);
		    
		    map.setOnMarkerClickListener(new OnMarkerClickListener() {
	            public boolean onMarkerClick(Marker arg0) 
	            {
	                return true;
	            }
	        });
		    
		    
		    myLocationButton =v.findViewById(0x2);
		       
			   if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			        // location button is inside of RelativeLayout
			        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
			        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
			        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,getResources().getDisplayMetrics());
			        params.setMargins(margin, margin, margin, margin);
			        myLocationButton.setLayoutParams(params);
			        
			      } 
		    
		    map.setMyLocationEnabled(true);
		    
		 
		
		return v;
	}
	
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) 
	    {
	       super.onActivityCreated(savedInstanceState);
	      
	       
	    
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
	

  private void initAnimation()
  {
  animShow=AnimationUtils.loadAnimation( context, R.anim.view_show);
  animHide=AnimationUtils.loadAnimation( context, R.anim.view_hide);
  }
  
 
 

  public void onClick(View v) 
  {
    switch(v.getId())
    {
        case R.id.pod_ok:
        	
         
        if(pod_ok_selection_button)
        {
        		
         if(show_camera.getVisibility()==View.INVISIBLE)
            { show_camera.setVisibility(View.VISIBLE);
              show_camera.startAnimation(animShow);
              //setMargins(pod_ok, 6, 1, 1, 6);
              //setMargins(pod_not_ok, 1, 1, 6, 6);
              pod_ok.setTextColor(Color.parseColor("#d84c00"));
              podstatus=1;
              pod_not_ok_selection_button=false;
            }
         else
            { 
             show_camera.setVisibility(View.INVISIBLE);
             show_camera.startAnimation(animHide);
             pod_ok.setTextColor(Color.parseColor("#aaaaaa"));
             // setMargins(pod_ok, 6, 1, 1, 6);
             // setMargins(pod_not_ok, 1, 1, 6, 6);
             podstatus=1;
             //show_camera.animate().translationY(0);
             pod_not_ok_selection_button=true;
            
            }  
        }
         
       
          
         
   	      break;
      
        case R.id.pod_not_ok:
        	
        	
        	if(pod_not_ok_selection_button)
        	{
       	 
        	  if(show_camera.getVisibility()==View.INVISIBLE)
       	      { 
        		  show_camera.setVisibility(View.VISIBLE);
        		  pod_not_ok.setTextColor(Color.parseColor("#d84c00"));
        		 // setMargins(pod_ok, 6, 1, 1, 6);
                 // setMargins(pod_not_ok, 1, 1, 6, 6);
        		  show_camera.startAnimation(animShow);
       	          podstatus=2;
       	          pod_ok_selection_button=false;
              }
        	  
	         else
	         
	          { 
	        	 show_camera.setVisibility(View.INVISIBLE);
	        	 show_camera.startAnimation(animHide);
	        	 pod_not_ok.setTextColor(Color.parseColor("#aaaaaa"));
	             podstatus=2;
	             pod_ok_selection_button=true;
              }  
        	  
        	}
	    	  break;
	    	  
        case R.id.camera:
     
   	        Intent camera_intent=new Intent(context,OpenCameraActivity.class);
   	        camera_intent.putExtra("podstatus", podstatus);
   	        startActivity(camera_intent);
   	       
        	show_camera.setVisibility(View.INVISIBLE);
        	// finishing podok main screen after starting OpenGallaryActivity
   	        break;
        
        case R.id.memory:
       	     Intent memory_intent=new Intent(context,OpenGallaryActivity.class);
       	     memory_intent.putExtra("podstatus",podstatus);
	    	 startActivity(memory_intent);
	    	
	    	 show_camera.setVisibility(View.INVISIBLE);
	    	  // finishing podok main screen after starting OpenGallaryActivity
	    	 break;

      }

	}


	  
	// this is to store current stage to the memory to maintain current session  	 
	   private void writestateToInternalStorage( ) 
	   {
	        byte b=6;
	                   
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
	            map.setMyLocationEnabled(true);
		      
	            }
	       };
	       
	       
	       
	       private void setMargins (View view, int left, int top, int right, int bottom) {
	    	    if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
	    	        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
	    	        p.setMargins(left, top, right, bottom);
	    	        view.requestLayout();
	    	    }
	    	}

}
