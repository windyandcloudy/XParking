package com.example.qlguixe.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("money")
    @Expose
    private long money;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("email")
    @Expose
    private String email;


    public Account(String _id, String username, String password, String phone, String address, String dob, long money, String role, String email) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.money = money;
        this.role = role;
        this.email= email;
    }

    public Account(String username, String password, String phone, String address, String dob, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.email = email;
    }

    public Account() {
    }

    public Account(String _id) {
        this._id = _id;
    }

    public Account(String username, String password, String phone, String address, String dob, long money, String role) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.money = money;
        this.role = role;
    }

    public Account(String username, String password, String phone, String address, String dob) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.dob = dob;

    }

    public Account(long money) {
        this.money = money;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Account{" +
                "_id='" + _id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", money=" + money +
                ", role='" + role + '\'' +
                '}';
    }
}
