package com.eswar.tictactoe;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class AI {
    private final static String tag = "tag";
    private int difficulty;
    private final static int AI_LOST = -1, DRAW = 1, AI_WIN = 2, NO_RESULT = 0;
    private final static int MAX = 2000, MIN = -2000;
    private final static int EASY = 0, MEDIUM = 1, DIFFICULT = 2;
    private boolean aiMove;

    public AI(int grids[][], int difficulty) {
        this.difficulty = difficulty;
        Log.d(tag, "Inside AI constructor for difficulty: " + this.difficulty);
    }

    //    public void update(int grids[][], boolean aiMove){
//        Log.d(tag, "Inside AI update()");
//        this.aiMove = aiMove;
//        try {
//            for (int row = 0; row < 3; ++row) {
//                for (int col = 0; col < 3; ++col) {
//                    boards[row][col] = grids[row][col];
//                }
//            }
//        }
//        catch (NullPointerException npe){
//            Log.d(tag, "Null Pointer Exception in updating boards in AI's update");
//        }
//        catch (Exception e){
//            Log.d(tag, "Exception in updating boards in AI's update :" + e.toString());
//        }
//
//    }
    public int[] bestMove(int[][] boards, boolean turnAI, int depth, int alpha, int beta) {

        Log.d(levelString(depth), "Entered in bestMove() for " + (turnAI ? "AI" : "Player") + "\'s turn");
        printBoard(boards, depth);

        int bestMove = -1;
        int currentScore = 0;
        int bestScore = ((turnAI) ? MIN : MAX);

        int result = findResult(boards);

        if ((result != NO_RESULT) || (depth == 0)) {

            if (result == AI_WIN)
                Log.d(levelString(depth), "AI_WIN returned in terminal condition");
            else if (result == AI_LOST)
                Log.d(levelString(depth), "AI_LOST returned in terminal condition");
            else if (result == DRAW)
                Log.d(levelString(depth), "DRAW returned in terminal condition");

            if (turnAI) {
                bestScore = result + 10 - depth;
            } else {
                bestScore = result - 10 + depth;
            }
        } else {

            ArrayList<Integer> freeSlots = new ArrayList<>();
            freeSlots = findFreeSlots(boards);
//            Log.d(tag, "freeSlots in bestMove() is : " + Arrays.toString(freeSlots.toArray()));

            for (int index = 0; index < freeSlots.size(); ++index) {

                int trialBoards[][] = copy(boards);

                int place = freeSlots.get(index);

                int row = findRowCol(place)[0];
                int col = findRowCol(place)[1];

                if (turnAI) {
//                    Log.d(tag, "In AI's bestMove row = " + String.valueOf(row) + ", col = " + String.valueOf(col));
                    trialBoards[row][col] = 5;
                    currentScore = bestMove(trialBoards, false, depth - 1, alpha, beta)[0];
                } else {
//                    Log.d(tag, "In AI's bestMove row = " + String.valueOf(row) + ", col = " + String.valueOf(col));
                    trialBoards[row][col] = 1;
                    currentScore = bestMove(trialBoards, true, depth - 1, alpha, beta)[0];
                }

//                Log.d(levelString(depth), "currentScore = " + String.valueOf(currentScore));

                if (turnAI) {
                    if (currentScore > bestScore) {
//                        Log.d(levelString(depth), "Previous bestScore was: " + String.valueOf(bestScore) + " and bestMove was: " + String.valueOf(bestMove));
                        bestScore = currentScore;
                        bestMove = place;
//                        Log.d(levelString(depth), "Now, bestScore was changed to: " + String.valueOf(bestScore) + " and bestMove assigned is : " + bestMove);
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
                }
                Log.d(levelString(depth),"alpha = " + String.valueOf(alpha) + ", beta = " + String.valueOf(beta));
                if(alpha >= beta) {
                    Log.d(levelString(depth),"Breaking for loop for alpha > beta condition");
                    break;
                }

//                Log.d(tag, " ");
//                Log.d(levelString(depth), "All conditions for (" + String.valueOf(row) + "," + String.valueOf(col) + ") at level = " + String.valueOf(freeSlots.size()) + " completed! and bestScore was: " + String.valueOf(bestScore));
            }
//            Log.d(tag, " ");
            Log.d(levelString(depth), "End of for loop for level = " + String.valueOf(freeSlots.size()));
//            Log.d(tag, "Best score returned = " + String.valueOf(bestScore) + " and Best move returned = (" + String.valueOf(findRowCol(bestMove)[0]) + ", " + String.valueOf(findRowCol(bestMove)[1]) + ")");
//            Log.d(tag, " ");
        }
        return new int[]{bestScore, bestMove};

    }

    public int getMove(int[][] boards) {
        int index = 0;

        int[][] trialBoards = copy(boards);
        if (difficulty == EASY) {
            index = easy(trialBoards);
        } else if (difficulty == MEDIUM) {
            index = medium(trialBoards);
        } else if (difficulty == DIFFICULT) {
            index = bestMove(trialBoards, true, findFreeSlots(trialBoards).size(), MIN, MAX)[1];
        }

//        if(index == 6){
//            Log.d(tag, "GREAT!!!!!! Correct Move Returned!!!!!!");
//        }

        return index;
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
//        Log.d(tag, "Inside findFreeSlots()");
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
//        Log.d(tag, "In AI's findResult() boards called is : ");
//        printBoard(boards);
        int horizontalSum = 0, verticalSum = 0, rightDiagonalSum = 0, leftDiagonalSum = 0;
        String reason = "None";
        int result = NO_RESULT;
        boolean gameOver = false;
//        Log.d(tag, "Initialised reason is : " + reason);

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
                Log.d(tag, "Game is draw!");

            } else {
                result = NO_RESULT;
//                Log.d(tag, "Game is neither draw nor over");
            }
        } else {
            Log.d(tag, "Game Over! Reason is : " + reason);
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

//    private static class AIMove{
//        static int bestMove = 0, bestScore = 1000;
//        private static boolean turnAI = true;
//        public static void updateTurn(){
//            if(turnAI)
//                bestScore = 1000;
//            else{
//                bestScore = -1000;
//            }
//            turnAI = !turnAI;
//        }
//        public static boolean isTurnAI(){
//            return turnAI;
//        }
//        public static int[] getBestScore(){
//            return new int[] {bestScore, bestMove};
//        }
//        public static void updateBestScore(int updatedBestScore, int updatedBestMove){
//            bestScore = updatedBestScore;
//            bestMove = updatedBestMove;
//        }
//
//    }

    public int medium(int[][] boards){
//        Log.d(tag, "In medium()");
//        int move = -1, counter = 0;
//        int matches = 0;
//        int matchesIndices[] = new int[]{-1, -1};
//
//            for (int i = 0; i < 3; ++i){
//                for (int j = 0; j < 2; ++j){
//                    if(boards[i][j] == boards[i][j+1] && boards[i][j] != 0){
//                        ++matches;
//                        matches *= (boards[i][j] == 1) ? -1 : 1;
//                        matchesIndices[0] = i;
//                        matchesIndices[1] = j;
//                    }
//                    else{
//                        matches = 0;
//                        matchesIndices[0] = -1;
//                        matchesIndices[0] = -1;
//                    }
//                }
//                if((matches == -1 && !aiMove) || (matches == 1 && aiMove)){
//                    move = (matchesIndices[1] == 1) ? index(matchesIndices[0], matchesIndices[1] - 1) : index(matchesIndices[0], matchesIndices[1] + 2);
//                    return move;
//                }
//            }
//            for (int j = 0; j < 3; ++j){
//                for (int i = 0; i < 2; ++j){
//                    if(boards[i][j] == boards[i+1][j] && boards[i][j] != 0){
//                        ++matches;
//                        matches *= (boards[i][j] == 1) ? -1 : 1;
//                        matchesIndices[0] = i;
//                        matchesIndices[1] = j;
//                    }
//                    else{
//                        matches = 0;
//                        matchesIndices[0] = -1;
//                        matchesIndices[0] = -1;
//                    }
//                }
//                if((matches == -1 && !aiMove) || (matches == 1 && aiMove)){
//                    move = (matchesIndices[0] == 1) ? index(matchesIndices[0]-1, matchesIndices[1]) : index(matchesIndices[0]+2, matchesIndices[1]);
//                    return move;
//                }
//            }
//            for (int i = 0; i < 2; ++i){
//                if(boards[i][i] == boards[i+1][i+1] && boards[i][i] != 0){
//                    ++matches;
//                    matches *= (boards[i][i] == 1) ? -1 : 1;
//                    matchesIndices[0] = matchesIndices[1] = i;
//                }
//                else{
//                    matches = 0;
//                    matchesIndices[0] = -1;
//                    matchesIndices[0] = -1;
//                }
//                if((matches == -1 && !aiMove) || (matches == 1 && aiMove)){
//                    move = (matchesIndices[0] == 1) ? index(matchesIndices[0]-1, matchesIndices[1]-1) : index(matchesIndices[0]+2, matchesIndices[1]+2);
//                    return move;
//                }
//            }
//            Log.d(tag, "Immediate move found is" + String.valueOf(move));
//            for(int i = 0; i < freeSlots.length; ++i) {
//                times = 1;
//                move = freeSlots[i];
//                int row = findRowCol(move)[0], col = findRowCol(move)[1];
//                boards[row][col] = (aiMove ? 5 : 1);
//                aiMove = !aiMove;
//                ++times;
//                if(move != -1){
//                    return move;
//                }
//                if(times <= 3 && (findResult(boards) == NO_RESULT && findResult(boards) != DRAW) && move == -1) {
//                    move = medium();
//                }
//                else if(times == 3 && (findResult(boards) == NO_RESULT || findResult(boards) == DRAW) && move == -1){
//                    return easy();
//                }
//
//            }
        return easy(boards);
    }


}
