//thanks to https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792 as a starting point for the code below
package com.example.android.bumble.network.pojo;

import com.example.android.bumble.network.PromptService;
import com.example.android.bumble.network.RetrofitClient;

public class ApiUtils {

    public static final String BASE_URL_PROMPT = "https://hgk660y4de.execute-api.us-east-1.amazonaws.com/Production/";
    public static final String BASE_URL_SUGGEST = "https://wyj2cjuid6.execute-api.us-east-1.amazonaws.com/Production/";

    public static PromptService getPromptService() {
        return RetrofitClient.getClient(BASE_URL_PROMPT).create(PromptService.class);
    }
}
