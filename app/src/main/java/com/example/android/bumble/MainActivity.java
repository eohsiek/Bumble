package com.example.android.bumble;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity{

    final Fragment homeFragment = new HomeFragment();
    final Fragment savedPromptsFragment = new SavedPromptsFragment();
    final Fragment suggestWordFragment = new SuggestWordFragment();
    final Fragment promptFragment = new PromptFragment();
    private FirebaseAnalytics mFirebaseAnalytics;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().add(R.id.main_container, homeFragment).commit();
        navigation.setSelectedItemId(R.id.navigation_home);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().replace(R.id.main_container, homeFragment).commit();
                    return true;
                case R.id.navigation_favorites:
                    fragmentManager.beginTransaction().replace(R.id.main_container, savedPromptsFragment, "savedPromptsFragment").commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentManager.beginTransaction().replace(R.id.main_container, suggestWordFragment, "suggestWordFragment").commit();
                    return true;
            }
            return false;
        }
    };

    public void getPrompt(View v) {
        Button b = (Button)v;
        String buttonText = b.getText().toString();
        Bundle promptInfo = new Bundle();
        promptInfo.putString("promptType", buttonText);
        promptFragment.setArguments(promptInfo);
        // log prompt to Analytics
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, buttonText);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "prompt");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        fragmentManager.beginTransaction().replace(R.id.main_container, promptFragment, "promptFragment").commit();
    }

    public BottomNavigationView getNavigation() {
        return navigation;
    }

    public void getNewPrompt(View v) {
        PromptFragment promptFragment = (PromptFragment) getSupportFragmentManager().findFragmentByTag("promptFragment");
        promptFragment.getPrompt();
    }

    public void sendWord(View v) {
        SuggestWordFragment suggestWordFragment = (SuggestWordFragment) getSupportFragmentManager().findFragmentByTag("suggestWordFragment");
        suggestWordFragment.sendWord();
    }

    public void processFavorite(View v) {
        Toast.makeText(this, "Favorite button pressed", Toast.LENGTH_SHORT).show();
        PromptFragment promptFragment = (PromptFragment) getSupportFragmentManager().findFragmentByTag("promptFragment");
        promptFragment.processFavorite();
    }

}
