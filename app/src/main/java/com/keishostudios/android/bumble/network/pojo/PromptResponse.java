package com.keishostudios.android.bumble.network.pojo;

public class PromptResponse
{
    private String prompt;

    public String getPrompt ()
    {
        return prompt;
    }

    public void setPrompt (String prompt)
    {
        this.prompt = prompt;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [prompt = "+prompt+"]";
    }
}

