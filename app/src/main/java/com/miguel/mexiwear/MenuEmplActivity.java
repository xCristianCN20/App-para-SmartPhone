package com.miguel.mexiwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.miguel.mexiwear.databinding.ActivityMenuEmplBinding;

public class MenuEmplActivity extends Activity {

    private ActivityMenuEmplBinding binding;
    String user;
    TextView txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuEmplBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txtUser = (TextView) findViewById(R.id.user);

        Bundle parametros = getIntent().getExtras();
        String dato = parametros.getString("usuario");
        user = dato;
        txtUser.setText(user);
    }

    public void listaPostulaciones(View view) {
        Intent vistaPostulaciones = new Intent(getApplicationContext(), ListaPostulacionesActivity.class);
        vistaPostulaciones.putExtra("usuario", user);
        startActivity(vistaPostulaciones);
    }

    public void listaMisVacantes(View view) {
        Intent vistaMisVacantes = new Intent(getApplicationContext(), ListaMisVacantesActivity.class);
        vistaMisVacantes.putExtra("usuario", user);
        startActivity(vistaMisVacantes);
    }
}