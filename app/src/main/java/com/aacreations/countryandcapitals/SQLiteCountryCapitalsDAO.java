package com.aacreations.countryandcapitals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.CellSignalStrength;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SQLiteCountryCapitalsDAO extends SQLiteOpenHelper implements CountryCapitalsDAO{
    private static final String TAG = "SQLiteCCDAO";
    public static final String DB_NAME = "CountriesAndCapitals.db";
    @SuppressLint("SdCardPath")
    public static final String DB_LOCATION = "/data/data/com.aacreations.countryandcapitals/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    // implement SQLiteCountryCapitalsDAO as a Singleton
    @SuppressLint("StaticFieldLeak")
    private static SQLiteCountryCapitalsDAO instance = null;
    protected int testId = 0;
    private final String TEST_ID = "Test Id 109683";

    private SQLiteCountryCapitalsDAO(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        SharedPreferences preferences = context.getSharedPreferences(TEST_ID, Context.MODE_PRIVATE);
        if (preferences != null) {
            testId = preferences.getInt(TEST_ID, 0);
        }
    }

    /**
     * Get an instance of the app's Singleton database helper object
     *
     * @param context
     * @return a SQLite database helper object
     */
    static SQLiteCountryCapitalsDAO getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG, "getInstance: creating new instance");
            instance = new SQLiteCountryCapitalsDAO(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.");
        }

    }


    public void openDatabase() throws SQLException
    {
        String myPath = DB_LOCATION + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    @Override
    public void create(@NonNull CountryCapital toBeCreated, @Table.Tables int table) {
        openDatabase();
        String countryName = toBeCreated.getCountryName();
        String capitalName = toBeCreated.getCapitalName();
        String continent = toBeCreated.getContinent();
//        byte[] answerSent = toBeCreated.getImage();
        String sSQL = "";
        switch (table) {
            case Table.TABLE_COUNTRIES_AND_CAPITALS:
                sSQL = "INSERT INTO " + Table.getTableName(table) + "(Country,Capitals,Continent,Colour) VALUES ('" + countryName + "','" + capitalName + "','" + continent + "','" + toBeCreated.getColour() + "');";
                break;
            case Table.TABLE_OLD_TESTS:
                sSQL = "INSERT INTO " + Table.getTableName(table) + "(TestId,Country,UserAnswer,CorrectAnswer,QuestionsCorrect,QuestionsWrong, TotalQuestions, Percentage, Date, Time, TimeTaken)" +
                        "VALUES (\"" + testId + "\",\"" + countryName + "\",\"" + toBeCreated.getAnswerSent() + "\",\"" + capitalName + "\",\"" + toBeCreated.getQuestionsCorrect() + "\",\"" + toBeCreated.getQuestionsWrong()
                        + "\",\"" + toBeCreated.getTotalQuestions() + "\",\"" + toBeCreated.getPercentage() + "\",\"" + toBeCreated.getDate() + "\",\"" + toBeCreated.getTime() + "\",\"" + toBeCreated.getTimeTaken() + "\");";

                SharedPreferences preferences = mContext.getSharedPreferences(TEST_ID, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(TEST_ID, testId);
                editor.apply();
                break;
        }
        Log.d(TAG, "create: " + sSQL);
        mDatabase.execSQL(sSQL);
        closeDatabase();
    }

    @Override
    public CountryCapital read(int id, @Table.Tables int table) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + Table.getTableName(table) + " WHERE Id = '" + id + "'", null);
        CountryCapital countryCapital = null;
        if (cursor != null) {
            cursor.moveToFirst();
//            countryCapital = new CountryCapital(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5), cursor.getBlob(6));
            countryCapital = new CountryCapital(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            Log.d(TAG, "read: " + cursor.getString(2));
            cursor.close();
        }
        closeDatabase();
        return countryCapital;
    }

    public List<CountryCapital> readAllFromCountryCapitals(@NonNull String continent) {
        switch (continent) {
            case MainAccess.Options.ASIA:
            case MainAccess.Options.AFRICA:
            case MainAccess.Options.NORTH_AMERICA:
            case MainAccess.Options.SOUTH_AMERICA:
            case MainAccess.Options.EUROPE:
            case MainAccess.Options.OCEANA:
            case MainAccess.Options.ALL:
                break;
            default:
                throw new IllegalArgumentException(continent + " is not a continent");
        }
        CountryCapital countryCapital;
        List<CountryCapital> countryCapitalList =  new ArrayList<>();
        openDatabase();
        Log.d(TAG, "readAllFromCountryCapitals: database is opened");

        Cursor cursor;

        if (continent.equals(MainAccess.Options.ALL)) {
            cursor = mDatabase.rawQuery("SELECT * FROM CountriesAndCapitals", null);
        } else {
            cursor = mDatabase.rawQuery("SELECT * FROM CountriesAndCapitals WHERE Continent = '" + continent + "'", null);
        }
        Log.d(TAG, "readAllFromCountryCapitals: cursor set");
        if (cursor != null) {
            cursor.moveToFirst();
            Log.d(TAG, "readAllFromCountryCapitals: cursor has moved to first");
            do {
                countryCapital = new CountryCapital(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                countryCapitalList.add(countryCapital);
            } while (cursor.moveToNext());
            cursor.close();
        }
        closeDatabase();
        Log.d(TAG, "readAllFromCountryCapitals: complete");
        return countryCapitalList;
    }

    public List<CountryCapital> readAllFromOldTests(boolean idSortOrder, int id) {
        CountryCapital countryCapital;
        List<CountryCapital> countryCapitalList =  new ArrayList<>();
        openDatabase();
        Log.d(TAG, "readAllFromOldTests: database is opened");
        Cursor cursor;
        if (idSortOrder) {
             cursor = mDatabase.rawQuery("SELECT * FROM OldTests WHERE TestId = " + id + " ORDER BY TestId", null);
        } else {
            cursor = mDatabase.rawQuery("SELECT * FROM OldTests", null);
        }
        Log.d(TAG, "readAllFromOldTests: cursor set");
        if (cursor != null) {
            cursor.moveToFirst();
            Log.d(TAG, "readAllFromOldTests: cursor has moved to first");
            do {
                Log.d(TAG, "readAllFromOldTests: do");
                try {
//                countryCapital = new CountryCapital(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5), cursor.getBlob(6));
                    countryCapital = new CountryCapital(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                            ,cursor.getInt(4),cursor.getInt(5),cursor.getInt(6), cursor.getString(7), cursor.getLong(8), cursor.getString(9), cursor.getString(10));
                    countryCapitalList.add(countryCapital);
                    Log.d(TAG, "readAllFromOldTests: " + countryCapital.ttoString());
                } catch (IndexOutOfBoundsException ignored) {

                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        closeDatabase();
        Log.d(TAG, "readAllFromOldTests: complete");
        return countryCapitalList;
    }

    public List<CountryCapital> readIdFromOldTests() {
        CountryCapital countryCapital;
        List<CountryCapital> countryCapitalList =  new ArrayList<>();
        openDatabase();
        Log.d(TAG, "readIdFromOldTests: database is opened");
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT DISTINCT TestId, QuestionsCorrect, QuestionsWrong, TotalQuestions, Percentage, Date, Time, TimeTaken FROM OldTests ORDER BY TestId ", null);

        Log.d(TAG, "readIdFromOldTests: cursor set");
        if (cursor != null) {
            cursor.moveToFirst();
            Log.d(TAG, "readIdFromOldTests: cursor has moved to first");
            do {
                Log.d(TAG, "readIdFromOldTests: do");
                try {
//                countryCapital = new CountryCapital(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getBlob(5), cursor.getBlob(6));
                    countryCapital = new CountryCapital(cursor.getInt(0) ,cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),
                            cursor.getString(4), cursor.getLong(5), cursor.getString(6), cursor.getString(7));
                    countryCapitalList.add(countryCapital);
                    Log.d(TAG, "readIdFromOldTests: " + countryCapital.ttoString());
                } catch (IndexOutOfBoundsException ignored) {

                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        closeDatabase();
        Log.d(TAG, "readIdFromOldTests: complete");
        return countryCapitalList;
    }

    @Override
    public void update(int id, @NonNull CountryCapital updateValue, @Table.Tables int table) {
        openDatabase();

        String sSql = "UPDATE " + Table.getTableName(table) + " WHERE Id = '" + id + "'";
        Log.d(TAG, "delete: " + sSql);
        mDatabase.execSQL(sSql);
        closeDatabase();
    }

    @Override
    public void delete(int id, @Table.Tables int table) {
        runSQL("DELETE FROM " + Table.getTableName(table) + " WHERE Id = '" + id + "'");
    }

    public void runSQL(String sql) {
        openDatabase();
        Log.d(TAG, "runSQL: " + sql);
        mDatabase.execSQL(sql);
        closeDatabase();
    }

    /**
     * turns a byte[] into a {@link Bitmap}
     * @param images the byte[] of images
     * @return a bitmap
     */
    public static Bitmap decodeImage(byte[] images) {
        return BitmapFactory.decodeByteArray(images, 0, images.length);
    }

    /**
     *
     * @param name name of the image to get
     * @param assetManager an asset manager
     * @return the image converted into bitmap or returns null image does not exist
     */
    public static Bitmap getImageAsBitmap(String name, AssetManager assetManager) {
        Log.d(TAG, "getImage: called");
        try {
            InputStream inputStream = assetManager.open("flags/" + name + ".png");
            Log.d(TAG, "getImage: image opened");
            int size = inputStream.available();
            Log.d(TAG, "getImage: image is available");
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            Log.d(TAG, "getImage: image read");
            return decodeImage(buffer);
        } catch (IOException ioException) {
            Log.e(TAG, "getImage: EXCEPTION, EXCEPTION");
            Log.d(TAG, "getImageAsBitmap: NAME = " + name);
            return null;
        }
    }

    public void newTest() {
        testId++;
    }

}
