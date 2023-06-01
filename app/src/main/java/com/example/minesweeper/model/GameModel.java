package com.example.minesweeper.model;

import android.view.View;

import com.example.minesweeper.util.Status;
import com.example.minesweeper.util.Tile;
import com.example.minesweeper.view.GameViewListener;

import java.util.List;
import java.util.Random;

public class GameModel{
    public Tile[][] tiles;
    private final GameViewListener viewListener;
    private boolean minesSet = false;
    private final int mines;
    private int revealedTiles;
    public GameModel(int rows, int columns, int mines, GameViewListener view){
        tiles = new Tile[rows][columns];
        this.mines = mines;
        viewListener = view;
        this.init();
    }
    private void init(){
        for(int i = 0; i < tiles.length; ++i){
            for(int j = 0; j < tiles[i].length; ++j){
                tiles[i][j] = new Tile();
            }
        }
        createEvent(Status.INIT);
    }
    public void restart(){
        init();
        minesSet = false;
        revealedTiles = 0;
    }
    private void createMines(int amount, int clickX, int clickY){
        //es duerfen maximal so viele minen existieren wie Anzahl Felder - 9 (das Startfeld + 8 darum herum) - 1 (damit das Spiel nicht nach dem ersten Klick zu Ende ist).
        if(amount > tiles.length * tiles[0].length - 10){
            System.out.println("Too many Mines! Maximum amount is: " + (tiles.length * tiles[0].length - 10));
            System.exit(1);
        }
        int x;
        int y;
        Random rand = new Random();
        for(int i = 0; i < amount; ++i){
            x = rand.nextInt(tiles.length);
            y = rand.nextInt(tiles[0].length);
            if(tiles[x][y].isMine() || ((x >= clickX - 1 && x <= clickX + 1) && (y >= clickY - 1 && y <= clickY + 1))){
                --i;
            }else{
                tiles[x][y].setMine();
            }
        }
    }
    private int countNeighborMines(int x, int y){
        int count = 0;
        for(int i = -1; i < 2; ++i){
            for(int j = -1; j < 2; ++j){
                if(x + i < 0 || x + i > tiles.length-1 || y + j < 0 || y + j > tiles[0].length-1 || (i == 0 && j == 0)){
                    continue;
                }
                if(tiles[x+i][y+j].isMine()){
                    ++count;
                }
            }
        }
        return count;
    }
    public void flagTile(int x, int y){
        if(tiles[x][y].revealed){
            return;
        }else {
            tiles[x][y].flagged = !tiles[x][y].flagged;
        }
        createEvent(Status.UPDATE);
    }
    public void revealTile(int x, int y){
        if(tiles[x][y].revealed || tiles[x][y].flagged){
            return;
        }else if(tiles[x][y].isMine()){
            //if mine was touched, reveal all mines
            tiles[x][y].activatedMine = true;
            for(int i = 0; i < tiles.length; ++i){
                for(int j = 0; j < tiles[i].length; ++j) {
                    if(tiles[i][j].isMine()){
                        tiles[i][j].revealed = true;
                    }
                    tiles[i][j].flagged = false;
                }
            }
            createEvent(Status.GAME_OVER);
        }else{
            if(!minesSet){
                minesSet = true;
                createMines(mines, x, y);
            }
            tiles[x][y].revealed = true;
            ++revealedTiles;
            tiles[x][y].neighborMines = countNeighborMines(x, y);
            if(tiles[x][y].neighborMines == 0){
                for(int i = -1; i < 2; ++i){
                    for(int j = -1; j < 2; ++j){
                        if(x + i < 0 || x + i > tiles.length - 1 || y + j < 0 || y + j > tiles[0].length - 1 || (i == 0 && j == 0)){
                            continue;
                        }
                        revealTile(x+i, y+j);
                    }
                }
            }
            if(revealedTiles == tiles.length * tiles[0].length - mines){
                createEvent(Status.WIN);
                return;
            }
            createEvent(Status.UPDATE);
        }
    }
    private void createEvent(Status status){
        GameModelEvent event = new GameModelEvent(this, status);
        viewListener.update(event);
    }
}
