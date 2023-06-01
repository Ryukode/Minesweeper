package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.minesweeper.controller.GameController;
import com.example.minesweeper.util.GameSettings;
import com.example.minesweeper.view.GameView;

public class MainActivity extends AppCompatActivity {
    private GameController gameController;
    private GameView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        this.view = findViewById(R.id.GameView);
        this.gameController = new GameController(this.view, new GameSettings());
        Button resetButton = findViewById(R.id.button);
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        view.setOnTouchListener(view);
        resetButton.setOnTouchListener(view);
        toggleButton.setOnCheckedChangeListener(view);
        TextView win = findViewById(R.id.textWin);
        view.setTextView(win);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}