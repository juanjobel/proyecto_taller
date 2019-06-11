package com.juanj.prueba_taller.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juanj.prueba_taller.Entidades.Servicio;
import com.juanj.prueba_taller.R;

import java.util.ArrayList;

public class AdaptadorServicios extends  RecyclerView.Adapter<AdaptadorServicios.ViewHolderServicios> implements View.OnClickListener{

        ArrayList<Servicio> listaServicios;

public AdaptadorServicios(ArrayList<Servicio> listaServicios) { this.listaServicios = listaServicios;}
private View.OnClickListener listener;

@NonNull
@Override
public ViewHolderServicios onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_servicios, null,false);

        view.setOnClickListener(this);

        return new ViewHolderServicios(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolderServicios viewHolderServicios, int i) {
    //viewHolderServicios.etiDescripcion.setText(listaServicios.get(i).getDescripcion());
    viewHolderServicios.etiTipo.setText(listaServicios.get(i).getTipo());
    viewHolderServicios.etiSistema.setText(listaServicios.get(i).getTipo_sistema());
    viewHolderServicios.etiFechaInicio.setText(listaServicios.get(i).getFecha_inicio());
    viewHolderServicios.etiFechaFin.setText(listaServicios.get(i).getFecha_fin());
   // viewHolderServicios.etiMovil.setText(listaServicios.get(i).getMovil());
    viewHolderServicios.etiPresupuesto.setText(String.valueOf(listaServicios.get(i).getPresupuesto()));
    viewHolderServicios.etiPagado.setText(listaServicios.get(i).getPagado());
   // viewHolderServicios.etiEstado.setText(listaServicios.get(i).getEstado());
    //viewHolderServicios.etiMovil.setText(listaServicios.get(i).getMovil());
   // viewHolderServicios.etiDescripcion.setText(listaServicios.get(i).getDescripcion());
   // viewHolderServicios.etiNombreCliente.setText(listaServicios.get(i).getNombre_cliente());
    //viewHolderServicios.etiFoto.setImageResource(listaServicios.get(i).getFoto());
    viewHolderServicios.etiMatricula.setText(listaServicios.get(i).getMatricula());



}

@Override
public int getItemCount() {
        return listaServicios.size();
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

public void setListaServicios(ArrayList<Servicio> lista){
        this.listaServicios=lista;
        }

public class ViewHolderServicios extends RecyclerView.ViewHolder {

    TextView etiTipo, etiSistema, etiFechaInicio, etiFechaFin,etiMatricula,
            etiPresupuesto, etiPagado;

    ImageView etiFoto;

    public ViewHolderServicios(@NonNull View itemView) {
        super(itemView);
       // etiNombreCliente=itemView.findViewById(R.id.nombre_cliente);
        etiMatricula=itemView.findViewById(R.id.matricula_coche);
        etiTipo=itemView.findViewById(R.id.tipo);
        etiSistema=itemView.findViewById(R.id.sistema);
        etiFechaInicio=itemView.findViewById(R.id.fecha_inicio);
        etiFechaFin=itemView.findViewById(R.id.fecha_fin);
       // etiMovil=itemView.findViewById(R.id.movil_cliente);
        etiPresupuesto=itemView.findViewById(R.id.presupuesto);
        etiPagado=itemView.findViewById(R.id.pagado);
      //  etiEstado=itemView.findViewById(R.id.estado);
       // etiDescripcion=itemView.findViewById(R.id.descripcion);
       // etiFoto=itemView.findViewById(R.id.foto_servicio);


    }


}
}
