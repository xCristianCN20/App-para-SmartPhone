package com.miguel.mexiwear;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguel.mexiwear.databinding.ActivityVacanteBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VacanteActivity extends Activity {
    private ActivityVacanteBinding binding;

    Integer intIdV;
    Integer idUsr;
    Integer boolPost = 1;
    String vacante;
    Double Salario;
    String Descripcion;
    String NombreEmpleador;
    String Direccion;

    TextView txtVacante;
    TextView txtV1_1;
    TextView txtV2;
    TextView txtV2_1;
    TextView txtV3;
    TextView txtV3_1;
    TextView txtV4;
    TextView txtV4_1;
    ImageView imgV3;

    String URL = "https://appmiguel.proyectoarp.com/MexiTrabajo/detalleVacante.php";
    String URL2 = "https://appmiguel.proyectoarp.com/MexiTrabajo/checkPostVacante.php";
    String URL3 = "https://appmiguel.proyectoarp.com/MexiTrabajo/postularmeVacante.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVacanteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtVacante=(TextView)findViewById(R.id.txtVacante);
        txtV1_1=(TextView)findViewById(R.id.txtV1_1);
        txtV2=(TextView)findViewById(R.id.txtV2);
        txtV2_1=(TextView)findViewById(R.id.txtV2_1);
        txtV3=(TextView)findViewById(R.id.txtV3);
        txtV3_1=(TextView)findViewById(R.id.txtV3_1);
        txtV4=(TextView)findViewById(R.id.txtV4);
        txtV4_1=(TextView)findViewById(R.id.txtV4_1);
        imgV3 = (ImageView)findViewById(R.id.imgV3);

        Bundle parametros = getIntent().getExtras();
        Integer dato = Integer.valueOf(parametros.getString("intIdV"));
        Integer idUser = Integer.valueOf(parametros.getString("idUser"));
        intIdV = dato;
        idUsr = idUser;

        Toast.makeText(this, idUsr.toString(), Toast.LENGTH_SHORT).show();

        StringRequest respuesta = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("null")){
                    try{
                        JSONArray datos=new JSONArray(response);
                        for(int x=0; x<datos.length();x++){
                            JSONObject valores = datos.getJSONObject(x);
                            vacante = valores.getString("varPuesto");
                            Salario = valores.getDouble("decSalario");
                            Descripcion = valores.getString("varDescripcion");
                            NombreEmpleador = valores.getString("varNombre");
                            Direccion = valores.getString("varUbicacion");
                        }
                        llenaPersona();
                        checkPost();
                    }catch(Exception e){
                        //Error Extracción de los datos, posiblemente contraseña incorrecta
                        Toast.makeText(VacanteActivity.this, "Fallo al procesar", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Error Usuario no encontrado posiblemente nunca entre aquí
                    Toast.makeText(VacanteActivity.this, "Error al procesar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Fallo en la solicitud
                Toast.makeText(VacanteActivity.this, "ERROR Conexión", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idVacante",intIdV.toString());
                return parametros;
            }
        };
        RequestQueue envio = Volley.newRequestQueue(getApplicationContext());
        envio.add(respuesta);

    }

    public void llenaPersona(){

        txtVacante.setText(String.valueOf(vacante));
        txtV1_1.setText(String.valueOf(Descripcion));
        txtV2.setText("Empleador:");
        txtV2_1.setText(String.valueOf(NombreEmpleador));
        txtV3.setText("Dirección");
        txtV3_1.setText(String.valueOf(Direccion));
        txtV4_1.setText(String.valueOf(Salario));
    }

    public void checkPost(){
        StringRequest respuesta = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.equals("null")){
                    Toast.makeText(VacanteActivity.this, "Ya estás postulado a esta vacante", Toast.LENGTH_SHORT).show();
                    imgV3.setImageResource(R.drawable.post);
                    boolPost = 1;
                }
                else{
                    boolPost = 0;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Fallo en la solicitud
                Toast.makeText(VacanteActivity.this, "ERROR de Conexión", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("idVacante",intIdV.toString());
                parametros.put("idUser",idUsr.toString());
                return parametros;
            }
        };
        RequestQueue envio = Volley.newRequestQueue(getApplicationContext());
        envio.add(respuesta);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }

    public void postularme(View view) {
        if(boolPost==0){
            StringRequest respuesta = new StringRequest(Request.Method.POST, URL3, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("SI")){
                        Toast.makeText(VacanteActivity.this, "Listo!!", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                    else{
                        //Error Usuario no encontrado posiblemente nunca entre aquí
                        Toast.makeText(VacanteActivity.this, "Fallo al postularse", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Fallo en la solicitud
                    Toast.makeText(VacanteActivity.this, "ERROR de Conexión", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parametros = new HashMap<>();
                    parametros.put("IdVac",intIdV.toString());
                    parametros.put("IdPost",idUsr.toString());
                    return parametros;
                }
            };
            RequestQueue envio = Volley.newRequestQueue(getApplicationContext());
            envio.add(respuesta);
        }
        else {
            Toast.makeText(this, "Ya Postulado", Toast.LENGTH_SHORT).show();
        }
    }
}