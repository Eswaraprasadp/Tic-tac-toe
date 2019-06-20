package com.eswar.tictactoe;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity {
    private ArrayList<Row> rows = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private int difficulty = AI.DIFFICULT;
    private DetailsAdapter adapter;
    private ListView listView;
    private TextView difficultyText, bestTimeText;
    private LinearLayout detailsBlock, noneBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        difficulty = getIntent().getIntExtra("Difficulty", 2);

        listView = (ListView)findViewById(R.id.details_list_view);
        difficultyText = (TextView)findViewById(R.id.details_difficulty);
        bestTimeText = (TextView)findViewById(R.id.details_best_time);

        difficultyText.setText(AI.getDifficultyString(difficulty));
//        difficultyText.setTextColor(getResources().getColor(getColorDifficulty(difficulty)));

        detailsBlock = (LinearLayout)findViewById(R.id.details_block);
        noneBlock = (LinearLayout)findViewById(R.id.none_block);

        detailsBlock.setVisibility(View.VISIBLE);
        noneBlock.setVisibility(View.GONE);

        try{
            rows = myDbh.getAllRows(difficulty);
            for(Row row : rows) {
                ids.add(row.getIdString());
            }
        }
        catch (Exception e){
            Log.d(tag, "Exception in getting rows");
        }

        if(rows.size() != 0){
            detailsBlock.setVisibility(View.VISIBLE);
            noneBlock.setVisibility(View.GONE);

            long bestTime = sharedPref.getLong(bestTimeKey(difficulty), (long)(-1));

            String bestTimeTextString = "<font color=#D79FF9>" + getResources().getString(R.string.best_time) + "</font>";
            String secString = "<font color=#FFFFFF>" + getResources().getString(R.string.sec) + "</font>";
            String bestTimeValueString = "";
            if(bestTime != (long)-1) {
                bestTimeValueString = "<font color=#ffffff>" + getTimeString(bestTime) + "</font>";
                if(Build.VERSION.SDK_INT >= 24){
                    bestTimeText.setText(Html.fromHtml(bestTimeTextString + bestTimeValueString + secString, Html.FROM_HTML_MODE_LEGACY));
                }
                else {
                    bestTimeText.setText(Html.fromHtml(bestTimeTextString + bestTimeValueString + secString));
                }
            }
            else{
                bestTimeValueString = "<font color=#ffffff> NA</font>";
                bestTimeText.setText(Html.fromHtml(bestTimeTextString + bestTimeValueString));
            }
        }
        else{
            detailsBlock.setVisibility(View.GONE);
            noneBlock.setVisibility(View.VISIBLE);
        }


        try{
            adapter = new DetailsAdapter(HistoryActivity.this, rows, ids);
            listView.setAdapter(adapter);
        }
        catch (Exception e){
            Log.d(tag, "Cannot set adapter in History Activity: " + e.toString());
        }

    }
}
