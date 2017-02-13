package com.example.chetakdriverapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




public class MyStageAdapter extends BaseAdapter {
	
	
	 private Context context;
   
     private ArrayList<Stage> transit=new ArrayList<Stage>(10);
     
       private  ImageView driver_image;
	 
	    private	Bitmap pics=null;
	    private FileInputStream fis=null;
	    
   
	

	public MyStageAdapter(Context con ,ArrayList<Stage> mydata)
	{
		context=con;
		transit=mydata;
		
	}
	
	MyStageAdapter()
	{
		
	}
	
	
     
     
     
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return transit.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return transit.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View custome_layout=convertView;
		Stage record_holder=transit.get(position);
		
		try {
			fis = context.openFileInput("desiredFilename1.png");
		    } 
		catch (FileNotFoundException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(fis!=null)
		{
        
		  pics=BitmapFactory.decodeStream(fis);
         try {
  			   fis.close();
  		     } 
         
         catch (IOException e) 
         {
  			
  			e.printStackTrace();
  		  }
         
		}

		if(convertView==null)
		{
	     LayoutInflater  Myinflate= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	     custome_layout=(LinearLayout)Myinflate.inflate(R.layout.stagelist_data, null);
	     
	 	TextView msgbox = (TextView) custome_layout.findViewById(R.id.msgbox);
		TextView datetimebox = (TextView) custome_layout.findViewById(R.id.msgdatetime);
		
		/*ImageView image=(ImageView)custome_layout.findViewById(R.id.confirmdriverimage);
		image.setImageBitmap(pics);*/
		
		msgbox.setText(record_holder.getBoxmessage());
		datetimebox.setText(record_holder.getBoxdatatime());
		
		}
	
		return custome_layout;

	}
	
	
	
	


}