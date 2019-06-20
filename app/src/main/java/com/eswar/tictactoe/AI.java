package com.eswar.tictactoe;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AI {
    private final static String tag = "tag";
    private int difficulty, initialDepth;
    public final static int AI_LOST = -1, DRAW = 1, AI_WIN = 2, NO_RESULT = 0;
    private final static int MAX = 2000, MIN = -2000;
    public final static int EASY = 0, MEDIUM = 1, DIFFICULT = 2;
    private boolean firstTime = true, secondTime = false;

    public AI(int grids[][], int difficulty) {
        this.difficulty = difficulty;
    }

    public int[] alphaBeta(int[][] boards, boolean turnAI, int depth, int alpha, int beta) {

        int bestMove = -1;
        int currentScore = 0;
        int bestScore = ((turnAI) ? MIN : MAX);

        int result = findResult(boards);

        if ((result != NO_RESULT) || (depth == 0)) {

            if (result == AI_WIN) {
                bestScore = result + 20 - depth;
            } else if (result == AI_LOST) {
                bestScore = result - 20 + depth;
            } else if (result == DRAW) {
                bestScore = result;
            }

            return new int[]{bestScore, -1};
        } else {

            ArrayList<Integer> freeSlots = new ArrayList<>();
            freeSlots = findFreeSlots(boards);

            for (int index = 0; index < freeSlots.size(); ++index) {

                int trialBoards[][] = copy(boards);

                int place = freeSlots.get(index);

                int row = findRowCol(place)[0];
                int col = findRowCol(place)[1];

                if (turnAI) {
                    trialBoards[row][col] = 5;
                    currentScore = alphaBeta(trialBoards, false, depth - 1, alpha, beta)[0];
                } else {
                    trialBoards[row][col] = 1;
                    currentScore = alphaBeta(trialBoards, true, depth - 1, alpha, beta)[0];
                }

                if (turnAI) {
                    if (currentScore > bestScore) {

                        bestScore = currentScore;
                        bestMove = place;

                    }
                    if(bestScore > alpha){
                        alpha = bestScore;
                    }
                } else {
                    if (currentScore < bestScore) {//
                        bestScore = currentScore;
                        bestMove = place;//
                    }
                    if(bestScore < beta){
                        beta = bestScore;
                    }
                    if(alpha >= beta) {
//                        if((depth >= (initialDepth - 2)) && (firstTime || secondTime)){
//                            Log.d(levelString(depth), "Breaking for loop for alpha >= beta" + ". alpha = " + String.valueOf(alpha) + ", beta = " + String.valueOf(beta));
//                        }
                        break;
                    }
                }

            }
//            if((depth >= (initialDepth - 2)) && (firstTime || secondTime)) {
//                Log.d(levelString(depth), "In alphaBeta() for " + (turnAI ? "AI" : "Player") + "\'s turn" + ". Best score = " + String.valueOf(bestScore) + ", alpha = " + String.valueOf(alpha) + ", beta = " + String.valueOf(beta));
//                printBoard(boards, depth);
//            }

            return new int[]{bestScore, bestMove};
        }

    }

    public int getMove(int[][] boards) {
        int index = 0;
        initialDepth = findFreeSlots(boards).size();

        if (difficulty == EASY) {
            index = easy(boards);
        } else if (difficulty == MEDIUM) {
            index = medium(boards, true, initialDepth, MIN, MAX)[1];
        } else if (difficulty == DIFFICULT) {
            index = alphaBeta(boards, true, initialDepth, MIN, MAX)[1];
        }
        if(firstTime){
            firstTime = false;
            secondTime = true;
        }
        else if(secondTime){
            secondTime = false;
        }

        return index;
    }
    public int[] medium(int[][] boards, boolean turnAI, int depth, int alpha, int beta) {

        int bestMove = -1;
        int currentScore = 0;
        int bestScore = ((turnAI) ? MIN : MAX);

        int result = findResult(boards);

        if ((result != NO_RESULT) || (depth <= (initialDepth - 2))) {

            if (result == AI_WIN) {
                bestScore = result + 20 - depth;
            } else if (result == AI_LOST) {
                bestScore = result - 20 + depth;
            } else if (result == DRAW) {
                bestScore = result;
            }else {
                bestScore = result;
            }

            return new int[]{bestScore, -1};
        } else {

            ArrayList<Integer> freeSlots = new ArrayList<>();
            freeSlots = findFreeSlots(boards);

            for (int index = 0; index < freeSlots.size(); ++index) {

                int trialBoards[][] = copy(boards);

                int place = freeSlots.get(index);

                int row = findRowCol(place)[0];
                int col = findRowCol(place)[1];

                if (turnAI) {
                    trialBoards[row][col] = 5;
                    currentScore = medium(trialBoards, false, depth - 1, alpha, beta)[0];
                } else {
                    trialBoards[row][col] = 1;
                    currentScore = medium(trialBoards, true, depth - 1, alpha, beta)[0];
                }

                if (turnAI) {
                    if (currentScore > bestScore) {

                        bestScore = currentScore;
                        bestMove = place;

                    }
                    if(bestScore > alpha){
                        alpha = bestScore;
                    }
                } else {
                    if (currentScore < bestScore) {
//                        Log.d(levelString(depth), "Previous bestScore was: " + String.valueOf(bestScore) + " and bestMove was: " + String.valueOf(bestMove));
                        bestScore = currentScore;
                        bestMove = place;
//                        Log.d(levelString(depth), "Now, bestScore was changed to: " + String.valueOf(bestScore) + " and bestMove assigned is : " + bestMove);
                    }
                    if(bestScore < beta){
                        beta = bestScore;
                    }
                    if(alpha >= beta) {
//                        if((depth >= (initialDepth - 2)) && (firstTime || secondTime)){
//                            Log.d(levelString(depth), "Breaking for loop for alpha >= beta" + ". alpha = " + String.valueOf(alpha) + ", beta = " + String.valueOf(beta));
//                        }
                        break;
                    }
                }
//                if((depth >= (initialDepth - 2)) && (firstTime || secondTime)){
//                    Log.d(levelString(depth), "Score for (" + String.valueOf(row) + ", " + String.valueOf(col) + ") = " + String.valueOf(currentScore));
//                }
            }

            return new int[]{bestScore, bestMove};
        }

    }

    public int easy(int[][] boards) {
        ArrayList<Integer> freeSlots = findFreeSlots(boards);
        int index = (int) (Math.random() * freeSlots.size());
        try {
            Log.d(tag, "Index selected by AI is : " + String.valueOf(freeSlots.get(index)));
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            Log.d(tag, "Array Out Of Bounds Exception in finding random number in AI's easy(). Random number: " + String.valueOf(index));
        }
        return freeSlots.get(index);
    }

    private ArrayList<Integer> findFreeSlots(int[][] boards) {
        ArrayList<Integer> freeSlots = new ArrayList<>();
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (boards[row][col] == 0) {
                    try {
                        freeSlots.add(3 * row + col);
                    } catch (NullPointerException npe) {
                        Log.d(tag, "NULL Pointer Exception in assigning freesSlots");
                    } catch (Exception e) {
                        Log.d(tag, "Exception in assigning freeSlots: " + e.toString());
                    }
                }
            }
        }
        return freeSlots;
    }

    public int findResult(int[][] boards) {

        int horizontalSum = 0, verticalSum = 0, rightDiagonalSum = 0, leftDiagonalSum = 0;
        String reason = "None";
        int result = NO_RESULT;
        boolean gameOver = false;

        for (int i = 0; i < 3 && !gameOver; ++i) {
            horizontalSum = 0;
            for (int j = 0; j < 3; ++j) {
                horizontalSum += boards[i][j];
            }
            if (horizontalSum == 3) {
                gameOver = true;
                result = AI_LOST;
                reason = "Horizontal match at row = " + String.valueOf(i);
                break;
            } else if (horizontalSum == 15) {
                gameOver = true;
                result = AI_WIN;
                reason = "Horizontal match at row = " + String.valueOf(i);
                break;
            }
        }
        for (int j = 0; j < 3 && !gameOver; ++j) {
            verticalSum = 0;
            for (int i = 0; i < 3; ++i) {
                verticalSum += boards[i][j];
            }
            if (verticalSum == 3) {
                gameOver = true;
                result = AI_LOST;
                reason = "Vertical match at column = " + String.valueOf(j);
                break;
            } else if (verticalSum == 15) {
                gameOver = true;
                result = AI_WIN;
                reason = "Vertical match at column = " + String.valueOf(j);
                break;
            }
        }
        rightDiagonalSum = 0;
        leftDiagonalSum = 0;
        for (int j = 0; j < 3; ++j) {
            rightDiagonalSum += boards[j][j];
            leftDiagonalSum += boards[j][2 - j];
        }
        if (rightDiagonalSum == 3 && !gameOver) {
            gameOver = true;
            result = AI_LOST;
            reason = "Right Diagonal match";
        } else if (rightDiagonalSum == 15 && !gameOver) {
            gameOver = true;
            result = AI_WIN;
            reason = "Right Diagonal match";
        }
        if (leftDiagonalSum == 3 && !gameOver) {
            gameOver = true;
            result = AI_LOST;
            reason = "Left Diagonal match";
        } else if (leftDiagonalSum == 15 && !gameOver) {
            gameOver = true;
            result = AI_WIN;
            reason = "Left Diagonal match";
        }


        if (!gameOver) {
            boolean draw = isDraw(boards);
            if (draw) {
                gameOver = true;
                result = DRAW;
//                Log.d(tag, "Game is draw!");

            } else {
                result = NO_RESULT;
//                Log.d(tag, "Game is neither draw nor over");
            }
        } else {
//            Log.d(tag, "Game Over! Reason is : " + reason);
        }
        if ((result != NO_RESULT) && (result != AI_WIN) && (result != AI_LOST) && (result != DRAW)) {
            Log.d(tag, "BUG!!! RESULT is INVALID");
        }
        return result;
    }

    public boolean isDraw(int[][] boards){
        boolean draw = true;
        for (int i = 0; i < 3 && draw; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (boards[i][j] == 0) {
                    draw = false;
                    break;
                }
            }
        }
        return draw;
    }

    private int[] findRowCol(int index){
        int row = (int)(index/3);
        int col = index % 3;
        return new int[]{row, col};
    }
    private int index(int row, int col){
        return 3*row + col;
    }
    private int index(int[] coordinates){
        return coordinates[0]*3 + coordinates[1];
    }

    public int[][] copy(int boards[][]){
        int newBoard[][] = new int[3][3];
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 3; ++col)
                newBoard[row][col] = boards[row][col];

        return newBoard;
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
                    boardString += "_ ";
                }
            }
            Log.d(tag, boardString);
            boardString = "";
        }

    }
    public void printBoard(int[][] board, int level){
        String boardString = "";
        for (int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                if(board[i][j] == 1)
                    boardString += "X ";
                else if(board[i][j] == 5)
                    boardString += "O ";
                else{
                    boardString += "_ ";
                }
            }
            Log.d(levelString(level), boardString);
            boardString = "";
        }

    }
    private String levelString(int level){
        return "level" + String.valueOf(level);
    }


    public static String getDifficultyString(int difficulty){
        String difficultyString;
        if(difficulty == EASY){
            difficultyString = "Easy";
        }
        else if(difficulty == MEDIUM){
            difficultyString = "Medium";
        }
        else{
            difficultyString = "Hard";
        }
        return difficultyString;
    }



}
