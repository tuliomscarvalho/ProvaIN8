package com.example.appin8.interface_grafica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.appin8.R;
import com.example.appin8.negocio.Alerta;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HistoricoActivity extends AppCompatActivity {

    ListView lv_historico;
    ImageView bt_voltar_menu;
    JSONArray jsonArrayHistorico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        lv_historico = findViewById(R.id.lv_historico);
        bt_voltar_menu = findViewById(R.id.bt_voltar_menu);
        bt_voltar_menu.setVisibility(View.VISIBLE);

        String resposta = readFromFile(getApplicationContext());

        jsonArrayHistorico = new JSONArray();

        //Cria o parse de tratamento
        JSONParser parser = new JSONParser();

        try {
            jsonArrayHistorico = (JSONArray) parser.parse(resposta);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.v("JSON Leitura", jsonArrayHistorico.toJSONString());
        if(jsonArrayHistorico.toJSONString().equals("[]")){
            Alerta.historicoVazio(this);
        }

        lv_historico.setAdapter(new AdapterHistorico( getApplicationContext(), jsonArrayHistorico));

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

    // Volta para a MenuActivity
    public void voltaMenu(View view){
        bt_voltar_menu.setVisibility(View.GONE);
        finish();
    }
}
