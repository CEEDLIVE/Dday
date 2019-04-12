package com.example.ceedlive.dday.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.ceedlive.dday.BaseActivity;
import com.example.ceedlive.dday.Constant;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.helper.DatabaseHelper;
import com.example.ceedlive.dday.http.RetrofitApiService;
import com.example.ceedlive.dday.http.RetrofitConnection;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingActivity extends BaseActivity {

    private static final String TAG = "LoadingActivity";
    private DatabaseHelper mDatabaseHelper;

    private String[] mArrWiseSaying;
    private TextView mTvWiseSaying;

    private Thread mThread;

    private Retrofit mRetrofit;
    private RetrofitApiService mRetrofitApiService;

    private RetrofitConnection mRetrofitConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        createTable();
        loadWiseSaying();
        startLoading();

        mRetrofit = RetrofitConnection.getInstance(RetrofitApiService.API_URL);
        mRetrofitApiService = mRetrofit.create(RetrofitApiService.class);

//        retrofitGet();
//        retrofitCreate();
//        retrofitUpdate();
        retrofitDelete();
    }

    private void createTable() {
        mDatabaseHelper = DatabaseHelper.getInstance(LoadingActivity.this);
    }

    private void loadWiseSaying() {
        mTvWiseSaying = findViewById(R.id.loading_tv_wise_saying);
        mArrWiseSaying = new String[]{
                "낭비한 시간에 대한 후회는 더 큰 시간 낭비이다.",
                "달력은 열정적인 이들이 아니라, 신중한 이들을 위한 것이다.",
                "미래는 현재 우리가 무엇을 하는가에 달려 있다.",
                "나는 영토는 잃을지 몰라도 결코 시간은 잃지 않을 것이다.",
                "우리가 진정으로 소유하는 것은 시간 뿐이다. 가진 것이 달리 아무 것도 없는 이에게도 시간은 있다.",
                "우아함이란 이제 갖 사춘기를 벗어난 이들의 특권이 아니라, 이미 스스로의 미래를 꽉 잡고 있는 이들의 것이다.",
                "미래는 여기 있다. 아직 널리 퍼지지 않았을 뿐이다."
        };

        int randomIndex = (int) (Math.random() * mArrWiseSaying.length);
        String randomWiseSaying = mArrWiseSaying[randomIndex];
        mTvWiseSaying.setText(randomWiseSaying);
    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, Constant.LOADING_DELAY_MILLIS);
    }

    private void retrofitGet() {
        Call<ResponseBody> comment = mRetrofitApiService.getDummyEmployee(24494);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse");
                int responseCode = response.code();
                ResponseBody responseBody = (ResponseBody) response.body();

                String json = "";
                try {
                    json = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "retrofitGet - responseCode: " + responseCode);
                Log.e(TAG, "retrofitGet - json: " + json);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "retrofitGet - onFailure: ");
            }
        });
    }

    private void retrofitCreate() {

//        {"name":"test","salary":"123","age":"23"}

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "ceed1");
        jsonObject.addProperty("salary", "300");
        jsonObject.addProperty("age", "35");

        Call<ResponseBody> comment = mRetrofitApiService.createDummyEmployee(jsonObject);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse");
                int responseCode = response.code();
                ResponseBody responseBody = (ResponseBody) response.body();

                String json = "";
                try {
                    json = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "retrofitGet - responseCode: " + responseCode);
                Log.e(TAG, "retrofitGet - json: " + json);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "retrofitGet - onFailure: ");
            }
        });
    }

    private void retrofitUpdate() {

//        {"name":"test","salary":"123","age":"23"}

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "ceed10");
        jsonObject.addProperty("salary", "3000");
        jsonObject.addProperty("age", "350");

        Call<ResponseBody> comment = mRetrofitApiService.updateDummyEmployee(24494, jsonObject);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse");
                int responseCode = response.code();
                ResponseBody responseBody = (ResponseBody) response.body();

                String json = "";
                try {
                    json = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "retrofitGet - responseCode: " + responseCode);
                Log.e(TAG, "retrofitGet - json: " + json);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "retrofitGet - onFailure: ");
            }
        });
    }

    private void retrofitDelete() {

//        {"name":"test","salary":"123","age":"23"}

        Call<ResponseBody> comment = mRetrofitApiService.deleteDummyEmployee(24494);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse");
                int responseCode = response.code();
                ResponseBody responseBody = (ResponseBody) response.body();

                String json = "";
                try {
                    json = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "retrofitGet - responseCode: " + responseCode);
                Log.e(TAG, "retrofitGet - json: " + json);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e(TAG, "retrofitGet - onFailure: ");
            }
        });
    }

//    private void requestGet() {
//        httpConnection = OkHttpConnection.getInstance();
//        mThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                httpConnection.requestGet(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, "onFailure: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) {
//                        try {
//                            String result = response.body().string();
//                            Log.e(TAG, "onResponse: " + result);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//        mThread.start();
//    }

//    private void requestPost() {
//        httpConnection = OkHttpConnection.getInstance();
//        mThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                httpConnection.requestPost(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.e(TAG, "onFailure: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) {
//                        try {
//                            String result = response.body().string();
//                            Log.e(TAG, "onResponse: " + result);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
//        mThread.start();
//    }
}
