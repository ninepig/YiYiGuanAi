package com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Handler;
import android.util.Log;

import com.example.yamengwenjing.publicsharedkey.PublicSharedKey;
import com.example.yamengwenjing.publicsharedkey.sensorDataEntity;
import com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.dbPackage.DatabaseHelper;
import com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.dbPackage.SensorDbEntity;
import com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.util.AccelerSlidingWindows;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;


public class SensorService extends Service implements SensorEventListener {

    private static final String TAG = "yygi/SensorService";

    //todo change sample/judge rate . it is too high LIKE 100 TIMES A SENCOND
    // samplerate means the frequecy of grabbing accmeter data then store;
    private static final int sampleRate = 40;
    private static final int judgeRate = 40;


    //we just use acceleroeter
    private final static int SENS_LINEAR_ACCELERATION = Sensor.TYPE_LINEAR_ACCELERATION;

    SensorManager mSensorManager;

    public SensorService() {
    }

    private Intent mIntent;
    public static  final String SENDINGADL_ACTION = "ADLresultAction";
    public static final String Sensor_service_Sending_Broadcast = "yamengwenjing.yiyiguanai.service";
    DatabaseHelper helper  ;




    @Override
    public void onCreate() {
        super.onCreate();
        mIntent = new Intent(Sensor_service_Sending_Broadcast);
        StartMeasurement();
        helper = DatabaseHelper.getHelper(this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMeasurement();
    }
    // 用sensormanger注册对应的sensor（看你需求），然后在onsensorChanged之中就可以获取了

    private void StartMeasurement() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(SENS_LINEAR_ACCELERATION);
        if (mSensorManager != null) {
            if (accelerometerSensor != null) {
                mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                Log.w(TAG, "No SENS_LINEAR_ACCELERATION found");
            }
        }
    }


    private void stopMeasurement() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        //
//        throw new UnsupportedOperationException("Not yet implemented");
        Log.e(TAG,"connected");
        return null;
    }


    /*
    根据samplerate 处理平均值，然后sendADLstatus 和 storeTheDataToSQLITE的rate不一样。
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

//        Log.e("wenjingSAMPLE ", "thisEventValue" + Arrays.toString(event.values));
        handleSensorData(event);
    }

    int countForADL = 1;
    int countForStoring = 1;
    long initialTimeStampForADL;
    long initialTimestampForStore;


// 不能用自带的时间戳，有问题，在这里搞时间戳
//不能用均值，变化太快了
    //放弃用accelerwindows的的做法，直接取单个数据，用第一个sample rate产生的时间，
    private void handleSensorData(SensorEvent event) {

        if(countForADL == 1){
            initialTimeStampForADL = System.currentTimeMillis();
        }
        if(countForStoring == 1){
            initialTimestampForStore = System.currentTimeMillis();
        }
        if(countForADL == judgeRate) {
//            Log.e("wenjingtest", "thisEventValue" + Arrays.toString(event.values));
            sensorDataEntity thisEntity = new sensorDataEntity(initialTimeStampForADL, PublicSharedKey.Acc_sensor_name,event.values);
            sendADLStuts(thisEntity);
            countForADL = 1;
        }else{
            countForADL++;
        }
        if (countForStoring == sampleRate) {
            sensorDataEntity thisEntity = new sensorDataEntity(initialTimestampForStore, PublicSharedKey.Acc_sensor_name,event.values);
            storeTheDataToSQLITE(thisEntity);
            countForStoring = 1;
        }else{
            countForStoring++;
        }
    }
    //TODO
    private void storeTheDataToSQLITE(sensorDataEntity eventValue) {
        SensorDbEntity thisEntity = new SensorDbEntity(""+eventValue.getTimeStamp(),PublicSharedKey.Acc_sensor_name,Arrays.toString(eventValue.getValue()));
        Log.d("wenjingyang", "storeTheDataToSQLITE: "+thisEntity.toString());
        try {
            helper.getSensorDbEntityDao().create(thisEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void sendADLStuts(sensorDataEntity eventValue) {

       String adlresult =  getADLresult(eventValue);

        sendingStatus thisClass = new sendingStatus(adlresult);
        thisClass.start();

    }


    // Change SampleRate
    // change thredHOLD value

    private String getADLresult(sensorDataEntity eventValue) {

        double xVel = eventValue.getValue()[0];
        double yVel = eventValue.getValue()[1];
        double zVel = eventValue.getValue()[2];

        double mag = Math.sqrt(xVel*xVel+yVel*yVel+zVel*zVel);
        String adlResult;
        if(mag<1){
            adlResult ="sitting";
        }else if (mag<10){
            adlResult ="walking";
        }else {
            adlResult = "jogging";
        }
        Timestamp beginStamp = new Timestamp(eventValue.getTimeStamp());
        Date beginDate = new Date(beginStamp.getTime());

        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());

//        Log.e("wenjingtestTime","beginTime"+beginDate+"getResultTime"+date + "adlACt"+adlResult+"mag"+mag);
        return adlResult;
    }


    class sendingStatus extends Thread{
        String adlStatus;

        public sendingStatus(String statues){
            adlStatus =statues;

        }

        @Override
        public void run() {
            mIntent.putExtra(SENDINGADL_ACTION,adlStatus);
            sendBroadcast(mIntent);
        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {



    }
}
