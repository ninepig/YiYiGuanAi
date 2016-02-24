package com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.util;

import android.content.Context;
import android.util.Log;
import android.util.SparseLongArray;

import com.example.yamengwenjing.publicsharedkey.PublicSharedKey;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yamengwenjing on 2016/2/21.
 */

//TODO 这个wearable的传输还没写好

public class WearableClient {


    private static final String TAG = "yiyiguanai/wearableClient";
    private static final int CLIENT_CONNECTION_TIMEOUT = 15000;
    public static WearableClient instance;
    private SparseLongArray lastSensorData;


    public static WearableClient getInstance(Context context) {
        if (instance == null) {
            instance = new WearableClient(context.getApplicationContext());
        }

        return instance;
    }

    private Context context;
    private GoogleApiClient googleApiClient;
    private ExecutorService executorService;

    public WearableClient(Context applicationContext) {
        this.context = applicationContext;
        googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
        executorService = Executors.newCachedThreadPool();
//        lastSensorData = new SparseLongArray();
    }

    public void sendSensorData(final int sensorType, final String timestamp, final String values) {


//        lastSensorData.put(sensorType, t);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendSensorDataInBackground(sensorType,timestamp, values);
            }
        });
    }

    private void sendSensorDataInBackground(int sensorType, String timestamp, String values) {


        Log.d(TAG, "AcychDataToPhone: SENDING IN BACKGROUND");

        Log.d(TAG, "AcychDataToPhone: SENDING IN BACKGROUND  aaaaa");
        PutDataMapRequest dataMap = PutDataMapRequest.create(PublicSharedKey.HELD_HOLDER_PATH);

        Log.d(TAG, "AcychDataToPhone: SENDING IN BACKGROUND datamap ");
        dataMap.getDataMap().putString(PublicSharedKey.SENSORVALUE,values);
        dataMap.getDataMap().putString(PublicSharedKey.SENSORNAME, PublicSharedKey.Acc_sensor_name);
        dataMap.getDataMap().putString(PublicSharedKey.TIMESTAMP, timestamp);
//        Log.d(TAG, "AcychDataToPhone: SENDING IN BACKGROUND ready");
        PutDataRequest putDataRequest = dataMap.asPutDataRequest();


        send(putDataRequest);
    }


    private boolean validateConnection() {
        Log.d(TAG, "AcychDataToPhone: " +"validating");
        if (googleApiClient.isConnected()) {
            return true;
        }
        Log.d(TAG, "AcychDataToPhone: " +"resulting");
        ConnectionResult result = googleApiClient.blockingConnect(CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        Log.d(TAG, "AcychDataToPhone: " +result.isSuccess());
        return result.isSuccess();
    }

    private void send(PutDataRequest putDataRequest) {
        Log.d(TAG, "AcychDataToPhone: " +"sending");
        if (validateConnection()) {
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    Log.v(TAG, "Sending sensor data: " + dataItemResult.getStatus().isSuccess());
                }
            });
        }
    }
}
