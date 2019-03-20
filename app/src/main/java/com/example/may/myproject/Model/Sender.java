package com.example.may.myproject.Model;

import android.app.Notification;

public class Sender {
    public String to;
    public Notification notification;

    public Sender(String token, Notification notification) {
        this.to = token;
        this.notification = notification;
    }

}
