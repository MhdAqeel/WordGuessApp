package com.example.wordguess.data.remote;

import com.example.wordguess.data.model.ApiModels.TipResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TipApiService {
    @GET("tip")
    Call<TipResponse> getTip(@Query("word") String word);
}