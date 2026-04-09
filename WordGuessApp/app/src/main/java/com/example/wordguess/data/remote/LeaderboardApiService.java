package com.example.wordguess.data.remote;

import com.example.wordguess.data.model.LeaderboardEntry;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LeaderboardApiService {
    @POST("leaderboard")
    Call<Void> submitScore(@Body LeaderboardEntry entry);

    @GET("leaderboard")
    Call<List<LeaderboardEntry>> getLeaderboard();
}