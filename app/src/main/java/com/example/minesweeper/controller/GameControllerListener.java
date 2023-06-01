package com.example.minesweeper.controller;

import com.example.minesweeper.view.GameViewEvent;

import java.util.EventListener;

public interface GameControllerListener extends EventListener {
    public void update(GameViewEvent event);
}
