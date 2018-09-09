package com.example.android.bumble;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromptFragment extends Fragment {

    private String promptType;
    private TextView title;

    public PromptFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prompt, container, false);

        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);
        navigation.getMenu().setGroupCheckable(0,true, true);

        promptType = getArguments().getString("promptType");
        title = view.findViewById(R.id.title);

        title.setText(promptType);

        return view;
    }

}
