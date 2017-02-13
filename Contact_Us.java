package com.example.chetakdriverapp;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Contact_Us extends Fragment implements OnClickListener
{
   Context context;
  
   private Intent intent;
   
   Button call;
   Button msg;
   Button whatsupp;
	
	
 
	
   public Contact_Us(Context c)
	{
	   //URL url=new URL();
	   context=c;

	}
	
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_contact, null);
        
        call=(Button)root.findViewById(R.id.sendcall);
        msg=(Button)root.findViewById(R.id.sendmessage);
        
        call.setOnClickListener(this);
        msg.setOnClickListener(this);
        
        whatsupp=(Button)root.findViewById(R.id.whatsupp);
        whatsupp.setOnClickListener(this);
        
        
        return root;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
       super.onActivityCreated(savedInstanceState);
       //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
       
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		switch(v.getId())
		{
			
		case R.id.sendcall:
			
		              {
		            	  Intent intent = new Intent(Intent.ACTION_CALL);

		            	  intent.setData(Uri.parse("tel:" +"9811747777"));
		            	  context.startActivity(intent);
			
			              break;
			      
	               	  }		      
		case R.id.sendmessage:
			
			
		          {


                      Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                      smsIntent.setType("vnd.android-dir/mms-sms");
                      smsIntent.putExtra("address", "9811747777");
                      smsIntent.putExtra("sms_body","Body of Message");
                      startActivity(smsIntent);
                      break;
			          
		            }
		          
		 case R.id.whatsupp:
         {
             openWhatsApp();
             break;
         }
			
			
			
			
			default :
				
				     break;
		}
		
		
		
		
	}
	
	private void openWhatsApp() {
        String smsNumber = "9811747777";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }


	


}
