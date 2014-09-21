package com.aaron.sos;

import java.io.IOException;
import java.text.ChoiceFormat;

import android.R.integer;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.f;


public class MyActivity extends ActionBarActivity {
    private ImageButton button;
    private Model mModel;
    private String addString ;
    private boolean onService = false;
    private int choice = 1 ;
    private MediaPlayer player;
//    private LocationClient mLocationClient = null;  
//	private BDLocationListener myListener = new MyLocationListener();  
    private Intent service;
    private AlarmService.MyBinder myBinder;
    private AlarmService alarmService;
    private ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			alarmService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			myBinder = (AlarmService.MyBinder) arg1;
			alarmService = myBinder.getService();
			alarmService.startSendMessage();
		}
	};
	
	public static final String PHONE = "PHONE";
	public static final String MESSAGE = "MESSAGE";
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_my);
        service = new Intent(MyActivity.this,AlarmService.class);
        startService(service);
        mModel = Util.load(this);
        if(mModel == null){
            mModel = new Model("00000000","Help");

            try {
                Util.save(mModel,this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Model t = Util.load(this);
            
        }

         player  =    MediaPlayer.create(this,R.raw.alarm);
        player.setLooping(true);
        button = (ImageButton) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
        	

          @Override
          public void onClick(View v) {
              if(choice == 1){
              	if(!onService){
              		choice =2;
              		
  				button.setBackgroundResource(R.drawable.d);
  				
  				Intent bindIntent = new Intent(MyActivity.this,AlarmService.class);
  				bindService(bindIntent, connection, BIND_AUTO_CREATE);
  				
  				onService = true;
                }
              }else {
                  button.setBackgroundResource(R.drawable.s);
                  
                  if(onService){  
                  	alarmService.stopSendMessage();
                  	unbindService(connection);  
                  	
                  	onService = false;
                  }else {
                	  Log.d("tttt", "STOP MUSIC");
                  	player.pause();
					}
                  choice=1;
              }
          }
        	

        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				 
                player.start();
                button.setBackgroundResource(R.drawable.d);
                choice = 2;
				return true;
			}
		});
        
        
        
    }
    
    private void setLocationOption() {  
    	
        LocationClientOption option = new LocationClientOption();  
        option.setOpenGps(true);  
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息    
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02  
        option.setScanSpan(5000);	// 设置发起定位请求的间隔时间为5000ms
        
//        mLocationClient.setLocOption(option);  
    }  
    
    class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
		            return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
			addString = "地址:"+location.getAddrStr()+"  经度："+location.getLatitude()+"纬度："+location.getLongitude();
//			mModel.setAddString(addString);
			Log.d("Test",sb.toString());
			
		}
	}
    
    
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	if(onService){
    		alarmService.stopSendMessage();
          	unbindService(connection);  
    	}
    	player.stop();
    	stopService(service);
    	super.onDestroy();
    	
    	
//    	mLocationClient.stop();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mModel = Util.load(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SetActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
