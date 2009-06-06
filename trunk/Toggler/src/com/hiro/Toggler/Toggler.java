package com.hiro.Toggler;

// Yes... I know this code is a mess

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Toggler extends Activity {
    /** Called when the activity is first created. */
	WifiManager wfm = null;
	LocationManager gpsstatus = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Handles for System Stuff
        wfm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        gpsstatus = (LocationManager)(this.getSystemService(Context.LOCATION_SERVICE));
        updateToggles();

        
    }
    public void updateToggles(){
    	// Handles for System Stuff
        wfm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        gpsstatus = (LocationManager)(this.getSystemService(Context.LOCATION_SERVICE));
        
        // Layout Images
        ImageView wifiiv = (ImageView)findViewById(R.id.ImageView01);
        ImageView gpsiv = (ImageView)findViewById(R.id.gpsicon);
        ImageView ivBatteryLevel = (ImageView)findViewById(R.id.meter);
        
        // Layout Buttons
        ToggleButton wifionoff_button = (ToggleButton)findViewById(R.id.wifi);
        ToggleButton gpsonoff_button = (ToggleButton)findViewById(R.id.gps);
        SeekBar brightseek = (SeekBar)findViewById(R.id.brightnesseek);
        
        // Set Button States
        gpsonoff_button.setOnClickListener(gpsButtonListener);
        //gpsonoff_button.setVisibility(4); // Hide it for now
        gpsonoff_button.setText("GPS");
        wifionoff_button.setText("Wifi");
        // Brightness slider is setup on a scale of 51 max. Brightness
        // goes to 255, so convert it and set the slider
        brightseek.setProgress( (int)(android.provider.Settings.System.getInt(getContentResolver(),  
			     android.provider.Settings.System.SCREEN_BRIGHTNESS,255)/5)  );
 
		// Setup Button Callbacks
        wifionoff_button.setOnClickListener(wifiButtonListener);
		brightseek.setOnSeekBarChangeListener(seekListener);
		
		// Set initial button states
        if(wfm.isWifiEnabled()){ 
        
        	wifionoff_button.setPressed(true);
        	wifionoff_button.setSelected(true);
        	wifionoff_button.setChecked(true);
        	wifionoff_button.setText("Wifi On");
        	wifiiv.setBackgroundResource(R.drawable.wifi);
        	
        } else {
        	
        	wifionoff_button.setPressed(false);
        	wifionoff_button.setChecked(false);
        	wifionoff_button.setText("Wifi Off");
        	wifiiv.setBackgroundResource(R.drawable.wifioff);
        	
        }
        
        
        if(gpsstatus.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
        	
        	gpsonoff_button.setPressed(true);
        	gpsonoff_button.setChecked(true);
        	gpsonoff_button.setSelected(true);
        	gpsonoff_button.setText("GPS On");
        	gpsiv.setBackgroundResource(R.drawable.gpson);
        	
        	
        } else {
        	
        	gpsonoff_button.setPressed(false);
        	gpsonoff_button.setChecked(false);
        	gpsonoff_button.setText("GPS Off");
        	gpsiv.setBackgroundResource(R.drawable.gpsoff);
        	
        }    	
    }
    @Override
    public void onResume() {
         super.onResume();
         updateToggles();
         // I need to know when the battery status changes
         registerReceiver(mBatteryInfoReceiver, new IntentFilter(
                   Intent.ACTION_BATTERY_CHANGED));
    } 
    
    @Override
    public void onPause() {
         super.onPause();

         unregisterReceiver(mBatteryInfoReceiver);         
    }
    
    // Send me your tired and weak.... battery events
    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             String action = intent.getAction();
             if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            	 // Grab info from the Intent Extras
            	 // How did I know what to grab? Lots and lots
            	 // of googling and other intrepid programmers
            	 // Let's face it, the android docs kind of suck
            	 Integer level = intent.getIntExtra("level",0);
            	 Integer status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
            	 // Grab our layout textviews
            	 TextView tv = (TextView)findViewById(R.id.BattLeveltv);
            	 TextView charging = (TextView)findViewById(R.id.charging);
            	 ImageView ivBatteryLevel = (ImageView)findViewById(R.id.meter);
            	 
            	 String tmpstring = level.toString() + "%";
            	 tv.setText(tmpstring); 
            	
            	 // The image-list thing is pretty neat
            	 ivBatteryLevel.setImageResource(R.drawable.batterymeter);
            	 if(level <= 5){
            		 ivBatteryLevel.setImageLevel(0);
            	 }
            	 if(level > 5 && level <= 10){
            		 ivBatteryLevel.setImageLevel(1);
            	 }
            	 if(level > 10 && level <=15){
            		 ivBatteryLevel.setImageLevel(2);
            	 }
            	 
            	 if(level > 16 && level <= 25){
            		 ivBatteryLevel.setImageLevel(2);
            	 }
            	 if(level > 26 && level <= 50){
            		 ivBatteryLevel.setImageLevel(3);
            	 }
            	 if(level > 50 && level <= 65){
            		 ivBatteryLevel.setImageLevel(4);
            	 }
            	 if(level > 65 && level <= 75){
            		 ivBatteryLevel.setImageLevel(5);
            	 }
            	 if(level > 75 && level <= 85){
            		 ivBatteryLevel.setImageLevel(6);
            	 }
            	 if(level > 75 && level <= 85){
            		 ivBatteryLevel.setImageLevel(7);
            	 }
            	 if(level > 85 && level <= 100){
            		 ivBatteryLevel.setImageLevel(8);
            	 }
            	 if(status == BatteryManager.BATTERY_STATUS_CHARGING){
            		 charging.setText("Charging");
            		 ivBatteryLevel.setImageLevel(9);
 
            	 }
            	 if(status == BatteryManager.  BATTERY_PLUGGED_AC){
            		 charging.setText("Charging");
            		 ivBatteryLevel.setImageLevel(9);
            	 }
            	 if(status == BatteryManager.BATTERY_STATUS_FULL){
            		 charging.setText("Full");
            		 ivBatteryLevel.setImageLevel(8);
            	 }
            	 if(status == BatteryManager.BATTERY_STATUS_NOT_CHARGING )
            	 {
            		 charging.setText("Discharging");
            	 }
            	 if(status == BatteryManager.BATTERY_STATUS_DISCHARGING){
            		 charging.setText("Discharging");
            	 }
            	 
        		 // yup... the battery states dont make any sense to me either
        		
             }
        }
   }; 
   
   // Purely for watching integers...
   public void alertmeint(Integer status){
  	 new AlertDialog.Builder(this)
     .setTitle("Status")
     .setMessage("Status: "+ status.toString())
     .show();
   }
   private OnSeekBarChangeListener seekListener = new OnSeekBarChangeListener(){
	   
	// This whole thing is basically a hack.
	   // The first setting of brightness is the old way.
	   // The second is the cupcake way, and it's not supposed
	   // to be persistent. However, doing it this way is persistent...
	   // for some reason
	   // In case you dont know, cupcake is the 1.5 API
	   public void  onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
		   if(fromUser){
				Integer tmpInt = seekBar.getProgress();
				tmpInt = tmpInt * 5; // 51 (seek scale) * 5 = 255 (max brightness)
				// Old way
				android.provider.Settings.System.putInt(getContentResolver(),  
					     android.provider.Settings.System.SCREEN_BRIGHTNESS,  
					     tmpInt); // 0-255    
				 tmpInt = Settings.System.getInt(getContentResolver(),
						 Settings.System.SCREEN_BRIGHTNESS,-1);
			        // Cupcake way..... sucks 
			        WindowManager.LayoutParams lp = getWindow().getAttributes();
			        //lp.screenBrightness = 1.0f;
			        Float tmpFloat = (float)tmpInt / 255;
			        if(tmpFloat < 0.1){ tmpFloat = (float)0.1; };
			        lp.screenBrightness = tmpFloat;
			        getWindow().setAttributes(lp); 
		   }
		   
	   }

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// put awesomeness here
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// and here too
	}
   };
    private OnClickListener wifiButtonListener = new OnClickListener(){
		public void onClick(View v){
			
	    	ToggleButton onoff_button = (ToggleButton)findViewById(R.id.wifi);
	    	ImageView wifiiv = (ImageView)findViewById(R.id.ImageView01);
	    	
	    	if(onoff_button.isChecked()){
	    			// set label
	    			// toggle service
	    		try{
	    			wfm.setWifiEnabled(true);
	    		} catch (SecurityException e) {
	    			ToggleButton wifionoff_button = (ToggleButton)findViewById(R.id.wifi);
	    			wifionoff_button.setText("Security Failure");
	    			// why set the button like this? i just like it... so there
	    		}
	    		
	            	onoff_button.setText("Wifi On");
	            	wifiiv.setBackgroundResource(R.drawable.wifi);
	            } else {
	            	wfm.setWifiEnabled(false);
	            	onoff_button.setText("Wifi Off");
	            	wifiiv.setBackgroundResource(R.drawable.wifioff);
	            };
	    	

	    	
		}
};

private OnClickListener gpsButtonListener = new OnClickListener(){
	public void onClick(View v){
    	//
		ToggleButton gpsonoff_button = (ToggleButton)findViewById(R.id.gps);
		ImageView gpsiv = (ImageView)findViewById(R.id.gpsicon);
        if(gpsstatus.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
        	
        	gpsonoff_button.setPressed(true);
        	gpsonoff_button.setChecked(true);
        	gpsonoff_button.setSelected(true);
        	gpsonoff_button.setText("GPS On");
        	gpsiv.setBackgroundResource(R.drawable.gpson);
        	
        } else {
        	
        	gpsonoff_button.setPressed(false);
        	gpsonoff_button.setChecked(false);
        	gpsonoff_button.setText("GPS Off");
        	gpsiv.setBackgroundResource(R.drawable.gpsoff);
        }
        // Yea take em to the real config menu, cuz we are totally
        // handicapped! wheeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
		startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		
	}
};

}