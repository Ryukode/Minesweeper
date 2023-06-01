package com.example.minesweeper.view;

import com.example.minesweeper.model.GameModelEvent;

import java.util.EventListener;

public interface GameViewListener extends EventListener {
    void update(GameModelEvent event);
}
