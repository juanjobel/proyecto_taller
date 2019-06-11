package com.juanj.prueba_taller.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juanj.prueba_taller.Entidades.Pieza;
import com.juanj.prueba_taller.R;

import java.util.ArrayList;

public class AdaptadorPiezas extends  RecyclerView.Adapter<AdaptadorPiezas.ViewHolderPiezas> implements View.OnClickListener{

    ArrayList<Pieza> listaPiezas;

    public AdaptadorPiezas(ArrayList<Pieza> listaPiezas) { this.listaPiezas = listaPiezas;}
    private View.OnClickListener listener;

    @NonNull
    @Override
    public AdaptadorPiezas.ViewHolderPiezas onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_piezas, null,false);

        view.setOnClickListener((View.OnClickListener) this);

        return new AdaptadorPiezas.ViewHolderPiezas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPiezas.ViewHolderPiezas viewHolderPiezas, int i) {
        viewHolderPiezas.etiNombre.setText(listaPiezas.get(i).getNombre_pieza());
        viewHolderPiezas.etiMarca.setText(listaPiezas.get(i).getMarca_pieza());
        viewHolderPiezas.etiModelo.setText(listaPiezas.get(i).getModelo_pieza());
        viewHolderPiezas.etiPrecio.setText(listaPiezas.get(i).getPrecio());
        viewHolderPiezas.etiReferencia.setText(listaPiezas.get(i).getReferencia());
        //viewHolderPiezas.etiFoto.setImageResource(listaPiezas.get(i).getFoto_pieza());

    }

    @Override
    public int getItemCount() {
        return listaPiezas.size();
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

    public void setListaPiezas(ArrayList<Pieza> lista){
        this.listaPiezas =lista;
    }

    public class ViewHolderPiezas extends RecyclerView.ViewHolder {

        TextView etiNombre, etiMarca, etiModelo, etiPrecio, etiReferencia, etiProveedor;
        ImageView etiFoto;

        public ViewHolderPiezas(@NonNull View itemView) {
            super(itemView);
            etiNombre=itemView.findViewById(R.id.nombre);
            etiMarca=itemView.findViewById(R.id.marca);
            etiModelo=itemView.findViewById(R.id.modelo);
            etiReferencia=itemView.findViewById(R.id.referencia);
            etiPrecio =itemView.findViewById(R.id.precio);
            etiProveedor=itemView.findViewById(R.id.id_proveedor);
            etiFoto=itemView.findViewById(R.id.foto);
        }
    }
}
