package com.eswar.tictactoe;

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

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LinearLayout table;
    private Button resetButton;
    private TextView resultText;
    private int[][] grids = new int[3][3];
    Button gridButtons[][] = new Button[3][3];
    DisplayMetrics metrics;
    int height, width;
    private int result;
    private final static int AI_LOST = -1, DRAW = 1, AI_WIN = 2, NO_RESULT = 0;
    boolean waitFlag = false, gameOver = false;
    boolean aiMove = false;
    AI ai;
    private int difficulty;
    private final static int EASY = 0, MEDIUM = 1, DIFFICULT = 2;
    private final static String tag = "tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        difficulty = getIntent().getIntExtra("Difficulty", 2);

        Log.d(tag, "Difficulty selected is : " + String.valueOf(difficulty));

//        metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        height = metrics.heightPixels;
//        width = metrics.widthPixels;

        table = (LinearLayout)findViewById(R.id.llTable);

        resetButton = (Button)findViewById(R.id.btnReset);
        resultText = (TextView)findViewById(R.id.tvResult);

        resetButton.setOnClickListener(reset);

        initBoard();
        init();
    }
    private void init(){
        Log.d(tag, "Inside init()");
        resultText.setVisibility(View.INVISIBLE);
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
//                String text = "";
//                if(grids[i][j] == 1){
//                    text = "X";
//                }
//                else if(grids[i][j] == 5){
//                    text = "O";
//                }
                grids[i][j] = 0;
                gridButtons[i][j].setText("");
            }
        }
        gameOver = false;
        result = NO_RESULT;
        aiMove = false;
        waitFlag = false;
//        for (int i = 0; i < 3; ++i){
//            for (int j = 0; j < 3; ++j){
//                grids[0][0] = 0;
//            }
//        }
        resetButton.setText(R.string.reset);

        Log.d(tag, "About to initialize ai");
        ai = new AI(grids, difficulty);
//        callAI();

    }
    private void callAI(){
        Log.d(tag, "Inside callAI");
            if(aiMove) {
                try {
//                    ai.printBoard(grids);
                    int choice = ai.getMove(grids);;
//                    Log.d(tag, "After calling ai.getMove(), grids is ");
//                    ai.printBoard(grids);
                    Log.d(tag, "AI's  choice is  : " + String.valueOf(choice));
                    int[] rowCol = findRowCol(choice);
                    int row = rowCol[0], col = rowCol[1];
                    try {
                        gridButtons[row][col].callOnClick();
                        Log.d(tag, "gridButtons[" + String.valueOf(row) + "][" + String.valueOf(col) + "] callOnClick performed");
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
            if(!waitFlag && !gameOver) {
                Log.d(tag, "Inside onClick");
                waitFlag = true;
                int tagButton = Integer.parseInt(v.getTag().toString());
                Log.d(tag, "tag of button is " + tagButton);
                int[] rowCol = findRowCol(tagButton);
                int row = rowCol[0], col = rowCol[1];
                Log.d("tag", "Row : " + String.valueOf(row) + ", Column : " + String.valueOf(col));
                if (!aiMove) {
                    gridButtons[row][col].setText("X");
                    grids[row][col] = 1;
//                    Log.d(tag, "Grids and GridButtons set for move done by player");
                }
                else{
                    gridButtons[row][col].setText("O");
                    grids[row][col] = 5;
//                    Log.d(tag, "Grids and GridButtons set for move done by AI");
                }
                aiMove = !aiMove;
                try {
//                    Log.d(tag, "Finding results for " + Arrays.deepToString(grids) + " in MainActivity");
                    result = ai.findResult(grids);
                    if(result == AI_LOST){
                        gameOver = true;
                        resultText.setText(R.string.win);
                    }
                    else if(result == AI_WIN){
                        gameOver = true;
                        resultText.setText(R.string.lost);
                    }
                    else if(result == DRAW){
                        gameOver = true;
                        resultText.setText(R.string.draw);
                    }
                }
                catch (Exception e){
                    Log.d(tag, "Error in calling ai.findResult()");
                }
                if(!gameOver){
                    waitFlag = false;
                    if(aiMove) {
                        try {
                            callAI();
                        }
                        catch (Exception e) {
                            Log.d(tag, "Exception in calling callAI(): " + e.toString());
                        }
                    }
                }
                else{
                    waitFlag = true;
                    resultText.setVisibility(View.VISIBLE);
                    resetButton.setText(R.string.play_again);
                }
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

        Log.d(tag, "Inside initBoard()");
    }
    int pxFromDp(float dp) {
        return (int)(dp * metrics.densityDpi / 160f) ;
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
