package com.miguel.mexiwear.Modelo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel.mexiwear.PostulacionActivity;
import com.miguel.mexiwear.R;

import java.util.ArrayList;

public class clsModelo_Listar extends RecyclerView.Adapter<clsModelo_Listar.viewHolderListaPostulaciones>{
    ArrayList<clsPostulacion> postulaciones;

    public clsModelo_Listar(ArrayList<clsPostulacion> postulaciones){
        this.postulaciones = postulaciones;
    }

    @NonNull
    @Override
    public viewHolderListaPostulaciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wearpostulalist,null,false);
        return new viewHolderListaPostulaciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderListaPostulaciones holder, int position) {
        holder.txtNombreP.setText(postulaciones.get(position).getNombre());
        holder.txtDirecc.setText(postulaciones.get(position).getDireccion());
        holder.txtIdP.setText(String.valueOf(postulaciones.get(position).getIdPostulacion()));

        //Eventos
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return postulaciones.size();
    }

    public class viewHolderListaPostulaciones extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView txtNombreP;
        TextView txtDirecc;
        TextView txtIdP;

        ImageView ivInfo;

        public viewHolderListaPostulaciones(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            txtNombreP = itemView.findViewById(R.id.txtNombreP);
            txtDirecc = itemView.findViewById(R.id.txtDirecc);
            txtIdP = itemView.findViewById(R.id.txtIdP);
            ivInfo = itemView.findViewById(R.id.ivInfo);
        }

        public void setOnClickListeners() {
            ivInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivInfo:
                    Intent postulacion = new Intent(context, PostulacionActivity.class);
                    postulacion.putExtra("intIdP",txtIdP.getText());
                    context.startActivity(postulacion);
                    break;
            }
        }
    }
}
