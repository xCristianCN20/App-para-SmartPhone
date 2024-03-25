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
import com.miguel.mexiwear.Modelo.clsModelo_ListarVacantesNear;
import com.miguel.mexiwear.Modelo.clsVacanteList;
import com.miguel.mexiwear.databinding.ActivityListaVacantesCercaBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaVacantesCercaActivity extends Activity {

    private ActivityListaVacantesCercaBinding binding;
    String user;
    Integer idUsr=0;
    ArrayList<clsVacanteList> vacantes;
    String Direccion;
    RecyclerView rvTrabajosNear;
    String URL = "https://appmiguel.proyectoarp.com/MexiTrabajo/getVacantesNear.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaVacantesCercaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vacantes = new ArrayList<clsVacanteList>();
        rvTrabajosNear = (RecyclerView) findViewById(R.id.rvacantesnear);
        rvTrabajosNear.setLayoutManager(new GridLayoutManager(this, 1));

        Bundle parametros = getIntent().getExtras();
        String dato = parametros.getString("usuario");
        String direcc = parametros.getString("Direccion");
        Integer idUser = parametros.getInt("idUser");
        user = dato;
        idUsr = idUser;
        Direccion = direcc;

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
                            String varPuesto = valores.getString("varPuesto");
                            String varNombre = valores.getString("varNombre");
                            Integer intIdOfertaLaboral = valores.getInt("intIdOfertaLaboral");
                            vacantes.add(new clsVacanteList(varPuesto,varNombre,intIdOfertaLaboral,idUsr));
                        }
                        clsModelo_ListarVacantesNear Modelo_ListarVac =  new clsModelo_ListarVacantesNear(vacantes);
                        rvTrabajosNear.setAdapter(Modelo_ListarVac);
                    }catch(Exception e){
                        //Error Extracción de los datos, posiblemente contraseña incorrecta
                        Toast.makeText(ListaVacantesCercaActivity.this, "Datos de Usuario fallidos 1", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Error Usuario no encontrado posiblemente nunca entre aquí
                    Toast.makeText(ListaVacantesCercaActivity.this, "Sin Vacantes Cerca", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Fallo en la solicitud
                Toast.makeText(ListaVacantesCercaActivity.this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("direccion",Direccion);
                return parametros;
            }
        };
        RequestQueue envio = Volley.newRequestQueue(getApplicationContext());
        envio.add(respuesta);
    }//Fin Metodo
}