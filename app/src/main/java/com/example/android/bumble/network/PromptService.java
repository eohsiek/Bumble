//thanks to https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792 as a starting point for the code below
package com.example.android.bumble.network;

import android.os.Build;

import com.example.android.bumble.BuildConfig;
import com.example.android.bumble.network.pojo.PromptResponse;
import com.example.android.bumble.network.pojo.SuggestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;



public interface PromptService {

    @Headers("x-api-key: " + BuildConfig.apikey)

    @GET("?")
    Call<PromptResponse> getPrompt(
            @Query("promptType") String promptType,
            @Query("includeAdjective1") Integer includeAdjective1,
            @Query("includeAdjective2") Integer includeAdjective2,
            @Query("includeAdverb") Integer includeAdverb,
            @Query("includePlace") Integer includePlace,
            @Query("includePlaceAdjective") Integer includePlaceAdjective);

    @GET("?")
    Call<SuggestionResponse> sendWord(
            @Query("tableName") String tableName,
            @Query("suggestedWord") String suggestedWord);
}
