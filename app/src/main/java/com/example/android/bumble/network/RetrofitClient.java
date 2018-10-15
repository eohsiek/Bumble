//thanks to https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792 tutorial for the code below
package com.example.android.bumble.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        return retrofit;
    }
}
