package com.juanj.prueba_taller.Adaptadores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanj.prueba_taller.Entidades.RecibirMensaje;
import com.juanj.prueba_taller.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdaptadorChat extends RecyclerView.Adapter<AdaptadorChat.ViewHolderMensajes> {

    private List<RecibirMensaje> listMensaje = new ArrayList<>();
    private Context c;

    @NonNull
    @Override
    public AdaptadorChat.ViewHolderMensajes onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.view_mensajes,viewGroup,false);
        return new ViewHolderMensajes(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorChat.ViewHolderMensajes viewHolderMensajes, int i) {
        viewHolderMensajes.getNombre().setText(listMensaje.get(i).getNombre());
        viewHolderMensajes.getMensaje().setText(listMensaje.get(i).getMensaje());
        if(listMensaje.get(i).getType_mensaje().equals("2")){
            viewHolderMensajes.getMensaje().setVisibility(View.VISIBLE); }
        Long codigoHora = listMensaje.get(i).getHora();
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");//a pm o am
        viewHolderMensajes.getHora().setText(sdf.format(d));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public AdaptadorChat(Context c) {
        this.c = c;
    }

    public void addMensaje(RecibirMensaje mensaje){
        listMensaje.add(mensaje);
        notifyItemInserted(listMensaje.size());
    }
    public class ViewHolderMensajes extends RecyclerView.ViewHolder{

        private TextView nombre;
        private TextView mensaje;
        private TextView hora;

        public ViewHolderMensajes(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreMensaje);
            mensaje = itemView.findViewById(R.id.mensajeMensaje);
            hora = itemView.findViewById(R.id.horaMensaje);
        }

        public TextView getNombre() {
            return nombre;
        }

        public void setNombre(TextView nombre) {
            this.nombre = nombre;
        }

        public TextView getMensaje() {
            return mensaje;
        }

        public void setMensaje(TextView mensaje) {
            this.mensaje = mensaje;
        }

        public TextView getHora() {
            return hora;
        }

        public void setHora(TextView hora) {
            this.hora = hora;
        }

    }
}
