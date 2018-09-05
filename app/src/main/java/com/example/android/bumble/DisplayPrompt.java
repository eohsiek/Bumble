package com.example.android.bumble;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_prompt);
        mService = ApiUtils.getPromptService();

        Intent intent = getIntent();
        String promptType = intent.getStringExtra("promptType");
        TextView title = findViewById(R.id.title);

        title.setText(promptType);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getPrompt();
    }

    public void getPrompt() {
        Log.d("DisplayPromptActivity", "call API");
        mService.getAnswers().enqueue(new Callback<PromptResponse>() {

            @Override

            public void onResponse(Call<PromptResponse> call, Response<PromptResponse> response) {

                if(response.isSuccessful()) {
                    TextView promptText = findViewById(R.id.promptText);
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
        recreate();
    }

}
