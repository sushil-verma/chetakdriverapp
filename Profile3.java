package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Profile3 extends Fragment
{
    Context context;
    
   private static volatile TextView f_name;
   private static volatile  TextView l_name;
   private static volatile	TextView mobile;
   private static volatile ImageView driver_image;
  	
  	private String Imagelinkurl=null;
    private String Image_url_Received=null;
    boolean result=false;
	
     
    FileInputStream fis=null;
    private	Bitmap pics=null;
    private SharedPreferences driver_data;
    private String xx=null;
  
   
    ImageView dfltpics;
    
    private RoundImage roundedImage;
    
    private FileInputStream fos=null;
 
    
    private static volatile int x=1;

   public Profile3(Context c)
	{
	   context=c;
	
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.myprofilecontent2, null);
        
    	 driver_data=getActivity().getSharedPreferences("driverdate",getActivity().MODE_PRIVATE);
        
    	 
    	 f_name=(TextView)root.findViewById(R.id.first_name);
         l_name=(TextView)root.findViewById(R.id.last_name);
         mobile=(TextView)root.findViewById(R.id.profilemobile);
         driver_image=(ImageView)root.findViewById(R.id.imageView_round);
        
         
         
         xx=driver_data.getString("first_name", "F_name");
         f_name.setText(driver_data.getString("first_name", "F_name"));
         l_name.setText(driver_data.getString("last_name", "L_name"));
         mobile.setText(driver_data.getString("mobile_no", "M_no"));
         
      
		try {
			fos = getActivity().openFileInput("desiredFilename1.png");
		    } 
		catch (FileNotFoundException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(fos!=null)
		{
        
		  pics=BitmapFactory.decodeStream(fos);
         try {
  			   fos.close();
  		     } 
         
         catch (IOException e) 
         {
  			
  			e.printStackTrace();
  		  }
         
         driver_image.setImageBitmap(pics);
         
		}
         
       
        return root;
    }
 
  
   
    

}
