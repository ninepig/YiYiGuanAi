package com.example.yamengwenjing.yiyiguanai;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yamengwenjing.publicsharedkey.sensorDataEntity;
import com.example.yamengwenjing.yiyiguanai.dbPackage.DatabaseHelper;
import com.example.yamengwenjing.yiyiguanai.dbPackage.SensorDbEntity;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TestActivty extends AppCompatActivity {


    DatabaseHelper helper  ;

    Handler mHander;
    RequestQueue thisQueue;
    sensorDataEntity testEvent;
    TextView counterView;
    int Counter = 0;

    Button qatestButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);
        thisQueue = Volley.newRequestQueue(this);

//        float[] array = {(float) 1.22, (float) 2.222, (float) 3.2222};
//        testEvent = new sensorDataEntity(System.currentTimeMillis(),"testSensor",array);
//        mHander = new Handler() ;
        counterView = (TextView) findViewById(R.id.counterView);

        qatestButton = (Button)findViewById(R.id.testQaButton);
        qatestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("wenjingyang", "clicked");
                TestPost();
            }
        });

//        new RunUploadThread().start();

//        helper = DatabaseHelper.getHelper(getApplicationContext());
//        long time = System.currentTimeMillis();
//        try {
//
//            TableUtils.clearTable(helper.getConnectionSource(), SensorDbEntity.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        long time2 =System.currentTimeMillis();
//        Log.e("TAG","TIME COMSUMING "+ (time2-time));
//        SensorDbEntity test1 = new SensorDbEntity("111","acc","valueassadasdsad");

//        try {
//            helper.getSensorDbEntityDao().create(test1);
//            SensorDbEntity test2 = new SensorDbEntity("222","acc","valueassadasdsad");
//            helper.getSensorDbEntityDao().create(test2);
//            SensorDbEntity test3 = new SensorDbEntity("333","acc","valueassadasdsad");
//            helper.getSensorDbEntityDao().create(test3);
//
//            testList();
//            helper.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        long time = System.currentTimeMillis();

//        for(int i = 0 ;i <=1000;i++){
//            try {
//                helper.getSensorDbEntityDao().create(test1);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        long time2 =System.currentTimeMillis();
//        Log.e("TAG","TIME COMSUMING "+ (time2-time));
    }



    private void TestPost() {

        String postUrl = "http://129.63.16.136/yygaqa/qa";



        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("wenjingyang", s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("wenjingyang", volleyError.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("sent ", "怀孕注意事项");
                return map;
            }
        };
        thisQueue.add(stringRequest);


    }

//    private void TestQAPost( final sensorDataEntity event) {
//
//        String postUrl = "http://129.63.16.123:8080/yytest/insert";
//
//
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, postUrl,new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.d("wenjingyang", s);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.d("wenjingyang", volleyError.toString());
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("id", "wenjingYangTestAgain");
//                map.put("coll", "newTest");
//
//                JSONObject abc = new JSONObject();
//
//                try {
//                    abc.put("sensor name", event.getSensorName());
//                    abc.put("thisPointTimeStamp", event.getTimeStamp());
//
//                    float[] thisValue = event.getValue();
//                    JSONArray mJSONArray = new JSONArray();
//
//                    for(int i = 0; i< thisValue.length;i++){
//                        mJSONArray.put(thisValue[i]);
//                    }
//                    //JSONArray mJSONArray = new JSONArray(Arrays.asList(event.getDataPoint().getValues()));
//
//
//                    Log.i("wenjingTestOutPutmJSONArray", "" + mJSONArray.toString());
//                    abc.put("thisPointValue",mJSONArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                map.put("val",abc.toString());
//
//                Log.i("wenjingTestOutPut", "" + abc.toString());
////                Log.i("wenjingTestOutPut", "" + map.toString());
//
//                return map;
//            }
//        };
//        thisQueue.add(stringRequest);
//
//
//    }


//    class RunUploadThread extends  Thread{
//
//
//        @Override
//        public void run() {
//
//            while(true){
//
//                try {
//                    Thread.sleep(500);
//                    TestPost(testEvent);
//                    mHander.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Counter++;
//                       counterView.setText(""+Counter);
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }


    private void testList() {

        List<SensorDbEntity> users = null;
        try {
            users = helper.getSensorDbEntityDao().queryForAll();
            Log.e("TAG", users.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
