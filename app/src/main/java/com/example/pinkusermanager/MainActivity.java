package com.example.pinkusermanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Context context;
    LayoutInflater layoutInflater;
    UserStaticInfo userStaticInfo;
    static UserListAdapter userListAdapter;
    static FrameLayout UserPanel;
    static TextView NameTextView, StateTextView, AgeTextView;

    int positionActiveUser;

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
        listView = findViewById(R.id.listView);

        userListAdapter = new UserListAdapter();
        listView.setAdapter(userListAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            positionActiveUser = position;
            InitPanel(UserStaticInfo.users.get(position));
            UserVisibility(true);
        });
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

    private static void InitPanel(User item) {
        NameTextView.setText(item.getName());
        StateTextView.setText(item.getState());
        AgeTextView.setText(String.valueOf(item.getAge()));
    }

    public static void UpdateListAndUserPanel(User user) {
        userListAdapter.notifyDataSetChanged();
        InitPanel(user);
    }

    private class UserListAdapter extends BaseAdapter {
        @Override
        public int getCount() { return UserStaticInfo.users.size(); }

        @Override
        public User getItem(int position) { return UserStaticInfo.users.get(position); }

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