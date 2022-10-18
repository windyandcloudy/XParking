package com.example.qlguixe.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistical {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("timeCome")
    @Expose
    private String timeCome;

    @SerializedName("timeOut")
    @Expose
    private String timeOut;

    @SerializedName("isOut")
    @Expose
    private int isOut;

    @SerializedName("transport")
    @Expose
    private Transport transport;


    public Statistical() {
    }

    public Statistical(String _id, String timeCome, String timeOut, int isOut, Transport transport) {
        this._id = _id;
        this.timeCome = timeCome;
        this.timeOut = timeOut;
        this.isOut = isOut;
        this.transport = transport;
    }

    public Statistical(String timeCome, String timeOut, int isOut, Transport transport) {
        this.timeCome = timeCome;
        this.timeOut = timeOut;
        this.isOut = isOut;
        this.transport = transport;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTimeCome() {
        return timeCome;
    }

    public void setTimeCome(String timeCome) {
        this.timeCome = timeCome;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public int getIsOut() {
        return isOut;
    }

    public void setIsOut(int isOut) {
        this.isOut = isOut;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
