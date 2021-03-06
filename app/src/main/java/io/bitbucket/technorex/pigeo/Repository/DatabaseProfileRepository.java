package io.bitbucket.technorex.pigeo.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Utils.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseProfileRepository implements ProfileRepository {
    private Context context;

    public DatabaseProfileRepository(Context context) {
        this.context = context;
    }

    @Override
    public Profile retrieveProfile() {
        Profile profile=null;

        try (SQLiteDatabase db = new DbHelper(context).getReadableDatabase()){
            Cursor cursor = db
                    .query("PROFILE",null,null,null,null,null,null);
            while (cursor.moveToNext()){
                profile=new Profile(
                        cursor.getString(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("user_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password_hash")),
                        cursor.getString(cursor.getColumnIndexOrThrow("national_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone_no"))
                );
            }
            cursor.close();
        }
        return profile;
    }

    public void addProfile(Profile profile) {
        ContentValues contentValues = getContentValues(profile);

        try (SQLiteDatabase db = new DbHelper(context).getWritableDatabase()) {
            db.insertOrThrow("PROFILE", null, contentValues);
        }
    }

    @Override
    public List<Profile> getProfiles() {
        List<Profile> profiles = new ArrayList<>();
        try (SQLiteDatabase db = new DbHelper(context).getReadableDatabase()){
            Cursor cursor = db
                    .query("PROFILE",null,null,null,null,null,null);
            while (cursor.moveToNext()){
                profiles.add(
                        new Profile(
                                cursor.getString(cursor.getColumnIndexOrThrow("_id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("user_name")),
                                cursor.getString(cursor.getColumnIndexOrThrow("email_id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("password_hash")),
                                cursor.getString(cursor.getColumnIndexOrThrow("national_id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("phone_no"))
                        )
                );
            }
            cursor.close();
        }
        return profiles;
    }

    @Override
    public void updateProfile(Profile profile) {
        try (SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            db.update("PROFILE",getContentValues(profile),"_id=?",new String[]{profile.getId()});
        }
    }

    @Override
    public void retrieveProfile(String email, OnResultListener<Profile> resultListener) {
        resultListener.onResult(retrieveProfile());
    }

    @Override
    public void reset() {

        String clearProfile
                = "DELETE FROM PROFILE";

        try (SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            db.execSQL(clearProfile);
        }
    }

    private ContentValues getContentValues(Profile profile){
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", profile.getId());
        contentValues.put("user_name",profile.getUserName());
        contentValues.put("email_id",profile.getEmailID());
        contentValues.put("password_hash",profile.getPasswordHash());
        contentValues.put("national_id",profile.getNationalID());
        contentValues.put("phone_no",profile.getPhoneNO());
        return contentValues;
    }
}
