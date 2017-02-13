package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class ToDestinationPointMap extends Fragment implements OnClickListener
{
	
	private Context context;
	private MapView m;
	private GoogleMap map=null;
    private Marker mycurrent_location_marker=null;
    private Location mLastLocation;
    private LatLng MyPoint;
    private Button call_maveen1;
    private Button call_maveen2;
    private Button call_consignee1;
    private Button call_consignee2;
    private TextView destination;
    private LinearLayout linearlayouttop;
    private LinearLayout linearlayout_when_reached;
    private Button nextbutton;
    private boolean control_pick_up_button=false;
    private Animation animShow,animHide;
    private double mylati;
	private double mylongi;
    private double longitude;
	private double latitude;
    private String to;
	private String from;
	private String shipment_no=null;
    private String imei_no=null;
    private Marker driver_marker=null; 
    private  LatLng current_location,destination_location;
    private ArrayList<LatLng> markerPoints;
    private  static final String MyPREFERENCES ="MyPrefs" ;
	private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	private  View zoomControls;
	private  View myLocationButton;
    private String Consigner_contact_no;
	private static int color=1;
    private SharedPreferences Activity_log;
	private SharedPreferences.Editor activity_log_editor;
	private SharedPreferences Activity_log_time;
	private SharedPreferences.Editor activity_log_time_editor;
	private  ArrayList<LatLng> points = new ArrayList<LatLng>();
	private Animation startAnimation;
	private ArrayList<LocationListener> mListenerList = new ArrayList<LocationListener>();
	private LocationManager mLoctionManager = null;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private Location mCurBestLocation = null;
	private static int screenSize=0;
	
    public ToDestinationPointMap(Context c)
    {
      context=c;
       
    }
    
    
  
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflat and return the layout
		View v = inflater.inflate(R.layout.dummymapfregment, container, false);
		m = (MapView) v.findViewById(R.id.thirdpagemap);
		m.onCreate(savedInstanceState);
		map=m.getMap();
	    initAnimation();
		
		 call_maveen1=(Button)v.findViewById(R.id.call_mayveen);
		 call_consignee1=(Button)v.findViewById(R.id.call_customer);
		 
		 call_maveen2=(Button)v.findViewById(R.id.call_mayveen_top);
		 call_consignee2=(Button)v.findViewById(R.id.call_customer_top);
	       
	     destination=(TextView)v.findViewById(R.id.to0);
		 linearlayouttop=(LinearLayout)v.findViewById(R.id.time_distance_layout0);
	     linearlayouttop.setVisibility(View.VISIBLE);

     //  this is vehicle/distance/time view at bottom with reached at pickup point      
   
	     linearlayout_when_reached=(LinearLayout)v.findViewById(R.id.top_of_thirdview0);
	     linearlayout_when_reached.setVisibility(View.INVISIBLE);
    
	 //  addressBox=(TextView)findViewById(R.id.address);
         nextbutton=(Button)v.findViewById(R.id.next30);
         nextbutton.setVisibility(View.INVISIBLE);
         nextbutton.setOnClickListener(this); 
         nextbutton.setText("Reached At Destination");
         
         call_consignee1.setText("Call Consignee");
         call_consignee2.setText("Call Consignee");
         
         call_maveen1.setOnClickListener(this);   
         call_maveen2.setOnClickListener(this);   
         call_consignee1.setOnClickListener(this);   
         call_consignee2.setOnClickListener(this);   
         
  
	    map.setOnMarkerClickListener(new OnMarkerClickListener() {
	            public boolean onMarkerClick(Marker arg0) 
	            {
	                return true;
	            }
	        });
	    
	    // commented on 4/2/17
	    
	     /*  myLocationButton =v.findViewById(0x2);
	       
		   if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
		        // location button is inside of RelativeLayout
		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
		        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,getResources().getDisplayMetrics());
		        params.setMargins(margin, margin, margin, margin);
		        myLocationButton.setLayoutParams(params);
		        
		      } 
		   */
		   
		   
	    
         myLocationButton =v.findViewById(0x2);
	    
	    
	     screenSize = getResources().getConfiguration().screenLayout &
	            Configuration.SCREENLAYOUT_SIZE_MASK;

	    String toastMsg;
	    switch(screenSize) {
	        case Configuration.SCREENLAYOUT_SIZE_LARGE:
	        	
	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
	 		        // location button is inside of RelativeLayout
	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,480,getResources().getDisplayMetrics());
	 		        params.setMargins(margin, margin, margin, margin);
	 		        myLocationButton.setLayoutParams(params);
	 		        
	 		      } 
	        	//Toast.makeText(context, "large", Toast.LENGTH_LONG).show();
	            break;
	        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
	        	
	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
	 		        // location button is inside of RelativeLayout
	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,getResources().getDisplayMetrics());
	 		        params.setMargins(margin, margin, margin, margin);
	 		        myLocationButton.setLayoutParams(params);
	 		        
	 		      } 
	        	//Toast.makeText(context, "normal", Toast.LENGTH_LONG).show();
	            break;
	        case Configuration.SCREENLAYOUT_SIZE_SMALL:
	        	
	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
	 		        // location button is inside of RelativeLayout
	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,280,getResources().getDisplayMetrics());
	 		        params.setMargins(margin, margin, margin, margin);
	 		        myLocationButton.setLayoutParams(params);
	 		        
	 		      } 
	        	//Toast.makeText(context, "small", Toast.LENGTH_LONG).show();
	            break;
	       
	        default:
	        	
	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
	 		        // location button is inside of RelativeLayout
	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,getResources().getDisplayMetrics());
	 		        params.setMargins(margin, margin, margin, margin);
	 		        myLocationButton.setLayoutParams(params);
	 		        
	 		      } 
	         //Toast.makeText(context, "default", Toast.LENGTH_LONG).show();
	    }
		   
		   
		   
	
	    
	    zoomControls  = v.findViewById(0x1);
	     
	    if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) 
	    {
	        // location button is inside of RelativeLayout
	        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

	        // Align it to - parent BOTTOM|LEFT
	       // params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	       // params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	       // params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

	        // Update margins, set to 10dp
	        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,65,getResources().getDisplayMetrics());
	        params.setMargins(margin, margin, margin, margin);

	        zoomControls.setLayoutParams(params);
	        
	      } 
     
         (map.getUiSettings()).setZoomControlsEnabled(true);
         
         init();
     
		return v;
	}
	
	
	private void init() {
		
		List<String> providers = getLocationManager().getAllProviders();
	
		for (String provider : providers) {
			MyLocationListener listener = new MyLocationListener();
			getLocationManager().requestLocationUpdates(
					provider, 2000, 100, listener);
			mListenerList.add(listener);
		}

	}

	private void uninit() {

		for (LocationListener listener : mListenerList) {
			if (listener != null)
				getLocationManager().removeUpdates(listener);
		}
	}
		
		
		private final LocationManager getLocationManager()
		{
			if (mLoctionManager == null) {
			
				mLoctionManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			}
			return mLoctionManager;
		}

		private class MyLocationListener implements LocationListener {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				Toast.makeText(context, "onstatuschange="+provider.toString()+ status, Toast.LENGTH_LONG).show();
			//	mTextView.setText("onStatusChanged: provider" + provider.toString(+ status);
			}

			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(context, "onProviderEnabled="+provider.toString(), Toast.LENGTH_LONG).show();
				//mTextView.setText("onProviderEnabled: " + provider.toString());
			}

			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(context, "onProviderDisabled="+provider.toString(), Toast.LENGTH_LONG).show();
			//	mTextView.setText("onProviderDisabled: " + provider.toString());
			}

		
			@Override
			public void onLocationChanged(Location location) {
				//mLog.debug("onLocationChanged " + location.toString());
				updateCurBestLocation(location);
				
				  final LatLng MyPoint = new LatLng(mCurBestLocation.getLatitude(),mCurBestLocation.getLongitude());	
		           
		           //   map.clear();
		      
					    PolylineOptions lineOptions = new PolylineOptions();
					    points.add(MyPoint);						
						
					
						lineOptions.addAll(points);
						lineOptions.width(5);
						lineOptions.color(Color.GREEN);	
						
				
					    map.addPolyline(lineOptions);
					    if(driver_marker!=null)
					    driver_marker.remove();
					    driver_marker= map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.newtruck)).position(MyPoint).title("My Current Location"));
					    
					    
					    //	mTextView.setText("onLocationChanged " + location.toString());
			}
		}
		
		
		private Location getLastKnownLocation() {
			
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);//
			criteria.setAltitudeRequired(false);// 
			criteria.setBearingRequired(false);//
			criteria.setCostAllowed(true);// 
			criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 
			String bestProvider = mLoctionManager.getBestProvider(criteria, true);//
		//	mLog.debug("bestProvider = " + bestProvider);
//		/	GpsStatus gpsStatus = getLocationManager().getGpsStatus(null);
	
			Location location = getLocationManager().getLastKnownLocation(
					LocationManager.GPS_PROVIDER);
			if (location == null) {
				location = getLocationManager().getLastKnownLocation(
						LocationManager.NETWORK_PROVIDER);			
			}
			updateCurBestLocation(location);
		//	mLog.debug("getLastKnownLocation location=" + location);
			
			return location;
		}
		
		private void updateCurBestLocation(Location location) {
			if (location == null)
				return;
			
			if (isBetterLocation(location, mCurBestLocation)) {
				mCurBestLocation = location;
				//mLog.debug("updateCurBestLocation mCurBestLocation=" + location);	
	
			}
			
		}
		
		

		/** Determines whether one Location reading is better than the current Location fix
		  * @param location  The new Location that you want to evaluate
		  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
		  */
		protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
		    if (currentBestLocation == null) {
		        // A new location is always better than no location
		        return true;
		    }

		    // Check whether the new location fix is newer or older
		    long timeDelta = location.getTime() - currentBestLocation.getTime();
		    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		    boolean isNewer = timeDelta > 0;

		    // If it's been more than two minutes since the current location, use the new location
		    // because the user has likely moved
		    if (isSignificantlyNewer) {
		        return true;
		    // If the new location is more than two minutes older, it must be worse
		    } else if (isSignificantlyOlder) {
		        return false;
		    }

		    // Check whether the new location fix is more or less accurate
		    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		    boolean isLessAccurate = accuracyDelta > 0;
		    boolean isMoreAccurate = accuracyDelta < 0;
		    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		    // Check if the old and new location are from the same provider
		    boolean isFromSameProvider = isSameProvider(location.getProvider(),
		            currentBestLocation.getProvider());

		    // Determine location quality using a combination of timeliness and accuracy
		    if (isMoreAccurate) {
		        return true;
		    } else if (isNewer && !isLessAccurate) {
		        return true;
		    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
		        return true;
		    }
		    return false;
		}

		/** Checks whether two providers are the same */
		private static boolean isSameProvider(String provider1, String provider2) {
		    if (provider1 == null) {
		      return provider2 == null;
		    }
		    return provider1.equals(provider2);
		}

		
//		if (mNetListener != null)
//			getLocationManager().removeUpdates(mNetListener);
//		if (mGpsListener != null)
//			getLocationManager().removeUpdates(mGpsListener);
//}
	
	/*private void blink(){
	    final Handler handler = new Handler();
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
	        int timeToBlink = 1000;    //in milissegunds
	        try{Thread.sleep(timeToBlink);}catch (Exception e) {}
	           
	        handler.post(new Runnable() {
	                @Override
	                    public void run() {
	                   
	                	

	                	switch(color)
	                	{
	                	case 1:
	                		nextbutton.setTextColor(Color.parseColor("#d84c00"));
	                		color=2;
	                	break;

	                	case 2:
	                		nextbutton.setTextColor(Color.WHITE);
	                		color=1;
	                		
	                	break;

	                	}
	                    blink();
	                }
	                });
	            }
	        }).start();
	    }*/
	
	
	@Override
	    public void onActivityCreated(Bundle savedInstanceState) 
	    {
	       super.onActivityCreated(savedInstanceState);
	     
	         SharedPreferences sharedPreflocal = context.getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
			 String retrievedshipmentno = sharedPreflocal.getString("shipmentno", "000");
			
	       try
           {
				
				 final  TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				 imei_no=	telephonyManager.getDeviceId();	           
           }
           
           
          catch(Exception e)
          {
          	e.printStackTrace();
          	System.out.println("Error in getting IMeI no");
          }
	       
	       final String myurl="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+retrievedshipmentno+"%23"+"starttrip";
	       
	       downloaddata(myurl);
	       
	       /* mycallable hitting=new mycallable();
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
	    	
	        mylati=hitting.getmylatitute();
	    	mylongi=hitting.getmylongitute();
	    	longitude=hitting.getlong();
	    	latitude=hitting.getlat();
	    	to=hitting.get_destination_address();
            from=hitting.get_source_address();
            Consigner_contact_no=hitting.get_consigner_contact_no();
            destination.setText(to);
         
         // shipment_no=hitting.getshipmentno();
         
	     // for testing purpose
            mylati=28.518603;
	    	mylongi=77.0816284;
	    	longitude=81.870117;
	    	latitude=24.620388;
	    	to="kolkata";
            from="chennai";
            shipment_no="123";
            
            
            editor.putString("customer_lat", String.valueOf(latitude));
            editor.putString("customer_longi", String.valueOf(longitude));
            editor.commit();
            
            
           
            
            try{
				
				 final  TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				 imei_no=	telephonyManager.getDeviceId();	           
              } 
           
            catch(Exception e)
            {
             	e.printStackTrace();
           	    
             }
	    	
          // writeimeiandshipment(shipment_no,imei_no);
     	   
     	   
            if(mylati!=0.0 && mylongi!=0.0)
            {  	
     	   current_location=new LatLng(mylati,mylongi);
     	   destination_location=new LatLng(latitude,longitude);
        // addressBox.setText(address);
     	   markerPoints = new ArrayList<LatLng>();
     	   MarkerOptions options = new MarkerOptions();
     		 	    
     	   if(markerPoints.size()>1)
     	     {
     		 	markerPoints.clear();
     		 	map.clear();					
     		 }
     		 	   
     		 	   markerPoints.add(current_location);
     		 	   
     		 	   options.position(current_location);
     		 	   
     		 	   if(markerPoints.size()==1)
     		 	   {
     		 			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.gbaloon));
     		 	   }
     		 	   
     		 	   map.addMarker(options);
     		 	   
     		       markerPoints.add(destination_location);

     		 	   options.position(destination_location);
     		 	   
     		 	   if(markerPoints.size()==2)
     		 	   {
     		 			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.rbaloon));
     		 	   }
     		 	   
     		 	   map.addMarker(options);
     		 	   
     		 	Circle circle = map.addCircle(new CircleOptions()
   	           .center(destination_location)
   	           .radius(2000)
   	           .strokeWidth(2)
   	           .strokeColor(Color.parseColor("#64b5f6"))
   	           .fillColor(Color.parseColor("#3264b5f6")))   ;
     		 			
     		      CameraPosition cameraPosition = new CameraPosition.Builder().target(current_location).zoom(6.0f).build();
                  CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                  map.moveCamera(cameraUpdate);
                  map.setMyLocationEnabled(true);
                  
                    if(markerPoints.size() >= 2){					
          			LatLng origin = markerPoints.get(0);
          			LatLng dest = markerPoints.get(1);
          			
          			// below lines draw route map on the map but now postponed
          		  	   String url = getDirectionsUrl(origin, dest);				
          		       DownloadTask downloadTask = new DownloadTask();
          		       downloadTask.execute(url);
          			
          		       // commented on 23/12/2016
          			  // CameraPosition cameraPosition = new CameraPosition.Builder().target(origin).zoom(5.0f).build();
                     //  CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                       map.moveCamera(cameraUpdate);
                       map.setMyLocationEnabled(true);
               
          		    }
                    
            }  
            
            else
            	Toast.makeText(context, "No lat.. long.. found", Toast.LENGTH_LONG).show();*/
            
            
	    
	}
	
	private void downloaddata(String url){
	    
	    
	    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
	                @Override
	                public void onResponse(String s) {
	                	showJSON(s);
	                	
	                	// writeimeiandshipment(shipment_no,imei_no);
	               	   
	                 if(mylati!=0.0 && mylongi!=0.0)
	                 
	                 {  	
	               	   
	               	   current_location=new LatLng(mylati,mylongi);
	               	   destination_location=new LatLng(latitude,longitude);
	               	   markerPoints = new ArrayList<LatLng>();
	               	   MarkerOptions options = new MarkerOptions();
	               		 	    
	               	   if(markerPoints.size()>1)
	               	     {
	               		 	markerPoints.clear();
	               		 	map.clear();					
	               		 }
	               		 	   
	               		 	   markerPoints.add(current_location);
	               		 	   
	               		 	   options.position(current_location);
	               		 	   
	               		 	   if(markerPoints.size()==1)
	               		 	   {
	               		 			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.gbaloon));
	               		 	   }
	               		 	   
	               		 	   map.addMarker(options);
	               		 	   
	               		       markerPoints.add(destination_location);

	               		 	   options.position(destination_location);
	               		 	   
	               		 	   if(markerPoints.size()==2)
	               		 	   {
	               		 			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.rbaloon));
	               		 	   }
	               		 	   
	               		 	   map.addMarker(options);
	               		 	   
	               		 	   
	               		 	  Circle circle = map.addCircle(new CircleOptions()
	               	           .center(destination_location)
	               	           .radius(2000)
	               	           .strokeWidth(2)
	               	           .strokeColor(Color.parseColor("#64b5f6"))
	               	           .fillColor(Color.parseColor("#3264b5f6")))   ;
	               		 	   
	               		 	  
	               		 			
	               		        CameraPosition cameraPosition = new CameraPosition.Builder().target(current_location).zoom(6.0f).build();
	                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
	                            map.moveCamera(cameraUpdate);
	                            map.setMyLocationEnabled(true);
	                            
	                            
	                                if(markerPoints.size() >= 2){					
	                      			LatLng origin = markerPoints.get(0);
	                      			LatLng dest = markerPoints.get(1);
	                      			
	                      			// below lines draw route map on the map but now postponed
	                      		  	   String url = getDirectionsUrl(origin, dest);				
	                      		       DownloadTask downloadTask = new DownloadTask();
	                      		       downloadTask.execute(url);
	                      			
	                      			// CameraPosition cameraPosition = new CameraPosition.Builder().target(origin).zoom(5.0f).build();
	                                // CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
	                                //  map.moveCamera(cameraUpdate);
	                                //  map.setMyLocationEnabled(true);
	                           
	                      		    }
	                                
	                            
	                     }
	                     else 
	                  	   Toast.makeText(context, "No lat.. long.. found", Toast.LENGTH_LONG).show();
	                   
	                
	                }
	            },
	            
	            new Response.ErrorListener() {
	                @Override
	                public void onErrorResponse(VolleyError volleyError) {
	                   
	                    //loading.dismiss();
	                 
	                    volleyError.printStackTrace();
	                }

				
	            })
	    
	     {
	     
	    	
	    };

	    RequestQueue requestQueue = Volley.newRequestQueue(context);
	    requestQueue.add(stringRequest);

	}


	void showJSON(String local)
	{
	try {
		 JSONObject jsonResponse = new JSONObject(local);
		
		 JSONArray jsonMainNode=jsonResponse.optJSONArray("chetak");
		
		 JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
		 
	     mylati=Double.parseDouble(jsonChildNode.optString("lat").toString());
	     mylongi=Double.parseDouble(jsonChildNode.optString("long").toString());
	     latitude=Double.parseDouble(jsonChildNode.optString("customerlat").toString());
	     longitude=Double.parseDouble(jsonChildNode.optString("customerlong").toString());
		 to=jsonChildNode.optString("customeraddress");
		 from=jsonChildNode.optString("driveraddress");
		 shipment_no=jsonChildNode.optString("shipmentno");
		 Consigner_contact_no=jsonChildNode.optString("consignorcontact");
		 destination.setText(to);
		 
		 
		/* mylati=28.518603;
	 	 mylongi=77.0816284;
	  	 longitude=81.870117;
	 	 latitude=24.620388;
	 	 to="33 kapashera new delhi 110028";
	     from="chennai"; 
	     shipment_no="123";
	     Consignee_contact_no="9911427348";*/
		/* 
		 sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	     editor = sharedpreferences.edit();
	     
	     editor.putString("customer_lat", String.valueOf(latitude));
	     editor.putString("customer_longi", String.valueOf(longitude));
	     editor.commit();*/
	     
	     
	   
		//System.out.println("data received.........."+"mylongi"+mylatitest+"mylati"+mylongitest+longitude+latitude+address);
	  }
	   catch(JSONException js )
	   {
		  js.printStackTrace();
	   }

	   catch(Exception e)
	   {
		  System.out.println("sharedprefferenced problem in json coding"); 
	   }

	}
	
	
	
	
	
	
	@Override
	public void onResume() 
	 {
		super.onResume();
		m.onResume();
		LocalBroadcastManager.getInstance(context).registerReceiver(fifthMessageReceiver, new IntentFilter("FIFTH_UI_UPDATE_BROADCAST"));
	  }
	
	@Override
	public void onPause() 
	{
		super.onPause();
		m.onPause();
		LocalBroadcastManager.getInstance(context).unregisterReceiver(fifthMessageReceiver);
		//getActivity().finish();
    }
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		m.onDestroy();
		writestateToInternalStorage();
		uninit();
	}
	
	@Override
	public void onLowMemory() 
	{
		super.onLowMemory();
		m.onLowMemory();
	}

    // this is receiver to receive data from the background service to update destination remaining distance and time 
	 private BroadcastReceiver fifthMessageReceiver = new BroadcastReceiver() 
     {
    	     
             @Override
             public void onReceive(Context context, Intent intent) {
             
        	 System.out.println("Receiving broadcast from the service");
             double longitute = intent.getDoubleExtra("longitute",0.0);
             double latitute = intent.getDoubleExtra("latitute",0.0);
             double distance_local=intent.getDoubleExtra("Distance",0.0);
             double time_local=intent.getDoubleExtra("Travel_time",0.0);
           //imei_no=intent.getStringExtra("IMEI");
                            
             
             /*time_text_view.setText(Double.toString(time_local));
      	     distance_text_view.setText(Double.toString(distance_local));
            
      	     time_text_view_final.setText(Double.toString(time_local));
      	     
      	     distance_text_view_final.setText(Double.toString(distance_local));*/
             
             if(latitute==0.0)
            	 
             {
            	 Toast.makeText(context, "No lat.. long.. found", Toast.LENGTH_LONG).show(); 
             }
           
             else
             { 	 
             
      	     if(distance_local<5 && control_pick_up_button==false)
      	     {
      	    	 
      	    	if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
      		        // location button is inside of RelativeLayout
      		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

      		        // Align it to - parent BOTTOM|LEFT
      		       // params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
      		       // params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      		        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      		       // params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

      		        // Update margins, set to 10dp
      		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,120,getResources().getDisplayMetrics());
      		        params.setMargins(margin, margin, margin, margin);

      		        zoomControls.setLayoutParams(params);
      		        
      		      } 	 
      	    	
      	    	 /*if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
     		        // location button is inside of RelativeLayout
     		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
     		        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
     		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,320,getResources().getDisplayMetrics());
     		        params.setMargins(margin, margin, margin, margin);
     		        myLocationButton.setLayoutParams(params);
     		        
     		      } */
      	    	
      	    	
      	    	switch(screenSize) {
    	        case Configuration.SCREENLAYOUT_SIZE_LARGE:
    	        	
    	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
    	 		        // location button is inside of RelativeLayout
    	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
    	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,420,getResources().getDisplayMetrics());
    	 		        params.setMargins(margin, margin, margin, margin);
    	 		        myLocationButton.setLayoutParams(params);
    	 		        
    	 		      } 
    	        	//Toast.makeText(context, "large", Toast.LENGTH_LONG).show();
    	            break;
    	        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
    	        	
    	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
    	 		        // location button is inside of RelativeLayout
    	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
    	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,320,getResources().getDisplayMetrics());
    	 		        params.setMargins(margin, margin, margin, margin);
    	 		        myLocationButton.setLayoutParams(params);
    	 		        
    	 		      } 
    	        	//Toast.makeText(context, "normal", Toast.LENGTH_LONG).show();
    	            break;
    	        case Configuration.SCREENLAYOUT_SIZE_SMALL:
    	        	
    	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
    	 		        // location button is inside of RelativeLayout
    	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
    	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,220,getResources().getDisplayMetrics());
    	 		        params.setMargins(margin, margin, margin, margin);
    	 		        myLocationButton.setLayoutParams(params);
    	 		        
    	 		      } 
    	        	//Toast.makeText(context, "small", Toast.LENGTH_LONG).show();
    	            break;
    	       
    	        default:
    	        	
    	        	if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
    	 		        // location button is inside of RelativeLayout
    	 		        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();
    	 		        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
    	 		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,380,getResources().getDisplayMetrics());
    	 		        params.setMargins(margin, margin, margin, margin);
    	 		        myLocationButton.setLayoutParams(params);
    	 		        
    	 		      } 
    	         //Toast.makeText(context, "default", Toast.LENGTH_LONG).show();
    	    }
      	      control_pick_up_button=true;
      	      show_button();
      	     }
      	
      	     
      	      //25/1/17
             /*final LatLng MyPoint = new LatLng(latitute,longitute);	
              
             // map.clear();
	         // driver_marker= map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.newtruck)).position(MyPoint).title("My Current Location"));
             driver_marker= map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.dotsgreen)).position(MyPoint).title("My Current Location"));*/
      	     
      	   /* final LatLng MyPoint = new LatLng(latitute,longitute);	
           
           //   map.clear();
      
			    PolylineOptions lineOptions = new PolylineOptions();
			    points.add(MyPoint);						
				
			
				lineOptions.addAll(points);
				lineOptions.width(5);
				lineOptions.color(Color.GREEN);	
				
		
			    map.addPolyline(lineOptions);	*/
	      
          }
             
        } // closing of else
       }; 

       
      /* private void showLIneonmap()
   	{
   		
   		
    	   Toast.makeText(context, "no gps", Toast.LENGTH_SHORT).show();
    	   
    	   LocationManager	 locationManager=(LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
       	boolean isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
       	if(isGPSEnabled)
       	locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,1,1, this);
       	else
       		Toast.makeText(context, "no gps", Toast.LENGTH_SHORT).show();
    
   	}*/

  private void initAnimation()
  {
  animShow=AnimationUtils.loadAnimation( context, R.anim.view_show);
  animHide=AnimationUtils.loadAnimation( context, R.anim.view_hide);
  startAnimation = AnimationUtils.loadAnimation(context, R.anim.blink);
  }
  
 
  public void show_button()
  {
	  
	// blink();
        
  	linearlayouttop.setVisibility(View.INVISIBLE);
  	linearlayout_when_reached.setVisibility(View.VISIBLE);
  	linearlayout_when_reached.startAnimation(animShow);	
  	nextbutton.setVisibility(View.VISIBLE);
  	nextbutton.startAnimation(startAnimation);	
  }

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	switch(v.getId())
	{
	
	case R.id.call_mayveen:
	case R.id.call_mayveen_top:
        	                 {
		
        	                	 Intent intent = new Intent(Intent.ACTION_CALL);
        	                	 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       		            	     intent.setData(Uri.parse("tel:" +"9811747777"));
       		            	    
       		            	     context.startActivity(intent);
       			
       			                 break;
		
		
	                         }
        	                 
	case R.id.call_customer:
	case R.id.call_customer_top:
		
	                       {
		
	                    	   Intent intent = new Intent(Intent.ACTION_CALL);

	 		            	   intent.setData(Uri.parse("tel:" +Consigner_contact_no));
	 		              	   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 		            	   context.startActivity(intent);
	 			
	 			               break;
		
		
	                       }
	                      
	
	
	case R.id.next30:
		
	                {
		
	                	hitserver();
	                    Intent mynext=new Intent(context,PODokNOTok.class);
	                	mynext.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); 
	                	context.startActivity(mynext);
	   
	                }
	
	}
	
	
}


/*private  class mycallable implements Runnable
{
    hitdata ht;
    private volatile double  longi;
    private volatile double  lati;
    private volatile double  mylongi;
    private volatile double  mylati;
    private volatile String  source;
    private volatile String  destination;
    private volatile String consigner_contact;
    
    final TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	
  public mycallable()
	{
	   ht =new hitdata();
	   ht.IMEI_NO=telephonyManager.getDeviceId();	
	}
	
  public double getlong()
  {
	  return longi;
  }
  
  public double getlat()
  {
	  return lati;
  }
  
  public double getmylongitute()
  {
	  return mylongi;
  }
  
  public double getmylatitute()
  {
	  return mylati;
  }
  
  
  public String get_source_address()
  {
	  return source;
  }
  
  public String get_destination_address()
  {
	  return destination;
  }
  
  public String get_consigner_contact_no()
  {
	  return consigner_contact;
  }
  
  
 
  
	public void run()
	{
		
		try {
			
			   Log.i("ready to hit the server", " ....");
			 
			      //final String request="http://192.168.0.204/online/mobilelocation.php?msg="+ht.IMEI_NO+"%23"+"ok";
			        final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+ht.IMEI_NO+"%23"+"retrievedshipmentno"+"%23"+"starttrip";	
			        JSONObject jsonResponse;    
			        Log.d("request for second page", ""+request);
		        	
		        	URL url = new URL(request);
		            HttpURLConnection con = (HttpURLConnection) url.openConnection();
		          
		            System.out.println("data uploades for second page");
		            
		            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
		            String local=null;
					try {
						local = br.readLine();
						 Log.i("Json data received..longi,lati,adddress",local);
					 } catch (Exception e) { 
						
						 Toast.makeText(context,"Error in time reading",Toast.LENGTH_LONG).show();
					 }
						
					 System.out.println("the recived url"+local);
					
					 try {
						 jsonResponse = new JSONObject(local);
						
						 JSONArray jsonMainNode=jsonResponse.optJSONArray("chetak");
						
						
						int lengthJsonArr = jsonMainNode.length();
						for(int i=0;i<lengthJsonArr;i++)
						{
							
						 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						
						 mylati=Double.parseDouble(jsonChildNode.optString("lat").toString());
						 mylongi=Double.parseDouble(jsonChildNode.optString("long").toString());
						 source=jsonChildNode.optString("driveraddress");
						 lati=Double.parseDouble(jsonChildNode.optString("customerlat").toString());
						 longi=Double.parseDouble(jsonChildNode.optString("customerlong").toString());
						 destination=jsonChildNode.optString("customeraddress");
						 consigner_contact=jsonChildNode.optString("consigneecontact");
				
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
}*/


    public class hitdata
	 {
		public String IMEI_NO;
	 }
    
  
    // this hit to send the msg that vehicle has reached the destination point 
    private void hitserver()
	{
		SharedPreferences sharedPref = context.getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
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

		System.out.println("server hitted");
		
	}
	
	class hitmessage implements Runnable
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
			
	        //final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+"waitingforload"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
	        final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+"waitingforunload"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
	        
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
	
	
	
	
	
	// writing shipmentid and emeino to the sharedprefference	
	  private void writeimeiandshipment(String shipmentno,String imei)
	   {
		  SharedPreferences sharedPref = context.getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
		  Editor editor = sharedPref.edit();
		  editor.putString("shipmentno", shipmentno); 
		  editor.putString("imeino", imei);         // value is the string you want to save
		  editor.commit();
	  }
	  
	  
	  
	// this is to store current stage to the memory to maintain current session  	 
	   private void writestateToInternalStorage( ) 
	   {
	        byte b=5;
	                   
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
	   
	   
	   
	   
	   private String getDirectionsUrl(LatLng origin,LatLng dest)
		{
		    String str_origin = "origin="+origin.latitude+","+origin.longitude;
			String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		    String sensor = "sensor=false";			
		    String parameters = str_origin+"&"+str_dest+"&"+sensor;
		    String output = "json";
		    String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		     return url;
		}
		
		
		 private String downloadUrl(String strUrl) throws IOException{
		        String data = "";
		        InputStream iStream = null;
		        HttpURLConnection urlConnection = null;
		        try{
		                URL url = new URL(strUrl);
                       urlConnection = (HttpURLConnection) url.openConnection();

		                urlConnection.connect();
                       iStream = urlConnection.getInputStream();
                       BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                       StringBuffer sb  = new StringBuffer();

		                String line = "";
		                while( ( line = br.readLine())  != null){
		                        sb.append(line);
		                }
		                
		                data = sb.toString();

		                br.close();

		        }catch(Exception e){
		                Log.d("Exception while downloading url", e.toString());
		        }finally{
		                iStream.close();
		                urlConnection.disconnect();
		        }
		        return data;
		     }
		 
		 
		 
		 private class DownloadTask extends AsyncTask<String, Void, String>{			
				
			   @Override
				protected String doInBackground(String... url) {
						
					String data = "";
							
					try{
						
						data = downloadUrl(url[0]);
					}catch(Exception e){
						Log.d("Background Task",e.toString());
					}
					return data;		
				}
				
		        @Override
				protected void onPostExecute(String result) {			
					super.onPostExecute(result);			
					
					ParserTask parserTask = new ParserTask();
				    parserTask.execute(result);
						
				}		
			}
		 
		 private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
		    	
		    
				@Override
				protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
					
					JSONObject jObject;	
					List<List<HashMap<String, String>>> routes = null;			           
		            
		            try{
		            	jObject = new JSONObject(jsonData[0]);
		            	DirectionsJSONParser parser = new DirectionsJSONParser();
		            	
		            	// Starts parsing data
		            	routes = parser.parse(jObject);    
		            }catch(Exception e){
		            	e.printStackTrace();
		            }
		            return routes;
				}
				
				// Executes in UI thread, after the parsing process
				@Override
				protected void onPostExecute(List<List<HashMap<String, String>>> result) {
					ArrayList<LatLng> points = null;
					PolylineOptions lineOptions = null;
					MarkerOptions markerOptions = new MarkerOptions();
					
					// Traversing through all the routes
					for(int i=0;i<result.size();i++){
						points = new ArrayList<LatLng>();
						lineOptions = new PolylineOptions();
						
						// Fetching i-th route
						List<HashMap<String, String>> path = result.get(i);
						
						// Fetching all the points in i-th route
						for(int j=0;j<path.size();j++){
							HashMap<String,String> point = path.get(j);					
							
							double lat = Double.parseDouble(point.get("lat"));
							double lng = Double.parseDouble(point.get("lng"));
							LatLng position = new LatLng(lat, lng);	
							
							points.add(position);						
						}
					
						lineOptions.addAll(points);
						lineOptions.width(5);
						lineOptions.color(Color.RED);	
						
					}
				
					map.addPolyline(lineOptions);							
				}			
	 
	}
		 
		 /*@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
			 	 final LatLng MyPoint = new LatLng(location.getLatitude(),location.getLongitude());	
		         PolylineOptions lineOptions = new PolylineOptions();
				 points.add(MyPoint);						
				 lineOptions.addAll(points);
				 lineOptions.width(5);
			     lineOptions.color(Color.GREEN);	
				 map.addPolyline(lineOptions);		
				 Toast.makeText(context, "gps ok ", Toast.LENGTH_SHORT).show();
				
			}
*/



			/*@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}




			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}




			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}*/
		 
		 
		    		

}
