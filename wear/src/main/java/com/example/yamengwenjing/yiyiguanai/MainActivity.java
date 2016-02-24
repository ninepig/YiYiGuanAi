package com.example.yamengwenjing.yiyiguanai;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yamengwenjing.publicsharedkey.PublicSharedKey;
import com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.dbPackage.DatabaseHelper;
import com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.dbPackage.SensorDbEntity;
import com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.service.SensorService;
import com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.util.WearableClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends Activity{
//public class MainActivity extends Activity implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener{

    private String TAG = "wenjingYiYiGuanai";

    private TextView mTextView;
    private ImageView mImageView;

//    GoogleApiClient mGoogleApiClient;
    String adlResult;
    DatabaseHelper helper ;
    WearableClient client;


    private  BroadcastReceiver adlReusltReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adlResult = intent.getStringExtra(SensorService.SENDINGADL_ACTION);
            Log.e("wenjingADLresult","adlResult"+adlResult);
            switch (adlResult){
                case "sitting":
                    mTextView.setText("you are sitting");
                    mImageView.setImageResource(R.drawable.sitting);
                    break;
                case "walking":
                    mTextView.setText("you are walking");
                    mImageView.setImageResource(R.drawable.walking);
                    break;
                case "jogging":
                    mTextView.setText("you are jogging");
                    mImageView.setImageResource(R.drawable.runningicon);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mImageView = (ImageView) stub.findViewById(R.id.adlImage);
            }
        });


        startService(new Intent(this, SensorService.class));

        client = WearableClient.getInstance(this);
        helper = DatabaseHelper.getHelper(getApplicationContext());


//        setAmbientEnabled()

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Wearable.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//
//        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
//        MessageReceiver messageReceiver = new MessageReceiver();
//        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

    }

    public void triggerAsh(View view){
        AcychDataToPhone();

    }
    private void AcychDataToPhone() {

        List<SensorDbEntity> users = null;
        try {
            users = helper.getSensorDbEntityDao().queryForAll();
            for (SensorDbEntity thisEntity:users) {
                Log.d(TAG, "AcychDataToPhone: "+thisEntity.toString());
                client.sendSensorData(PublicSharedKey.ACC_SENOR,thisEntity.getTimeStamp(),thisEntity.getSensorData());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public class MessageReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Bundle data = intent.getBundleExtra("datamap");
//            // Display received data in UI
//            String display = "Received from the data Layer\n" +
//                    "Hole: " + data.getString("hole") + "\n" +
//                    "Front: " + data.getString("front") + "\n" +
//                    "Middle: "+ data.getString("middle") + "\n" +
//                    "Back: " + data.getString("back");
//            mTextView.setText(display);
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(adlReusltReceiver, new IntentFilter(SensorService.Sensor_service_Sending_Broadcast));

    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(adlReusltReceiver);

    }



    @Override
    protected void onStop() {
//        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
        stopService(new Intent(this, SensorService.class));
        super.onStop();
    }


//    @Override
//    public void onConnected(Bundle bundle) {
//        String message = "Hello phone\n Via the data layer";
//        //Requires a new thread to avoid blocking the UI
//        new SendToDataLayerThread("/message_path", message).start();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//
//    }
//
//    class SendToDataLayerThread extends Thread {
//        String path;
//        String message;
//
//        // Constructor to send a message to the data layer
//        SendToDataLayerThread(String p, String msg) {
//            path = p;
//            message = msg;
//        }
//
//        public void run() {
//            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
//            for (Node node : nodes.getNodes()) {
//                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, message.getBytes()).await();
//                if (result.getStatus().isSuccess()) {
//                    Log.v("myTag", "Message: {" + message + "} sent to: " + node.getDisplayName());
//                } else {
//                    // Log an error
//                    Log.v("myTag", "ERROR: failed to send Message");
//                }
//            }
//        }
//    }

}
