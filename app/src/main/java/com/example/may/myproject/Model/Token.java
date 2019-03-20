package com.example.may.myproject.Model;

public class Token {

    private String token;
    private boolean isServerToken;

    public Token() {
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public Token(String token, boolean isServerToken) {
        this.token = token;
        this.isServerToken = isServerToken;

    }
}
