package com.example.minesweeper.util;

public class Tile {
    private boolean isMine = false;
    public boolean flagged = false;
    public boolean revealed = false;
    public boolean activatedMine = false;
    public int neighborMines = 0;
    public void setMine(){
        this.isMine=true;
    }
    public boolean isMine(){
        return isMine;
    }
}