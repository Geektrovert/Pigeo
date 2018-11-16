package io.bitbucket.technorex.pigeo.Repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.Utils.DbHelper;

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
                    .query("PROFILE",null,"_id=?",new String[]{"1"},null,null,null);
            while (cursor.moveToNext()){
                profile=new Profile(
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

    @Override
    public void updateProfile(Profile profile) {

    }
}
