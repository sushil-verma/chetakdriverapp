package com.example.chetakdriverapp;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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


@SuppressWarnings("deprecation")
public class OpenGallaryActivity extends Activity implements OnClickListener {
	
	private int PICK_IMAGE_REQUEST = 1;
	private ArrayList<Bitmap> imageIDs=new ArrayList<Bitmap>(10);
	private Gallery gallery;
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    private ImageButton delete;
    private ImageButton open_gallary;
    private String UPLOAD_URL="http://121.241.125.91/cc/mavyn/online/upload.php";
    private Intent intent1234;
    private ActionBar actionBar;
    private Button send;
    private static int index_of_pics;
    private ImageView imageView;
    private ProgressDialog loading=null;
    private int podstatus=0;
    private String retrievedshipmentno=null;
    private String retrievedimeino=null;
    private String podmessage=null;
    
    
 
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opengallary);
		//writestateToInternalStorage();
		
		
		podstatus=getIntent().getIntExtra("podstatus",0);
		Readimeiandshipment();
		
		if(podstatus==1)
			podmessage="podok";	
		else
			podmessage="podnotok";	
		
		gallery = (Gallery)findViewById(R.id.gallery2);
	    gallery.setAdapter(new ImageAdapter(this));
	    intent1234=new Intent(this,LastActivity.class);
	    actionBar = getActionBar();
	    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
	    actionBar.setCustomView(R.layout.cameraactionbar);
	    getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(0);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        showFileChooser();
        send=(Button)actionBar.getCustomView().findViewById(R.id.send);
        send.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.image2);
        open_gallary=(ImageButton)actionBar.getCustomView().findViewById(R.id.take_photo);
		open_gallary.setOnClickListener(this);
	    delete=(ImageButton)actionBar.getCustomView().findViewById(R.id.delete);
		delete.setOnClickListener(this);
  
	    gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position,long id)
		{
					
					index_of_pics=position;
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
		public int getCount() {
			return imageIDs.size();
		}
		// returns the ID of an item
		public Object getItem(int position) {
		 return imageIDs.get(position);
		}
		// returns the ID of an item
		public long getItemId(int position){
			return position;
		}
		// returns an ImageView view
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			imageView.setImageBitmap(imageIDs.get(position));
			imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
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
	    showFileChooser();
	    break;
		case R.id.send:
			
			   
			
	
			int no=imageIDs.size();
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
							Toast.makeText(this, "upload Interrupted", Toast.LENGTH_SHORT).show();
						  }
					    }
				    }
			      }
						
			    catch (Exception e1) {
						// TODO Auto-generated catch block
						Toast.makeText(this," Network Off ",Toast.LENGTH_LONG).show();
					}
			 
			
			
	        break;
	        
		  case R.id.delete:
			  
			  try 
			    {
					imageIDs.remove(index_of_pics);
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
	
	
	 private void showFileChooser() 
	  {
	        Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
	  }
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		
		
		//if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {		
            //Uri filePath = data.getData();
		    Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
		    bitmap=getScaledBitmap(picturePath,150, 100);
		    imageIDs.add(bitmap);
		    gallery.setAdapter(new ImageAdapter(this));
		    imageView.setImageBitmap(bitmap);
        }
		
		
	}

	private void uploadImage(Bitmap pics){
        //Showing the progress dialog
	
		bitmap=pics;
		
        //final ProgressDialog loading = ProgressDialog.show(this,"Uploading..."," Images",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        
                    	//loading.dismiss();
                      /*  Toast.makeText(OpenGallaryActivity.this," Success" , Toast.LENGTH_SHORT).show();
                        loading.dismiss();
            			
            			startActivity(intent1234);
            			finish();*/
                    	
                    	loading.dismiss();
                    	
                        int result=0;
                    	
                    	try 
                    	{
							result=Integer.parseInt(s);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    			
                    			
                    	if(result==1)
                    	{
                    	    
                    	    startActivity(intent1234);
                    	    Toast.makeText(OpenGallaryActivity.this," Success" , Toast.LENGTH_SHORT).show();
             		    	finish();
                    	}
                    	
                    	else
                          Toast.makeText(OpenGallaryActivity.this,"Error Resend" , Toast.LENGTH_SHORT).show();
            			
                      
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        
                        //loading.dismiss();
                       // System.out.println("the error is="+volleyError.getMessage().toString());
                        Toast.makeText(OpenGallaryActivity.this,"Weak signal", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
            	
                String image = getStringImage(bitmap);
                String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String time=new SimpleDateFormat("HH:mm:ss").format(new Date());
                System.out.println("the image string is="+image);
 
                Map<String,String> params = new Hashtable<String, String>();
 
                params.put(KEY_IMAGE,image);
                params.put("imei",retrievedimeino);
                params.put("shipment",retrievedshipmentno);
                params.put("podstatus",podmessage);
                params.put("date",date);
                params.put("time",time);
            
                return params;
            }
        };
 
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
 
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
	
	
	
	
	 
	 public String getStringImage(Bitmap bmp)
	   {
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        bmp.compress(Bitmap.CompressFormat.JPEG,25, baos);
	      
	        byte[] imageBytes = baos.toByteArray();
	        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
	        return encodedImage;
	    }
	 
	 
	 
	 private Bitmap getScaledBitmap(String picturePath, int width, int height) 
	    {
		    BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
		    sizeOptions.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(picturePath, sizeOptions);
            int inSampleSize = calculateInSampleSize(sizeOptions, width, height);
            sizeOptions.inJustDecodeBounds = false;
		    sizeOptions.inSampleSize = inSampleSize;
            return BitmapFactory.decodeFile(picturePath, sizeOptions);
		}

		private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		    // Raw height and width of image
		    final int height = options.outHeight;
		    final int width = options.outWidth;
		    int inSampleSize = 1;

		    if (height > reqHeight || width > reqWidth) {

		        // Calculate ratios of height and width to requested height and
		        // width
		        final int heightRatio = Math.round((float) height / (float) reqHeight);
		        final int widthRatio = Math.round((float) width / (float) reqWidth);

		        // Choose the smallest ratio as inSampleSize value, this will
		        // guarantee
		        // a final image with both dimensions larger than or equal to the
		        // requested height and width.
		        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		    }

		    return inSampleSize;
		}

		
		private void Readimeiandshipment()
		{
			SharedPreferences sharedPref = getSharedPreferences("Drivefile", Context.MODE_PRIVATE);
		    retrievedshipmentno = sharedPref.getString("shipmentno", "000");
		    retrievedimeino = sharedPref.getString("imeino", "000");
			
		}
		
		
		@Override 
		public void onStop() 
		{
		 super.onStop();
		}
		
		
		 private void writestateToInternalStorage( ) 
			{
                 byte b=8;
                
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
		
}
