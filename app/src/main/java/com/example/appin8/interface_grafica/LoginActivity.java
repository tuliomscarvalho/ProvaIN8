package com.example.appin8.interface_grafica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appin8.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    // Inicia a tela de menu
    public void iniciaTelaMenu(View view){
        Intent tela = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(tela);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
