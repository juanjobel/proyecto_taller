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

import com.juanj.prueba_taller.Adaptadores.AdaptadorVehiculos;
import com.juanj.prueba_taller.Detalles.VehiculoDetalle;
import com.juanj.prueba_taller.Entidades.Vehiculo;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class VehiculoActivity extends AppCompatActivity {

    ArrayList<Vehiculo> listaVehiculos;
    RecyclerView recyclerVehiculos;
    AdaptadorVehiculos adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        construirRecycler();
    }

    private void construirRecycler() {
        listaVehiculos =new ArrayList<>();
        recyclerVehiculos =findViewById(R.id.RecyclerId);

        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter= new AdaptadorVehiculos(listaVehiculos);

        recyclerVehiculos.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                Vehiculo vehiculo= listaVehiculos.get(recyclerVehiculos.getChildAdapterPosition(v));

                String matricula=vehiculo.getMatricula();
                bundle.putString("mat",matricula);
                bundle.putSerializable("coche",vehiculo);
                //Toast.makeText(getApplicationContext(),matricula+"",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(VehiculoActivity.this, VehiculoDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });

        recyclerVehiculos.setAdapter(adapter);

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
            progressDialog = ProgressDialog.show(VehiculoActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }
        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                 //   new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculos");
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculos");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Vehiculo vehiculo=null;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    vehiculo=new Vehiculo();
                   // vehiculo.setId(obj.getInt("_id"));
                    vehiculo.setMatricula(obj.getString("matricula"));
                    vehiculo.setMarca_vehiculo(obj.getString("marca_vehiculo"));
                    vehiculo.setModelo_vehiculo(obj.getString("modelo_vehiculo"));
                    vehiculo.setNum_bastidor(obj.getString("num_bastidor"));
                    vehiculo.setNombre_cliente(obj.getString("nombre_cliente"));
                    vehiculo.setMovil(obj.getString("movil"));





                    listaVehiculos.add(vehiculo);
                }
                progressDialog.dismiss();
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error su puta madre!", ex);
                System.out.println(ex.getMessage());
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                adapter.setListaVehiculos(listaVehiculos);
                adapter.notifyDataSetChanged();
            }
        }


    }
}
