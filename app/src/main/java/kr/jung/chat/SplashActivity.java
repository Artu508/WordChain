package kr.jung.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Thread(() -> {
            WordChainManager.loadRequiredData(getResources());
            startActivity(new Intent(SplashActivity.this, LogInActivity.class));
            finish();
        }).start();
    }
}