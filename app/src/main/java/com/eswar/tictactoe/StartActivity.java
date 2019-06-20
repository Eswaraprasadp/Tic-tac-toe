package com.eswar.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartActivity extends BaseActivity {
    private Button play, history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        play = (Button)findViewById(R.id.btnPlay);
        history = (Button)findViewById(R.id.btnViewHistory);

        if(sharedPref.getBoolean("firstTime", true)){
            myDbh.deleteTable();
            sharedPref.edit().clear().apply();

            sharedPref.edit().putBoolean("firstTime", false).apply();
        }

//        Log.d(tag, "Best time to beat: ");
//        Log.d(tag, "Easy: " + myDbh.getBestTimeWin(AI.EASY));
//        Log.d(tag, "Medium: " + myDbh.getBestTimeWin(AI.MEDIUM));
//        Log.d(tag, "Hard: " + myDbh.getBestTimeWin(AI.DIFFICULT));

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LevelActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LevelHistory.class);
                startActivity(intent);
            }
        });
    }

}
