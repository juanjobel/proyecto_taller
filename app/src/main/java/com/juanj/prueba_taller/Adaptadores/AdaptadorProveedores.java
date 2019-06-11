package com.juanj.prueba_taller.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.juanj.prueba_taller.Entidades.Proveedor;
import com.juanj.prueba_taller.R;

import java.util.ArrayList;

public class AdaptadorProveedores extends  RecyclerView.Adapter<AdaptadorProveedores.ViewHolderProveedores> implements View.OnClickListener{

    ArrayList<Proveedor> listaProveedores;

    public AdaptadorProveedores(ArrayList<Proveedor> listaProveedores) { this.listaProveedores = listaProveedores;}
    private View.OnClickListener listener;

    @NonNull
    @Override
    public AdaptadorProveedores.ViewHolderProveedores onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_proveedores, null,false);

        view.setOnClickListener((View.OnClickListener) this);

        return new AdaptadorProveedores.ViewHolderProveedores(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorProveedores.ViewHolderProveedores viewHolderProveedores, int i) {
        viewHolderProveedores.etiNombre.setText(listaProveedores.get(i).getNombre_proveedor());
        viewHolderProveedores.etiDomicilio.setText(listaProveedores.get(i).getDomicilio());
        viewHolderProveedores.etiTelefono.setText(listaProveedores.get(i).getTelefono());
        viewHolderProveedores.etiEmail.setText(listaProveedores.get(i).getEmail());

    }



    @Override
    public int getItemCount() {
        return listaProveedores.size();
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

    public void setListaProveedores(ArrayList<Proveedor> lista){
        this.listaProveedores =lista;
    }


    public class ViewHolderProveedores extends RecyclerView.ViewHolder {

        TextView etiNombre, etiDomicilio, etiTelefono, etiEmail;
        MapView etiLocalizacion;

        public ViewHolderProveedores(@NonNull View itemView) {
            super(itemView);
            etiNombre=itemView.findViewById(R.id.nombre);
            etiDomicilio=itemView.findViewById(R.id.domicilio);
            etiTelefono=itemView.findViewById(R.id.telefono);
            etiEmail=itemView.findViewById(R.id.email);
            etiLocalizacion =itemView.findViewById(R.id.mapa);
        }
    }
}
