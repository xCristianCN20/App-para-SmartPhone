package com.miguel.mexiwear;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguel.mexiwear.databinding.ActivityLoginBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    EditText txtVinc;
    private ActivityLoginBinding binding;

    NotificationCompat.Builder mBuilder;
    int mNotification = 1;
    String channelId = "my_channel _01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        txtVinc = (EditText)findViewById(R.id.txtVinc);

    }

    public void inciarSesion(View view)
    {//Inicio Metodo
        String URL = "https://appmiguel.proyectoarp.com/MexiTrabajo/loginWear.php";
        StringRequest respuesta = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String correo = "";
                String Rol = "";
                String Direccion = "";
                Integer IdUser=0;
                if(!response.equals("null")){
                    try{
                        JSONArray datos=new JSONArray(response);
                        for(int x=0; x<datos.length();x++){
                            JSONObject valores = datos.getJSONObject(x);
                            correo = valores.getString("varEmail");
                            Rol = valores.getString("intIdRol");
                            Direccion = valores.getString("varMunicipio");
                            IdUser = valores.getInt("intIdUsuario");
                        }
                        //HOLASADASpresentador_login.acceso(usuarioEncontrado);
                        //Toast.makeText(LoginActivity.this, "Datos de Sesión: " + correo + "- " + Rol, Toast.LENGTH_SHORT).show();
                        if(Integer.valueOf(Rol) == 3){
                            Intent vistaEmpleador = new Intent(getApplicationContext(), MenuEmplActivity.class);
                            vistaEmpleador.putExtra("usuario", correo);
                            vistaEmpleador.putExtra("direccion",Direccion);
                            startActivity(vistaEmpleador);
                            notificar();
                        }
                        else if(Integer.valueOf(Rol) == 2){
                            Intent vistaTrabajador = new Intent(getApplicationContext(), MenuTrabajadorActivity.class);
                            vistaTrabajador.putExtra("usuario", correo);
                            vistaTrabajador.putExtra("idUser", IdUser);
                            vistaTrabajador.putExtra("direccion",Direccion);
                            startActivity(vistaTrabajador);
                            notificar();
                        }
                    }catch(Exception e){
                        //Error Extracción de los datos, posiblemente contraseña incorrecta
                        Toast.makeText(LoginActivity.this, "Datos Incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Error Usuario no encontrado posiblemente nunca entre aquí
                    Toast.makeText(LoginActivity.this, "Datos de Sesión incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Fallo en la solicitud
                Toast.makeText(LoginActivity.this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("pinCode",txtVinc.getText().toString());
                return parametros;
            }
        };
        RequestQueue envio = Volley.newRequestQueue(getApplicationContext());
        envio.add(respuesta);
    }//Fin Metodo

    void notificar(){
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this,"");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "MexiTrabajo";
            String description = "Bienvenido de Nuevo";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(channelId,name,importance);

            mChannel.setDescription(description);
            mChannel.enableLights(true);

            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            mNotificationManager.createNotificationChannel(mChannel);

            mBuilder = new NotificationCompat.Builder(this,channelId);
        }

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender();


        mBuilder.setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentTitle("MexiTrabajo")
                .setContentText("Inicio de Sesión desde App Móvil - Wear Vinculado");

        mBuilder.setChannelId(channelId);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(mNotification,mBuilder.build());
    }
}