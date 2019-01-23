package io.bitbucket.technorex.pigeo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.*;
import io.bitbucket.technorex.pigeo.Domain.Notification;
import io.bitbucket.technorex.pigeo.Domain.Profile;
import io.bitbucket.technorex.pigeo.R;
import io.bitbucket.technorex.pigeo.Repository.DatabaseProfileRepository;
import io.bitbucket.technorex.pigeo.Service.ProfileDatabaseService;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends Activity {
    private List<Notification> notifications = new ArrayList<>();

    @SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
    private RecyclerView notifactionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Notifications");
        prepareListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveNotifications();
    }

    private void retrieveNotifications() {
        ProfileDatabaseService profileDatabaseService = new ProfileDatabaseService(this);
        Profile profile = profileDatabaseService.retrieveProfile();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/" + profile.getPhoneNO());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifications.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Notification notification = ds.getValue(Notification.class);
                    assert notification != null;
                    //Log.e("----NOTIFICATION--->>>", notification.toString());
                    notifications.add(notification);
                }
                NotificationsListAdapter notificationsListAdapter = (NotificationsListAdapter) notifactionsRecyclerView.getAdapter();
                assert notificationsListAdapter != null;
                notificationsListAdapter.setNotifications(notifications);
                notificationsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void prepareListView() {
        notifactionsRecyclerView = findViewById(R.id.notifications_list);
        notifactionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveNotifications();
        notifactionsRecyclerView.setAdapter(new NotificationsListAdapter(notifications));
    }

    private class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListItemViewHolder>{
        private List<Notification> notifications;

        NotificationsListAdapter(List<Notification> notifications){
            this.notifications = notifications;
        }
        public void setNotifications(List<Notification> notifications){
            this.notifications = notifications;
        }

        @Override
        public int getItemCount(){
            return notifications.size();
        }

        @NonNull
        @Override
        public NotificationsListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(NotificationActivity.this).inflate(R.layout.row_notifications_list,viewGroup,false);

            return new NotificationsListItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationsListItemViewHolder notificationsListItemViewHolder, int i) {

            final Notification notification  = notifications.get(i);
            String notification_text = notification.getName() + " is asking you for help.";

            notificationsListItemViewHolder.notificationText.setText(notification_text);
            notificationsListItemViewHolder.cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseProfileRepository databaseProfileRepository
                            = new DatabaseProfileRepository(NotificationActivity.this);
                    Profile profile = databaseProfileRepository.retrieveProfile();
                    DatabaseReference databaseReference
                            = FirebaseDatabase.getInstance().getReference("/"+profile.getPhoneNO()+"/");
                    databaseReference.child(notification.getContactNumber()).setValue(null);
                }
            });

            notificationsListItemViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(NotificationActivity.this, SOSActivity.class).putExtra("receiveHelp", false));
                }
            });
        }
    }

    private class NotificationsListItemViewHolder extends RecyclerView.ViewHolder{
        private TextView notificationText;
        private Button acceptButton, cancelButton;

        NotificationsListItemViewHolder(@NonNull View view){
            super(view);
            notificationText = view.findViewById(R.id.notification_text);
            acceptButton = view.findViewById(R.id.acceptSOS);
            cancelButton = view.findViewById(R.id.cancelSOS);
        }
    }
}
