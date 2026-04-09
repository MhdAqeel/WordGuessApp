package com.example.wordguess.data.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // Change these to your actual hosted endpoints
    private static final String WORD_BASE_URL = "http://10.0.2.2:3000/";
    private static final String TIP_BASE_URL = "http://10.0.2.2:3000/";
    private static final String LEADERBOARD_BASE_URL = "http://10.0.2.2:3000/";

    private static OkHttpClient client() {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(logger).build();
    }

    public static WordApiService wordApi() {
        return new Retrofit.Builder()
                .baseUrl(WORD_BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WordApiService.class);
    }

    public static TipApiService tipApi() {
        return new Retrofit.Builder()
                .baseUrl(TIP_BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TipApiService.class);
    }

    public static LeaderboardApiService leaderboardApi() {
        return new Retrofit.Builder()
                .baseUrl(LEADERBOARD_BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeaderboardApiService.class);
    }
}