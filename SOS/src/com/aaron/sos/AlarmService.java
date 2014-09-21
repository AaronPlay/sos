package com.aaron.sos;

import android.R.bool;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.SmsManager;
import android.util.Log;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.f;

public class AlarmService extends Service{
	private static final String TAG = "MYSERVICE";
	private WakeLock wakeLock = null;  
	
    private LocationClient mLocationClient = null;  
	private BDLocationListener myListener = new MyLocationListener();
	
    private Model mModel;
    private String addString;
    private MyBinder mBinder = new MyBinder();  
    private ThreadFlag thread ;

    class ThreadFlag extends Thread 
    { 
        public volatile boolean exit = false; 
        public void run() 
        { 	

            while (!exit){
                	
    					sendMessage();
    					try {
							sleep(1000*60*30);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            }
            
        } 
        
    } 

    
	 //��ȡ��Դ�������ָ÷�������ĻϨ��ʱ��Ȼ��ȡCPUʱ����������  
    private void acquireWakeLock()  
    {  	
        if (null == wakeLock)  
        {  
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);  
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PostLocationService");  
            if (null != wakeLock)  
            {  
                wakeLock.acquire();  
            }  
        }  
    }  
      
    //�ͷ��豸��Դ��  
    private void releaseWakeLock()  
    {  
        if (null != wakeLock)  
        {  
            wakeLock.release();  
            wakeLock = null;  
        }  
    }  
	
    @Override
    public void onCreate() {
    	// TODO Auto-generated method stub
    	super.onCreate();
    	acquireWakeLock();
    	
    	mModel = Util.load(this);
    	
    	mLocationClient = new LocationClient(getApplicationContext()); // ����
    	mLocationClient.registerLocationListener(myListener); // ע���������  
    	setLocationOption();  
    	mLocationClient.start();// ��ʼ��λ
    	
    }
    
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	mLocationClient.stop();
    	releaseWakeLock();  
    	super.onDestroy();
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public void stopSendMessage(){
		Log.d(TAG, "STOP STOP STOP");
		 
		thread.exit = true;

		thread.interrupt();
	}
	
	public void startSendMessage(){
		Log.d(TAG,"START");
		thread  = new ThreadFlag();
		thread.start();
		
	}
	
	class MyBinder extends Binder{
		public AlarmService getService(){
			return AlarmService.this;
		}
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	private void sendMessage(){
		if(addString != null){
			Log.d(TAG,mModel.getMessage()+"  " +addString);	
		SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mModel.getPhone(), null,mModel.getMessage()+"  "+addString ,null,null);
		}
	}
	
    private void setLocationOption() {  
    	
        LocationClientOption option = new LocationClientOption();  
        option.setOpenGps(true);  
        option.setIsNeedAddress(true);// ���صĶ�λ���������ַ��Ϣ    
        option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02  
        option.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
        
        mLocationClient.setLocOption(option);  
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
			String latitude = String.format("%.2f", location.getLatitude());
			String longitude = String.format("%.2f", location.getLongitude());
			
			addString = "��ַ:"+location.getAddrStr()+"(200m����)"+"  ���ȣ�"+latitude+"γ�ȣ�"+longitude;
			
			
		}
	}
	
}
