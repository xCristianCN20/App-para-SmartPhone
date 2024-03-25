package com.miguel.mexiwear.Modelo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miguel.mexiwear.R;
import com.miguel.mexiwear.SolicitudActivity;

import java.util.ArrayList;

public class clsModelo_ListarSolicitud extends RecyclerView.Adapter<clsModelo_ListarSolicitud.viewHolderListaSolicitudes>{
    ArrayList<clsSolicitud> solicitudes;

    public clsModelo_ListarSolicitud(ArrayList<clsSolicitud> solicitudes){
        this.solicitudes = solicitudes;
    }

    @NonNull
    @Override
    public viewHolderListaSolicitudes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wearsolicitudlist,null,false);
        return new viewHolderListaSolicitudes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderListaSolicitudes holder, int position) {
        holder.txtNombreS.setText(solicitudes.get(position).getNombre());
        holder.txtDireccS.setText(solicitudes.get(position).getDireccion());
        holder.txtIdS.setText(String.valueOf(solicitudes.get(position).getIdSolicitud()));

        //Eventos
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return solicitudes.size();
    }

    public class viewHolderListaSolicitudes extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        TextView txtNombreS;
        TextView txtDireccS;
        TextView txtIdS;

        ImageView ivInfoS;

        public viewHolderListaSolicitudes(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            txtNombreS = itemView.findViewById(R.id.txtNombreS);
            txtDireccS = itemView.findViewById(R.id.txtDireccS);
            txtIdS = itemView.findViewById(R.id.txtIdS);
            ivInfoS = itemView.findViewById(R.id.ivInfoS);
        }

        public void setOnClickListeners() {
            ivInfoS.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivInfoS:
                    Intent solicitud = new Intent(context, SolicitudActivity.class);
                    solicitud.putExtra("intIdS",txtIdS.getText());
                    context.startActivity(solicitud);
                    break;
            }
        }
    }

}
