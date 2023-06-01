package com.example.minesweeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.minesweeper.R;
import com.example.minesweeper.controller.GameControllerListener;
import com.example.minesweeper.model.GameModel;
import com.example.minesweeper.model.GameModelEvent;
import com.example.minesweeper.util.Status;

public class GameView extends View implements GameViewListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener {
    private final Paint paint;
    private GameModel model;
    private GameControllerListener listener;
    private Status gameState;
    private TextView resultMessage;
    private int cellSize;
    private long lastTouchTime = System.currentTimeMillis();

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint = new Paint();
        paint.setTextSize(100);
    }

    public void setCellSize(){
        this.cellSize = this.getWidth() / model.tiles.length;
    }

    public void setModel(GameModel model){
        this.model = model;
    }

    private void createEvent(int cellX, int cellY, Status status){
        GameViewEvent event = new GameViewEvent(this, status, cellX, cellY);
        listener.update(event);
    }

    public void setGameControllerListener(GameControllerListener listener){
        this.listener = listener;
    }

    public void setTextView(TextView text){
        this.resultMessage = text;
        text.setTextSize(50f);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean checked){
        System.out.println("Toggle-Button toggled!");
        createEvent(-1, -1, Status.TOGGLE_REVEAL);
    }

    @Override
    public void update(GameModelEvent event) {
        this.gameState = event.status;
        this.invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent){
        //Prevent too many inputs
        long newTouchTime = System.currentTimeMillis();
        if(newTouchTime - lastTouchTime < 200){
            return false;
        }
        lastTouchTime = newTouchTime;
        if(view instanceof GameView){
            System.out.println("GameView touched!");
            //calculate which cell was touched
            int touchX = (int) motionEvent.getX();
            int touchY = (int) motionEvent.getY();
            int cellX = (touchX - touchX % this.cellSize) / this.cellSize;
            int cellY = (touchY - touchY % this.cellSize) / this.cellSize;
            if(this.gameState == Status.GAME_OVER || this.gameState == Status.WIN){
                return false;
            } else if(cellX < 0 || cellY < 0){
                System.out.println("Touched outside of GameView!");
            }else{
                createEvent(cellX, cellY, Status.TOUCH);
            }
        }else if(view instanceof Button){
            System.out.println("Reset-Button touched!");
            resultMessage.setText("");
            createEvent(-1, -1, Status.RESET);
        }
        return true;
    }

    @Override
    public void onDraw(Canvas canvas){
        setCellSize();
        paint.setTextSize(cellSize - 2);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(0, 0, this.getRight(), this.getBottom(), paint);
        switch (this.gameState){
            case INIT:
                System.out.println("INIT");
                paint.setColor(Color.GRAY);
                for(int i = 0; i < model.tiles[0].length; ++i){
                    for(int j = 0; j < model.tiles.length; ++j){
                        canvas.drawRect(i * cellSize + 5, j * cellSize + 5, (i+1) * cellSize - 5, (j+1) * cellSize - 5, paint);
                    }
                }
                break;
            case WIN:
            case GAME_OVER:
            case UPDATE:
                System.out.println("UPDATE");
                for(int i = 0; i < model.tiles.length; ++i){
                    for(int j = 0; j < model.tiles[0].length; ++j){
                        if(model.tiles[i][j].revealed){
                            paint.setColor(Color.LTGRAY);
                            canvas.drawRect(i * cellSize + 5, j * cellSize + 5, (i+1) * cellSize - 5, (j+1) * cellSize - 5, paint);
                            switch(model.tiles[i][j].neighborMines){
                                case 1:
                                case 5:
                                    paint.setColor(Color.MAGENTA);
                                    break;
                                case 2:
                                case 6:
                                    paint.setColor(Color.CYAN);
                                    break;
                                case 3:
                                case 7:
                                    paint.setColor(Color.BLACK);
                                    break;
                                case 4:
                                case 8:
                                    paint.setColor(Color.YELLOW);
                                    break;
                            }
                            if(model.tiles[i][j].neighborMines != 0){
                                canvas.drawText("" + model.tiles[i][j].neighborMines, i * cellSize + 30, (j+1) * cellSize - 20, paint);
                            }
                        }else if(model.tiles[i][j].flagged){
                            paint.setColor(Color.RED);
                            canvas.drawRect(i * cellSize + 5, j * cellSize + 5, (i+1) * cellSize - 5, (j+1) * cellSize - 5, paint);
                        }else{
                            paint.setColor(Color.GRAY);
                            canvas.drawRect(i * cellSize + 5, j * cellSize + 5, (i+1) * cellSize - 5, (j+1) * cellSize - 5, paint);
                        }
                        switch (this.gameState){
                            case GAME_OVER:
                                System.out.println("GAME OVER");
                                resultMessage.setText(R.string.lose_msg);
                                resultMessage.setTextColor(Color.RED);
                                if(model.tiles[i][j].isMine()){
                                    if(model.tiles[i][j].activatedMine){
                                        paint.setColor(Color.RED);
                                        canvas.drawRect(i * cellSize + 5, j * cellSize + 5, (i+1) * cellSize - 5, (j+1) * cellSize - 5, paint);
                                    }
                                    paint.setColor(Color.BLACK);
                                    canvas.drawText("X", i * cellSize + 30, (j+1) * cellSize - 20, paint);
                                }
                                break;
                            case WIN:
                                System.out.println("WIN");
                                resultMessage.setText(R.string.win_msg);
                                resultMessage.setTextColor(Color.GREEN);
                        }
                    }
                }
                break;
        }
    }
}
