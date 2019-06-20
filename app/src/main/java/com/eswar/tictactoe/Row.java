package com.eswar.tictactoe;

import android.provider.BaseColumns;
import android.util.Log;

import java.util.Locale;

public class Row{
    private int _result = AI.NO_RESULT, _moves = 1, _difficulty = AI.DIFFICULT;
    private long _id = 1;
    private long _time;
    private String _date;
    private final static String tag = "tag";
    private String _timeString;

    public Row(){}

    public Row(long time, String date, int result, int moves){
        this._time = time;
        this._date = date;
        this._result = result;
        this._moves = moves;
    }
    public Row(int difficulty, long time, String date, int result, int moves){
        this._difficulty = difficulty;
        this._time = time;
        this._date = date;
        this._result = result;
        this._moves = moves;
    }

    public long getId(){
        return this._id;
    }
    public long getTime(){
        return this._time;
    }
    public String getDate(){
        return this._date;
    }
    public int getResult(){
        return this._result;
    }
    public int getMoves(){
        return this._moves;
    }
    public int getDifficulty(){
        return this._difficulty;
    }

    public String getTimeString(){
        return String.format(Locale.US, "%.2f", (double)this._time/1000);
    }
    public String getResultString(){
        String resultString = "";
        if(this._result == AI.AI_LOST){
            resultString = "Won";
        }
        else if(this._result == AI.AI_WIN){
            resultString = "Lost";
        }
        else if(this._result == AI.DRAW){
            resultString = "Draw";
        }
        return resultString;
    }
    public String getMovesString(){
        return String.valueOf(this._moves);
    }
    public String getIdString(){
        return String.valueOf(this._id);
    }

    public void setId(long id){
        this._id = id;
    }

    public void display(){
        Log.d(tag, "ID: " + this._id + ", Difficulty: " + AI.getDifficultyString(this._difficulty) + ", Time: " + this.getTimeString() + " sec, Date: " + this._date + ", Result: " + getResultString() + ", Moves: " + this._moves);
    }

}
