package com.example.chetakdriverapp;



import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class Mediaservice extends Service {
	
	MediaPlayer mp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate()
	{
		mp=MediaPlayer.create(this,R.raw.alrm2);
		mp.setLooping(true);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		
		mp.start();
		return android.app.Service.START_STICKY;
	}
	
	
	 @Override
	  public void onDestroy()
	  {
		   if(mp.isPlaying())
		   {
		   try {
			    
			    mp.pause();
				mp.stop();
			    mp.release();
		       } catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				System.out.println("error in stoping");
				e.printStackTrace();
			  } 
		   
		   }
		   
		   super.onDestroy();
	  }
	
	 
	 
	

}
