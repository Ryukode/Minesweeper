package com.example.minesweeper.controller;

import android.view.MotionEvent;
import android.view.View;

import com.example.minesweeper.model.GameModel;
import com.example.minesweeper.util.GameSettings;
import com.example.minesweeper.util.Status;
import com.example.minesweeper.view.GameView;
import com.example.minesweeper.view.GameViewEvent;
import com.example.minesweeper.view.GameViewListener;

public class GameController implements GameControllerListener {
    private final GameModel model;
    private final GameView view;
    private final GameSettings settings;
    private boolean reveal = true;
    public GameController(GameView view, GameSettings settings){
        this.settings = settings;
        this.view = view;
        this.view.setGameControllerListener(this);
        this.model = new GameModel(settings.rows, settings.columns, settings.mines, this.view);
        view.setModel(model);
    }
    @Override
    public void update(GameViewEvent event){
        if(event.status == Status.RESET){
            model.restart();
        }else if(event.status == Status.TOGGLE_REVEAL){
            reveal = !reveal;
        }else if(reveal){
            model.revealTile(event.cellX, event.cellY);
        }else{
            model.flagTile(event.cellX, event.cellY);
        }
    }
}
