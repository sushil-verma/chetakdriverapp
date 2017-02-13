package com.example.chetakdriverapp;


import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class Receive extends BroadcastReceiver {

	 protected LocationManager locationManager;
	 public static boolean isGPSEnabled = false;
	 boolean isNetworkEnabled = false;
	 
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		
		     locationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
			
		     isGPSEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
		     isNetworkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
		     
		     Intent intent =new Intent(context,Myservice.class);
		     context.startService(intent);
			
		
		
     	 /*  if(isGPSEnabled ||isNetworkEnabled)
		    {
		     Intent intent =new Intent(context,Myservice.class);
		     context.startService(intent);
		    }
    	*/
			

	}
	
	   boolean knowtheapplicaiton(String processName,Context c)
	    {
	    	 if (processName == null) 
	    	  return false;

	    	 ActivityManager manager = 
	    	    (ActivityManager) c.getSystemService(c.ACTIVITY_SERVICE);
	    	 List<RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
	    	 for (RunningAppProcessInfo process : processes)
	    	 {
	    	    if (processName.equals(process.processName))
	    	    {
	    	        return true;
	    	    }
	    	 }
	    	 return false;
	    	}

}
