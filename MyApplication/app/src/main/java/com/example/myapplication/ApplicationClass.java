package com.example.myapplication;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

public class ApplicationClass extends Application
{
    public static String age;
    public static String sex;
    public static String suji;

    public void onCreate() {
        super.onCreate();
    }

    public String getAge()
    {
        return this.age;
    }
    public String getSex()
    {
        return this.sex;
    }
    public String getSuji()
    {
        return this.suji;
    }
}
