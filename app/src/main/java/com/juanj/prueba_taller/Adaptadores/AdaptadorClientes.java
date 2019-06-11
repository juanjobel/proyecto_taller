package com.juanj.prueba_taller.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.juanj.prueba_taller.Entidades.Cliente;
import com.juanj.prueba_taller.R;

import java.util.ArrayList;

public class AdaptadorClientes extends  RecyclerView.Adapter<AdaptadorClientes.ViewHolderClientes> implements View.OnClickListener{

    ArrayList<Cliente> listaClientes;

    public AdaptadorClientes (ArrayList<Cliente> listaClientes) { this.listaClientes = listaClientes;}
    private View.OnClickListener listener;


    @NonNull
    @Override
    public ViewHolderClientes onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_clientes, null,false);

        view.setOnClickListener(this);

        return new ViewHolderClientes(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorClientes.ViewHolderClientes viewHolderClientes, int i) {
        viewHolderClientes.etiNombre.setText(listaClientes.get(i).getNombre());
        viewHolderClientes.etiApellido1.setText(listaClientes.get(i).getApellido1());
        viewHolderClientes.etiApellido2.setText(listaClientes.get(i).getApellido2());
        viewHolderClientes.etiDocumento.setText(listaClientes.get(i).getDocumento());
        viewHolderClientes.etiMovil.setText(listaClientes.get(i).getMovil());
        viewHolderClientes.etiDireccion.setText(listaClientes.get(i).getDireccion());

    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
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

    public void setListaClientes(ArrayList<Cliente> lista){
        this.listaClientes=lista;
    }

    public class ViewHolderClientes extends RecyclerView.ViewHolder {

        TextView etiNombre, etiApellido1, etiApellido2, etiDocumento,
        etiMovil, etiDireccion;

        public ViewHolderClientes(@NonNull View itemView) {
            super(itemView);
            etiNombre=itemView.findViewById(R.id.nombre);
            etiApellido1=itemView.findViewById(R.id.apellido1);
            etiApellido2=itemView.findViewById(R.id.apellido2);
            etiDocumento=itemView.findViewById(R.id.documento);
            etiMovil=itemView.findViewById(R.id.movil);
            etiDireccion=itemView.findViewById(R.id.direccion);

        }
    }
}
