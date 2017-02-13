package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.chetakdriverapp.MyAlert.Rejecthit;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyAlertNotificationDirect extends Activity implements OnClickListener {

	/*MyBroadcastReceiver receiver;
	IntentFilter filter;*/
	private CountDownTimer countDownTimer;
	private final long startTime = 30 * 1000;
    private final long interval = 1 * 1000;
    private TextView source=null;
    private TextView destination=null;
    private TextView km=null;
    private TextView price=null;
    private TextView time=null;
    private TextView eta=null;
    private TextView shipment_no=null;
    
    private Button Accept;
    private Button Reject;
    private int buttonlock=0;
    NotificationManager mNotificationManager;
    private static final int MY_NOTIFICATION_ID= 1;
    private static String src=null;
    private static String des=null;
    private static String kilo=null;
    private static String prc=null;
    private static String etavalue;
    private static String timevalue;
    private static String shipmentvalue;
	// Creates an explicit intent for an Activity in your app
	
		 @Override
		 protected void onCreate(Bundle savedInstanceState) {
		     super.onCreate(savedInstanceState);
		     getWindow().setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL);

		     // ...but notify us that it happened.
		     getWindow().setFlags(LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

		     mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		     
		     setContentView(R.layout.activity_my_alertdirect);
		     source=(TextView)findViewById(R.id.myalertsource);
		     destination=(TextView)findViewById(R.id.myalertdestination);
		     km=(TextView)findViewById(R.id.myalertkm);
		     price=(TextView)findViewById(R.id.myalertfreight);
		     
		     time=(TextView)findViewById(R.id.myalerttime);
		     eta=(TextView)findViewById(R.id.myalerteta);
		     
		     Accept=(Button)findViewById(R.id.accept);
		     Accept.setOnClickListener(this);
		     Reject=(Button)findViewById(R.id.reject);
		     Reject.setOnClickListener(this);
		    
		     
		     Intent localintent=getIntent();
		     src=localintent.getStringExtra("source");
		     des=localintent.getStringExtra("destination");
		     kilo=localintent.getStringExtra("km");
		     prc =localintent.getStringExtra("rate");
		     etavalue =localintent.getStringExtra("eta");
		     timevalue =localintent.getStringExtra("Travel_time");
		     shipmentvalue=localintent.getStringExtra("shipmetn_no");
		     if(shipmentvalue==null) /// this is done so to avoid software crash when shipmentvalue in null;
			    shipmentvalue="000";
		     
		    // Readorderdetail();
		    
		     source.setText(src);
		     destination.setText(des);
		     km.setText(kilo);
		     price.setText(prc);
		     eta.setText(etavalue);
		     time.setText(timevalue);
		   
		    // for testing purpose
		  /*   source.setText("new delhi");
		     destination.setText("bombay");
		     
		     km.setText("500");
		     price.setText("1000");*/
	
   }
		

		 
		 @Override
		  public boolean onTouchEvent(MotionEvent event) 
		 {
		    // If we've received a touch notification that the user has touched
		    // outside the app, finish the activity.
		    if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
		      //finish();
		      return false;
		    }

		    // Delegate everything else to Activity.
		    return super.onTouchEvent(event);
		  }


	@Override
	public void onClick(View v) 
    {
	  	if(buttonlock==0)
	  	{
	     buttonlock++;	
		 Intent i = new Intent("sunny"); // this is broadcast to stop alarm
   	     sendBroadcast(i);
         stopService(getIntent()); 
         
         switch(v.getId())
         {
         
         
             case R.id.accept:
             {
            	 Intent intent=new Intent(this,OrederAccept.class);
                 startActivity(intent); 
                 String ns = Context.NOTIFICATION_SERVICE;
                 finish();
              }
         
             case R.id.pass:
            	Rejecthit   message=new Rejecthit(shipmentvalue);  // here shipmentid will be same for all but vehicle id will come from the radio button click option
 				Thread t=new Thread(message);
 				t.start();
         	   
 				String response=message.getReposnse();
 				
 				
 				if(response!=null)
 				Toast.makeText(this,"Shipment canceled succussfully",Toast.LENGTH_SHORT).show();
 				
 			    finish();
                 break;
                 
                 default: 
                 
                 break;
         
            }
            
        
	  	 }
  }
			
	@Override
	protected void onStop()
	{
		super.onStop();
		 NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(R.drawable.chetak)
			        .setContentTitle("Notification")
			        .setContentText("A new Order!")
			        .setAutoCancel(true);
		Intent intent = new Intent(this, MyAlertNotificationDirect.class);
	    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		    intent.putExtra("source",src);
	        intent.putExtra("destination",des);
	        intent.putExtra("rate",prc);
	        intent.putExtra("km",kilo);
	        intent.putExtra("eta",etavalue);
	        intent.putExtra("Travel_time",timevalue);
	        intent.putExtra("shipmetn_no",shipmentvalue);
		       
		       

	
		PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0,intent, 0);
		mBuilder.setContentIntent(pending);
	
		// mId allows you to update the notification later on.
		mNotificationManager.notify(MY_NOTIFICATION_ID, mBuilder.build());


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
		 
	@Override
	protected void onResume()
	{
		super.onResume();
	}	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
       // nMgr.cancelAll();
		NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notif.cancel(MY_NOTIFICATION_ID);
	}
	
	
	/*private void Readorderdetail()
	{
		SharedPreferences sharedPref = getSharedPreferences("Orderfile", Context.MODE_PRIVATE);
		src = sharedPref.getString("source", "000");
	    des = sharedPref.getString("destination", "000");
	    kilo = sharedPref.getString("rate", "000");
	    prc = sharedPref.getString("km", "000");
	}
	*/
	
	
	class Rejecthit implements Runnable
	{

		private URL url=null;
		private HttpURLConnection con=null ;
		private BufferedReader bufferinput=null;
		private  volatile String mylocal;
		private volatile String shipmentid=null;
		private final  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
   	    String url_string =null;

		
		public Rejecthit(String shipmentid)
		{
			this.shipmentid=shipmentid;
			
			url_string="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+telephonyManager.getDeviceId()+"%23"+shipmentid+"%23"+"reject";
		
		}
		
		public String getReposnse()
		{
			return mylocal;
		}
		@Override
		public void run() {
			
	     			
			try {
				  url = new URL(url_string);
				  con = (HttpURLConnection) url.openConnection();
				  
		          bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
		           try 
		           {
					  mylocal = bufferinput.readLine();
					
					  System.out.println("json data"+mylocal);
					  bufferinput.close();
				    } 
		           catch (IOException e) 
				   {
					// TODO Auto-generated catch block
					  e.printStackTrace();
				    }
		            catch (Exception e) 
		            {
					  // TODO Auto-generated catch block
					     System.out.println("json data not fetched");
				    }
			         
			    } 
			
			   catch (MalformedURLException e) 
			   {
			     e.printStackTrace();
			   } 
			   
			   catch (IOException e) 
			   {
				 e.printStackTrace();
			   }
			
			con.disconnect();
		
		}
		
	  }
		
}
