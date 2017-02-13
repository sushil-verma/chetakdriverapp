package com.example.chetakdriverapp;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SeventhActivity1 extends Activity implements OnClickListener {
	Button delivered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seventh);
		ActionBar action=getActionBar();
		action.hide();
		writestateToInternalStorage();
		delivered=(Button)findViewById(R.id.delivered);
		delivered.setOnClickListener(this);
	
	  }

	@Override
	public void onClick(View v) {
		hitserver();
		Intent intent=new Intent(this,FirstActivity.class);
		startActivity(intent);
		finish();
		
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
		   byte b=0;
	        try {
	       
	        FileOutputStream fos = openFileOutput("status.txt", Context.MODE_PRIVATE);
	   
	        
	        fos.write(b);
	        //image.compress(Bitmap.CompressFormat.PNG, 100, fos);
	      
	        fos.close();
	        
	            } 
	        catch (Exception e) 
	        {
	        Log.e("Error_In_status_saveToInternalStorage_view4", e.getMessage());
	      
	        }
	 }
	
	private void hitserver()
	{
		SharedPreferences sharedPref = getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
		String retrievedshipmentno = sharedPref.getString("shipmentno", "000");
		String retrievedimeino = sharedPref.getString("imeino", "000");
		hitmessage hit=new hitmessage(retrievedimeino,retrievedshipmentno);
		Thread t=new Thread(hit);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("server hitted");

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
			
	        final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+"delivered"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
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
