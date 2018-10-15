package com.example.android.bumble;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity{


    final Fragment homeFragment = new HomeFragment();
    final Fragment savedPromptsFragment = new SavedPromptsFragment();
    final Fragment suggestWordFragment = new SuggestWordFragment();
    final Fragment promptFragment = new PromptFragment();
    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-1915477212292828~2627008740");

        mAdView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(savedInstanceState == null) {
            fragmentManager.beginTransaction().add(R.id.main_container, homeFragment).commit();
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {
            //default to previous fragment
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
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
        Toast.makeText(this, "Prompt Saved to Favorites", Toast.LENGTH_SHORT).show();
        PromptFragment promptFragment = (PromptFragment) getSupportFragmentManager().findFragmentByTag("promptFragment");
        promptFragment.processFavorite();
    }

    public void showCredits(MenuItem mi) {
        new AlertDialog.Builder(this)
                .setTitle("Credits")
                .setMessage("Bumble's Drawing Prompts written by Liz Ohsiek.  Artwork by Nadia Ohsiek")

                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
