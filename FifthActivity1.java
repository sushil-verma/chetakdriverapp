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

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/* this activity display the path of the delivery point on the map as well as the route 
 * he can travel along the route drawn as well as we can get his location or we can track his location while 
 * on the journey
 */


public class FifthActivity1 extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {
		    
	     private GoogleMap map;     
         private ArrayList<LatLng> markerPoints;
		 private DrawerLayout mDrawerLayout;
		 private ActionBarDrawerToggle mDrawerToggle; 
		 private CharSequence mDrawerTitle;
		 private CharSequence mTitle;
		 private ListView mDrawerList;
		 private String[] mPlanetTitles1;
		 private Location location;
		 private double longitude;
		 private double latitude;
		 private LatLng current_location,destination_location;
	     private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
	     private Location mLastLocation;
	     private GoogleApiClient mGoogleApiClient;
		 private boolean mRequestingLocationUpdates = false;
		 private LocationRequest mLocationRequest;
         private Button nextbutton;
         private double mylongitute;
         private double mylatitute;
         private MapFragment mapFragment;
         private String address=" ";
	     private TextView addressBox;
	     private double distance;
	     private Animation animShow,animHide;
	     private  LinearLayout layout5;
	     private  LinearLayout layout3;
	     private TextView to;
	     private TextView from;
	     private TextView distance_update;
	     private TextView time_update;
	     private TextView distance_update_when_reached;
	     private TextView time_update_when_reached;
	     private String driver_address;
	     private String customer_address;
	     private boolean control_pick_up_button=false;
	     private ProgressDialog dialog=null; 
	     private String retrievedshipmentno=null;
	     private String retrievedimeino=null;
	     private Integer []icon_image={R.drawable.hm,R.drawable.deliverred,R.drawable.profile,R.drawable.support};
	     
	     private void initAnimation()
		  {
		    animShow=AnimationUtils.loadAnimation( this, R.anim.view_show);
		    animHide=AnimationUtils.loadAnimation( this, R.anim.view_hide);
		  }
		    
		 public void show_button()
		  {
		    layout3.setVisibility(View.INVISIBLE);
		    layout5.setVisibility(View.VISIBLE);
		    layout5.setAnimation(animShow);
		    nextbutton.setVisibility(View.VISIBLE);
		  }
		    
		    @Override
		     protected void onPause()
		     {
		    	 super.onPause();
		    	 LocalBroadcastManager.getInstance(this).unregisterReceiver(fifthMessageReceiver);
		   
		     }
	
	/* Broadcast received are the longitute ,latitute and the distance remained in journey
	 * it also checkes weather the driver is near 200 m or not if yes then it shows the button to 
	 * reached slide button
	 */
		    private BroadcastReceiver fifthMessageReceiver = new BroadcastReceiver() 
		     {
		    	 Marker driver_marker;
		         @Override
		         public void onReceive(Context context, Intent intent) {
		             
		        	 System.out.println("Receiving broadcast from the service");
		             double longitute = intent.getDoubleExtra("longitute",0.0);
		             double latitute = intent.getDoubleExtra("latitute",0.0);
		             double distance=intent.getDoubleExtra("distance",0.0);
		             double time=intent.getDoubleExtra("Travel_time",0.0);
		             distance_update.setText(Double.toString(distance));
		             time_update.setText(Double.toString(time));
		    	     distance_update_when_reached.setText(Double.toString(distance));
		    	     time_update_when_reached.setText(Double.toString(time));
		             
		            
		    	     if(distance<0.2 && control_pick_up_button==false)
		      	     {
		      	      control_pick_up_button=true;
		      	      show_button();
		      	     }
		            
		             final LatLng MyPoint = new LatLng(latitute,longitute);	
		             if(driver_marker!=null)
				      {
		            	 driver_marker.remove();
				      }
			    	 
			        Marker driver_marker= map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.newtruck)).position(MyPoint).title("My Current Location"));
			        CameraPosition cameraPosition = new CameraPosition.Builder().target(MyPoint).zoom(16.0f).build();
	                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
	                map.moveCamera(cameraUpdate);
	               
			      
		            }
		       }; 
		       
		       
		      /* @Override
			    protected void onResume() {
			        super.onResume();
			        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("UI_UPDATE_BROADCAST"));
			 
			        checkPlayServices();
			    }
		    
		    */
		    
		    
		
	     @Override
	     protected void onStart() 
	     {
	         super.onStart();
	       
	          if (mGoogleApiClient != null) 
	            {
		            mGoogleApiClient.connect();
		        }
	    
	     }
	     
	     @Override
	     protected void onStop() {
	         super.onStop();
	       
	         try 
	         {
				mGoogleApiClient.disconnect();
				
		     } 
	         catch (Exception e) 
		     {
			    e.printStackTrace();
				System.out.println(" error in fifthActivity in disconnecting");
			 }
			    
	     
	      }
	     
	     

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_test_main);
	        initAnimation();
	    //   
	        SharedPreferences sharedPref = getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
			retrievedshipmentno = sharedPref.getString("shipmentno", "000");
			retrievedimeino = sharedPref.getString("imeino", "000");
	        
	        
	        
	        
	        
	   //   write the active view no in the file
	        dialog= new ProgressDialog(this);
	        writestateToInternalStorage();
	        to=(TextView)findViewById(R.id.Destination5);
	    //  from=(TextView)findViewById(R.id.current_location5);
	        distance_update=(TextView)findViewById(R.id.distance5);
	        time_update=(TextView)findViewById(R.id.time_top_fifth);
	        distance_update_when_reached=(TextView)findViewById(R.id.distance_top_fifth);
	        time_update_when_reached=(TextView)findViewById(R.id.time_top_fifth);
	        
	        layout3=(LinearLayout)findViewById(R.id.button_container5);
	        layout3.setVisibility(View.VISIBLE);
	        
	        layout5=(LinearLayout)findViewById(R.id.top_of_fifthview);
	        layout5.setVisibility(View.INVISIBLE);
	        
	       
	        
	  //    addressBox=(TextView)findViewById(R.id.customer_address);
	        nextbutton=(Button)findViewById(R.id.next5);
	        nextbutton.setVisibility(View.INVISIBLE);
	        mDrawerLayout =(DrawerLayout)findViewById(R.id.fifth);
	        mapFragment =(MapFragment) getFragmentManager().findFragmentById(R.id.map5);
	        ActionBar actionBar = getActionBar();
	        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
	        actionBar.setCustomView(R.layout.myswitch);
	   //   Switch My_toggle = (Switch) actionBar.getCustomView().findViewById(R.id.searchfield);
	   //   My_toggle.setChecked(true);
		    nextbutton.setOnClickListener(this);
		    map=mapFragment.getMap(); 
		    mylocationButton_layout();
		    map.setOnMarkerClickListener(new OnMarkerClickListener() {
	            public boolean onMarkerClick(Marker arg0) {
	                return true;
	            }
	        });
		   // map.getUiSettings().setZoomControlsEnabled(true);
		    
		    if(true)
		    {
		    System.out.println("readign data from server");	
		   
		    mycallable hitting=new mycallable();
	    	Thread localthread=new Thread(hitting);
	    	localthread.start();
	    	try {
				localthread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	longitude=hitting.getlong();
	    	latitude=hitting.getlat();
	    	mylongitute=hitting.getmylongitute();
	    	mylatitute=hitting.getmylatitute();
	    	driver_address=hitting.get_source_address();
	    	customer_address=hitting.get_destination_address();
	   
		    }
		
	   //   from.setText(driver_address);
		    to.setText(customer_address);
		    current_location=new LatLng(mylatitute,mylongitute);
	        destination_location=new LatLng(latitude,longitude);
	   
		    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
			        | ActionBar.DISPLAY_SHOW_HOME); 
		    
		   
	        mTitle = mDrawerTitle = getTitle();
	        mPlanetTitles1 = getResources().getStringArray(R.array.planets_array);
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	        mDrawerList = (ListView) findViewById(R.id.fifthlistview);
	        mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles1));
	        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		    getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);
	        getActionBar().setIcon(R.drawable.ic_drawer);
	        CustomListAdapter adapter=new CustomListAdapter(this, mPlanetTitles1, icon_image);
	        mDrawerList.setAdapter(adapter);
	        
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
	            
	
	            
	   map=mapFragment.getMap(); 
	   View myLocationButton = mapFragment.getView().findViewById(0x2);
	 
	   markerPoints = new ArrayList<LatLng>();
	
	   MarkerOptions options = new MarkerOptions();
	    
	   if(markerPoints.size()>1){
			markerPoints.clear();
			map.clear();					
		}
	   
	   markerPoints.add(current_location);
	   
	   options.position(current_location);
	   
	   if(markerPoints.size()==1){
			//options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
	        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.green32));
	   }
	   
	   map.addMarker(options);
	   
       markerPoints.add(destination_location);

	   options.position(destination_location);
	   
	   if(markerPoints.size()==2)
	   {
	    	//	options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pink32));
	   }
	   
	   map.addMarker(options);
	   
	   if(markerPoints.size() >= 2){					
			LatLng origin = markerPoints.get(0);
			LatLng dest = markerPoints.get(1);
			
			// below lines draw route map on the map but now postponed
			/*String url = getDirectionsUrl(origin, dest);				
		       DownloadTask downloadTask = new DownloadTask();
		       downloadTask.execute(url)*/;
			
			 CameraPosition cameraPosition = new CameraPosition.Builder().target(origin).zoom(5.0f).build();
             CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
             map.moveCamera(cameraUpdate);
             map.setMyLocationEnabled(true);
     
		}
	   
	    }
	   
	   /*map.setOnMapClickListener(new OnMapClickListener() {
	    			
	    			@Override
	    			public void onMapClick(LatLng point) {
	    				
	    				// Already two locations				
	    				if(markerPoints.size()>1){
	    					markerPoints.clear();
	    					map.clear();					
	    				}
	    				
	    				// Adding new item to the ArrayList
	    				markerPoints.add(point);				
	    				
	    				// Creating MarkerOptions
	    				MarkerOptions options = new MarkerOptions();
	    				
	    				// Setting the position of the marker
	    				options.position(point);
	    				
	    				*//** 
	    				 * For the start location, the color of marker is GREEN and
	    				 * for the end location, the color of marker is RED.
	    				 *//*
	    				if(markerPoints.size()==1){
	    					options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
	    				}else if(markerPoints.size()==2){
	    					options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
	    				}
	    							
	    				
	    				// Add new marker to the Google Map Android API V2
	    				map.addMarker(options);
	    				
	    				// Checks, whether start and end locations are captured
	    				if(markerPoints.size() >= 2){					
	    					LatLng origin = markerPoints.get(0);
	    					LatLng dest = markerPoints.get(1);
	    					
	    					// Getting URL to the Google Directions API
	    					String url = getDirectionsUrl(origin, dest);				
	    					
	    					DownloadTask downloadTask = new DownloadTask();
	    					
	    					// Start downloading json data from Google Directions API
	    					downloadTask.execute(url);
	    				}
	    				
	    			}
	    		});
	            
	
	    }*/

	  /*  @Override
	    public void onMapReady(GoogleMap map) {
	        // Add a marker in Sydney, Australia, and move the camera.
	    	this.map=map;
	    	final LatLng MyPoint = new LatLng(28.5186866 , 77.081841);	
	    	final LatLng YourPoint = new LatLng(26.5186866 , 71.081841);	
	    	
	        map.addMarker(new MarkerOptions().position(MyPoint).title("chetak cargo"));
	        map.addMarker(new MarkerOptions().position(YourPoint).title("Customer"));
	        map.moveCamera(CameraUpdateFactory.newLatLng(MyPoint));
	       // Marker TP = googleMap.addMarker(new MarkerOptions().position(MyPoint).title("chetak cargo"));
	        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        map.getUiSettings().setZoomGesturesEnabled(true);
	        
	       // googleMap.moveCamera(CameraUpdateFactory.newLatLng(MyPoint));
	        
	        Polyline line = map.addPolyline(new PolylineOptions()
	        .add(MyPoint,YourPoint)
	        .width(5)
	        .color(Color.RED));
	        // code for google map direction start from here
	        
	        markerPoints = new ArrayList<LatLng>();
	        map.setMyLocationEnabled(true);	
	        
        
	        
	       }*/
	    
	    public boolean isOnline() 
	    {
		    ConnectivityManager cm =
		                      (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    return netInfo != null && netInfo.isConnectedOrConnecting();
		}
	    
	    public class hitdata
   	   {
   	     public String IMEI_NO;
   	 
   	   }
	
	    
	 private  class mycallable implements Runnable
		{
		    hitdata ht;
		    private volatile double  longi;
		    private volatile double  lati;
		    private volatile double  mylongi;
		    private volatile double  mylati;
		    private volatile String  source;
		    private volatile String  destination;
		    final TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			
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
		  
			public void run()
			{
				
				try {
					
					   Log.i("ready to hit the server", " ....");
					 
					  // final String request="http://192.168.0.204/online/mobilelocation.php?msg="+ht.IMEI_NO+"%23"+"ok";
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
								
								 Toast.makeText(getApplicationContext(),"Error in time reading",Toast.LENGTH_LONG).show();
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
		}
	    	

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
	
		private class DrawerItemClickListener implements ListView.OnItemClickListener 
		{
		        @Override
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		        {
		            //selectItem(position);
		        }
		}
		
		// code for google directio
		
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
						lineOptions.width(2);
						lineOptions.color(Color.RED);	
						
					}
				
					map.addPolyline(lineOptions);							
				}			
	 
	}
		 
		 
		 private void displayLocation() {
			 
		        mLastLocation = LocationServices.FusedLocationApi
		                .getLastLocation(mGoogleApiClient);
		 
		        if (mLastLocation != null) {
		            double latitude = mLastLocation.getLatitude();
		            double longitude = mLastLocation.getLongitude();
		 
		        } 
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
	
		 @Override
	    protected void onResume() {
		    super.onResume();
		     LocalBroadcastManager.getInstance(this).registerReceiver(fifthMessageReceiver, new IntentFilter("FIFTH_UI_UPDATE_BROADCAST"));
		     checkPlayServices();
	    }
		 
	
		 @Override
			public void onConnectionFailed(ConnectionResult result) 
		    {
		      System.out.println("connection failed");
			}

			@Override
			public void onConnected(Bundle connectionHint) 
			{
		    
			}

			@Override
			public void onConnectionSuspended(int cause) 
			
			{
			  System.out.println("error in connection server");
	        }
		
			private void mylocationButton_layout()
			{
			
			View myLocationButton = mapFragment.getView().findViewById(0x2);
		       
			   if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			        // location button is inside of RelativeLayout
			        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();

			        // Align it to - parent BOTTOM|LEFT
			       // params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			       // params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			       // params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

			        // Update margins, set to 10dp
			        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25,getResources().getDisplayMetrics());
			        params.setMargins(margin, margin, margin, margin);

			        myLocationButton.setLayoutParams(params);
			        
			      } 
			}
			
			@Override
			public void onClick(View v) 
			{
				hitserver(retrievedshipmentno,retrievedimeino);
				Intent mynext=new Intent(this,PODokNOTok.class);
				startActivity(mynext);
				this.finish();
				//android.os.Process.killProcess(android.os.Process.myPid());

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
			
			private void writestateToInternalStorage() 
			{
				   byte b=5;
			        try {
			       
			        FileOutputStream fos = openFileOutput("status.txt", Context.MODE_PRIVATE);
			   
			        
			        fos.write(b);
			        //image.compress(Bitmap.CompressFormat.PNG, 100, fos);
			      
			        fos.close();
			        
			            } 
			        catch (Exception e) 
			        {
			        Log.e("Error_In_status_saveToInternalStorage_view5", e.getMessage());
			      
			        }
			 }
			
			private void hitserver(String shipment,String imei)
			{
				/*SharedPreferences sharedPref = getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
				String retrievedshipmentno = sharedPref.getString("shipmentno", "000");
				String retrievedimeino = sharedPref.getString("imeino", "000");*/
				dialog.setMessage("Please wait...");
				dialog.show();
				hitmessage hit=new hitmessage(imei,shipment);
				Thread t=new Thread(hit);
				t.start();
				try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
				System.out.println("server hitted");
				dialog.dismiss();	
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
	
}

