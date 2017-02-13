package com.example.chetakdriverapp;

import java.io.BufferedReader;
import java.io.IOException;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Canceal extends ListFragment implements OnItemClickListener
{
   Context context;
   private ArrayList<CancelTransit_record> transit=new ArrayList<CancelTransit_record>(10);;
   CancelListAdapter myadapter;
   private Intent intent;

   
   public Canceal(Context c)
	{
	   //URL url=new URL();
	   context=c;

	}
   
   

   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
 
   }
	
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.lsttest, null);
    
        return root;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
       super.onActivityCreated(savedInstanceState);
       //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
       final  TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		
		
	   final  String url_string ="http://121.241.125.91/cc/mavyn/online/driverwiseupdate.php?msg=canceldata"+"&imeino="+telephonyManager.getDeviceId();
       
	  //String url ="http://121.241.125.91/cc/mavyn/online/ownerwiseupdate.php?msg=transit";
     
	   transit.removeAll(transit);
       GrabURL Excecute=new GrabURL(context);
	   Excecute.execute(url_string);
       getListView().setOnItemClickListener(this);

      
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
	{
		//intent=new Intent(getActivity(),TransitDetailNext.class);
		//startActivity(intent);
	}
    
 
  private class GrabURL extends AsyncTask<String, Void, String> 
{
       Context localcontext;
	 
	  private String content =  null;
      private boolean error = false;
	  private ProgressDialog dialog = new ProgressDialog(getActivity());

	  private Transit_record  temprary_record;
	 
	  private volatile String Date_Time=null;
      private volatile String Truck_type=null;
	  private volatile String From=null;
	  private volatile String To=null;
	  private volatile String shipmentno=null;
	  private String local;
	  
	
	  public GrabURL(Context c)
	   {
		   localcontext=c;  
	   }
	      
	
	  protected void onPreExecute() 
	  {
	   dialog.setMessage("Getting your data... Please wait...");
	   dialog.show();
	  }

	 
	  protected String doInBackground(String... urls) 
	  {

	  // String URL = null;
	   BufferedReader bufferinput =null;
	   HttpURLConnection con=null;
	   
	   try
	   {
		 
		  URL url = new URL(urls[0]);   
		  System.out.println(urls[0]);
		  System.out.println("data hit");
          con = (HttpURLConnection) url.openConnection();
          bufferinput = new BufferedReader(new InputStreamReader((con.getInputStream())));
           try 
           {
			  local = bufferinput.readLine();
			  System.out.println("json data"+local);
		    } 
            catch (Exception e) 
            {
			  // TODO Auto-generated catch block
			     System.out.println("json data not fetched");
		    }
	         
              Log.i("Json data received..",local);
	    }
	   
	   catch(IOException e)
	   {
		   e.printStackTrace();
		   Log.i("Erro", "data downloading erro");
		   
	   }
	
	   try 
	   {
		 bufferinput.close();
	   } 
	   catch (IOException e) 
	   {
		// TODO Auto-generated catch block
		  e.printStackTrace();
	    }
	     con.disconnect();
	   
	   return local;
}

	/*  protected void onCancelled() 
	  {
	   dialog.dismiss();
	   Toast toast = Toast.makeText(localcontext,"Error connecting to Server", Toast.LENGTH_LONG);
	   toast.setGravity(Gravity.TOP, 25, 400);
	   toast.show();
	   }
*/
	  protected void onPostExecute(String content) 
	  {
	  // dialog.dismiss();
	 
	       displayCountryList(content);
	       myadapter=new CancelListAdapter(localcontext,transit);
	       setListAdapter(myadapter);
	       dialog.dismiss();
	  }

   }

// closing AsyncTask

	 private void displayCountryList(String response)
	 {

	  JSONObject responseObj; 


	  try {

	 
	   responseObj = new JSONObject(response); 
	   JSONArray countryListObj = responseObj.optJSONArray("canceldata");  //.getJSONArray("transit");
       
	   int length=countryListObj.length();
	   
	   System.out.println("json array length="+length);
	  
	   for (int i=0; i<length; i++)
	   {
		   
		JSONObject jsonChildNode=countryListObj.getJSONObject(i);
		String  Date_time=jsonChildNode.optString("date_time");
		String  truck_type=jsonChildNode.optString("truck_type");
		String  from=jsonChildNode.optString("from");
		String  to= jsonChildNode.optString("to");
		String  truck_no=jsonChildNode.optString("truckno");
		String  remarks="test";//jsonChildNode.optString("remarks");
		int shipmentNo=Integer.parseInt(jsonChildNode.optString("shipmentno").toString());
		
		CancelTransit_record tempclass=new CancelTransit_record(Date_time,truck_type,to,from,shipmentNo,remarks,truck_no);
	    transit.add(tempclass);
	   }

	  } 
	     catch (JSONException e) 
	     {
	       e.printStackTrace();
	       System.out.println("Error in reading json object");
	     }
      }

}
