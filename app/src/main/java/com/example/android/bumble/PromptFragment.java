package com.example.android.bumble;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bumble.network.PromptService;
import com.example.android.bumble.network.pojo.ApiUtils;
import com.example.android.bumble.network.pojo.PromptResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromptFragment extends Fragment  {

    private String promptType;
    private TextView title;
    private PromptService mService;
    private ImageView loadingImage;
    private TextView promptText;
    private SharedPreferences.Editor editor;
    public static final String USER_SETTINGS = "UserSettings";
    public static final String USER_SETTING_ADJECTIVE1 = "Adjective1";
    public static final String USER_SETTING_ADJECTIVE2 = "Adjective2";
    public static final String USER_SETTING_ADVERB = "Adverb";
    public static final String USER_SETTING_LOCATION = "Location";
    public static final String USER_SETTING_LOCATION_ADJECTIVE = "LocationAdjective";
    public static final String SCENE = "Scene";
    private Switch adjective1Switch;
    private Switch adjective2Switch;
    private Switch adverbSwitch;
    private Switch locationSwitch;
    private Switch locationAdjectiveSwitch;
    private Boolean adjective1SwitchState;
    private Boolean adjective2SwitchState;
    private Boolean adverbSwitchState;
    private Boolean locationSwitchState;
    private Boolean locationAdjectiveSwitchState;

    public PromptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prompt, container, false);

        SharedPreferences preferences = this.getActivity().getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        mService = ApiUtils.getPromptService();

        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);
        navigation.getMenu().setGroupCheckable(0,true, true);

        promptType = getArguments().getString("promptType");
        title = view.findViewById(R.id.title);
        loadingImage = view.findViewById(R.id.loadingImage);
        promptText = view.findViewById(R.id.promptText);
        adjective1Switch = view.findViewById(R.id.adjective1Switch);
        adjective2Switch = view.findViewById(R.id.adjective2Switch);
        adverbSwitch = view.findViewById(R.id.adverbSwitch);
        locationSwitch = view.findViewById(R.id.locationSwitch);
        locationAdjectiveSwitch = view.findViewById(R.id.locationAdjectiveSwitch);

        title.setText(promptType);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loader)
                .into(loadingImage);

        /********
         *  Set visibility of switches based on prompt type
         */

        if(promptType.equals(SCENE)) {
            adverbSwitch.setVisibility(View.VISIBLE);
            locationSwitch.setVisibility(View.VISIBLE);
            locationAdjectiveSwitch.setVisibility(View.VISIBLE);
        }

        else {
            adverbSwitch.setVisibility(View.INVISIBLE);
            locationSwitch.setVisibility(View.INVISIBLE);
            locationAdjectiveSwitch.setVisibility(View.INVISIBLE);
        }

        /********
         Set defaults from shared preferences
         */
        adjective1SwitchState = preferences.getBoolean(promptType + USER_SETTING_ADJECTIVE1, true);
        adjective1Switch.setChecked(adjective1SwitchState);

        adjective2SwitchState = preferences.getBoolean(promptType + USER_SETTING_ADJECTIVE2, true);
        adjective2Switch.setChecked(adjective2SwitchState);

        adverbSwitchState = preferences.getBoolean(promptType + USER_SETTING_ADVERB, true);
        adverbSwitch.setChecked(adverbSwitchState);

        locationSwitchState = preferences.getBoolean(promptType + USER_SETTING_LOCATION, true);
        locationSwitch.setChecked(locationSwitchState);

        locationAdjectiveSwitchState = preferences.getBoolean(promptType + USER_SETTING_LOCATION_ADJECTIVE, true);
        locationAdjectiveSwitch.setChecked(locationAdjectiveSwitchState);

        editor = preferences.edit();

        /********
         * Switch Listeners
         */
        adjective1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    editor.putBoolean(promptType + USER_SETTING_ADJECTIVE1, true);
                    adjective1SwitchState = true;
                }
                else {
                    editor.putBoolean(promptType + USER_SETTING_ADJECTIVE1, false);
                    adjective1SwitchState = false;
                }
                editor.commit();
            }
        });

        adjective2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    editor.putBoolean(promptType + USER_SETTING_ADJECTIVE2, true);
                    adjective2SwitchState = true;
                }
                else {
                    editor.putBoolean(promptType + USER_SETTING_ADJECTIVE2, false);
                    adjective2SwitchState = false;
                }
                editor.commit();
            }
        });

        adverbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    editor.putBoolean(promptType + USER_SETTING_ADVERB, true);
                    adverbSwitchState = true;
                }
                else {
                    editor.putBoolean(promptType + USER_SETTING_ADVERB, false);
                    adverbSwitchState = false;
                }
                editor.commit();
            }
        });

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    editor.putBoolean(promptType + USER_SETTING_LOCATION, true);
                    locationSwitchState = true;
                }
                else {
                    editor.putBoolean(promptType + USER_SETTING_LOCATION, false);
                    locationSwitchState = false;
                }
                editor.commit();
            }
        });

        locationAdjectiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    editor.putBoolean(promptType + USER_SETTING_LOCATION_ADJECTIVE, true);
                    locationAdjectiveSwitchState = true;
                }
                else {
                    editor.putBoolean(promptType + USER_SETTING_LOCATION_ADJECTIVE, false);
                    locationAdjectiveSwitchState = false;
                }
                editor.commit();
            }
        });

        getPrompt();

        return view;
    }

    public void getPrompt() {
        promptText.setText("");
        loadingImage.setVisibility(View.VISIBLE);
        /* Convert boolean to int for API */
        int includeAdjective1 = adjective1SwitchState ? 1 : 0;
        int includeAdjective2 = adjective2SwitchState ? 1 : 0;
        int includeAdverb = adverbSwitchState ? 1 : 0;
        int includePlace = locationSwitchState ? 1 : 0;
        int includePlaceAdjective = locationAdjectiveSwitchState ? 1 : 0;

        /* make asynchronous call to API */

        mService.getPrompt(promptType, includeAdjective1, includeAdjective2, includeAdverb, includePlace, includePlaceAdjective)
                .enqueue(new Callback<PromptResponse>() {

                    @Override

                    public void onResponse(Call<PromptResponse> call, Response<PromptResponse> response) {

                        if(response.isSuccessful()) {
                            loadingImage.setVisibility(View.INVISIBLE);
                            promptText.setText(response.body().getPrompt());
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

}
