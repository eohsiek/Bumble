package com.example.android.bumble;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bumble.database.Favorite;
import com.example.android.bumble.database.FavoriteViewModel;
import android.arch.lifecycle.ViewModelProviders;
import com.example.android.bumble.network.PromptService;
import com.example.android.bumble.network.pojo.ApiUtils;
import com.example.android.bumble.network.pojo.PromptResponse;
import com.google.firebase.analytics.FirebaseAnalytics;

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
    private ImageView topImage;
    private TextView promptText;
    private FloatingActionButton fab;
    private SharedPreferences.Editor editor;
    public static final String USER_SETTINGS = "UserSettings";
    public static final String USER_SETTING_ADJECTIVE1 = "Adjective1";
    public static final String USER_SETTING_ADJECTIVE2 = "Adjective2";
    public static final String USER_SETTING_ADVERB = "Adverb";
    public static final String USER_SETTING_LOCATION = "Location";
    public static final String USER_SETTING_LOCATION_ADJECTIVE = "LocationAdjective";
    public static final String USER_SETTING_LAST_PROMPT = "LastPrompt";
    public static final String SCENE = "Scene";
    public static final String CHARACTER = "Character";
    public static final String PLACE = "Place";
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
    private FirebaseAnalytics mFirebaseAnalytics;

    private SharedPreferences preferences;

    public PromptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("adjectiveTest", "oncreate");

        View view = inflater.inflate(R.layout.fragment_prompt, container, false);

        preferences = this.getActivity().getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        mService = ApiUtils.getPromptService();

        promptType = getArguments().getString("promptType");
        title = view.findViewById(R.id.title);
        loadingImage = view.findViewById(R.id.loadingImage);
        topImage = view.findViewById(R.id.topImage);
        promptText = view.findViewById(R.id.promptText);
        adjective1Switch = view.findViewById(R.id.adjective1Switch);
        adjective2Switch = view.findViewById(R.id.adjective2Switch);
        adverbSwitch = view.findViewById(R.id.adverbSwitch);
        locationSwitch = view.findViewById(R.id.locationSwitch);
        locationAdjectiveSwitch = view.findViewById(R.id.locationAdjectiveSwitch);

        int currentOrientation = getResources().getConfiguration().orientation;


        fab = view.findViewById(R.id.floatingActionButton);

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
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                topImage.setImageResource(R.drawable.bumblescenetall);
            }
            else {
                topImage.setImageResource(R.drawable.bumblescenetall);
            }
        }

        else {
            adverbSwitch.setVisibility(View.INVISIBLE);
            locationSwitch.setVisibility(View.INVISIBLE);
            locationAdjectiveSwitch.setVisibility(View.INVISIBLE);

            if(promptType.equals(CHARACTER)) {
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    topImage.setImageResource(R.drawable.bumblecharactertall);
                }
                else {
                    topImage.setImageResource(R.drawable.bumblecharacterwide);
                }
            }
            else if (promptType.equals(PLACE)) {
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    topImage.setImageResource(R.drawable.bumbleplacetall);
                }
                else {
                    topImage.setImageResource(R.drawable.bumbleplacewide);
                }
            }
            else {
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    topImage.setImageResource(R.drawable.bumbleobjecttall);
                }
                else {
                    topImage.setImageResource(R.drawable.bumbleobjectwide);
                }
            }
        }

        setSwitches();
        editor = preferences.edit();

        /********
         * Switch Listeners
         */
        adjective1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("adjective1SwitchAction", String.valueOf(isChecked));
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
        if (savedInstanceState != null) {
            String savedPrompt = savedInstanceState.getString(USER_SETTING_LAST_PROMPT);
            loadingImage.setVisibility(View.INVISIBLE);
            promptText.setText(savedPrompt);
        }
        else {
            getPrompt();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void onResume(){
        super.onResume();
        setSwitches();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(USER_SETTING_LAST_PROMPT, promptText.getText().toString());

        //Save the fragment's state here
    }


    public void getPrompt() {

        // log prompt to Analytics
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, promptType);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "newPrompt");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        promptText.setText("");
        loadingImage.setVisibility(View.VISIBLE);

        fab.setImageResource(R.drawable.ic_favorite_black_24dp);
        fab.setEnabled(true);

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
                            String newprompt = response.body().getPrompt();
                            promptText.setText(newprompt);
                            editor = preferences.edit();
                            editor.putString(USER_SETTING_LAST_PROMPT, newprompt).commit();
                            //update widget
                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), PromptWidget.class));
                            PromptWidget widget = new PromptWidget();
                            widget.onUpdate(getActivity(), AppWidgetManager.getInstance(getActivity()),ids);
                        }else {
                            int statusCode  = response.code();
                            // handle request errors depending on status code
                            Log.d("DisplayPromptError", String.valueOf(statusCode));
                        }
                    }

                    @Override
                    public void onFailure(Call<PromptResponse> call, Throwable t) {
                        //showErrorMessage();
                        Log.d("DisplayPromptActivity", "error loading prompt from API");

                    }
                });
    }

    public void getNewPrompt() {
        Log.i("getnew", "yes");
    }

    public void processFavorite() {
        // log prompt to Analytics
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "addFavorite");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        final Favorite favorite = new Favorite();
        promptText = getActivity().findViewById(R.id.promptText);
        favorite.favoritePrompt = (String) promptText.getText();

        FavoriteViewModel favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
        favoriteViewModel.insert(favorite);

        FloatingActionButton fab = getActivity().findViewById(R.id.floatingActionButton);
        fab.setImageResource(R.drawable.ic_notifications_black_24dp);
        fab.setEnabled(false);
    }

    public void setSwitches() {

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
    }


}
