package com.example.ceedlive.dday;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseActivity extends AppCompatActivity {
    protected final Gson gson = new GsonBuilder().create();

    protected void initialize() {

    }
}
