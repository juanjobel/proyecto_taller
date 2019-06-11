package com.juanj.prueba_taller.Actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.juanj.prueba_taller.Adaptadores.AdaptadorServicios;
import com.juanj.prueba_taller.Detalles.ServicioDetalle;
import com.juanj.prueba_taller.Entidades.Servicio;
import com.juanj.prueba_taller.MainActivity;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ServicioActivity extends AppCompatActivity {
    ArrayList<Servicio> listaServicios;
    RecyclerView recyclerServicios;
    AdaptadorServicios adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);

        construirRecycler();

    }
    private void construirRecycler() {
        listaServicios =new ArrayList<>();
        recyclerServicios =findViewById(R.id.RecyclerId);

        recyclerServicios.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter= new AdaptadorServicios(listaServicios);

        recyclerServicios.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                Servicio servicio= listaServicios.get(recyclerServicios.getChildAdapterPosition(v));
                bundle.putSerializable("id1",servicio);
                int id=servicio.get_id();
                //Toast.makeText(getApplicationContext(),id+"",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(ServicioActivity.this, ServicioDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });

        recyclerServicios.setAdapter(adapter);

    }

    private void llenarLista() {

        TareaWSListar tareaWSListar=new TareaWSListar();
        tareaWSListar.execute();
    }


    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;




        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ServicioActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }
        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    //new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/piezas");
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/servicios");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Servicio servicio=null;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    servicio=new Servicio();
                    servicio.set_id(obj.getInt("_id"));
                    servicio.setDescripcion(obj.getString("descripcion"));
                    servicio.setEstado(obj.getString("estado"));
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String inputDateStr=obj.getString("fecha_inicio");
                    Date date = inputFormat.parse(inputDateStr);
                    String outputDateStr = outputFormat.format(date);
                    servicio.setFecha_inicio(outputDateStr);
                    if(obj.getString("fecha_fin").equals("null")){
                        servicio.setFecha_fin("Aún en taller");
                    }else{
                        inputDateStr=obj.getString("fecha_fin");
                        date = inputFormat.parse(inputDateStr);
                        outputDateStr = outputFormat.format(date);
                        servicio.setFecha_fin(outputDateStr);
                    }

                    servicio.setMatricula(obj.getString("matricula"));
                    servicio.setFoto(obj.getString("foto"));

                    if(obj.getString("pagado").equals("1")){
                        servicio.setPagado("pagado");
                    }else{
                        servicio.setPagado("no pagado");
                    }

                    servicio.setPresupuesto(obj.getString("presupuesto")+"€");
                    servicio.setTipo(obj.getString("tipo"));
                    servicio.setTipo_sistema(obj.getString("tipo_sistema"));
                    servicio.setNombre_cliente(obj.getString("nombre_cliente"));
                    servicio.setMovil(obj.getString("movil"));

                    listaServicios.add(servicio);
                }
                progressDialog.dismiss();
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error su puta madre!", ex);
                System.out.println(ex.getMessage());
                progressDialog.dismiss();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                adapter.setListaServicios(listaServicios);
                adapter.notifyDataSetChanged();
            }
        }


    }

}