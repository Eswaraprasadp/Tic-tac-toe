package com.eswar.tictactoe;

import android.provider.BaseColumns;

public class EntriesConstants implements BaseColumns{
    private EntriesConstants(){}

    public static final String TABLE_NAME = "best_times";
    public static final String COLUMN_NAME_TIME = "time_taken";
    public static final String COLUMN_NAME_MOVES = "moves";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_RESULT = "result";
}
