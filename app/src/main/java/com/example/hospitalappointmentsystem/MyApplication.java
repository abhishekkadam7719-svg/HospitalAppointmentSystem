package com.example.hospitalappointmentsystem;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Map<String, String> config = new HashMap<>();

        config.put("cloud_name","v3cx5ict\n");
        config.put("api_key","272965315761829");
        config.put("api_secret","bVuMqJ4SOh7e7qrDGfeK1OBaJ9s");

        MediaManager.init(this,config);
    }
}