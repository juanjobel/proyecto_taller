package com.juanj.prueba_taller.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanj.prueba_taller.Entidades.Vehiculo;
import com.juanj.prueba_taller.R;

import java.util.ArrayList;

public class AdaptadorVehiculos extends  RecyclerView.Adapter<AdaptadorVehiculos.ViewHolderVehiculos> implements View.OnClickListener{

    ArrayList<Vehiculo> listaVehiculos;

    public AdaptadorVehiculos(ArrayList<Vehiculo> listaVehiculos) { this.listaVehiculos = listaVehiculos;}
    private View.OnClickListener listener;

    @NonNull
    @Override
    public AdaptadorVehiculos.ViewHolderVehiculos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_vehiculos, null,false);

        view.setOnClickListener((View.OnClickListener) this);

        return new AdaptadorVehiculos.ViewHolderVehiculos(view);    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderVehiculos viewHolderVehiculos, int i) {
        viewHolderVehiculos.etiMarca.setText(listaVehiculos.get(i).getMarca_vehiculo());
        viewHolderVehiculos.etiModelo.setText(listaVehiculos.get(i).getModelo_vehiculo());
        viewHolderVehiculos.etiMatricula.setText(listaVehiculos.get(i).getMatricula());
        viewHolderVehiculos.etiNum_bastidor.setText(listaVehiculos.get(i).getNum_bastidor());
        viewHolderVehiculos.etiCliente.setText(listaVehiculos.get(i).getNombre_cliente());
        viewHolderVehiculos.etiMovil.setText(listaVehiculos.get(i).getMovil());
        //viewHolderVehiculos.etiFoto.setText(listaVehiculos.get(i).getFoto_vehiculo());

    }

    @Override
    public int getItemCount() {
        return listaVehiculos.size();
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

    public void setListaVehiculos(ArrayList<Vehiculo> lista){
        this.listaVehiculos =lista;
    }



    public class ViewHolderVehiculos extends RecyclerView.ViewHolder {

        TextView etiMarca, etiModelo, etiMatricula, etiNum_bastidor, etiCliente, etiMovil;
        //ImageView etiFoto;

        public ViewHolderVehiculos(@NonNull View itemView) {
            super(itemView);

            etiMarca=itemView.findViewById(R.id.marca);
            etiModelo=itemView.findViewById(R.id.modelo);
            etiNum_bastidor=itemView.findViewById(R.id.num_bastidor);
            etiMatricula =itemView.findViewById(R.id.matricula);
            etiCliente=itemView.findViewById(R.id.cliente);
            etiMovil=itemView.findViewById(R.id.movil);
            //etiFoto=itemView.findViewById(R.id.foto);
        }

    }
}
