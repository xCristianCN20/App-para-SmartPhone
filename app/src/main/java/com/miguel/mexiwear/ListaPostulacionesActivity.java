package com.miguel.mexiwear;

import android.app.Activity;
import android.os.Bundle;
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
import com.miguel.mexiwear.Modelo.clsModelo_Listar;
import com.miguel.mexiwear.Modelo.clsPostulacion;
import com.miguel.mexiwear.databinding.ActivityListaPostulacionesBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaPostulacionesActivity extends Activity {

    private ActivityListaPostulacionesBinding binding;
    String user;
    ArrayList<clsPostulacion> postulaciones;
    RecyclerView rvpostulaciones;
    String URL = "https://appmiguel.proyectoarp.com/MexiTrabajo/getPostulaciones.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListaPostulacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postulaciones = new ArrayList<clsPostulacion>();
        rvpostulaciones = (RecyclerView) findViewById(R.id.rvpostulaciones);
        rvpostulaciones.setLayoutManager(new GridLayoutManager(this,1));

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
                        for(int x=0; x<datos.length();x++){
                            JSONObject valores = datos.getJSONObject(x);
                            String Nombre = valores.getString("varNombre");
                            String Direccion = valores.getString("varMunicipio");
                            Integer idPostulacion = valores.getInt("intIdPostulacion");
                            postulaciones.add(new clsPostulacion(Nombre,Direccion,idPostulacion));
                        }
                        clsModelo_Listar Modelo_Listar =  new clsModelo_Listar(postulaciones);
                        rvpostulaciones.setAdapter(Modelo_Listar);
                    }catch(Exception e){
                        //Error Extracción de los datos, posiblemente contraseña incorrecta
                        Toast.makeText(ListaPostulacionesActivity.this, "Datos de Usuario fallidos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Error Usuario no encontrado posiblemente nunca entre aquí
                    Toast.makeText(ListaPostulacionesActivity.this, "Oops no tienes postulaciones", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Fallo en la solicitud
                Toast.makeText(ListaPostulacionesActivity.this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
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

}