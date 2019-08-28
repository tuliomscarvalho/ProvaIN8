package com.example.appin8.negocio;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.appin8.R;

public class Alerta {

    // Mostra um alerta para o usuario e após o usuário clicar em "OK" ele é redirecionado para a tela anterior
    public static void retornaErroConversao(final Context context) {
        if(!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            builder.setTitle("Atenção")
                    .setMessage("Por favor, insira um valor válido!")
                    .setCancelable(false)
                    .setIcon(R.drawable.logo_coin)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    // Mostra um alerta para o usuario e após o usuário clicar em "OK" ele é redirecionado para a tela anterior
    public static void historicoVazio(final Context context) {
        if(!((Activity) context).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
            builder.setTitle("Histórico")
                    .setMessage("Nenhum histórico encontrado!")
                    .setCancelable(false)
                    .setIcon(R.drawable.logo_coin)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ((Activity) context).finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
