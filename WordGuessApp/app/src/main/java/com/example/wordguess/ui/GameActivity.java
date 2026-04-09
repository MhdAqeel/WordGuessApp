package com.example.wordguess.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wordguess.data.local.PreferencesManager;
import com.example.wordguess.data.model.GameState;
import com.example.wordguess.data.model.LeaderboardEntry;
import com.example.wordguess.data.repository.GameRepository;
import com.example.wordguess.databinding.ActivityGameBinding;
import java.time.Instant;

public class GameActivity extends AppCompatActivity {
    private ActivityGameBinding binding;
    private final GameRepository repo = new GameRepository();
    private final GameState state = new GameState();
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PreferencesManager prefs = new PreferencesManager(this);
        String name = prefs.getPlayerName();
        state.playerName = name == null ? "Player" : name;

        setupListeners();
        loadNewRound(state.level);
    }

    private void setupListeners() {
        binding.btnGuess.setOnClickListener(v -> submitGuess());
        binding.btnLength.setOnClickListener(v -> askLength());
        binding.btnLetterCount.setOnClickListener(v -> askLetterOccurrence());
        binding.btnTip.setOnClickListener(v -> askTip());
        binding.btnLeaderboard.setOnClickListener(v -> startActivity(new Intent(this, LeaderboardActivity.class)));
    }

    private void loadNewRound(int level) {
        state.level = level;
        state.score = 100;
        state.attemptsUsed = 0;
        state.tipUsed = false;
        state.tipText = "";
        state.roundOver = false;
        state.roundWon = false;
        state.elapsedSeconds = 0;
        state.message = "Guess the secret word!";

        repo.fetchRandomWord(word -> {
            state.secretWord = word.toLowerCase();
            startTimer();
            render();
        });
    }

    private void submitGuess() {
        if (state.roundOver) return;

        String guess = binding.etGuess.getText().toString().trim().toLowerCase();
        if (guess.isEmpty()) return;

        if (guess.equals(state.secretWord)) {
            state.roundOver = true;
            state.roundWon = true;
            state.message = "Correct! Moving to next level.";
            stopTimer();
            submitLeaderboard();
            render();

            mainHandler.postDelayed(() -> loadNewRound(state.level + 1), 1200);
            return;
        }

        state.score = Math.max(0, state.score - 10);
        state.attemptsUsed += 1;

        if (state.score <= 0 || state.attemptsUsed >= state.maxAttempts) {
            state.roundOver = true;
            state.message = "Round lost. New word loading...";
            stopTimer();
            render();
            mainHandler.postDelayed(() -> loadNewRound(state.level), 1000);
        } else {
            state.message = "Wrong guess.";
            render();
        }
    }

    private void askLength() {
        if (state.roundOver) return;
        state.score = Math.max(0, state.score - 5);
        state.message = "The word has " + state.secretWord.length() + " letters.";
        render();
    }

    private void askLetterOccurrence() {
        if (state.roundOver) return;
        String text = binding.etLetter.getText().toString().trim();
        if (text.length() != 1) {
            state.message = "Enter one letter only.";
            render();
            return;
        }
        char ch = Character.toLowerCase(text.charAt(0));
        int count = 0;
        for (char c : state.secretWord.toCharArray()) if (c == ch) count++;

        state.score = Math.max(0, state.score - 5);
        state.message = "Letter '" + ch + "' appears " + count + " time(s).";
        render();
    }

    private void askTip() {
        if (state.roundOver) return;
        if (state.attemptsUsed < 5) {
            state.message = "Tip available only after 5 attempts.";
            render();
            return;
        }
        if (state.tipUsed) {
            state.message = "Tip already used this round.";
            render();
            return;
        }

        repo.fetchTip(state.secretWord, tip -> {
            state.tipUsed = true;
            state.score = Math.max(0, state.score - 10);
            state.tipText = tip;
            state.message = "Tip: " + tip;
            render();
        });
    }

    private void submitLeaderboard() {
        LeaderboardEntry entry = new LeaderboardEntry(
                state.playerName, state.score, state.elapsedSeconds, state.level, Instant.now().toString()
        );
        repo.submitScore(entry, ok -> {});
    }

    private void startTimer() {
        stopTimer();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!state.roundOver) {
                    state.elapsedSeconds++;
                    render();
                    timerHandler.postDelayed(this, 1000);
                }
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void stopTimer() {
        if (timerRunnable != null) timerHandler.removeCallbacks(timerRunnable);
    }

    private void render() {
        binding.tvWelcome.setText("Welcome, " + state.playerName);
        binding.tvScore.setText("Score: " + state.score);
        binding.tvAttempts.setText("Attempts: " + state.attemptsUsed + "/" + state.maxAttempts);
        binding.tvLevel.setText("Level: " + state.level);
        binding.tvTimer.setText("Time: " + state.elapsedSeconds + "s");
        binding.tvMessage.setText(state.message);
        binding.btnTip.setEnabled(state.attemptsUsed >= 5 && !state.tipUsed && !state.roundOver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }
}