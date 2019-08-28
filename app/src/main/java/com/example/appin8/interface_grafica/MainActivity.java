package com.example.appin8.interface_grafica;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appin8.conexao.JsonPlaceHolder;
import com.example.appin8.R;
import com.example.appin8.negocio.Alerta;
import com.example.appin8.negocio.CoinRates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    CoinRates coinRates = new CoinRates();
    private TextView nomeMoeda1, nomeMoeda2, valormoeda1, valorMoeda2, resultado;
    public EditText valorDesejado;
    private Button buttonconverte;
    private Spinner baseDesejada,moedaDesejada;
    private static final String BASE_URL = "https://api.exchangeratesapi.io/";
    private Call<CoinRates> coinRatesCall;
    private JsonPlaceHolder jsonPlaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nomeMoeda1 = findViewById(R.id.tv_nome_moeda1);
        nomeMoeda2 = findViewById(R.id.tv_nome_moeda2);
        valormoeda1 = findViewById(R.id.tv_valor_moeda1);
        valorMoeda2 = findViewById(R.id.tv_valor_moeda2);
        baseDesejada = findViewById(R.id.sp_seleciona_base);
        valorDesejado = findViewById(R.id.et_valor_desejado);
        moedaDesejada = findViewById(R.id.sp_seleciona_moeda);
        resultado = findViewById(R.id.tv_resultado);
        buttonconverte = findViewById(R.id.bt_converte);

        setValuesRate();
    }


    public void setValuesRate(){

        String[] moedas = {"BRL","USD","EUR"};
        ArrayAdapter<String> spinnerBaseArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_style,moedas);
        spinnerBaseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baseDesejada.setAdapter(spinnerBaseArrayAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolder = retrofit.create(JsonPlaceHolder.class);


        String[] moedasSelecionaveis;
        moedasSelecionaveis = new String[]{"USD", "EUR"};
        coinRatesCall = jsonPlaceHolder.getRatesBaseBRL();

        baseDesejada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valorDesejado.setText("");
                resultado.setText("");
                String[] moedasSelecionaveis = new String[0];
                switch (position) {
                    case 0:
                        moedasSelecionaveis = new String[]{"USD", "EUR"};
                        coinRatesCall = jsonPlaceHolder.getRatesBaseBRL();
                        break;
                    case 1:
                        moedasSelecionaveis = new String[]{"BRL", "EUR"};
                        coinRatesCall = jsonPlaceHolder.getRatesBaseUSD();
                        break;
                    case 2:
                        moedasSelecionaveis = new String[]{"USD", "BRL"};
                        coinRatesCall = jsonPlaceHolder.getRatesBaseEUR();
                        break;
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_style,moedasSelecionaveis);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                moedaDesejada.setAdapter(spinnerArrayAdapter);

                coinRatesCall.enqueue(new Callback<CoinRates>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<CoinRates> call, Response<CoinRates> response) {
                        if(!response.isSuccessful()){
                            Log.v("Code1: ", String.valueOf(response.code()));
                            return;
                        }
                        coinRates = response.body();
                        writeToAppFile(coinRates, getApplicationContext());

                        preencheCamposMenu();

                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onFailure(Call<CoinRates> call, Throwable t) {
                        Log.v("Code2: ", "Error: " + t.getMessage());
                        baseDesejada.setEnabled(false);
                        baseDesejada.setClickable(false);
                        readFromAppFile(getApplicationContext());

                        if(coinRates.getBase() != null){

                            preencheCamposMenu();

                        }else{
                            valormoeda1.setText(" - ");
                            valorMoeda2.setText(" - ");
                            valorDesejado.setClickable(false);
                            valorDesejado.setEnabled(false);
                            buttonconverte.setClickable(false);
                            buttonconverte.setEnabled(false);
                        }

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_style,moedasSelecionaveis);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moedaDesejada.setAdapter(spinnerArrayAdapter);

        coinRatesCall.enqueue(new Callback<CoinRates>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<CoinRates> call, Response<CoinRates> response) {
                if(!response.isSuccessful()){
                    Log.v("Code3: ", String.valueOf(response.code()));
                    return;
                }
                coinRates = response.body();
                writeToAppFile(coinRates, getApplicationContext());

                nomeMoeda1.setText("USD");
                valormoeda1.setText("R$ " + String.format("%.4f",1/coinRates.getCoins().getUSD()));
                nomeMoeda2.setText("EUR");
                valorMoeda2.setText("R$ " + String.format("%.4f",1/coinRates.getCoins().getEUR()));

            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<CoinRates> call, Throwable t) {
                Log.v("Code4: ", "Error: " + t.getMessage());
                baseDesejada.setEnabled(false);
                baseDesejada.setClickable(false);
                readFromAppFile(getApplicationContext());

                if(coinRates.getBase() != null){

                    preencheCamposMenu();

                }else{
                    valormoeda1.setText(" - ");
                    valorMoeda2.setText(" - ");
                    valorDesejado.setClickable(false);
                    valorDesejado.setEnabled(false);
                    buttonconverte.setClickable(false);
                    buttonconverte.setEnabled(false);
                }

            }
        });
    }

    public void preencheCamposMenu(){
        if(baseDesejada.getSelectedItem().equals("BRL")){
            verificaBusca();
            nomeMoeda1.setText("USD");
            valormoeda1.setText("R$ " + String.format("%.4f",1/coinRates.getCoins().getUSD()));
            nomeMoeda2.setText("EUR");
            valorMoeda2.setText("R$ " + String.format("%.4f",1/coinRates.getCoins().getEUR()));
        }else if(baseDesejada.getSelectedItem().equals("USD")){
            verificaBusca();
            nomeMoeda1.setText("BRL");
            valormoeda1.setText("$ "+ String.format("%.4f",1/coinRates.getCoins().getBRL()));
            nomeMoeda2.setText("EUR");
            valorMoeda2.setText("$ " + String.format("%.4f",1/coinRates.getCoins().getEUR()));
        }else {
            verificaBusca();
            nomeMoeda1.setText("BRL");
            valormoeda1.setText("€ "+ String.format("%.4f",1/coinRates.getCoins().getBRL()));
            nomeMoeda2.setText("USD");
            valorMoeda2.setText("€ " + String.format("%.4f",1/coinRates.getCoins().getUSD()));
        }
    }

    public void verificaBusca(){
        if(!baseDesejada.getSelectedItem().toString().equals("USD") && coinRates.getCoins().getUSD() == 1.0 ||
                !baseDesejada.getSelectedItem().toString().equals("EUR") && coinRates.getCoins().getEUR() == 1.0 ||
                !baseDesejada.getSelectedItem().toString().equals("BRL") && coinRates.getCoins().getBRL() == 1.0){
            valormoeda1.setText("-");
            valorMoeda2.setText("-");
            Alerta.alertaSemInternet(this);
        }
    }

    // Converte moeda
    public void coverteMoeda(View view){

        String simboloMoeda = null;
        String base = baseDesejada.getSelectedItem().toString();
        String moeda = moedaDesejada.getSelectedItem().toString();
        if(valorDesejado.getText().toString().equals(".") || valorDesejado.getText().toString().isEmpty()){
            valorDesejado.setText("");
            valormoeda1.setText("-");
            valorMoeda2.setText("-");
            Alerta.retornaErroConversao(this);
            return;
        }
        Double valor = Double.valueOf(valorDesejado.getText().toString());
        Double resposta = null;
        if(base.equals("BRL")){
            if(moeda.equals("USD")){
                resposta = (coinRates.getCoins().getUSD())*valor;
                simboloMoeda = "$ ";
            }else{
                resposta = (coinRates.getCoins().getEUR())*valor;
                simboloMoeda = "€ ";
            }
        } else if(base.equals("USD")){
            if (moeda.equals("BRL")){
                resposta = (coinRates.getCoins().getBRL())*valor;
                simboloMoeda = "R$ ";
            }else{
                resposta = (coinRates.getCoins().getEUR())*valor;
                simboloMoeda = "€ ";
            }
        }else{
            if (moeda.equals("BRL")){
                resposta = (coinRates.getCoins().getBRL())*valor;
                simboloMoeda = "R$ ";
            }else{
                resposta = (coinRates.getCoins().getUSD())*valor;
                simboloMoeda = "$ ";
            }
        }
        resultado.setText(simboloMoeda + String.format("%.4f",resposta));

        if(valor.equals(resposta)){

            valorDesejado.setText("");
            valorDesejado.setEnabled(false);
            valorDesejado.setClickable(false);

            resultado.setText("");
            resultado.setEnabled(false);
            resultado.setClickable(false);
        }else{
            salvaHistorico(base, moeda, valor, resposta);
        }

    }

    // Inicia Historico Activity
    public void iniciaHistorico(View view){
        Intent intent = new Intent(getApplicationContext(), HistoricoActivity.class );
        startActivity(intent);
    }

    public void salvaHistorico(String base, String moeda, Double valor, Double resultado){

        Date data = new Date();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtual = formatador.format(data);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        //Armazena dados em um Objeto JSON
        jsonObject.put("dataHistorico",dataAtual);
        jsonObject.put("moedaBase",base);
        jsonObject.put("valorBase",valor);
        jsonObject.put("moedaDestino",moeda);
        jsonObject.put("valorDestino",resultado);
        jsonArray.add(jsonObject);

        writeToFile(jsonArray.toJSONString(), jsonObject.toJSONString(), getApplicationContext());

    }

    private void writeToFile(String data,String dataObj, Context context) {

        String historico = readFromFile(getApplicationContext());
        if(historico.isEmpty()){
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("historico.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }else {
            JSONArray jsonArray;
            JSONObject jsonObject;
            JSONParser jsonParser = new JSONParser();

            try {
                jsonArray = (JSONArray) jsonParser.parse(historico);
                jsonObject = (JSONObject) jsonParser.parse(dataObj);
                jsonArray.add(jsonObject);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("historico.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(jsonArray.toString());
                outputStreamWriter.close();

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("historico.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void writeToAppFile(CoinRates data, Context context) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String jsonString = gson.toJson(data);
        Log.v("testeTulio", jsonString);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("appdata.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(jsonString);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void readFromAppFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("appdata.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        coinRates = gson.fromJson(ret, CoinRates.class);

    }

    boolean podeSair = false;
    @Override
    public void onBackPressed() {

        if (podeSair) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.texto_pressione_novamente, Toast.LENGTH_SHORT).show();
            podeSair = true;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                podeSair = false;
            }
        }, 2000);
    }

}