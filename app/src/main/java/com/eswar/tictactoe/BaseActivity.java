package com.eswar.tictactoe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

public class BaseActivity extends Activity {
    DatabaseHelper myDbh;
    SharedPreferences sharedPref;
    final static String tag = "tag";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDbh = new DatabaseHelper(BaseActivity.this);
        sharedPref = getSharedPreferences("bestTime", MODE_PRIVATE);

    }

    @Override
    protected void onPause() {
        super.onPause();

        myDbh.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myDbh = new DatabaseHelper(BaseActivity.this);
        sharedPref = getSharedPreferences("bestTime", MODE_PRIVATE);
    }

    public static String getTimeString(long time){

        return String.format(Locale.US, "%.2f", (double)time/1000);
    }
    public static String getResultString(int result){
        String resultString = "";
        if(result == AI.AI_LOST){
            resultString = "Won";
        }
        else if(result == AI.AI_WIN){
            resultString = "Lost";
        }
        else if(result == AI.DRAW){
            resultString = "Draw";
        }
        return resultString;
    }
    public static String bestTimeKey(int difficulty){
        String timeKey = "time " + AI.getDifficultyString(difficulty);
        return timeKey;
    }
    public int getColorDifficulty(int difficuty){
        int colorResourceCode;
        if(difficuty == AI.EASY){
            colorResourceCode = R.color.green;
        }
        else if(difficuty == AI.MEDIUM){
            colorResourceCode = R.color.orangeYellow;
        }
        else {
            colorResourceCode = R.color.red;
        }
        return colorResourceCode;
    }
}
