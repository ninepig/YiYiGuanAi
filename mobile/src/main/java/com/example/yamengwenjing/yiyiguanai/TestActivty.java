package com.example.yamengwenjing.yiyiguanai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.yamengwenjing.yiyiguanai.dbPackage.DatabaseHelper;
import com.example.yamengwenjing.yiyiguanai.dbPackage.SensorDbEntity;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TestActivty extends AppCompatActivity {


    DatabaseHelper helper  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);
        helper = DatabaseHelper.getHelper(getApplicationContext());
        long time = System.currentTimeMillis();
        try {

            TableUtils.clearTable(helper.getConnectionSource(), SensorDbEntity.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long time2 =System.currentTimeMillis();
        Log.e("TAG","TIME COMSUMING "+ (time2-time));
        SensorDbEntity test1 = new SensorDbEntity("111","acc","valueassadasdsad");

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

        for(int i = 0 ;i <=1000;i++){
            try {
                helper.getSensorDbEntityDao().create(test1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
//        long time2 =System.currentTimeMillis();
//        Log.e("TAG","TIME COMSUMING "+ (time2-time));
    }

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
