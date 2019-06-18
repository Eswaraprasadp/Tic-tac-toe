package com.eswar.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button easy, medium, hard;
    private int difficulty;
    private Intent intent;
    private final static int AI_LOST = -1, DRAW = 1, AI_WIN = 2, NO_RESULT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        easy = (Button)findViewById(R.id.btnEasy);
        medium = (Button)findViewById(R.id.btnMedium);
        hard = (Button)findViewById(R.id.btnHard);

        if(new AI(new int[][]{{0,0,0}, {0,0,0}, {0,0,0}}, 2).findResult(new int[][]{{1, 5, 1}, {1, 5, 5}, {5, 1, 5}}) != DRAW){
            Log.d("tag", "BUG in findResult!!!");
        }

        easy.setOnClickListener(click);
        medium.setOnClickListener(click);
        hard.setOnClickListener(click);

        intent = new Intent(StartActivity.this, MainActivity.class);
    }
    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnEasy:
                    difficulty = 0;
                    break;
                case R.id.btnMedium:
                    difficulty = 1;
                    break;
                case R.id.btnHard:
                    difficulty = 2;
                    break;
            }
            intent.putExtra("Difficulty", difficulty);
            startActivity(intent);
        }
    };
}
