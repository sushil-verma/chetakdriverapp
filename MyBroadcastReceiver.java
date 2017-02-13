package com.example.chetakdriverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		
		if(intent.getAction()=="super")
		{	
			System.out.println("broadcast received for starting="+intent.getAction());
	        Intent intent1=new Intent(context,Mediaservice.class);
	        context.startService(intent1);
	       
		}
	
	      if(intent.getAction()=="sunny")
	      {
	       System.out.println("broadcast received for stopping="+intent.getAction());
		    
	       Intent intent1=new Intent(context,Mediaservice.class);
	       context.stopService(intent1);//startService(intent1);
	     /*  Intent intent123=new Intent(context,ThirdActivity.class);
	       intent123.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	       context.startActivity(intent123);*/
		
	     }
	      
	      
	    
	}

}
