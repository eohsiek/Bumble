package com.example.android.bumble;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bumble.network.PromptService;
import com.example.android.bumble.network.pojo.ApiUtils;
import com.example.android.bumble.network.pojo.PromptResponse;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayPrompt extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_favorites:

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    private PromptService mService;
    private TextView title;
    private ImageView loadingImage;
    private TextView promptText;
    private SharedPreferences.Editor editor;
    private String promptType;
    public static final String USER_SETTINGS = "UserSettings";
    public static final String USER_SETTING_ADJECTIVE1 = "Adjective1";
    private Switch adjective1Switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(USER_SETTINGS, MODE_PRIVATE);
        Log.i("DisplayPromptPref", preferences.toString());

        setContentView(R.layout.activity_display_prompt);
        mService = ApiUtils.getPromptService();

        Intent intent = getIntent();
        promptType = intent.getStringExtra("promptType");
        title = findViewById(R.id.title);
        loadingImage = findViewById(R.id.loadingImage);
        promptText = findViewById(R.id.promptText);
        adjective1Switch = findViewById(R.id.adjective1Switch);

        title.setText(promptType);
        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.loader)
                .into(loadingImage);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Boolean adjective1SwitchState = preferences.getBoolean(promptType + USER_SETTING_ADJECTIVE1, true);
        adjective1Switch.setChecked(adjective1SwitchState);

        editor = getSharedPreferences(USER_SETTINGS, MODE_PRIVATE).edit();

        adjective1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    editor.putBoolean(promptType + USER_SETTING_ADJECTIVE1, true);
                }
                else {
                    editor.putBoolean(promptType + USER_SETTING_ADJECTIVE1, false);
                }
                editor.commit();
            }
        });

        getPrompt();
    }

    public void getPrompt() {
        Log.d("DisplayPromptActivity", "call API");
        promptText.setText("");
        loadingImage.setVisibility(View.VISIBLE);

        mService.getAnswers().enqueue(new Callback<PromptResponse>() {

            @Override

            public void onResponse(Call<PromptResponse> call, Response<PromptResponse> response) {

                if(response.isSuccessful()) {
                    loadingImage.setVisibility(View.INVISIBLE);
                    promptText.setText(response.body().getPrompt());
                    Log.d("DisplayPromptActivity", response.body().getPrompt());
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.d("DisplayPromptActivity", String.valueOf(statusCode));
                }
            }

            @Override
            public void onFailure(Call<PromptResponse> call, Throwable t) {
                //showErrorMessage();
                Log.d("DisplayPromptActivity", "error loading prompt from API");

            }
        });
    }

    public void getNewPrompt(View v) {
        getPrompt();
    }

}
