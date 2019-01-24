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
                                cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                        )
                );
            }
        }
        return contacts;
    }

    public List<Contact> getAllContacts(){
        List<Contact> contacts = new ArrayList<>();
        try(SQLiteDatabase db = new DbHelper(context).getReadableDatabase();
            Cursor cursor= db.query("ALLCONTACTS",null,null,null,null,null,null)){
            while (cursor.moveToNext()){
                contacts.add(
                        new Contact(
                                cursor.getString(cursor.getColumnIndexOrThrow("contact_name")),
                                cursor.getString(cursor.getColumnIndexOrThrow("contact_number")),
                                cursor.getString(cursor.getColumnIndexOrThrow("_id")),
                                cursor.getString(cursor.getColumnIndexOrThrow("checker"))
                        )
                );
            }
        }
        if(contacts.size() == 0 )
            return null;
        return contacts;
    }

    @Override
    public void addContact(Contact contact) {
        ContentValues contentValues = getContentValues(contact);
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            db.insertOrThrow("CONTACTS",null,contentValues);
        }
    }

    public void addToContacts(List<Contact> contacts) {
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()) {
            for(Contact contact : contacts){
                ContentValues contentValues = getContentValues(contact);
                db.insertOrThrow("CONTACTS",null,contentValues);
            }
        }
    }

    public void addToAllContacts(List<Contact> contacts){
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()) {
            for(Contact contact : contacts){
                ContentValues contentValues = getAllContentValues(contact);
                db.insertOrThrow("ALLCONTACTS",null,contentValues);
            }
        }
    }
    public String getContactCount(){
        int i=0;
        try(SQLiteDatabase db = new DbHelper(context).getReadableDatabase();
            Cursor cursor= db.query("CONTACTS",null,null,null,null,null,null)){
            while (cursor.moveToNext())
                i++;
        }
        return Integer.toString(i);
    }

    public void resetContacts() {
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            String delete = "DELETE FROM CONTACTS";
            db.execSQL(delete);
        }
    }

    public void resetAllContacts() {
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()) {
            String delete = "DELETE FROM ALLCONTACTS";
            db.execSQL(delete);
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            String deleteContact = "DELETE FROM CONTACTS " +
                                    "WHERE CONTACT_NAME='" +
                                    contact.getContactName() +
                                    "' AND CONTACT_NUMBER='" +
                                    contact.getContactNumber() +
                                    "'";
            db.execSQL(deleteContact);
        }
    }

    @Override
    public void updateContact(Contact contact) {
        ContentValues contentValues = getContentValues(contact);
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            db.update("CONTACTS",contentValues,"_id=?",new String[]{contact.getId()});
        }
    }

    public void updateContacts() {
        try(SQLiteDatabase db = new DbHelper(context).getWritableDatabase()){
            String update = "UPDATE ALLCONTACTS SET checker='no'";
            db.execSQL(update);
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
                        cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                );
            }
            cursor.close();
        }
        return contact;
    }

    @NonNull
    private ContentValues getContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("contact_name", contact.getContactName());
        values.put("contact_number", contact.getContactNumber());
        return values;
    }

    @NonNull
    private ContentValues getAllContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put("contact_name", contact.getContactName());
        values.put("contact_number", contact.getContactNumber());
        values.put("checker", contact.getChecker());
        return values;
    }
}
