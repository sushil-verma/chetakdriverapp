package com.example.chetakdriverapp;


import java.io.FileOutputStream;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
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
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/* this is final activity till now . this shows the end of the journey and the driver is now ready to 
 * get the another order from the customer 
 */

public class Sixth1 extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener,OnClickListener
{
	    private int podstatus=0;
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
	 // variables for google location finder
	    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	    private Location mLastLocation;
	 // Google client to interact with Google API
	    private GoogleApiClient mGoogleApiClient;
	 // boolean flag to toggle periodic location updates
	    private boolean mRequestingLocationUpdates = false;
	    private LocationRequest mLocationRequest;
	    private Button pod_ok;
	    private Button pod_not_ok;
	    private Button open_camera;
	    private Button open_galary;
	    private LinearLayout show_camera;
	    private Integer []icon_image={R.drawable.hm,R.drawable.deliverred,R.drawable.profile,R.drawable.support};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_sixth);
        final  Intent myservice=new Intent(this,Myservice.class);
        // write the current activity no in the file 
        writestateToInternalStorage();
        pod_ok=(Button)findViewById(R.id.pod_ok);
        pod_not_ok=(Button)findViewById(R.id.pod_not_ok);
        open_camera=(Button)findViewById(R.id.camera);
        open_galary=(Button)findViewById(R.id.memory);
        show_camera=(LinearLayout)findViewById(R.id.sub_menu);
        show_camera.setVisibility(View.INVISIBLE);
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        actionBar.setCustomView(R.layout.myswitch);
	 // Switch search = (Switch) actionBar.getCustomView().findViewById(R.id.searchfield);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
		        | ActionBar.DISPLAY_SHOW_HOME);
	 
	    pod_ok.setOnClickListener(this);
	    pod_not_ok.setOnClickListener(this);
	    open_camera.setOnClickListener(this);
	    open_galary.setOnClickListener(this);
	    mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
      //  mDrawerLayout = (DrawerLayout) findViewById(R.id.sixth);
     //   mDrawerList = (ListView) findViewById(R.id.sixthleft_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        CustomListAdapter thirdadapter=new CustomListAdapter(this,mPlanetTitles,icon_image); 
        mDrawerList.setAdapter(thirdadapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.ic_drawer);
        
        if (checkPlayServices()) 
        {
     
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

    }  // closing of on create method

    public void Map_Display(double lng,double lat)
    {
    	final LatLng MyPoint = new LatLng(lng,lat);	
    //	mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.sixthmap);
    	map=mapfragment.getMap();
      //map.setPadding(0,500,0,0);
        map.addMarker(new MarkerOptions().position(MyPoint).title("My Current Location"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(MyPoint).zoom(5.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(new OnMarkerClickListener() {
            public boolean onMarkerClick(Marker arg0) {
                return true;
            }
        });
  
     }
    

    @Override
	public void onClick(View v) 
       {
	     switch(v.getId())
	     {
	         case R.id.pod_ok:
	          if(show_camera.getVisibility()==View.INVISIBLE)
	             { show_camera.setVisibility(View.VISIBLE);
	               //show_camera.animate().translationY(show_camera.getHeight());
	             podstatus=1;
	             }
	          else
	             { 
	              show_camera.setVisibility(View.INVISIBLE);
	              podstatus=1;
	              //show_camera.animate().translationY(0);
	             }  
	    	      break;
	         case R.id.pod_not_ok:
	        	 if(show_camera.getVisibility()==View.INVISIBLE)
	        	 { show_camera.setVisibility(View.VISIBLE);
	        	   podstatus=2;
	             }
		         else
		         { show_camera.setVisibility(View.INVISIBLE);
		           podstatus=2;
	             }  
		    	 break;
	         case R.id.camera:
	      
	    	 Intent camera_intent=new Intent(this,OpenCameraActivity.class);
	    	 camera_intent.putExtra("podstatus", podstatus);
	    	 startActivity(camera_intent);
	    	 show_camera.setVisibility(View.INVISIBLE);
	    	 break;
	         case R.id.memory:
	        	 Intent memory_intent=new Intent(this,OpenGallaryActivity.class);
	        	 memory_intent.putExtra("podstatus",podstatus);
		    	 startActivity(memory_intent);
		    	 show_camera.setVisibility(View.INVISIBLE);
		    	 break;
	
	      }
	
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
	

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			
			 if (mDrawerToggle.onOptionsItemSelected(item)) 
			   {
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
		  
		  
		  private void displayLocation() {
			  
		        mLastLocation = LocationServices.FusedLocationApi
		                .getLastLocation(mGoogleApiClient);
		 
		        if (mLastLocation != null) {
		            
		             Map_Display( mLastLocation.getLatitude(),mLastLocation.getLongitude());
		           
		            }
		        
		        
		        System.out.println("lat="+latitude);
		        System.out.println("lon="+longitude);
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
		       // params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		       // params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		      //  params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

		        // Update margins, set to 10dp
		        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,25,getResources().getDisplayMetrics());
		        params.setMargins(margin, margin, margin, margin);

		        myLocationButton.setLayoutParams(params);
		        
		      } 
		}
		 
		
		private void writestateToInternalStorage() 
		{
			   byte b=6;
		        try {
		       
		        FileOutputStream fos = openFileOutput("status.txt", Context.MODE_PRIVATE);
		   
		        
		        fos.write(b);
		        //image.compress(Bitmap.CompressFormat.PNG, 100, fos);
		      
		        fos.close();
		        
		            } 
		        catch (Exception e) 
		        {
		        Log.e("Error_In_status_saveToInternalStorage_view6", e.getMessage());
		      
		        }
		 }
		 
		 
	
	}