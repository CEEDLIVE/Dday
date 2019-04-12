package com.example.ceedlive.dday.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitApiService {

//    public static final String API_URL = "http://localhost:3000";
    public static final String API_URL = "https://jsonplaceholder.typicode.com";

//    @GET("posts/{id}")
//    Call<ResponseBody> getComment(@Path("id") int id);

    @GET("todos/{id}")
    Call<ResponseBody> getComment(@Path("id") int id);

    Call<ResponseBody> postComment();
}
