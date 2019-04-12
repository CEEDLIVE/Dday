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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

//        requestGet();
        createTable();
        loadWiseSaying();
        startLoading();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApiService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitApiService = mRetrofit.create(RetrofitApiService.class);

        retrofitGet();
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

        Call<ResponseBody> comment = mRetrofitApiService.getComment(1);
        comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse");
                int responseCode = response.code();

                Log.e(TAG, "retrofitGet - responseCode: " + responseCode);
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
