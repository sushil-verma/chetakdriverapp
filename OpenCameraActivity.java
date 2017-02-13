package com.example.chetakdriverapp;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chetakdriverapp.ToDestinationPointMap.hitmessage;


@SuppressWarnings("deprecation")
public class OpenCameraActivity extends Activity implements OnClickListener {
	
    private ArrayList<Bitmap> imageIDs=new ArrayList<Bitmap>(10);
 	private Gallery gallery;
    final static int REQUEST_IMAGE_VIEW=1;
    private Bitmap bitmap=null;
    private final String KEY_IMAGE="image";
    private Button submit;
    private Button open_gallary;
    private  String UPLOAD_URL="http://121.241.125.91/cc/mavyn/online/upload.php";
    private ImageView imageView;
    private Intent intent123;
    private	 ActionBar actionBar;
    private Intent intent;
    private int index=1;
    private ImageButton addpics;
    private ImageButton deletepics;
    private static int position_of_pics=0;
    private ProgressDialog loading=null;
    private int podstatus=0;
    private String retrievedshipmentno=null;
    private String retrievedimeino=null;
    private String podmessage="ok";
    
  /*  private SharedPreferences Activity_log;
    private SharedPreferences.Editor activity_log_editor;
    
    private SharedPreferences Activity_log_time;
    private SharedPreferences.Editor activity_log_time_editor;
   */
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opencamera);
	   // writestateToInternalStorage();
		
		/* Activity_log=getSharedPreferences("Activity_log",Context.MODE_PRIVATE);
         activity_log_editor=Activity_log.edit();
         
         
         Activity_log_time=getSharedPreferences("Activity_log_time",Context.MODE_PRIVATE);
         activity_log_time_editor=Activity_log_time.edit();*/
		
	    podstatus=getIntent().getIntExtra("podstatus",0);
		Readimeiandshipment();
		
		if(podstatus==1)
		podmessage="podok";
	    else
	    podmessage="podnotok";
	   
		imageView = (ImageView) findViewById(R.id.image1);
        intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUEST_IMAGE_VIEW);
	    intent123=new Intent(this,LastActivity.class);
    	getActionBar().setDisplayHomeAsUpEnabled(false);
	    getActionBar().setHomeButtonEnabled(false); 
	    getActionBar().setDisplayUseLogoEnabled(false);
   
	    actionBar = getActionBar();
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d84c00")));
	    actionBar.setCustomView(R.layout.cameraactionbar);
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		      //  | ActionBar.DISPLAY_SHOW_HOME);
	    submit=(Button)actionBar.getCustomView().findViewById(R.id.send);
	    submit.setOnClickListener(this);
	    addpics=(ImageButton)actionBar.getCustomView().findViewById(R.id.take_photo);
	    addpics.setOnClickListener(this);
	    deletepics=(ImageButton)actionBar.getCustomView().findViewById(R.id.delete);
	    deletepics.setOnClickListener(this);

     // Note that Gallery view is deprecated in Android 4.1---
	 	gallery = (Gallery) findViewById(R.id.gallery1);
	 //	gallery.setLayoutDirection(LayoutDirection.RTL);
		
		//LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600,400);
		//imageView.setLayoutParams(layoutParams);
		
		
	    gallery.setAdapter(new ImageAdapter(this));
	   
	    gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position,long id)
		{
	
					position_of_pics=position;
			        imageView.setImageBitmap(imageIDs.get(position));
					
	    }
		});
	}
	
	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;
		
		public ImageAdapter(Context c)
		{
			context = c;
			// sets a grey background; wraps around the images
		    TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
			itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
			a.recycle();
			
		}
		
		// returns the number of images
		public int getCount() 
		{
		  return imageIDs.size();
		}
		// returns the ID of an item
		public Object getItem(int position) 
		{
		 return imageIDs.get(position);
		}
		// returns the ID of an item
		public long getItemId(int position)
		{
		  return position;
		}
		// returns an ImageView view
		public View getView(int position, View convertView, ViewGroup parent) {
		  
			/*ImageView img=null;
			if(imageView1==null)
			{
			LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			img = (ImageView) inflater.inflate(R.layout.addimage, null);			
		    }img.setImageBitmap(imageIDs.get(position));
		    return img;*/
			
			 /*ImageView imageView;
		        if (convertView == null) {
		            // if it's not recycled, initialize some attributes
		            imageView = new ImageView(context);
		            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
		            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		            imageView.setPadding(8, 8, 8, 8);
		        } else {
		            imageView = (ImageView) convertView;
		        }

		        imageView.setImageBitmap(imageIDs.get(position));
		        return imageView;*/

		    ImageView imageView = new ImageView(context);
			imageView.setImageBitmap(imageIDs.get(position));
			imageView.setLayoutParams(new Gallery.LayoutParams(110, 110));
			imageView.setBackgroundResource(itemBackground);
			return imageView;
	
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	   
		switch(v.getId())
		{
		   case R.id.take_photo:
		   Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		   startActivityForResult(intent, REQUEST_IMAGE_VIEW);
		   System.out.println("Take Photo hitted"+"Take Photo Button hitted");
		   break;
		   case R.id.send:
			   
			   
		/*	    activity_log_editor.putInt("send_pod",1);
				activity_log_editor.commit();
				
				String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date()) ;
				String time=new SimpleDateFormat("HH:mm:ss").format(new Date());
				String date_time=date+"#"+time;
				activity_log_time_editor.putString("send_pod_time", date_time); 
				activity_log_time_editor.commit();*/
		   
			int no=imageIDs.size();
			System.out.println("no of images="+no);
			
			ConnectivityManager convm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info=convm.getActiveNetworkInfo();
           try {
				
			
			if(info.isConnected())
			{
			    loading = ProgressDialog.show(this,"Uploading...",no+"Images",false,false);
		    	for(int i=0;i<no;i++)
			    {	
			     uploadImage(imageIDs.get(i)); 
				 System.out.println("picture no="+i);
				 try {
					
					TimeUnit.MILLISECONDS.sleep(300);
				  } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					 Toast.makeText(this,"Upload Interrupted", Toast.LENGTH_SHORT).show();
				  }
			    }
		       //	loading.dismiss();
			}
			
		}
				
	    catch (Exception e1) {
				// TODO Auto-generated catch block
				Toast.makeText(this,"Network Off ",Toast.LENGTH_LONG).show();
			}
		    
             
         
		    	
		  break;
		    	
		 case R.id.delete: 
			 
		    try 
		    {
				imageIDs.remove(position_of_pics);
				gallery.setAdapter(new ImageAdapter(this));
			    imageView.setImageBitmap(null);
			} 
			catch (Exception e) 
			{
				
		     Toast.makeText(this,"Gallary Empty",Toast.LENGTH_SHORT).show();
			}
			 break;

		}
	 }
	
	public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
		 //super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==  REQUEST_IMAGE_VIEW && resultCode== RESULT_OK)
		{
			
			Bundle extras=data.getExtras();
			Bitmap pic= (Bitmap) extras.get("data");
		
			imageView.setImageBitmap(pic);
		   
		    imageIDs.add(pic);
		    gallery.setAdapter(new ImageAdapter(this));
			
			
		   
	    }
	
		
	}

	private void uploadImage(Bitmap pics){
        //Showing the progress dialog
		//imageIDs.clear();
		bitmap=pics;
				
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                       loading.dismiss(); 
                    	
                    	int result=0;
                    	
                    	try {
							result=Integer.parseInt(s);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    			
                    			
                    	if(result==1)
                    	{
                    	    
             		    	startActivity(intent123); 
             		        Toast.makeText(OpenCameraActivity.this,"Success" , Toast.LENGTH_SHORT).show();
             		    
             		    	finish();
                    	}
                    	
                    	else
                        Toast.makeText(OpenCameraActivity.this,"Weak network signal" , Toast.LENGTH_SHORT).show();
                       
                    
                    }
                },
                
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       
                        //loading.dismiss();
                        Toast.makeText(OpenCameraActivity.this,"Error...Resend ", Toast.LENGTH_SHORT).show();
                        loading.dismiss();   
                        volleyError.printStackTrace();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
              
                String image = getStringImage(bitmap);
                String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String time=new SimpleDateFormat("HH:mm:ss").format(new Date());
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put("imei",retrievedimeino);
                params.put("shipment",retrievedshipmentno);
                params.put("podstatus",podmessage);
                params.put("date",date);
                params.put("time",time);
               
           //   dummy value for trial work.
           /*   params.put("imei","911448200781416");
                params.put("shipment","13");
                params.put("podstatus","podok");
                params.put("date",date);
                params.put("time",time);*/
                
                
                
                
                return params;
            }
        };
 
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
   
    }
	
	
	
	@Override 
	public void onStop() 
	{
	 super.onStop();
	}

	private void Readimeiandshipment()
	{
		SharedPreferences sharedPref = getSharedPreferences("Drivefile", Context.MODE_PRIVATE);   // declare at ToPickUpPointMap.java
	    retrievedshipmentno = sharedPref.getString("shipmentno", "000");
	    retrievedimeino = sharedPref.getString("imeino", "000");
		
	}
	
	
	 private void writestateToInternalStorage( ) 
		{
             byte b=7;
            
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
	 
	 
	 /*private void hitserver()
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
				
		        //final String request="http://121.241.125.91/cc/mavyn/online/mobilelocation.php?msg="+imei_no+"%23"+shipment_no+"%23"+"waitingforload"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
		        final String request="http://121.241.125.91/cc/mavyn/online/upload.php?msg="+imei_no+"%23"+shipment_no+"%23"+"pod_submitted"+"%23"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"%23"+new SimpleDateFormat("HH:mm:ss").format(new Date());
		        
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
				
				  
	    }*/
	
}
