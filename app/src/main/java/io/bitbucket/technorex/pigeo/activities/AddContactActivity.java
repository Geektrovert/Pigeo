package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.DatabaseContactRepository;
import io.bitbucket.technorex.pigeo.Service.ContactDatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends Activity {

    private List<Contact> contacts = new ArrayList<>();
    private ProgressDialog progressDialog;

    private RecyclerView contactsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        progressDialog = new ProgressDialog(this);
        prepareListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.back)
            finish();
        else if(item.getItemId() == R.id.re_sync)
            retrieveContacts();
        else if(item.getItemId() == R.id.save){
            //TODO: need to add code for saving contacts
        }
        return super.onOptionsItemSelected(item);
    }

    private void retrieveContacts() {
        contacts = getContactsFromDatabase();

        if(contacts == null){
            contacts = getContacts();
        }
        Toast.makeText(this,contacts.get(1).toString(),Toast.LENGTH_LONG).show();
        AddContactAdapter addContactAdapter = (AddContactActivity.AddContactAdapter) contactsRecyclerView.getAdapter();
        assert addContactAdapter != null;
        addContactAdapter.setContacts(contacts);
        addContactAdapter.notifyDataSetChanged();
    }

    private List<Contact> getContactsFromDatabase() {
        DatabaseContactRepository databaseContactRepository
                = new DatabaseContactRepository(this);
        List<Contact> contacts;
        contacts = databaseContactRepository.getAllContacts();
        return contacts;
    }

    private class RetrieveAllContacts extends Thread{
        RetrieveAllContacts(){

        }

        @Override
        public void run() {
            synchronized (contacts){
                contacts = getContacts();
            }
        }
    }
    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        assert cursor != null;

        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",new String[]{ id },null);

            assert phoneCursor != null;
            while (phoneCursor.moveToNext()){
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contacts.add(new Contact(name,phoneNumber,id));
            }
            phoneCursor.close();
        }
        cursor.close();
        DatabaseContactRepository databaseContactRepository
                = new DatabaseContactRepository(this);
        databaseContactRepository.addToAllContacts(contacts);
        return contacts;
    }

    private void prepareListView() {
        contactsRecyclerView = findViewById(R.id.add_contact_list);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsRecyclerView.setAdapter(new AddContactActivity.AddContactAdapter(contacts));
    }

    private class AddContactAdapter extends RecyclerView.Adapter<AddContactActivity.ContactListItemViewHolder>{
        private List<Contact> contacts;

        AddContactAdapter(List<Contact> contacts){
            this.contacts = contacts;
        }
        public void setContacts(List<Contact> contacts){
            this.contacts = contacts;
        }

        @Override
        public int getItemCount(){
            return contacts.size();
        }

        @NonNull
        @Override
        public AddContactActivity.ContactListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(AddContactActivity.this).inflate(R.layout.row_add_contact_list,viewGroup,false);

            return new AddContactActivity.ContactListItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AddContactActivity.ContactListItemViewHolder contactListItemViewHolder, int i) {

            final Contact contact  = contacts.get(i);

            contactListItemViewHolder.contactName.setText(contact.getContactName());
            contactListItemViewHolder.contactNumber.setText(contact.getContactNumber());
            if(contact.getChecker().equals("yes")){
                contactListItemViewHolder.checkBox.setChecked(true);
            }
            contactListItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = contactListItemViewHolder.checkBox;
                    if(checkBox.isChecked()){
                        checkBox.setChecked(false);
                    }
                    else
                        checkBox.setChecked(true);
                }
            });
        }
    }

    private class ContactListItemViewHolder extends RecyclerView.ViewHolder{
        private TextView contactName;
        private TextView contactNumber;
        private CheckBox checkBox;

        ContactListItemViewHolder(@NonNull View view){
            super(view);

            contactName=view.findViewById(R.id.contact_name);
            contactNumber=view.findViewById(R.id.contact_number);
            checkBox=view.findViewById(R.id.checkbox);
        }
    }
}
