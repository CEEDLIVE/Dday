package com.example.ceedlive.dday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ceedlive.dday.http.OkHttpConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BaseActivity extends AppCompatActivity {
    protected final Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     */
    protected abstract void initialize();
}
