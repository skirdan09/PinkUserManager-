package com.example.pinkusermanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Context context;
    LayoutInflater layoutInflater;
    UserStaticInfo userStaticInfo;
    static UserListAdapter userListAdapter;
    static FrameLayout UserPanel;
    static TextView NameTextView, StateTextView, AgeTextView;
    Button btnAction;
    EditText etMessage;
    LinearLayout messagesContainer;
    ScrollView scrollView;

    int positionActiveUser;
    boolean isArchiveMode = false;
    String currentSenderName = "Я";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        userStaticInfo = new UserStaticInfo();

        Init();
    }

    private void Init() {
        UserPanel = findViewById(R.id.userPanel);
        NameTextView = findViewById(R.id.NameTextView);
        StateTextView = findViewById(R.id.StateTextView);
        AgeTextView = findViewById(R.id.AgeTextView);
        btnAction = findViewById(R.id.btnAction);
        etMessage = findViewById(R.id.etMessage);
        messagesContainer = findViewById(R.id.messagesContainer);
        scrollView = findViewById(R.id.scrollView);
        listView = findViewById(R.id.listView);

        userListAdapter = new UserListAdapter();
        listView.setAdapter(userListAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            User clickedUser = getFilteredUsers().get(position);
            positionActiveUser = UserStaticInfo.users.indexOf(clickedUser);
            InitPanel(clickedUser);
            UserVisibility(true);
            LoadMessages(clickedUser);
        });
    }

    public void SendMessage(View view) {
        String text = etMessage.getText().toString().trim();
        if (!text.isEmpty()) {
            User currentUser = UserStaticInfo.users.get(positionActiveUser);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String profileId = currentUser.getProfileId();

            if (profileId != null) {

                database.getReference(UserStaticInfo.USERS_PROFILE_INFO).child(profileId).child("messages")
                        .push().child("text").setValue(text);

            }

            etMessage.setText("");
        }
    }

    private void LoadMessages(User user) {
        messagesContainer.removeAllViews();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String profileId = user.getProfileId();

        if (profileId != null) {
            database.getReference(UserStaticInfo.USERS_PROFILE_INFO).child(profileId).child("messages")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            messagesContainer.removeAllViews();
                            for (DataSnapshot msg : snapshot.getChildren()) {
                                String text = msg.child("text").getValue(String.class);
                                if (text != null) {
                                    addMessageBubble(text);
                                }
                            }
                            if (scrollView != null) {
                                scrollView.postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 100);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
        }
    }

    private void addMessageBubble(String text) {
        TextView messageView = new TextView(this);
        messageView.setText(text);
        messageView.setTextSize(18f);
        messageView.setTextColor(Color.BLACK);
        messageView.setBackgroundColor(Color.parseColor("#AAFFC0CB"));
        messageView.setPadding(20, 10, 20, 10);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        params.setMargins(0, 5, 0, 5);
        messageView.setLayoutParams(params);

        messagesContainer.addView(messageView);
    }

    public List<User> getFilteredUsers() {
        List<User> filtered = new ArrayList<>();
        for (User u : UserStaticInfo.users) {
            if (isArchiveMode) {
                if (u.isArchived()) filtered.add(u);
            } else {
                if (!u.isArchived()) filtered.add(u);
            }
        }
        return filtered;
    }

    public void OpenArchive(View view) {
        isArchiveMode = !isArchiveMode;
        String text = isArchiveMode ? "Назад в чаты" : "Архив";
        ((Button)findViewById(R.id.btnArchive)).setText(text);
        userListAdapter.notifyDataSetChanged();
    }

    public void ActionButton(View view) {
        User user = UserStaticInfo.users.get(positionActiveUser);

        if (user.isArchived()) {
            user.setArchived(false);
            Toast.makeText(this, "Чат возвращен в список", Toast.LENGTH_SHORT).show();
        } else {
            user.setArchived(true);
            Toast.makeText(this, "Чат добавлен в архив", Toast.LENGTH_SHORT).show();
        }

        UserVisibility(false);
        userListAdapter.notifyDataSetChanged();
    }

    private void UpdateActionButton(boolean isArchived) {
        if (isArchived) {
            btnAction.setText("Вернуть");
            btnAction.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.GREEN));
        } else {
            btnAction.setText("В архив");
            btnAction.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF69B4")));
        }
    }

    public void BackToList(View view) {
        UserVisibility(false);
    }

    public void EditUser(View view) {
        GoToUserProfile(positionActiveUser);
    }

    private void GoToUserProfile(int position) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(UserStaticInfo.POSITION, position);
        startActivity(intent);
    }

    private static void UserVisibility(boolean visible) {
        if (visible) UserPanel.setVisibility(View.VISIBLE);
        else UserPanel.setVisibility(View.GONE);
    }

    private void InitPanel(User item) {
        NameTextView.setText(item.getName());
        StateTextView.setText(item.getState());
        AgeTextView.setText(String.valueOf(item.getAge()));
        UpdateActionButton(item.isArchived());
    }

    public static void UpdateListAndUserPanel(User user) {
        userListAdapter.notifyDataSetChanged();
    }

    private class UserListAdapter extends BaseAdapter {
        @Override
        public int getCount() { return getFilteredUsers().size(); }

        @Override
        public User getItem(int position) { return getFilteredUsers().get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View currentView, ViewGroup parent) {
            User currentUser = getItem(position);
            if (currentView == null) {
                currentView = layoutInflater.inflate(R.layout.item_user, parent, false);
            }

            TextView nameView = currentView.findViewById(R.id.NameTextView);
            TextView stateView = currentView.findViewById(R.id.StateTextView);
            FrameLayout stateRound = currentView.findViewById(R.id.StateRound);

            nameView.setText(currentUser.getName());
            stateView.setText(currentUser.getState());

            switch (currentUser.getStateSignal()) {
                case 0: stateRound.setBackgroundResource(R.drawable.back_offline); break;
                case 1: stateRound.setBackgroundResource(R.drawable.back_online); break;
                case 2: stateRound.setBackgroundResource(R.drawable.back_departed); break;
            }

            return currentView;
        }
    }
}