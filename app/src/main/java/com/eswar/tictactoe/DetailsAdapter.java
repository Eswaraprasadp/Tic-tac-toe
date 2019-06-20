package com.eswar.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsAdapter extends ArrayAdapter<String> {
    private ArrayList<Row> rows;
    private ArrayList<String> ids;
    private Activity context;

    private TextView resultText, timeText, movesText, dateText;

    private Row row;

    public DetailsAdapter(Activity context, ArrayList<Row> rows, ArrayList<String> ids){
        super(context, R.layout.details_list, ids);

        this.context = context;
        this.rows = rows;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.details_list, null, true);

        row = rows.get(position);

        resultText = (TextView)rowView.findViewById(R.id.details_result);
        timeText = (TextView)rowView.findViewById(R.id.details_time);
        movesText = (TextView)rowView.findViewById(R.id.details_moves);
        dateText = (TextView)rowView.findViewById(R.id.details_date);

//        if(row.getResultString().equals("Won")){
//            resultText.setTextColor(getContext().getResources().getColor(R.color.purple));
//        }
//        else if(row.getResultString().equals("Lost")){
//            resultText.setTextColor(getContext().getResources().getColor(R.color.purple));
//        }
//        else if(row.getResultString().equals("Draw")){
//            resultText.setTextColor(getContext().getResources().getColor(R.color.purple));
//        }
//        else if(row.getResultString().equals("No Result")){
//            resultText.setTextColor(getContext().getResources().getColor(R.color.purple));
//        }


        resultText.setText(row.getResultString());

        String bestTimeTextString = "<font color=#D79FF9>" + getContext().getResources().getString(R.string.time) + "</font>";
        String bestTimeValueString = "<font color=#FFFFFF>" + row.getTimeString() + "</font>";
        String secString = "<font color=#FFFFFF>" + getContext().getResources().getString(R.string.sec) + "</font>";

        String movesTextString = "<font color=#D79FF9>" + getContext().getResources().getString(R.string.moves) + "</font>";
        String movesValueString = "<font color=#FFFFFF>" + row.getMovesString() + "</font>";

        String dateTextString = "<font color=#D79FF9>" + getContext().getResources().getString(R.string.date) + "</font>";
        String dateValueString = "<font color=#FFFFFF>" + row.getDate() + "</font>";

        if(Build.VERSION.SDK_INT >= 24){
            timeText.setText(Html.fromHtml(bestTimeTextString + bestTimeValueString + secString, Html.FROM_HTML_MODE_LEGACY));
            movesText.setText(Html.fromHtml(movesTextString + movesValueString, Html.FROM_HTML_MODE_LEGACY));
            dateText.setText(Html.fromHtml(dateTextString + dateValueString, Html.FROM_HTML_MODE_LEGACY));
        }
        else{
            timeText.setText(Html.fromHtml(bestTimeTextString + bestTimeValueString + secString));
            movesText.setText(Html.fromHtml(movesTextString + movesValueString));
            dateText.setText(Html.fromHtml(movesTextString + dateValueString));
        }

        return rowView;
    }
}
