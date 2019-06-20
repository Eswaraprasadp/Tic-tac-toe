package com.eswar.tictactoe;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends BaseActivity{
    private Button resetButton;
    private TextView resultText;
    private int[][] grids = new int[3][3];
    Button gridButtons[][] = new Button[3][3];
    private int result, noOfMoves;
    private final static int AI_LOST = -1, DRAW = 1, AI_WIN = 2, NO_RESULT = 0;
    boolean waitFlag = false, gameOver = false;
    boolean aiMove = false, firstTime = true;
    private AI ai;
    private int difficulty;
    private final static int EASY = 0, MEDIUM = 1, DIFFICULT = 2;
    private final static long MAX = Long.MAX_VALUE;
    private final static String tag = "tag";
    private long startTime = (long)0, elapsedTime = (long)0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        difficulty = getIntent().getIntExtra("Difficulty", 2);

        resetButton = (Button)findViewById(R.id.btnReset);
        resultText = (TextView)findViewById(R.id.tvResult);

        resetButton.setOnClickListener(reset);

        initBoard();
        init();
    }
    private void init(){
        resultText.setVisibility(View.INVISIBLE);
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                grids[i][j] = 0;
                gridButtons[i][j].setText("");
            }
        }
        gameOver = false;
        result = NO_RESULT;
        noOfMoves = 0;
        aiMove = false;
        waitFlag = false;
        firstTime = true;

        resetButton.setText(R.string.reset);

        ai = new AI(grids, difficulty);

        startTime = (long)0;
        elapsedTime = (long)0;
//        callAI();

    }
    private void ending(){
        elapsedTime = System.currentTimeMillis() - startTime;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String date = sdf.format(Calendar.getInstance().getTime());
//        Log.d(tag, "In MainActivity, elapsed time = " + String.valueOf(elapsedTime) + ", date = " + date + ", number of moves = " + String.valueOf(noOfMoves));

        resultText.setVisibility(View.VISIBLE);
        resetButton.setText(R.string.play_again);

        try {
            myDbh.add(new Row(difficulty, elapsedTime, date, result, noOfMoves));
//            Log.d(tag, "A new row added in MainActivity");
        }
        catch (Exception e){
//            Log.d(tag, "Exception in adding new row");
        }
        long bestTime = sharedPref.getLong("time " + AI.getDifficultyString(difficulty), MAX);
        if((elapsedTime < bestTime) && (result == AI.AI_LOST)) {
            sharedPref.edit().putLong(bestTimeKey(difficulty), elapsedTime).apply();
//            Log.d(tag, "Best time for difficulty " + AI.getDifficultyString(difficulty) + " changed");
        }

    }
    private void callAI(){
            if(aiMove) {
                try {
//                    ai.printBoard(grids);
                    int choice = ai.getMove(grids);

//                    Log.d(tag, "AI's  choice is  : " + String.valueOf(choice));
                    int[] rowCol = findRowCol(choice);
                    int row = rowCol[0], col = rowCol[1];
                    waitFlag = false;
                    try {
                        gridButtons[row][col].callOnClick();
                    } catch (ArrayIndexOutOfBoundsException aiobe) {
                        Log.d(tag, "Array Out of Bounds Exception in callAI()");
                    } catch (Exception e) {
                        Log.d(tag, "Unknown Exception in callAI()");
                    }
                }
                catch (Exception e){
                    Log.d(tag, "Exception in calling ai's update and making it's move in callAI()");
                    Log.d(tag, "Exception is : " + e.toString());
                }
            }
    }

    View.OnClickListener select = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tagButton = Integer.parseInt(v.getTag().toString());
            int[] rowCol = findRowCol(tagButton);
            int row = rowCol[0], col = rowCol[1];

            boolean valid = false;

            if(grids[row][col] == 0){
                valid = true;
            }

            if(!waitFlag && !gameOver && valid) {

                waitFlag = true;

                if (!aiMove) {
                    gridButtons[row][col].setText("X");
                    ++noOfMoves;
                    grids[row][col] = 1;
                } else {
                    gridButtons[row][col].setText("O");
                    grids[row][col] = 5;
                }

                if(firstTime){
                    startTime = System.currentTimeMillis();
                    firstTime = false;
                }
                try {
//                Log.d(tag, "Finding results for " + Arrays.deepToString(grids) + " in MainActivity");
                    result = ai.findResult(grids);
                    if (result == AI_LOST) {
                        gameOver = true;
                        resultText.setTextColor(getResources().getColor(R.color.perfectGreen));
                        resultText.setText(R.string.win);
                    } else if (result == AI_WIN) {
                        gameOver = true;
                        resultText.setTextColor(getResources().getColor(R.color.colorAccent));
                        resultText.setText(R.string.lost);
                    } else if (result == DRAW) {
                        gameOver = true;
                        resultText.setTextColor(getResources().getColor(R.color.orange));
                        resultText.setText(R.string.draw);
                    }
                } catch (Exception e) {
                    Log.d(tag, "Error in calling ai.findResult()");
                }

                if (!gameOver) {

                    if(aiMove){
                        aiMove = false;
                        waitFlag = false;
                    }
                    else{
                        aiMove = true;
                    }

                    if (aiMove) {
                        try {
                            callAI();
                        } catch (Exception e) {
                            Log.d(tag, "Exception in calling callAI(): " + e.toString());
                        }
                    }
                }
                else {
                    ending();
                }
            }
            else{
                Log.d(tag, "Does not go to setting text, waitFlag = " + String.valueOf(waitFlag) + ", gameOver = " + String.valueOf(gameOver) + ", valid = " + String.valueOf(valid));
            }
        }
    };
    View.OnClickListener debugOption  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int tagButton = Integer.parseInt(v.getTag().toString());
            int[] rowCol = findRowCol(tagButton);
            int row = rowCol[0], col = rowCol[1];

            gridButtons[row][col].setText("O");
            grids[row][col] = 5;

            aiMove = false;
        }
    };

    View.OnClickListener reset = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gameOver = true;
            init();
        }
    };

    private void initBoard(){
        gridButtons[0][0] = (Button)findViewById(R.id.grid11);
        gridButtons[0][1] = (Button)findViewById(R.id.grid12);
        gridButtons[0][2] = (Button)findViewById(R.id.grid13);
        gridButtons[1][0] = (Button)findViewById(R.id.grid21);
        gridButtons[1][1] = (Button)findViewById(R.id.grid22);
        gridButtons[1][2] = (Button)findViewById(R.id.grid23);
        gridButtons[2][0] = (Button)findViewById(R.id.grid31);
        gridButtons[2][1] = (Button)findViewById(R.id.grid32);
        gridButtons[2][2] = (Button)findViewById(R.id.grid33);

        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                gridButtons[i][j].setOnClickListener(select);
            }
        }

    }

    private int[] findRowCol(int index){
        int row = (int)(index/3);
        int col = index % 3;
        return new int[]{row, col};
    }

    public void printBoard(int[][] board){
        String boardString = "";
        for (int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                if(board[i][j] == 1)
                    boardString += "X ";
                else if(board[i][j] == 5)
                    boardString += "O ";
                else{
                    boardString += "  ";
                }
            }
            boardString += "\n";
        }
        Log.d(tag, boardString);
    }
}
