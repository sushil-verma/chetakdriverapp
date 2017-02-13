package com.example.chetakdriverapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;

public class GpsAlert extends Activity {

	  Intent intent;
	  
		 @Override
		 protected void onCreate(Bundle savedInstanceState) {
		     super.onCreate(savedInstanceState);
		    // setContentView();
		        ActionBar action=getActionBar();
				action.hide();
		       intent=new Intent(this,SplasScreen.class);
		    
		      CustomDialog.Builder alertDialogBuilder = new CustomDialog.Builder(this);
		      alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF7F27'>GPS OFF</font>"));

		     // set dialog message
		     alertDialogBuilder
		             .setMessage(Html.fromHtml("<font color='#FF7F27'>Switch On Gps</font>"))
		             .setCancelable(false)
		             .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>yes</font>"), new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface dialog, int id) {
		                     
		                     startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		                     
		                     dialog.cancel();
		                 
		                     finish();
		                 }
		             })
		             .setNegativeButton(Html.fromHtml("<font color='#FF7F27'>No</font>"), new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface dialog, int which) { 
		                	 dialog.cancel();
		                     finish();
		                 }
		              })
		             .setIcon(android.R.drawable.ic_dialog_alert)
		             .show();
		     
		    // alertDialogBuilder.show();
		     
		     
		    
		     // create alert dialog
		   //  AlertDialog alertDialog = alertDialogBuilder.create();

		     // show it
		    // alertDialog.show();
		
   }
		
@Override
protected void onResume() 
{
super.onResume();
		        
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
