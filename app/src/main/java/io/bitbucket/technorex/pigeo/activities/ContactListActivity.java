package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.ContactRepository;
import io.bitbucket.technorex.pigeo.Service.ContactDatabaseService;
import io.bitbucket.technorex.pigeo.Service.ContactServerService;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends Activity {
    private List<Contact> contacts = new ArrayList<>();

    private RecyclerView contactsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        prepareListView();
        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_list_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_contact)
            startActivity(new Intent(ContactListActivity.this,AddContactActivity.class));
        return super.onOptionsItemSelected(item);
    }

    private void retrieveContacts() {
        final ContactDatabaseService contactDatabaseService = new ContactDatabaseService(this);
        ContactServerService contactServerService = new ContactServerService();

        contactServerService.listCardsAsync(new ContactRepository.OnResultListener<List<Contact>>() {
            @Override
            public void onResult(List<Contact> data) {
                contacts = data;
                ContactListAdapter contactListAdapter= (ContactListAdapter) contactsRecyclerView.getAdapter();
                assert contactListAdapter != null;
                contactListAdapter.setContacts(data);
                contactListAdapter.notifyDataSetChanged();
                contactDatabaseService.resetContacts();

                for(Contact contact: data){
                    contactDatabaseService.addContact(contact);
                    Log.e("-----Contact --->>> ", contact.toString());
                }
            }
        });
    }

    private void prepareListView() {
        contactsRecyclerView = findViewById(R.id.contact_list);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveContacts();
        contactsRecyclerView.setAdapter(new ContactListAdapter(contacts));
    }

    private class ContactListAdapter extends RecyclerView.Adapter<ContactListItemViewHolder>{
        private List<Contact> contacts;

        ContactListAdapter(List<Contact> contacts){
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
        public ContactListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(ContactListActivity.this).inflate(R.layout.row_contact_list,viewGroup,false);

            return new ContactListItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactListItemViewHolder contactListItemViewHolder, int i) {

            final Contact contact  = contacts.get(i);

            contactListItemViewHolder.contactName.setText(contact.getContactName());
            contactListItemViewHolder.contactNumber.setText(contact.getContactNumber());
        }
    }

    private class ContactListItemViewHolder extends RecyclerView.ViewHolder{
        private TextView contactName;
        private TextView contactNumber;

        ContactListItemViewHolder(@NonNull View view){
            super(view);

            contactName=view.findViewById(R.id.contact_name);
            contactNumber=view.findViewById(R.id.contact_number);
        }
    }

    private void checkPermissions() {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.INTERNET,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_CONTACTS};
        String[] permission1 = {android.Manifest.permission.READ_CONTACTS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission : permission1){
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, 3);
                }
            }
        }
    }
}
