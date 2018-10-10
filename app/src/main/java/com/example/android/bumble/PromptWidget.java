package com.example.android.bumble;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class PromptWidget extends AppWidgetProvider {


    private SharedPreferences.Editor editor;
    public static final String USER_SETTINGS = "UserSettings";
    public static final String USER_SETTING_LAST_PROMPT = "LastPrompt";
    public static Context mContext;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        mContext = context;
        SharedPreferences preferences  = context.getSharedPreferences(USER_SETTINGS,Context.MODE_PRIVATE);
        String promptText = preferences.getString(USER_SETTING_LAST_PROMPT,null);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.prompt_widget);
        views.setTextViewText(R.id.appwidget_text, promptText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.prompt_widget);
            views.setOnClickPendingIntent(R.id.button3, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }




}

