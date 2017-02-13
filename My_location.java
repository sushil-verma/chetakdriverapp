package com.example.chetakdriverapp;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class My_location implements ConnectionCallbacks, OnConnectionFailedListener {
	
	private GoogleApiClient mGoogleApiClient;
    private Location location;
    double longitude;
	double latitude;
	private LatLng current_location;
	
	public My_location()
	{
		   mGoogleApiClient.connect();
		  if (mGoogleApiClient == null) {
	            
			  mGoogleApiClient = new GoogleApiClient.Builder(null, this, null)
	                .addConnectionCallbacks(this)
	                .addOnConnectionFailedListener(this)
	                .addApi(LocationServices.API)
	                .build();
	        }
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		 location = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient);
		
        if (location != null) {
        	
        	
          longitude=location.getLongitude();
          latitude= location.getLatitude();
          current_location=new LatLng(latitude, longitude);
          
          }
        mGoogleApiClient.disconnect();
        
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}
	
	
	public LatLng get()
	{
		return current_location;
	}
	

}
