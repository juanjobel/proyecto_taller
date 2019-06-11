package com.juanj.prueba_taller.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanj.prueba_taller.Entidades.Trabajador;
import com.juanj.prueba_taller.R;

import java.util.ArrayList;

public class AdaptadorTrabajadores extends  RecyclerView.Adapter<AdaptadorTrabajadores.ViewHolderTrabajadores> implements View.OnClickListener{

    ArrayList<Trabajador> listaTrabajadores;

    public AdaptadorTrabajadores(ArrayList<Trabajador> listaTrabajadores) { this.listaTrabajadores = listaTrabajadores;}
    private View.OnClickListener listener;

    @NonNull
    @Override
    public AdaptadorTrabajadores.ViewHolderTrabajadores onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_trabajadores, null,false);

        view.setOnClickListener((View.OnClickListener) this);

        return new AdaptadorTrabajadores.ViewHolderTrabajadores(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTrabajadores.ViewHolderTrabajadores viewHolderTrabajadores, int i) {
        viewHolderTrabajadores.etiNombre.setText(listaTrabajadores.get(i).getNombre());
        viewHolderTrabajadores.etiApellido1.setText(listaTrabajadores.get(i).getApellido1());
        viewHolderTrabajadores.etiApellido2.setText(listaTrabajadores.get(i).getApellido2());
        viewHolderTrabajadores.etiDocumento.setText(listaTrabajadores.get(i).getDocumento());

    }

    @Override
    public int getItemCount() {
        return listaTrabajadores.size();
    }
    public void setOnClickListener (View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {

        if(listener!=null){
            listener.onClick(v);
        }

    }

    public void setListaTrabajadores(ArrayList<Trabajador> lista){
        this.listaTrabajadores =lista;
    }

    public class ViewHolderTrabajadores extends RecyclerView.ViewHolder {

        TextView etiNombre, etiApellido1, etiApellido2, etiDocumento;

        public ViewHolderTrabajadores(@NonNull View itemView) {
            super(itemView);
            etiNombre=itemView.findViewById(R.id.nombre);
            etiApellido1=itemView.findViewById(R.id.apellido1);
            etiApellido2=itemView.findViewById(R.id.apellido2);
            etiDocumento=itemView.findViewById(R.id.documento);

        }
    }
}
