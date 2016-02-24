package com.example.yamengwenjing.publicsharedkey;

/**
 * Created by yamengwenjing on 2016/2/21.
 */
public class sensorDataEntity {

    public String sensorName;
    public long timeStamp;

    public sensorDataEntity(long timeStamp, String sensorName, float[] value) {
        this.timeStamp = timeStamp;
        this.sensorName = sensorName;
        this.value = value;
    }

    public float[] value;



    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float[] getValue() {
        return value;
    }

    public void setValue(float[] value) {
        this.value = value;
    }


}
