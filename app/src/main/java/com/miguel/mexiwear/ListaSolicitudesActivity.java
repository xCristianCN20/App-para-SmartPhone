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
import com.miguel.mexiwear.Modelo.clsModelo_ListarSolicitud;
import com.miguel.mexiwear.Modelo.clsSolicitud;
import com.miguel.mexiwear.databinding.ActivityListaSolicitudesBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaSolicitudesActivity extends Activity {

    private ActivityListaSolicitudesBinding binding;
    String user;
    ArrayList<clsSolicitud> solicitudes;
    RecyclerView rvsolicitudes;
    String URL = "https://appmiguel.proyectoarp.com/MexiTrabajo/getSolicitudes.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaSolicitudesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        solicitudes = new ArrayList<clsSolicitud>();
        rvsolicitudes = (RecyclerView) findViewById(R.id.rvsolicitudes);
        rvsolicitudes.setLayoutManager(new GridLayoutManager(this, 1));

        Bundle parametros = getIntent().getExtras();
        String dato = parametros.getString("usuario");
        user = dato;

        mostrarLista();

    }
    public void mostrarLista(){
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
                            Integer idSolicitud = valores.getInt("intIdSolicitud");
                            solicitudes.add(new clsSolicitud(Nombre,Direccion,idSolicitud));
                        }
                        clsModelo_ListarSolicitud Modelo_ListarSolic =  new clsModelo_ListarSolicitud(solicitudes);
                        rvsolicitudes.setAdapter(Modelo_ListarSolic);
                    }catch(Exception e){
                        //Error Extracción de los datos, posiblemente contraseña incorrecta
                        Toast.makeText(ListaSolicitudesActivity.this, "Datos de Usuario fallidos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Error Usuario no encontrado posiblemente nunca entre aquí
                    Toast.makeText(ListaSolicitudesActivity.this, "Oops No tienes Solicitudes", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Fallo en la solicitud
                Toast.makeText(ListaSolicitudesActivity.this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
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
    }
}