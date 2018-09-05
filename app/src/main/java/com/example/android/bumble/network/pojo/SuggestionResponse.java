package com.example.android.bumble.network.pojo;

public class SuggestionResponse
{
    private String ResponseType;

    private String ResponseMessage;

    public String getResponseType ()
    {
        return ResponseType;
    }

    public void setResponseType (String ResponseType)
    {
        this.ResponseType = ResponseType;
    }

    public String getResponseMessage ()
    {
        return ResponseMessage;
    }

    public void setResponseMessage (String ResponseMessage)
    {
        this.ResponseMessage = ResponseMessage;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ResponseType = "+ResponseType+", ResponseMessage = "+ResponseMessage+"]";
    }
}

