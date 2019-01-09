package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.CheckBox;
import android.widget.TextView;
import io.bitbucket.technorex.pigeo.Domain.Contact;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Service.ContactDatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends Activity {

    private List<Contact> contacts = new ArrayList<>();

    private RecyclerView contactsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        prepareListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveCards();
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
        return super.onOptionsItemSelected(item);
    }

    private void retrieveCards() {
        ContactDatabaseService contactDatabaseService = new ContactDatabaseService(this);
        contacts = contactDatabaseService.listContact();

        AddContactAdapter addContactAdapter = (AddContactActivity.AddContactAdapter) contactsRecyclerView.getAdapter();
        assert addContactAdapter != null;
        addContactAdapter.setContacts(contacts);
        addContactAdapter.notifyDataSetChanged();
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
