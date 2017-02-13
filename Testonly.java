package com.example.chetakdriverapp;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Testonly extends Activity {
	
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_alert);
		
	    btn=(Button)findViewById(R.id.acceptindirect);
	    
	    
		
		//blink();
	}

	private void blink(){
	    final Handler handler = new Handler();
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
	        int timeToBlink = 1000;    //in milissegunds
	        try{Thread.sleep(timeToBlink);}catch (Exception e) {}
	           
	        handler.post(new Runnable() {
	                @Override
	                    public void run() {
	                   
	                	ColorStateList mList = btn.getTextColors();
	                	int color = mList.getDefaultColor();

	                	switch(color)
	                	{
	                	case Color.WHITE:
	                		btn.setTextColor(Color.RED);
	                	break;

	                	case Color.RED:
	                		btn.setTextColor(Color.WHITE);
	                	break;

	                	}
	                    blink();
	                }
	                });
	            }
	        }).start();
	    }
	

	
}
