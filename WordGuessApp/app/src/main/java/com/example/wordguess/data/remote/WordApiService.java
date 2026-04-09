package com.example.wordguess.data.remote;

import com.example.wordguess.data.model.ApiModels.RandomWordResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WordApiService {
    @GET("random")
    Call<RandomWordResponse> getRandomWord();
}