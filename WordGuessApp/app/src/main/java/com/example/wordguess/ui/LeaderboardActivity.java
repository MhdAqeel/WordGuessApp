package com.example.wordguess.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wordguess.data.model.LeaderboardEntry;
import com.example.wordguess.data.repository.GameRepository;
import com.example.wordguess.databinding.ActivityLeaderboardBinding;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private ActivityLeaderboardBinding binding;
    private final GameRepository repo = new GameRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repo.getLeaderboard(this::renderLeaderboard);
    }

    private void renderLeaderboard(List<LeaderboardEntry> list) {
        list.sort(Comparator
                .comparingInt((LeaderboardEntry e) -> e.score).reversed()
                .thenComparingLong(e -> e.timeSeconds));

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (LeaderboardEntry e : list) {
            sb.append(i++).append(". ")
                    .append(e.name).append(" | Score ").append(e.score)
                    .append(" | ").append(e.timeSeconds).append("s")
                    .append(" | L").append(e.level).append("\n");
        }

        if (sb.length() == 0) sb.append("No leaderboard data yet.");
        binding.tvLeaderboard.setText(sb.toString());
    }
}