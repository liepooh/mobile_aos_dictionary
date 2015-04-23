package com.liepooh.dict.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity
{
    /*
     *---------------------------------------------------------
     *                     
     *                      공    통
     *                     
     *--------------------------------------------------------- 
     */
    private static MainActivity mInstance = null;
    final public String NAME_BADGE_COUNT = "Badge_Count";
    
    public static MainActivity getInstance()
    {
        return mInstance;
    }

    public static void setInstance(MainActivity mInstance)
    {
        MainActivity.mInstance = mInstance;
    }
    
    /*
     *---------------------------------------------------------
     *                     
     *              Wraping	SharedPreferences 
     *                     
     *---------------------------------------------------------
     */
    public void setValueSharedPreferences(String key, int value)
    {
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, value);
		editor.commit();
    }
    public int getValueSharedPreferences(String key)
    {
    	long value = 0;
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	
    	try 
    	{
			value = pref.getLong(key, 0);
		} 
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    	return (int)value; 
    }
    
    /*
     *---------------------------------------------------------
     *                     
     *                      아이콘 뱃지 업데이트
     *                     
     *---------------------------------------------------------
     */
    public void setBadgeCount(int count)
    {
    	int badgeCount = getValueSharedPreferences(NAME_BADGE_COUNT);
    	
    	if(count == 0)
    		badgeCount = 0;
    	else
    		++badgeCount;

    	Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
		intent.putExtra("badge_count_package_name", MainActivity.getInstance().getComponentName().getPackageName());
		intent.putExtra("badge_count_class_name", MainActivity.getInstance().getComponentName().getClassName());
		intent.putExtra("badge_count", badgeCount);
		sendBroadcast(intent);
		
		setValueSharedPreferences(NAME_BADGE_COUNT, badgeCount);
    }
    
    /*
     *---------------------------------------------------------
     *                     
     *                    액티비티 오버라이딩
     *                     
     *--------------------------------------------------------- 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setInstance(this);
        setBadgeCount(0);
    }
    
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
    
    /*
     *---------------------------------------------------------
     *                     
     *                       단축 아이콘
     *                     
     *---------------------------------------------------------
     */ 
    public void showShortcutService(final boolean bShow)
    {
        try
        {
            runOnUiThread( new Runnable()
            {
                @Override
                public void run()
                {
                    Intent serviceIntent = new Intent(getApplicationContext(),com.liepooh.dict.main.ShortcutService.class);
                    if(bShow == true)
                        startService(serviceIntent);
                    else
                        stopService(serviceIntent);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }        
    }
    
    /*
     *---------------------------------------------------------
     *                     
     *                      노티피케이션 바 
     *                     
     *---------------------------------------------------------
     */
    public void showNotificationBar(final boolean bShow)
    {
        try
        {
            runOnUiThread( new Runnable()
            {
                @Override
                public void run()
                {
                    Intent serviceIntent = new Intent(getApplicationContext(),com.liepooh.dict.main.NotificationBarService.class);
                    if(bShow == true)
                        startService(serviceIntent);
                    else
                        stopService(serviceIntent);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
       
    
    /*
     *---------------------------------------------------------
     *                     
     *                       버튼 리스너  
     *                     
     *---------------------------------------------------------
     */ 
    public void onClickButton(View v)
    {
        switch (v.getId())
        {
            case R.id.button_main_on:
            {
                showNotificationBar(true);
                showShortcutService(true);
            }break;
                
            case R.id.button_main_off:
            {
                showShortcutService(false);
            }break;
            
            case R.id.button_main_close:
            {
                finish();
            }break;
            
            case R.id.button_main_promotion:
            {
            	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.d4games.fk.main")));
            }break;
        }
    }
}
