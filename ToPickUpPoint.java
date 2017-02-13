package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

	public class ToPickUpPoint extends FragmentActivity{
		    
		
		 private DrawerLayout mDrawerLayout;
		 private ActionBarDrawerToggle mDrawerToggle; 
		 private CharSequence mDrawerTitle;
		 private CharSequence mTitle;
		 private ListView mDrawerList;
		 private String[] mPlanetTitles1;
		
	    // private Integer []icon_image={R.drawable.hm,R.drawable.deliverred,R.drawable.profile,R.drawable.support};
	     private Integer []icon_image={R.drawable.home,R.drawable.shipment,R.drawable.profile,R.drawable.suport};
		 FragmentManager fragmentManager = null;
		 ToPickUpPointMap dummymymap;
		 
		 
		 private    Contact_Us contact=new Contact_Us(this);
		 private    Profile3 profile=new Profile3(this);
		 
		 
		 private static volatile boolean cheked=false; 
		 private int mCurrentSelectedPosition = 0;
	 
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        writestateToInternalStorage();
	        dummymymap=new ToPickUpPointMap(this);
	        
	       
	        ActionBar actionBar = getActionBar();
	        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
	        actionBar.setCustomView(R.layout.myswitch);
		  
		
		    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
			        | ActionBar.DISPLAY_SHOW_HOME); 
	
		    
		    actionBar.setDisplayHomeAsUpEnabled(true);
		    actionBar.setHomeButtonEnabled(true);
		    actionBar.setIcon(R.drawable.ic_drawer);
		    
		    
		    mTitle = mDrawerTitle = getTitle();
	        mPlanetTitles1 = getResources().getStringArray(R.array.planets_array);
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.left_drawer);
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		    CustomListAdapter thirdadapter=new CustomListAdapter(this,mPlanetTitles1,icon_image); 
	        mDrawerList.setAdapter(thirdadapter);
	        mDrawerList.setItemChecked(mCurrentSelectedPosition, true);
	      //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		    
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

	            if (savedInstanceState == null) {
	               // selectItem(0);
	              }
	            
	       selectItem(mCurrentSelectedPosition);     
	            

	}///   ending onCreate method
	
    
     
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
     
     
  	    	

	@Override
    public boolean onOptionsItemSelected(MenuItem item) 
	{
			
			
			 if (mDrawerToggle.onOptionsItemSelected(item)) {
		            return true;
		          }
			 
			return super.onOptionsItemSelected(item);
    }
		
		//DrawerLayout Listener start.
		
     private class DrawerItemClickListener implements ListView.OnItemClickListener
     
     {
		   @Override
		   public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		   {
		            //selectItem(position);
		   }
     }
		
	
	
		
		
		@Override
		    protected void onResume() {
		        super.onResume();
		     
		 
		       
		    }
	
		
		
			@Override
			public void onDestroy()
			{
			 super.onDestroy();
			
			 
			}

		
			 @Override
			 protected void onStop() 
			 {
				 super.onStop();
				   
				    
			 }
			 
			 
		     @Override
		     protected void onPause()
		     {
		    	 super.onPause();
		    	// finish();
		    	 
		     }
		
		     @Override
		     protected void onStart() 
		     {
		         super.onStart();
		      
		     
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
				      transaction.replace(R.id.container,dummymymap);
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
			    		
			    		TextView title=(TextView) actionBar.getCustomView().findViewById(R.id.img);
						title.setText(mTitle);

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
				   
				   
				   /*if(position==2)
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
				      transaction.replace(R.id.container,profile );
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
	
		     private void writestateToInternalStorage( ) 
				{
	                    byte b=3;
	                   
				        try 
				        {
				        
				           FileOutputStream fos1 = openFileOutput("status.txt", Context.MODE_PRIVATE);
				           fos1.write(b);
				           fos1.close();
				        
				         } 
				        catch (Exception e) 
				        {
				          Log.e("Error_In_status_saveToInternalStorage_view3", e.getMessage());
				      
				        }
				}
		     
		     
		     
		     
		     private void onSectionAttached(int number) {
		 		switch (number) {
		 		case 0:
		 			  mTitle=getString(R.string.Home);
		 			  break;
		 		
		 		case 1:
		 			mTitle = getString(R.string.Shipment);
		 			break;
		 		
		 		/*case 2:
		 			mTitle = getString(R.string.payment);
		 			break;
		 			
		 	
		 		case 3:
		 			mTitle = getString(R.string.Rating);
		 			break;
		 			*/
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


	

	

