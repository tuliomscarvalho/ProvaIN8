package com.example.appin8.interface_grafica;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appin8.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AdapterHistorico extends BaseAdapter {

    private Context context;
    private JSONArray historico;
    private LayoutInflater inflater;
    private int selecionado = -1;

    AdapterHistorico(Context context, JSONArray historico) {
        this.context = context;
        this.historico = historico;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return historico.size();
    }

    @Override
    public Object getItem(int i) {
        return historico.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View itemPlano;
        if(view == null) {
            itemPlano = inflater.inflate(R.layout.layout_item_historico, null);
        }
        else {
            itemPlano = view;

            if(selecionado != -1) {
                LinearLayout bt = itemPlano.findViewById(R.id.ll_item_historico);
                if (i == selecionado) {
                    bt.setBackgroundResource(R.drawable.ic_border_login);
                } else {
                    bt.setBackgroundResource(R.drawable.ic_border_edit_text);
                }
            }
        }

        JSONObject jsonaux = (JSONObject) historico.get(i);

        ((TextView) itemPlano.findViewById(R.id.tv_data_historico)).setText(String.valueOf(jsonaux.get("dataHistorico")));
        ((TextView) itemPlano.findViewById(R.id.tv_moeda_base)).setText(String.valueOf(jsonaux.get("moedaBase")));

        String valorBase = "";
        if(String.valueOf(jsonaux.get("moedaBase")).equals("BRL")){
            valorBase = "R$ "+ String.format("%.2f",jsonaux.get("valorBase"));
        }else if(String.valueOf(jsonaux.get("moedaBase")).equals("USD")){
            valorBase = "$ "+ String.format("%.2f",jsonaux.get("valorBase"));
        }else{
            valorBase = "€ "+ String.format("%.2f",jsonaux.get("valorBase"));
        }
        ((TextView) itemPlano.findViewById(R.id.tv_valor_base)).setText(valorBase);


        ((TextView) itemPlano.findViewById(R.id.tv_moeda_destino)).setText(String.valueOf(jsonaux.get("moedaDestino")));

        String valorDestino = "";
        if(String.valueOf(jsonaux.get("moedaDestino")).equals("BRL")){
            valorDestino = "R$ "+ String.format("%.2f",jsonaux.get("valorDestino"));
        }else if(String.valueOf(jsonaux.get("moedaDestino")).equals("USD")){
            valorDestino = "$ "+ String.format("%.2f",jsonaux.get("valorDestino"));
        }else{
            valorDestino = "€ "+ String.format("%.2f",jsonaux.get("valorDestino"));
        }
        ((TextView) itemPlano.findViewById(R.id.tv_valor_destino)).setText(valorDestino);

        (itemPlano.findViewById(R.id.tv_data_historico)).setTag(i);
        (itemPlano.findViewById(R.id.tv_moeda_base)).setTag(i);
        (itemPlano.findViewById(R.id.tv_valor_base)).setTag(i);
        (itemPlano.findViewById(R.id.tv_moeda_destino)).setTag(i);
        (itemPlano.findViewById(R.id.tv_valor_destino)).setTag(i);

        return itemPlano;
    }

}
