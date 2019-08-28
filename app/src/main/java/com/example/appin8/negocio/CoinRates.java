package com.example.appin8.negocio;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class CoinRates {

    @SerializedName("rates")
    Coins coins;

    String base, date;

    public Coins getCoins() {
        return coins;
    }

    public void setCoins(Coins coins) {
        this.coins = coins;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
