package com.example.qlguixe.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transport {
    @SerializedName("_id")
    private String id;

    @SerializedName("trans_type")
    private String trans_type;

    @SerializedName("trans_name")
    private String trans_name;

    @SerializedName("trans_license")
    private String trans_license;

    @SerializedName("qr")
    private String qr;

    @SerializedName("own")
    private Account own;

    public Transport() {
    }

    public Transport(String id, String trans_type, String trans_name, String trans_license, String qr, Account own) {
        this.id = id;
        this.trans_type = trans_type;
        this.trans_name = trans_name;
        this.trans_license = trans_license;
        this.qr = qr;
        this.own = own;
    }

    public Transport(String trans_type, String trans_name, String trans_license, String qr, Account own) {
        this.trans_type = trans_type;
        this.trans_name = trans_name;
        this.trans_license = trans_license;
        this.qr = qr;
        this.own = own;
    }

    public Transport(String trans_type, String trans_name, String trans_license) {
        this.trans_type = trans_type;
        this.trans_name = trans_name;
        this.trans_license = trans_license;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public String getTrans_name() {
        return trans_name;
    }

    public void setTrans_name(String trans_name) {
        this.trans_name = trans_name;
    }

    public String getTrans_license() {
        return trans_license;
    }

    public void setTrans_license(String trans_license) {
        this.trans_license = trans_license;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public Account getOwn() {
        return own;
    }

    public void setOwn(Account own) {
        this.own = own;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "id='" + id + '\'' +
                ", trans_type='" + trans_type + '\'' +
                ", trans_name='" + trans_name + '\'' +
                ", trans_license='" + trans_license + '\'' +
                ", qr='" + qr + '\'' +
                ", own=" + own +
                '}';
    }
}
