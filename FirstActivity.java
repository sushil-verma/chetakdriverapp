package com.example.chetakdriverapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/* this activity display the driver current location as well as the driver can see his current location 
 * on the marker he may press the button to receive order from the customer and response .
 */
	public class FirstActivity extends FragmentActivity 
 
	{
	        private GoogleMap map; 
	        private MapFragment mapFragment;
		    private DrawerLayout mDrawerLayout;
		    private ListView mDrawerList;
		    private ActionBarDrawerToggle mDrawerToggle; 
            private CharSequence mDrawerTitle;
		    private CharSequence mTitle;
		    private String[] mPlanetTitles;
		    private double latitude=0.0;
		    private double longitude=0.0;
	        private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
		    private Location mLastLocation;
		    private GoogleApiClient mGoogleApiClient;
		    private boolean mRequestingLocationUpdates = false;
		    private LocationRequest mLocationRequest;
		    private Marker mycurrent_location_marker=null;
		    private Animation animShow,animHide;
		    private Integer []icon_image={R.drawable.home,R.drawable.shipment,R.drawable.profile,R.drawable.suport};
		   
		    FragmentManager fragmentManager = null;
		    HomeMap mymap;
		    private static volatile boolean cheked=false; 
		    private int mCurrentSelectedPosition = 0;
		    Context context;
		    
		    private  Contact_Us contact=new Contact_Us(this);
		    private  Profile3 profile=new Profile3(this);
		 
		    //private mapFragment
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        context=getApplicationContext();
	        
	        mymap=new HomeMap(context);
	        setContentView(R.layout.activity_main);
         
	        ActionBar actionBar=getActionBar();
	        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
	        actionBar.setCustomView(R.layout.myswitch);
		  
		    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
			        | ActionBar.DISPLAY_SHOW_HOME);
		    actionBar.setDisplayHomeAsUpEnabled(true);
		    actionBar.setHomeButtonEnabled(true);
		    actionBar.setIcon(R.drawable.ic_drawer);
	
		    mTitle = mDrawerTitle = getTitle();
	        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.left_drawer);
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		   
	       
	      
	        CustomListAdapter adapter=new CustomListAdapter(this, mPlanetTitles, icon_image);
	        mDrawerList.setAdapter(adapter);
	        mDrawerList.setItemChecked(mCurrentSelectedPosition, true);
	       // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
	        
	        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener()
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	            {
	            	if(mDrawerLayout.isDrawerOpen(mDrawerList)) {
	                    mDrawerLayout.closeDrawer(mDrawerList);

	              }
	              else {
	                    mDrawerLayout.openDrawer(mDrawerList);
	              }
	            	
	            	 selectItem(position);
	            }
	        });
	        
	        		
	        
	      
	        
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
           if (savedInstanceState == null) 
            {
	               
	        }
           
            selectItem(mCurrentSelectedPosition); 
	
	    }  // closing of on create method

	 
	    
	
	    @Override
	    public void onBackPressed() {
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	       // alertDialogBuilder.setTitle("Exit Application?");
	        alertDialogBuilder
	                .setMessage("Exit Application?")
	                .setCancelable(false)
	                .setPositiveButton("Yes",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog, int id) {
	                                moveTaskToBack(true);
	                                android.os.Process.killProcess(android.os.Process.myPid());
	                                System.exit(1);
	                            }
	                        })

	                .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int id) {

	                        dialog.cancel();
	                    }
	                });

	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
	    }
	
	
	
	   private void selectItem(int position)
	   {
		   fragmentManager=getFragmentManager();
		   FragmentTransaction	transaction=fragmentManager.beginTransaction();
		   onSectionAttached(position);
		   if(position==0)
		    {
			
			  restoreActionBar(); 
			  cheked=false;
		      transaction.replace(R.id.container,mymap );
		      transaction.commit();
		  
		    }
		   
		   
		   if(position==1 && cheked==false)
	    		
	    	{
			   restoreActionBar(); 
	    		cheked=true;
	    		ActionBar .Tab Tab1, Tab2,Tab3;
	            Canceal cancel=new Canceal(this);
	   		    Confirmed confirm=new Confirmed(this);
	   		    Delivered delivered=new Delivered(this);
	    	   
	    	    ActionBar actionBar = getActionBar();
	    		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	    		// Set Tab Icon and Titles
	    		Tab1 = actionBar.newTab().setText("Confirmed");
	    		Tab2 = actionBar.newTab().setText("Delivered");
	    		Tab3 = actionBar.newTab().setText("Cancelled");

	    		// Set Tab Listeners
	    		Tab1.setTabListener(new TabListener(confirm));
	    		Tab2.setTabListener(new TabListener(delivered));
	    		Tab3.setTabListener(new TabListener(cancel));
	    	//	Tab1.setCustomView(view.b)
	    

	    		// Add tabs to actionbar
	    		actionBar.addTab(Tab1);
	    		actionBar.addTab(Tab2);
	    		actionBar.addTab(Tab3);
	    		actionBar.setSelectedNavigationItem(0);
	    	
	    		
	    		
	        } 
		   
		 /*  if(position==2)
		    {
			  
			  restoreActionBar(); 
			  cheked=false;
		      transaction.replace(R.id.container,payment );
		      transaction.commit();
		    
		    }
		   
		   
		   
		   if(position==3)
		    {
			  
			  restoreActionBar(); 
			  cheked=false;
		      transaction.replace(R.id.container,rating );
		      transaction.commit();
		    
		    }
		   */
		   
		   if(position==2)
		    {
			  
			  restoreActionBar(); 
			  cheked=false;
		      transaction.replace(R.id.container,profile );
		      transaction.commit();
		    
		    }
		   
		   
		   if(position==3)
		    {
			
			  restoreActionBar(); 
			  cheked=false;
		      transaction.replace(R.id.container,contact );
		      transaction.commit();
		   
		    }
	   }
	
	
		
		 private class DrawerItemClickListener implements ListView.OnItemClickListener 
		 {
		        @Override
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        	
		        	
		        
		        }
		  }
		
	
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				
			     if (mDrawerToggle.onOptionsItemSelected(item)) 
			     {
			            return true;
			     }
				
				return super.onOptionsItemSelected(item);
	         } 
			
			
			 @Override
			    public boolean onPrepareOptionsMenu(Menu menu) {
			       boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			       return super.onPrepareOptionsMenu(menu);
			    }
		
			 @Override
			    protected void onStart() {
			        super.onStart();
			     
			    }
			 
		
			 @Override
			    protected void onResume() 
			    {
			        super.onResume();
			    
			    }
			 
			 
			  @Override
			    protected void onPause()
			    {
			    	super.onPause();
			  
			    }
			  
			  
			  @Override
				 protected void onStop() 
				 {
					   
					    super.onStop();
					   // finish();
				 }
			  
			   @Override
				 protected void onDestroy() 
				 {
					   
					    super.onDestroy();
				 }
			
			 public void restoreActionBar() 
				{
			    	
					ActionBar actionBar = getActionBar();
					actionBar.removeAllTabs();
					actionBar.setDisplayShowTitleEnabled(true);
			        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			        actionBar.setTitle(R.string.app_name);
				    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
					actionBar.setCustomView(R.layout.myswitch);
					actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
					        | ActionBar.DISPLAY_SHOW_HOME); 
					TextView title=(TextView) actionBar.getCustomView().findViewById(R.id.img);
					title.setText(mTitle);
					//actionBar.setDisplayShowCustomEnabled(true);
					//actionBar.setDisplayHomeAsUpEnabled(true);
					actionBar.setHomeButtonEnabled(true);
					//getActionBar().setDisplayShowHomeEnabled(true);
					
					actionBar.setIcon(R.drawable.ic_drawer);
				//	actionBar.setHideOnContentScrollEnabled(true); //need api 21

				 }
			 
			 
			 private void onSectionAttached(int number) {
			 		switch (number) {
			 		case 0:
			 			  mTitle=getString(R.string.Home);
			 			  break;
			 		
			 		case 1:
			 			mTitle = getString(R.string.Shipment);
			 			break;
			 		/*
			 		case 2:
			 			mTitle = getString(R.string.payment);
			 			break;
			 			
			 	
			 		case 3:
			 			mTitle = getString(R.string.Rating);
			 			break;*/
			 			
			 		case 2:
			 			mTitle = getString(R.string.profile);
			 			break;
			 		case 3:
			 			mTitle = getString(R.string.support);
			 			break;	
			 		
			 		default:
			 			   break;
			 			
			 		}
			 	}
			
	
		}
	