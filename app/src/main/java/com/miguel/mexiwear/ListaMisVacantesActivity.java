package com.miguel.mexiwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguel.mexiwear.Modelo.clsMiVacante;
import com.miguel.mexiwear.Modelo.clsModelo_ListarMisVacantes;
import com.miguel.mexiwear.databinding.ActivityListaMisVacantesBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaMisVacantesActivity extends Activity {

    private ActivityListaMisVacantesBinding binding;
    String user;
    ArrayList<clsMiVacante> misVacantes;
    RecyclerView rvmisvacantes;
    String URL = "https://appmiguel.proyectoarp.com/MexiTrabajo/getMisVacantes.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaMisVacantesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        misVacantes = new ArrayList<clsMiVacante>();
        rvmisvacantes = (RecyclerView) findViewById(R.id.rvmisvacantes);
        rvmisvacantes.setLayoutManager(new GridLayoutManager(this, 1));

        Bundle parametros = getIntent().getExtras();
        String dato = parametros.getString("usuario");
        user = dato;

        mostrarLista();
    }

    public void mostrarLista ()
    {//Inicio Metodo
        StringRequest respuesta = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("null")){
                    try{
                        JSONArray datos=new JSONArray(response);
                        misVacantes.clear();
                        for(int x=0; x<datos.length();x++){
                            JSONObject valores = datos.getJSONObject(x);
                            String Nombre = valores.getString("varPuesto");
                            String Turno = valores.getString("intTurno");
                            Integer idVacante = valores.getInt("intIdOfertaLaboral");
                            misVacantes.add(new clsMiVacante(Nombre,Turno,idVacante));
                        }
                        clsModelo_ListarMisVacantes Modelo_ListarMisVac = new clsModelo_ListarMisVacantes(misVacantes);
                        rvmisvacantes.setAdapter(Modelo_ListarMisVac);
                    }catch(Exception e){
                        //Error Extracción de los datos, posiblemente contraseña incorrecta
                        Toast.makeText(ListaMisVacantesActivity.this, "Datos de Usuario fallidos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Error Usuario no encontrado posiblemente nunca entre aquí
                    Toast.makeText(ListaMisVacantesActivity.this, "Oops No tienes vacantes activas", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Fallo en la solicitud
                Toast.makeText(ListaMisVacantesActivity.this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario",user);
                return parametros;
            }
        };
        RequestQueue envio = Volley.newRequestQueue(getApplicationContext());
        envio.add(respuesta);
    }//Fin Metodo

    public void verDesactivadas(View view) {
        Intent vistaMisVacOff = new Intent(getApplicationContext(), ListaVacOffActivity.class);
        vistaMisVacOff.putExtra("usuario", user);
        startActivity(vistaMisVacOff);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }
}