package com.example.appin8.conexao;

import com.example.appin8.negocio.CoinRates;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolder {

    @GET("latest?base=EUR")
    Call<CoinRates> getRatesBaseEUR();

    @GET("latest?base=USD")
    Call<CoinRates> getRatesBaseUSD();


    @GET("latest?base=BRL")
    Call<CoinRates> getRatesBaseBRL();

}
