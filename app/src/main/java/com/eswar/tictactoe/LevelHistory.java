package com.eswar.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LevelHistory extends BaseActivity {
    private Button easy, medium, hard;
    private int difficulty;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_history);

        easy = (Button)findViewById(R.id.btnHistoryEasy);
        medium = (Button)findViewById(R.id.btnHistoryMedium);
        hard = (Button)findViewById(R.id.btnHistoryHard);

        easy.setOnClickListener(click);
        medium.setOnClickListener(click);
        hard.setOnClickListener(click);

        intent = new Intent(LevelHistory.this, HistoryActivity.class);
    }
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnHistoryEasy:
                    difficulty = AI.EASY;
                    break;
                case R.id.btnHistoryMedium:
                    difficulty = AI.MEDIUM;
                    break;
                case R.id.btnHistoryHard:
                    difficulty = AI.DIFFICULT;
                    break;
            }
            intent.putExtra("Difficulty", difficulty);
            startActivity(intent);
        }
    };
}
