package com.example.chetakdriverapp;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LastActivityHomeFregment extends Fragment implements OnClickListener
{

	Context context;

    Button delivered;
   
    private  static final String MyPREFERENCES ="MyPrefs" ;
    private SharedPreferences sharedpreferences;
	private SharedPreferences.Editor editor;
	
	// never delete this
	     private SharedPreferences Activity_log;
	     private SharedPreferences.Editor activity_log_editor;
	     
	     private SharedPreferences Activity_log_time;
	     private SharedPreferences.Editor activity_log_time_editor;
	     private  SharedPreferences sharedPref=null;
	     private String retrievedshipmentno=null;
	     private String retrievedimeino=null;
	     private ProgressBar myprogressbar=null;
	     
	     

    public LastActivityHomeFregment(Context c)
    {
    	context=c;
   
    }
    

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		// inflat and return the layout
		View v = inflater.inflate(R.layout.activity_seventh, container, false);
	
	
		delivered=(Button)v.findViewById(R.id.delivered);
       
        delivered.setOnClickListener(this);
        
        myprogressbar=(ProgressBar)v.findViewById(R.id.mprogress);
        
        Activity_log=getActivity().getSharedPreferences("Activity_log",context.MODE_PRIVATE);
        activity_log_editor=Activity_log.edit();
        
        
        Activity_log_time=getActivity().getSharedPreferences("Activity_log_time",context.MODE_PRIVATE);
        activity_log_time_editor=Activity_log_time.edit();
        
        sharedPref = context.getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
 	    retrievedshipmentno = sharedPref.getString("shipmentno", "000");
 	    retrievedimeino = sharedPref.getString("imeino", "000");
	     
	    return v;
	}
	
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) 
	    {
	       super.onActivityCreated(savedInstanceState);
	      
	      
	    
	    }
	
	
	@Override
	public void onResume() 
	 {
		super.onResume();
		
	  }
	
	@Override
	public void onPause() 
	{
		super.onPause();
		//getActivity().finish();
		
    }
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		
	}
	
	@Override
	public void onLowMemory() 
	{
		super.onLowMemory();
	
	}

  

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	
	myprogressbar.setVisibility(View.VISIBLE);
	activity_log_editor.putInt("reached_origin",0);
	activity_log_editor.putInt("collect_document",0);
	activity_log_editor.putInt("start_trip",0);
	activity_log_editor.putInt("At_destination",0);
	activity_log_editor.putInt("send_pod",0);
	activity_log_editor.commit();
	
	
	activity_log_time_editor.putString("reached_origin_time", "0000-00-00#00:00:00");
	activity_log_time_editor.putString("collect_document_time", "0000-00-00#00:00:00");
	activity_log_time_editor.putString("start_trip_time", "0000-00-00#00:00:00");
	activity_log_time_editor.putString("At_destination_time", "0000-00-00#00:00:00");
	activity_log_time_editor.putString("send_pod_time", "0000-00-00#00:00:00");
	
	activity_log_time_editor.commit();
	
	
	
	
	
	if(hitserver()==1)
	{
		
	 Toast.makeText(getActivity().getApplicationContext(),"DELIVERY SUCCESFULL", Toast.LENGTH_LONG).show();	
	 Intent intent=new Intent(context,SplasScreen.class);
	 context.startActivity(intent);
	
	 writestateToInternalStorage() ;
	
	 myprogressbar.setVisibility(View.INVISIBLE);
	 getActivity().finish();
	
	}
	
	else
 	   Toast.makeText(getActivity().getApplicationContext(),"Error...Pls Redeliver", Toast.LENGTH_LONG).show();
	
}




     public class hitdata
	 {
		public String IMEI_NO;
	 }
    
  
    // this hit to send the msg that vehicle has reached the destination point 
     private int hitserver()
 	{
 		
 		hitmessage hit=new hitmessage(retrievedimeino,retrievedshipmentno);
 		Thread t=new Thread(hit);
 		t.start();
 		try {
 			t.join();
 		} catch (InterruptedException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		int realresponse=hit.Getresponse();
 		
 		
 		
 		return realresponse;

 		//System.out.println("server hitted");

 	}
 	
 	class hitmessage implements Runnable
 	{

 		private URL url=null;
 		private HttpURLConnection con=null ;
 		private String imei_no;
 		private String shipment_no;
 		
 		private int response;
 		
 		int Getresponse()
 		{
 			
 			return response; 
 		}
 		
 		 hitmessage(String imei, String shipmentno)
 		{
 			imei_no=imei;
 			shipment_no=shipmentno;
 		}
 		@Override
 		public void run() 
 		{
 			
 	        final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+"delivered"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
 			try {
 				  url = new URL(request);
 				  con = (HttpURLConnection) url.openConnection();
 				  if(con.getResponseMessage()!=null)
 				   response=1;
 				 
 				  else
 				 
 			 	   response=0;
 			     
 		         
 			    }
 		          
 			    catch (Exception e) 
 		        {
 					  // TODO Auto-generated catch block
 			       Toast.makeText(getActivity(),"Error", Toast.LENGTH_LONG).show();
 				}
 			         
 	    } 
 	
 	

  }
	
	
	
	  
	  
	// this is to store current stage to the memory to maintain current session  	 
	private void writestateToInternalStorage() 
	{
		   byte b=0;
	        try {
	       
	        FileOutputStream fos = context.openFileOutput("status.txt", Context.MODE_PRIVATE);
	   
	        
	        fos.write(b);
	        //image.compress(Bitmap.CompressFormat.PNG, 100, fos);
	      
	        fos.close();
	        
	            } 
	        catch (Exception e) 
	        {
	        Log.e("Error_In_status_saveToInternalStorage_view4", e.getMessage());
	      
	        }
	 }
	   
	   
	  
	   public boolean isOnline() 
		 {
			    ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			    NetworkInfo netInfo = cm.getActiveNetworkInfo();
			    return netInfo != null && netInfo.isConnectedOrConnecting();
	     }
		    		

}
