package com.example.chetakdriverapp;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
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

public class HomeMap extends Fragment 
{

	Context context;
	MapView m;
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	
	GoogleMap map=null;
    private Marker mycurrent_location_marker=null;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    LatLng MyPoint;
    
    private  View zoomControls;
    private  View myLocationButton;
    
    public HomeMap(Context c)
    {
    	
    	context=c;
    }
    
    
    
    
    @Override
    public void onCreate(Bundle saveinstance)
    {
    	super.onCreate(saveinstance);
    	
 
    }
    
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflat and return the layout
		View v = inflater.inflate(R.layout.mapfregment, container, false);
		m = (MapView) v.findViewById(R.id.map);
		m.onCreate(savedInstanceState);
		map=m.getMap();
		
		
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
		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,440,getResources().getDisplayMetrics());
		        params.setMargins(margin, margin, margin, margin);
		        myLocationButton.setLayoutParams(params);
		        
		      } 
	
	    
	    zoomControls  = v.findViewById(0x1);
	     
	    if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
	        // location button is inside of RelativeLayout
	        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

	        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	      
	        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics());
	        params.setMargins(margin, margin, margin, margin);

	        zoomControls.setLayoutParams(params);
	        
	      } 

	    map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
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


private BroadcastReceiver myMessageReceiver = new BroadcastReceiver() 
{
	 
    @Override
    public void onReceive(Context context, Intent intent) {
   	 
   	     
      
   	      System.out.println("Receiving broadcast from the service");
          double longitute = intent.getDoubleExtra("longitute",0.0);
          double latitute = intent.getDoubleExtra("latitute",0.0);
          final LatLng MyPoint = new LatLng(latitute,longitute);	
     
        if(mycurrent_location_marker!=null)
        {
           mycurrent_location_marker.remove();
        }
        mycurrent_location_marker= map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.gbaloon)).position(MyPoint).title("My Current Location"));
        
        CameraPosition cameraPosition = new CameraPosition.Builder().target(MyPoint).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       
      
    
        }
  };


 

}
