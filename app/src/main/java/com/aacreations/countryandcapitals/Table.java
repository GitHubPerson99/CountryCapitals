package com.aacreations.countryandcapitals;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class Table {

    public static final int TABLE_COUNTRIES_AND_CAPITALS = 0;
    public static final int TABLE_OLD_TESTS = 1;

    @IntDef({TABLE_OLD_TESTS, TABLE_COUNTRIES_AND_CAPITALS})
    public @interface Tables {

    }

    @NonNull
    @Contract(pure = true)
    public static String getTableName(@Tables int tableId) {
        switch (tableId) {
            case TABLE_OLD_TESTS:
                return "OldTests";
            case TABLE_COUNTRIES_AND_CAPITALS:
                return "CountriesAndCapitals";
            default:
                throw new IllegalArgumentException("The table id:" + tableId + " is not a tableId");
        }
    }

}
