package com.example.android.bumble;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final Fragment homeFragment = new HomeFragment();
    final Fragment savedPromptsFragment = new SavedPromptsFragment();
    final Fragment suggestWordFragment = new SuggestWordFragment();
    final Fragment promptFragment = new PromptFragment();

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
                    fragmentManager.beginTransaction().replace(R.id.main_container, savedPromptsFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    fragmentManager.beginTransaction().replace(R.id.main_container, suggestWordFragment).commit();
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

        navigation.getMenu().setGroupCheckable(0,false, true);
        fragmentManager.beginTransaction().replace(R.id.main_container, promptFragment).commit();
    }

    public BottomNavigationView getNavigation() {
        return navigation;
    }

}
