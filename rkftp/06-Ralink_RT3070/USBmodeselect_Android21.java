package com.android.settings;

import static android.provider.Settings.System.SCREEN_OFF_TIMEOUT;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IMountService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.CheckBoxPreference;
import android.provider.Settings;
import android.util.Log;
import android.view.IWindowManager;


import java.io.File; 
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import android.util.Log;



public class USBmodeselect extends PreferenceActivity {
       //implements Preference.OnPreferenceChangeListener 
	
    private static final String TAG = "USBmodeselect";

    /** If there is no setting in the provider, use this. */
    private static final int FALLBACK_SCREEN_TIMEOUT_VALUE = 30000;
    
    private static final int	OTG_MODE = 0;
    private static final int	HOST_MODE = 1;
    private static final int	SLAVE_MODE = 2;
	
    private static final int	MAX_USB_MODE = 1;

	private static final int[] mode_num = {
		HOST_MODE,
		OTG_MODE,
        SLAVE_MODE
	};

	private static final String[] mode_key = {
		"host_mode",
		"otg_mode",
        "slave_mode"
	};

	private CheckBoxPreference[] cb_mode = new CheckBoxPreference[MAX_USB_MODE];

/*
	private Preference.OnPreferenceClickListener OnModeSelectListener[] = //new Preference.OnPreferenceClickListener[MAX_USB_MODE];
		{
		OnClickOtg,
		OnClickHost,
		OnClickSlave,
		};
*/	
    
    private File devfile;
    private File wififile;
    private FileInputStream fin;
    private BufferedReader reader;
    private String line;
    private FileOutputStream fout;
    private PrintWriter pWriter;
    private boolean isChecked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getContentResolver();       
        addPreferencesFromResource(R.xml.usb_mode_select);

		Log.d(TAG, "MAX_USB_MODE="+MAX_USB_MODE);
		//for(int i=0; i<MAX_USB_MODE; i++)
		//{
			Log.d(TAG, "mode_key="+mode_key[0]);
			cb_mode[0] = (CheckBoxPreference) findPreference(mode_key[0]);
			cb_mode[0].setChecked(false);
			cb_mode[0].setOnPreferenceClickListener(OnModeSelectListener);
		//}
        
//        devfile = new File("/sys/devices/lm0/driver/force_usb_mode");
        devfile = new File("/sys/devices/platform/dwc_otg/driver/force_usb_mode");
	wififile = new File("/sys/devices/platform/dwc_otg/driver/usb_wifi_status");
       
        try{
	        fin= new FileInputStream(devfile);
	        reader= new BufferedReader(new InputStreamReader(fin));
	        line = reader.readLine();
        }catch(IOException re){
        }
		
		Log.d(TAG,"default mode="+line);

		int mode = Integer.valueOf(line);
		//for(int i=0; i<MAX_USB_MODE; i++)
		//{
			if(mode_num[0] == mode)
			{
				isChecked = true;
                cb_mode[0].setChecked(true);
			    //break;
            } else {
                isChecked = false;
                 cb_mode[0].setChecked(false);
            }
		//}
        try{
	        fin= new FileInputStream(wififile);
	        reader= new BufferedReader(new InputStreamReader(fin));
	        line = reader.readLine();
        }catch(IOException re){
        }
		Log.d(TAG, "usb wifi status="+line);
		int status = Integer.valueOf(line);
		if(status == 1)
        	cb_mode[0].setEnabled(false);
    }
    
    @Override
    protected void onResume() {
        super.onResume();  
    }

    @Override
    protected void onPause() {
        super.onPause();  
    }

 /*ЂĦµļ፽ЬӦ¶¼ˇµ㼷Ň¸�¿¾ͽ«usbʨ׃Ϊ¸ù¤طģʽ¶�¹ܵ½µ؊Ƿ������­՚¸ù¤طģʽЂ*/
    Preference.OnPreferenceClickListener OnModeSelectListener = new Preference.OnPreferenceClickListener (){
//    	@Override
		public boolean onPreferenceClick(Preference preference) {
			// TODO Auto-generated method stub
	 		try{
	 			int mode = -1;
				//for(int i=0; i<MAX_USB_MODE; i++)
				//{
					//cb_mode[0].setChecked(false);
					isChecked = !isChecked;
                    if(isChecked) {
                        mode = mode_num[0];
                        cb_mode[0].setChecked(true);
                    } else {
                        mode = mode_num[1];
                        cb_mode[0].setChecked(false);
                    }
                    /*if(cb_mode[i] == preference)
					{
						mode = mode_num[i];
						cb_mode[i].setChecked(true);
					}
				}*/
				Log.d(TAG, "New usb mode: "+mode);
				if( mode < 0 )
					return false;
				
				fout = new FileOutputStream(devfile);
			    pWriter = new PrintWriter(fout); 
	    		pWriter.println(mode);
	    		pWriter.flush();
			    pWriter.close();

			}catch(IOException re){
	        }
			return true;
		}
    };
}
