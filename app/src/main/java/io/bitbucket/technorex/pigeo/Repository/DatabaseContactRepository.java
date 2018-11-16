package io.bitbucket.technorex.pigeo.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.Utils.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseContactRepository implements ContactRepository{
    private Context context;
    public DatabaseContactRepository(Context context){

        this.context=context;
    }

    @Override
    public List<Contact> listContacts() {
        List<Contact> contacts = new ArrayList<>();
        try(SQLiteDatabase db = new DbHelper(context).getReadableDatabase();
            Cursor cursor= db.query("CONTACTS",null,null,null,null,null,null)){
            while (cursor.moveToNext()){
                contacts.add(
                        new Contact(
                                cursor.getString(cursor.getColumnIndexOrThrow("contact_name")),
                                cursor.getString(cursor.getColumnIndexOrThrow("contact_number")),
                                cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
                        )
                );
            }
        }
        return contacts;
    }

    @Override
    public void addContact(Contact contact) {
        ContentValues contentValues = getContentValues(contact);
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            db.insertOrThrow("CONTACTS",null,contentValues);
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            db.delete("CONTACTS","_id=?",new String[]{Integer.toString(contact.getId())});
        }
    }

    @Override
    public void updateContact(Contact contact) {
        ContentValues contentValues = getContentValues(contact);
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            db.update("CONTACTS",contentValues,"_id=?",new String[]{Integer.toString(contact.getId())});
        }
    }

    @Override
    public Contact retrieveContact(int id) {
        Contact contact=null;
        try(SQLiteDatabase db = new DbHelper(context).getReadableDatabase()) {
            Cursor cursor = db
                    .query("CONTACTS", null, "_id=?", new String[]{Integer.toString(id)}, null, null, null);
            while (cursor.moveToNext()) {
                contact = new Contact(
                        cursor.getString(cursor.getColumnIndexOrThrow("contact_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("contact_number")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("_id"))
                );
            }
            cursor.close();
        }
        return contact;
    }

    @NonNull
    private ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("contact_name",contact.getContactName());
        values.put("contact_number",contact.getContactNumber());
        return values;
    }
}
