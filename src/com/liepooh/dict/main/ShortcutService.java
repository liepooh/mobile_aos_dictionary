package com.liepooh.dict.main;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ShortcutService extends Service
{
    /*
     *---------------------------------------------------------
     *
     *                      공       통
     *                     
     *---------------------------------------------------------
     */
    final public String TAG = "WidgetWindowService";
    public LayoutInflater mInflater = null;
    public WindowManager mWindowMgr = null;
    Point mDeviceSize = null;
    Intent mSaveIntent = null;
    
    /*
     *---------------------------------------------------------
     *                     
     *                      단축 아이콘
     *                     
     *---------------------------------------------------------
     */
    public View mShortcutLayout = null;
    public WindowManager.LayoutParams mShortcutParam = null;
    
    public float mCurPosX = 0.f;
    public float mCurPosY = 0.f;
    public float mPrePosX = 0.f;
    public float mPrePosY = 0.f;
    
    public void MovingLayout(MotionEvent event)
    {
        switch(event.getActionMasked())
        {
            case MotionEvent.ACTION_DOWN:
            {
                mPrePosX = event.getRawX();
                mPrePosY = event.getRawY();
            }break;
            
            case MotionEvent.ACTION_MOVE:
            {
                mCurPosX = event.getRawX();
                mCurPosY = event.getRawY();
                
                float newPosX = mCurPosX - mPrePosX;
                float newPosY = mCurPosY - mPrePosY;
                
                mPrePosX = mCurPosX; 
                mPrePosY = mCurPosY;
                
                mShortcutParam.x += newPosX;
                mShortcutParam.y += newPosY;
                if(mShortcutLayout !=null && mShortcutParam != null)
                	mWindowMgr.updateViewLayout(mShortcutLayout, mShortcutParam);
            }break;
        }
    }
    
    public void CreateShortCut()
    {
        mShortcutLayout = mInflater.inflate(R.layout.service_shortcut, null);
        mShortcutLayout.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	MovingLayout(event);
            	return false;
            }
        });
        
        // 찾기
        ImageButton findButton = (ImageButton)mShortcutLayout.findViewById(R.id.button_shortcut_find);
        findButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showWebView();
            }
        });
        findButton.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	MovingLayout(event);
            	return false;
            }
        });
        
        // 설정
        ImageButton moveButton = (ImageButton)mShortcutLayout.findViewById(R.id.button_shortcut_move);
        moveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
        		
            }
        });
        moveButton.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	MovingLayout(event);
            	return false;
            }
        });
        
        // 닫기
        ImageButton closeButton = (ImageButton)mShortcutLayout.findViewById(R.id.button_shortcut_close);
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopService(mSaveIntent);
            }
        });
        closeButton.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	MovingLayout(event);
            	return false;
            }
        });
        
        mShortcutParam = new WindowManager.LayoutParams();
        mShortcutParam.setTitle("");
        mShortcutParam.type = WindowManager.LayoutParams.TYPE_PHONE;
        mShortcutParam.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mShortcutParam.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mShortcutParam.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        mWindowMgr.addView(mShortcutLayout, mShortcutParam);
    }
    
    
    /*
     *---------------------------------------------------------
     *                     
     *                      WebView
     *                     
     *---------------------------------------------------------
     */
    WebView mWebView = null;
    WindowManager.LayoutParams mWebViewParam = null;
    LinearLayout mWebViewLayout = null;
    
    public class FinderWebView extends WebViewClient 
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) 
        {
            // 웹페이지에서 선택한 다른 웹페이지 처리
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onLoadResource(view, url);
        }
        
        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
        }
    }
    
    public void CreateWebView()
    {
        mWebViewParam = new WindowManager.LayoutParams(); 
        mWebViewParam.setTitle("");
        mWebViewParam.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWebViewParam.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mWebViewParam.x = 0;
        mWebViewParam.y = 0;
        mWebViewParam.width = (int) (mDeviceSize.x - (mDeviceSize.x * 0.1));
        mWebViewParam.height = (int) (mDeviceSize.y - (mDeviceSize.y * 0.1));
        
        mWebViewLayout = new LinearLayout(getApplicationContext());
        mWebViewLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        
        mWebView = new WebView(getApplicationContext());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mWebViewLayout.addView(mWebView);
        mWebView.setWebViewClient(new FinderWebView());
        mWebView.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_UP)
                {
                    WebView webView = (WebView)v;
                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                        {
                            mWebView.loadUrl("about:blank");
                            mWebViewLayout.setVisibility(View.INVISIBLE);
                        }break;
                    }
                }
                return false;
            }
        });
        mWebViewLayout.setVisibility(View.INVISIBLE);
        mWindowMgr.addView(mWebViewLayout, mWebViewParam);
    }
    
    public void showWebView()
    {
        ClipboardManager clipboardMgr = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardMgr.getPrimaryClip();
        
        if(clipData == null)
            return;
        
        final String strSerachWord = clipData.getItemAt(0).getText().toString();
        
        if(strSerachWord.isEmpty())
            return;
        
        final String strNaverURL = "http://endic.naver.com/search.nhn?sLn=kr&isOnlyViewEE=N&query=";
        final String strQuarry = strNaverURL + strSerachWord + ".";
        
        mWebViewLayout.setVisibility(View.VISIBLE);
        mWebView.loadUrl(strQuarry);
    }
    
    /*
     *---------------------------------------------------------
     *                    
     *                     액티비티 오버라이딩
     *                     
     *---------------------------------------------------------
     */
    @Override
    public void onCreate()
    {
        mWindowMgr = (WindowManager)getSystemService(WINDOW_SERVICE);
        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        mDeviceSize = new Point(); 
        Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getSize(mDeviceSize);
        
        CreateWebView();
        CreateShortCut();
        
        Log.i(TAG, "onCreate()");
        super.onCreate();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i(TAG, "onStartCommand()");
        mSaveIntent = intent;
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onDestroy()
    {
        Log.i(TAG, "onDestroy()");
        mWindowMgr.removeView(mShortcutLayout);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.i(TAG, "onBind()");
        return null;
    }
} 
