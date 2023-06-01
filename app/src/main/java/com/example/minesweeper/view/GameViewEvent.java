package com.example.minesweeper.view;

import com.example.minesweeper.util.Status;

import java.util.EventObject;

public class GameViewEvent extends EventObject {
    public Status status;
    public int cellX, cellY;

    public GameViewEvent(Object source, Status status, int cellX, int cellY){
        super(source);
        this.status = status;
        this.cellX = cellX;
        this.cellY = cellY;
    }
}
