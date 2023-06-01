package com.example.minesweeper.model;

import com.example.minesweeper.util.Status;

import java.util.EventObject;

public class GameModelEvent extends EventObject {
    public Status status;

    public GameModelEvent(Object source, Status status){
        super(source);
        this.status = status;
    }
}
