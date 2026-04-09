package com.example.wordguess.data.repository;

import androidx.annotation.NonNull;
import com.example.wordguess.data.model.ApiModels.RandomWordResponse;
import com.example.wordguess.data.model.ApiModels.TipResponse;
import com.example.wordguess.data.model.LeaderboardEntry;
import com.example.wordguess.data.remote.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepository {

    public interface WordCallback { void onResult(String word); }
    public interface TipCallback { void onResult(String tip); }
    public interface BoolCallback { void onResult(boolean ok); }
    public interface LeaderboardCallback { void onResult(List<LeaderboardEntry> data); }

    public void fetchRandomWord(WordCallback callback) {
        RetrofitClient.wordApi().getRandomWord().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<RandomWordResponse> call, @NonNull Response<RandomWordResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().word != null) {
                    callback.onResult(response.body().word);
                } else callback.onResult("android");
            }

            @Override
            public void onFailure(@NonNull Call<RandomWordResponse> call, @NonNull Throwable t) {
                callback.onResult("android");
            }
        });
    }

    public void fetchTip(String word, TipCallback callback) {
        RetrofitClient.tipApi().getTip(word).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TipResponse> call, @NonNull Response<TipResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().tip != null) {
                    callback.onResult(response.body().tip);
                } else callback.onResult("No tip available");
            }

            @Override
            public void onFailure(@NonNull Call<TipResponse> call, @NonNull Throwable t) {
                callback.onResult("No tip available");
            }
        });
    }

    public void submitScore(LeaderboardEntry entry, BoolCallback callback) {
        RetrofitClient.leaderboardApi().submitScore(entry).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                callback.onResult(response.isSuccessful());
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                callback.onResult(false);
            }
        });
    }

    public void getLeaderboard(LeaderboardCallback callback) {
        RetrofitClient.leaderboardApi().getLeaderboard().enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<LeaderboardEntry>> call, @NonNull Response<List<LeaderboardEntry>> response) {
                callback.onResult(response.body() == null ? List.of() : response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<LeaderboardEntry>> call, @NonNull Throwable t) {
                callback.onResult(List.of());
            }
        });
    }
}