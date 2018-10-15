package com.example.android.bumble;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bumble.network.PromptService;
import com.example.android.bumble.network.pojo.ApiUtils;
import com.example.android.bumble.network.pojo.PromptResponse;
import com.example.android.bumble.network.pojo.SuggestionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestWordFragment extends Fragment {

    private PromptService mService;

    public SuggestWordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggest_word, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.wordtype_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.wordtype_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        return view;
    }

    public void sendWord() {
        /*get values from form */
        Spinner wordtypeSpinner = getActivity().findViewById(R.id.wordtype_spinner);
        String wordtype = wordtypeSpinner.getSelectedItem().toString();
        String tableName = wordtype.toLowerCase() + 's';

        final TextInputEditText textEditfield = getActivity().findViewById(R.id.suggestedWord);
        String suggestedWord = String.valueOf(textEditfield.getText());

       /* make asynchronous call to API */
        mService = ApiUtils.getSuggestService();
        mService.sendWord(tableName, suggestedWord)
                .enqueue(new Callback<SuggestionResponse>() {
                    @Override
                    public void onResponse(Call<SuggestionResponse> call, Response<SuggestionResponse> response) {

                        if(response.isSuccessful()) {
                            String responseType =  response.body().getResponseType();
                            if(responseType.equals("Success")) {
                                textEditfield.setText("");
                            }
                            Toast.makeText(getActivity(), response.body().getResponseMessage(), Toast.LENGTH_LONG).show();

                        }else {
                            int statusCode  = response.code();
                            // handle request errors depending on status code
                            Toast.makeText(getActivity(), "Error Submitting Word.  Please try again.", Toast.LENGTH_LONG).show();
                            Log.d("DisplayPromptActivity", String.valueOf(statusCode));
                        }
                    }

                    @Override
                    public void onFailure(Call<SuggestionResponse> call, Throwable t) {
                        //showErrorMessage();
                        Toast.makeText(getActivity(), "Error Submitting Word.  Please try again.", Toast.LENGTH_LONG).show();
                        Log.d("DisplayPromptActivity", "error loading prompt from API");

                    }
                });
    }

    public void sendWord(View view) {

    }
}
