package com.wifi.detector;

public class WifiDevice
{
    private String ssID;
    private int signalsStrength;
    private String deviceType;
    private String distance;
    private String dbm;

    public String getSsID() {
        return ssID;
    }

    public void setSsID(String ssID) {
        this.ssID = ssID;
    }

    public int getSignalsStrength() {
        return signalsStrength;
    }

    public void setSignalsStrength(int signalsStrength) {
        this.signalsStrength = signalsStrength;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDbm() {
        return dbm;
    }

    public void setDbm(String dbm) {
        this.dbm = dbm;
    }
}
