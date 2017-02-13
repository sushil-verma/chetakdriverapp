package com.example.chetakdriverapp;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

	


// this part receiving  currunt lal long from both myservice and google fued location server (code needs reform)//

public class ForthActivity1 extends FragmentActivity implements OnClickListener,ConnectionCallbacks, OnConnectionFailedListener 
	{
		    
	        private GoogleMap map;  
	        private MapFragment mapfragment;
		    private DrawerLayout mDrawerLayout;
		    private ListView mDrawerList;
		    private ActionBarDrawerToggle mDrawerToggle; 
            private CharSequence mDrawerTitle;
		    private CharSequence mTitle;
		    private String[] mPlanetTitles;
		    private double latitude=0.0;
		    private double longitude=0.0;
		    private Marker mycurrent_location_marker=null;
		    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
		    private Location mLastLocation;
		 // Google client to interact with Google API
		    private GoogleApiClient mGoogleApiClient;
		 // boolean flag to toggle periodic location updates
		    private boolean mRequestingLocationUpdates = false;
		    private LocationRequest mLocationRequest;
		    private Button mynext;
		    private Animation animShow,animHide;
		    private Integer []icon_image={R.drawable.hm,R.drawable.deliverred,R.drawable.profile,R.drawable.support};
			   /* Initialize the animation object 
			    * 
			    * 
			    * 
			    *  
			    */
		    
		    private  static final String MyPREFERENCES = "MyPrefs" ;
		    private SharedPreferences sharedpreferences;
	
	
	       private void initAnimation()
			{
			    animShow=AnimationUtils.loadAnimation( this, R.anim.view_show);
			    animHide=AnimationUtils.loadAnimation( this, R.anim.view_hide);
			}
	
		   @Override
		    public void onPause()
		    {
			   super.onPause();
		       LocalBroadcastManager.getInstance(this).unregisterReceiver(myMessageReceiver);
		    }
		    
		  
		 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        setContentView(R.layout.activity_updated__forth_);
	        initAnimation();
	        // write the currunt stage of the process to start over from this stage if the app is closed that is to maintain sessiion
	        writestateToInternalStorage();
	        final  Intent myservice=new Intent(this,Myservice.class);
	        mynext=(Button)findViewById(R.id.forthnext);
	        mynext.startAnimation(animShow);
	        ActionBar actionBar = getActionBar();
	        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
	        actionBar.setCustomView(R.layout.myswitch);
		    //Switch search = (Switch) actionBar.getCustomView().findViewById(R.id.searchfield);
		    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
			        | ActionBar.DISPLAY_SHOW_HOME);
	
		   
		  
	        //googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        
		    mynext.setOnClickListener(this);
		    
		    mTitle = mDrawerTitle = getTitle();
	        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.sunny);
	        mDrawerList = (ListView) findViewById(R.id.forthleft_drawer);
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	        CustomListAdapter adapter=new CustomListAdapter(this, mPlanetTitles, icon_image);
	        mDrawerList.setAdapter(adapter);
	        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);
	        getActionBar().setIcon(R.drawable.ic_drawer);
	        
	        if (checkPlayServices()) 
	        {
	        	 
	             // Building the GoogleApi client
	            buildGoogleApiClient();
	            displayLocation();
	            System.out.println("this is onCreate method");
	        }
	        
	        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close) 
	        {
	            public void onDrawerClosed(View view) {
	                getActionBar().setTitle(mTitle);
	                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	        }

	            public void onDrawerOpened(View drawerView) 
	            {
	            	getActionBar().setTitle(mDrawerTitle);
	                invalidateOptionsMenu(); 
	      
	            }
	            
	            
	        };
	            
	            
	            mDrawerLayout.setDrawerListener(mDrawerToggle);

	            if (savedInstanceState == null) {
	               // selectItem(0);
	              }
	            
	        // read customer lat /long from shared profference to put a marker of piup up point obn the map    
	            
	            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	            double latitute= Double.parseDouble(sharedpreferences.getString("customer_lat", "0.0"));
	            double longitute= Double.parseDouble(sharedpreferences.getString("customer_longi", "0.0"));
	       
	            final LatLng PickupPoint = new LatLng(latitute,longitute);	
		    	mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.forthmap);
		    	map=mapfragment.getMap();
		    	mylocationButton_layout();
		        map.setOnMarkerClickListener(new OnMarkerClickListener() {
			            public boolean onMarkerClick(Marker arg0) 
			            {
			            	
			            	arg0.showInfoWindow();
			                return true;
			            }
			        });
		        
		        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.destinationbollon)).position(PickupPoint).title("PicuUp Point"));
	
	    }  // closing of on create method

	   
	    
	    
	    
	   // Received broadcast from the background service to update currunt marker on the map
	    
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
	    
	    
	@Override
		public void onClick(View v) 
	   {
		   Intent intent=new Intent(this,ToDestinationPoint.class);
		   this.startActivity(intent);
	       finish();
		  // android.os.Process.killProcess(android.os.Process.myPid());

       }
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if(keyCode == KeyEvent.KEYCODE_BACK)
	   {
	   
	    return true;
	   }
	    
	    return false;
	}
		
		
		 private class DrawerItemClickListener implements ListView.OnItemClickListener {
		        @Override
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		            //selectItem(position);
		        }
		    }
		
		/*
		 @Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.main, menu);
				return true;
			}*/

			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				// Handle action bar item clicks here. The action bar will
				// automatically handle clicks on the Home/Up button, so long
				// as you specify a parent activity in AndroidManifest.xml.
				
				 if (mDrawerToggle.onOptionsItemSelected(item)) {
			            return true;
			        }
				 
				
				
				return super.onOptionsItemSelected(item);
	         } 
			
			
			 @Override
			    public boolean onPrepareOptionsMenu(Menu menu) {
			        // If the nav drawer is open, hide action items related to the content view
			        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
			        return super.onPrepareOptionsMenu(menu);
			    }
			 
			 
			 protected synchronized void buildGoogleApiClient() {
			        mGoogleApiClient = new GoogleApiClient.Builder(this)
			                .addConnectionCallbacks(this)
			                .addOnConnectionFailedListener(this)
			                .addApi(LocationServices.API).build();
			    }
			  
			  
			  private boolean checkPlayServices() {
			        int resultCode = GooglePlayServicesUtil
			                .isGooglePlayServicesAvailable(this);
			        if (resultCode != ConnectionResult.SUCCESS) {
			            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
			                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
			                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
			            } else {
			                Toast.makeText(getApplicationContext(),
			                        "This device is not supported.", Toast.LENGTH_LONG)
			                        .show();
			                finish();
			            }
			            return false;
			        }
			        return true;
			    }
			  
		
	// google play service fuesed location to show currunt position on map		  
			  
			  private void displayLocation() {
				  
			        mLastLocation = LocationServices.FusedLocationApi
			                .getLastLocation(mGoogleApiClient);
			 
			        if (mLastLocation != null) {
			            
			             Map_Display( mLastLocation.getLatitude(),mLastLocation.getLongitude());
			           
			            }
			        
			        
			        System.out.println("lat="+latitude);
			        System.out.println("lon="+longitude);
			    }
			 
			  
			  
			  public void Map_Display(double lng,double lat)
			    {
			    	final LatLng MyPoint = new LatLng(lng,lat);	
			    	mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.forthmap);
			    	map=mapfragment.getMap();
			    	//mylocationButton_layout();
			        map.setOnMarkerClickListener(new OnMarkerClickListener() {
				            public boolean onMarkerClick(Marker arg0) 
				            {
				                return true;
				            }
				        });
			        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.newtruck)).position(MyPoint).title("My Current Location"));
			       
			       // Marker TP = googleMap.addMarker(new MarkerOptions().position(MyPoint).title("chetak cargo"));
			        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			      //map.getUiSettings().setZoomGesturesEnabled(true);
			       

		            CameraPosition cameraPosition = new CameraPosition.Builder().target(MyPoint).zoom(15.0f).build();
		            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
		            
		            map.moveCamera(cameraUpdate);
		            //map.setMyLocationEnabled(true);
		            map.getUiSettings().setZoomControlsEnabled(true);
			    }

			
			
			 @Override
			    protected void onStart() {
			        super.onStart();
			        if (mGoogleApiClient != null) {
			            mGoogleApiClient.connect();
			        }
			    }
			 
			 
			
			 
			 
			 @Override
			    protected void onResume() {
			        super.onResume();
			        LocalBroadcastManager.getInstance(this).registerReceiver(myMessageReceiver, new IntentFilter("UI_UPDATE_BROADCAST"));
			        checkPlayServices();
			        displayLocation();
			    }
			 
			 
			 @Override
			 protected void onStop() 
			 {
				    mGoogleApiClient.disconnect();
				    super.onStop();
			 }

			@Override
			public void onConnectionFailed(ConnectionResult arg0) {
				// TODO Auto-generated method stub
				System.out.println("no connection available");
				
			}

			@Override
			public void onConnected(Bundle arg0) {
				// TODO Auto-generated method stub
				displayLocation();
				
			}

			@Override
			public void onConnectionSuspended(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
			private void mylocationButton_layout()
			{
			
			View myLocationButton = mapfragment.getView().findViewById(0x2);
		       
			   if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			        // location button is inside of RelativeLayout
			        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();

			        // Align it to - parent BOTTOM|LEFT
			        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
			        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

			        // Update margins, set to 10dp
			        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,getResources().getDisplayMetrics());
			        params.setMargins(margin, margin, margin, margin);

			        myLocationButton.setLayoutParams(params);
			        
			      } 
			}
			
			
			
			private void writestateToInternalStorage() 
			{
				   byte b=4;
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
			
	
}
	

	

	

