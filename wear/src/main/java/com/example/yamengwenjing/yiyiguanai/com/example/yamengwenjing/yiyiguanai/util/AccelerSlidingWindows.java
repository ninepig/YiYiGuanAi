package com.example.yamengwenjing.yiyiguanai.com.example.yamengwenjing.yiyiguanai.util;

import android.util.Log;

import com.example.yamengwenjing.publicsharedkey.sensorDataEntity;

import java.util.Arrays;

/**
 * Created by yamengwenjing on 2016/2/21.
 */
public class AccelerSlidingWindows {

    private float[][] accelerArray;
    private long[] timestamp;

    private int arrayLength ;
    private int count = 0;

    public AccelerSlidingWindows(int length){
        this.arrayLength =length;
        accelerArray = new float[arrayLength][3];
        timestamp = new long[arrayLength];
    }

    public sensorDataEntity getArrayAverage(){


        float[] averageAccer = new float[3];
        float sum = 0;
        float sum2 = 0;
        float sum3 = 0;
        for(int i = 0 ; i<arrayLength ;i++){
            sum += accelerArray[i][0];
            sum2 += accelerArray[i][1];
            sum3 += accelerArray[i][2];
        }

        averageAccer[0] = sum/arrayLength;
        averageAccer[1] = sum2/arrayLength;
        averageAccer[2] = sum3/arrayLength;

        long averageTimestamp = 0;
        long averageSumTimestamp = 0;
        for(int i = 0; i<arrayLength;i++){
            averageSumTimestamp += timestamp[i];
        }
        averageTimestamp = averageSumTimestamp/arrayLength;


        sensorDataEntity thisEntity = new sensorDataEntity(averageTimestamp,"accelermeter ",averageAccer);

        return thisEntity;

    }

    public void addNumber(float[] thisAccler,long thisTimeStamp){


          accelerArray[count%arrayLength][0] = thisAccler[0];
          accelerArray[count%arrayLength][1]=thisAccler[1];
          accelerArray[count%arrayLength][2]=thisAccler[2];

          timestamp[count%arrayLength] = thisTimeStamp;

          count++;


    }
}
