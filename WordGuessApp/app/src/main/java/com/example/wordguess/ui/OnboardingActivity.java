package com.example.wordguess.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wordguess.data.local.PreferencesManager;
import com.example.wordguess.databinding.ActivityOnboardingBinding;

public class OnboardingActivity extends AppCompatActivity {
    private ActivityOnboardingBinding binding;
    private PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new PreferencesManager(this);
        String saved = prefs.getPlayerName();
        if (saved != null && !saved.isBlank()) {
            startActivity(new Intent(this, GameActivity.class));
            finish();
            return;
        }

        binding.btnContinue.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            if (name.isEmpty()) {
                binding.etName.setError("Enter your name");
                return;
            }
            prefs.savePlayerName(name);
            startActivity(new Intent(this, GameActivity.class));
            finish();
        });
    }
}