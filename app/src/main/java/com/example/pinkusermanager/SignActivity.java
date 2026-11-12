package com.example.pinkusermanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignActivity extends AppCompatActivity {

    private EditText LoginTextView, PasswordTextView;
    private EditText NewLoginTextView, NewPasswordTextView, NewNameTextView, NewAgeTextView, NewStateTextView;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        Init();
        CheckSignInInfo();
    }

    private void Init() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tabSignIn);
        tabSpec.setIndicator("Вход");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tabSignUp);
        tabSpec.setIndicator("Регистрация");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        LoginTextView = findViewById(R.id.LoginTextView);
        PasswordTextView = findViewById(R.id.PasswordTextView);
        NewLoginTextView = findViewById(R.id.NewLoginTextView);
        NewPasswordTextView = findViewById(R.id.NewPasswordTextView);
        NewNameTextView = findViewById(R.id.NewNameTextView);
        NewAgeTextView = findViewById(R.id.NewAgeTextView);
        NewStateTextView = findViewById(R.id.NewStateTextView);
    }

    private void CheckSignInInfo() {
        SharedPreferences sp = getSharedPreferences(Transform.APP_PREFERENCES, Context.MODE_PRIVATE);
        String login = sp.getString(Transform.LOGIN, "");
        String password = sp.getString(Transform.PASSWORD, "");
        LoginTextView.setText(login);
        PasswordTextView.setText(password);
    }

    public void SignIn(View view) {
        if (Transform.StringNoNull(LoginTextView.getText().toString()) && Transform.StringNoNull(PasswordTextView.getText().toString())) {
            String login = LoginTextView.getText().toString();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(UserStaticInfo.USERS_SIGN_IN_INFO);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(login).exists()) {
                        String passFromDB = dataSnapshot.child(login).child(UserStaticInfo.PASSWORD).getValue(String.class);
                        if (passFromDB != null && passFromDB.equals(PasswordTextView.getText().toString())) {
                            String profileId = dataSnapshot.child(login).child(UserStaticInfo.PROFILE_ID).getValue(String.class);
                            goNext(profileId);
                        } else {
                            CantSignIn();
                        }
                    } else {
                        CantSignIn();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        } else {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
        }
    }

    public void SignUp(View view) {
        String login = NewLoginTextView.getText().toString();
        String password = NewPasswordTextView.getText().toString();
        String name = NewNameTextView.getText().toString();
        String age = NewAgeTextView.getText().toString();
        String state = NewStateTextView.getText().toString();

        if (Transform.StringNoNull(login) && Transform.StringNoNull(password) && Transform.StringNoNull(name) && Transform.StringNoNull(state)) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(UserStaticInfo.USERS_SIGN_IN_INFO).child(login);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.child(UserStaticInfo.PASSWORD).exists()) {
                        String id = database.getReference(UserStaticInfo.USERS_PROFILE_INFO).push().getKey();

                        database.getReference(UserStaticInfo.USERS_SIGN_IN_INFO).child(login).child(UserStaticInfo.PASSWORD).setValue(password);
                        database.getReference(UserStaticInfo.USERS_SIGN_IN_INFO).child(login).child(UserStaticInfo.PROFILE_ID).setValue(id);

                        database.getReference(UserStaticInfo.USERS_PROFILE_INFO).child(id).child(UserStaticInfo.NAME).setValue(name);
                        database.getReference(UserStaticInfo.USERS_PROFILE_INFO).child(id).child(UserStaticInfo.AGE).setValue(age);
                        database.getReference(UserStaticInfo.USERS_PROFILE_INFO).child(id).child(UserStaticInfo.STATE).setValue(state);

                        goNext(id);
                    } else {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (v != null) v.vibrate(200);
                        Toast.makeText(SignActivity.this, "Пользователь уже существует", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        } else {
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goNext(String profileId) {
        Transform.SaveUser(getSharedPreferences(Transform.APP_PREFERENCES, Context.MODE_PRIVATE), LoginTextView.getText().toString(), PasswordTextView.getText().toString());
        UserStaticInfo.profileId = profileId;
        Intent intent = new Intent(this, LoadedUserDataActivity.class);
        startActivity(intent);
        finish();
    }

    private void CantSignIn() {
        Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
    }
}