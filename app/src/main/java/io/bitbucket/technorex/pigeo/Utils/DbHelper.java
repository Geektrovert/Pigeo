package io.bitbucket.technorex.pigeo.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    //version will be updated in course of time
    private static final int DB_VERSION=2;

    private static final String DB_NAME = "Pigeo.db";

    public DbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*TODO: Need to redesign schema after more advancement of the project*/
        String createTableContact
                = "CREATE TABLE CONTACTS (" +
                "       _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "       contact_name TEXT," +
                "       contact_number TEXT" +
                "   )";

        String createTableProfile
                = "CREATE TABLE PROFILE (" +
                "       _id INTEGER PRIMARY KEY," +
                "       user_name TEXT," +
                "       email_id TEXT," +
                "       password_hash TEXT," +
                "       national_id TEXT," +
                "       phone_no TEXT" +
                "   )";

        String initializeProfile
                = "INSERT INTO PROFILE VALUES (" +
                "       '1'," +
                "       '-1-1'," +
                "       '-1'," +
                "       '-1'," +
                "       '-1'," +
                "       '-1'" +
                "   )";

        sqLiteDatabase.execSQL(createTableContact);
        sqLiteDatabase.execSQL(createTableProfile);
        sqLiteDatabase.execSQL(initializeProfile);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Nothing to do here
    }
}
