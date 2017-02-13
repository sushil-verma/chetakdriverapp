package com.example.chetakdriverapp;

import java.io.FileOutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class ShipCancealAlert extends Activity {

	
         private  Intent first;
         private  SharedPreferences orderrecord;
		 private  SharedPreferences orderrecord_destination;
		 private Editor editor;
		 @Override
		 protected void onCreate(Bundle savedInstanceState) {
		     super.onCreate(savedInstanceState);
		     
		     ActionBar action=getActionBar();
		     action.hide();
		     
		     writestateToInternalStorage();
		   
		     CustomDialog.Builder alertDialogBuilder = new CustomDialog.Builder(this);
		  
		       first=new Intent(this,SplasScreen.class);
		       
		       alertDialogBuilder.setTitle( Html.fromHtml("<font color='#FF7F27'>Alert</font>"));
			 
		      // alertDialogBuilder.setTitle("Alert");
             
		       alertDialogBuilder
		             .setMessage(Html.fromHtml("<font color='#FF7F27'>Shipment Cancelled</font>"))
		             .setCancelable(false)
		             .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>OK</font>"), new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface dialog, int id) {
		                    
		                	   dialog.cancel();
		                	  
							   startActivity(first); 
							   
							   finish();
		                	
		                  
		                 }
		             })
		             
		            
		       
		            .setIcon(android.R.drawable.ic_dialog_alert);
	   
		            alertDialogBuilder.show();
		    
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
			public boolean onKeyDown(int keyCode, KeyEvent event) 
			{
			    if(keyCode == KeyEvent.KEYCODE_BACK)
			   {
			   
			    return true;
			   }
			    
			    return false;
			}
		 
		
		 @Override
			protected void onDestroy()
			{
				super.onDestroy();
				
				  orderrecord = getSharedPreferences("orderdetail", Context.MODE_PRIVATE);
		         
		            editor = orderrecord.edit();
		          
		    	    editor.putString("c_name"," ");
		    	    editor.putString("c_address","  ");
		    	    editor.putString("c_mobime","  ");
		    	    editor.putString("ttime","  ");
		    	    editor.putString("tdate","  ");
		    	    editor.putString("km", "  ");
		    	    editor.putString("rate","  ");
		    	    editor.putString("eta","  " );
		    	    editor.commit();
		    	
		    	    orderrecord_destination=getSharedPreferences("Orderfile", Context.MODE_PRIVATE);
		    	    editor=orderrecord_destination.edit();
		    	    editor.putString("shipment"," ");
		    	    editor.putString("destination"," ");
		    	    editor.commit();
				
				
			}
		 
		 
		 
		 private void writestateToInternalStorage( ) 
			{
                 byte b=0;
                
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

		 
		 private static class CustomDialog extends AlertDialog 
		 {

		     private CustomDialog(Context context) {
		         super(context);
		     }

		     /**
		      * {@inheritDoc}
		      */
		     @Override
		     protected void onCreate(Bundle savedInstanceState) {
		         super.onCreate(savedInstanceState);
		         final Resources res = getContext().getResources();
		         final int id = res.getIdentifier("titleDivider", "id", "android");
		         final View titleDivider = findViewById(id);
		         if (titleDivider != null) {
		             titleDivider.setBackgroundColor(Color.RED);
		         }
		     }
		 		
		 }

		
}
