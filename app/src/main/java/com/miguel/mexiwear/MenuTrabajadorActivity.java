package com.miguel.mexiwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.miguel.mexiwear.databinding.ActivityMenuTrabajadorBinding;

public class MenuTrabajadorActivity extends Activity {

    private ActivityMenuTrabajadorBinding binding;
    String user;
    String Direccion;
    Integer idUsr;
    TextView userT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuTrabajadorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userT = (TextView) findViewById(R.id.userT);

        Bundle parametros = getIntent().getExtras();
        String dato = parametros.getString("usuario");
        Integer idUser = parametros.getInt("idUser");
        String direcc = parametros.getString("direccion");

        user = dato;
        idUsr = idUser;
        Direccion = direcc;
        userT.setText(user);
    }

    public void listaSolicitudes(View view) {
        Intent vistaSolicitudes = new Intent(getApplicationContext(), ListaSolicitudesActivity.class);
        vistaSolicitudes.putExtra("usuario", user);
        startActivity(vistaSolicitudes);
    }

    public void listaTrabajosCerca(View view) {
        Intent vistaTrabajosNear = new Intent(getApplicationContext(), ListaVacantesCercaActivity.class);
        vistaTrabajosNear.putExtra("usuario", user);
        vistaTrabajosNear.putExtra("idUser", idUsr);
        vistaTrabajosNear.putExtra("Direccion", Direccion);
        startActivity(vistaTrabajosNear);
    }
}