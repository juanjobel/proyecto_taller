package com.juanj.prueba_taller.Actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.juanj.prueba_taller.Adaptadores.AdaptadorTrabajadores;
import com.juanj.prueba_taller.Detalles.TrabajadorDetalle;
import com.juanj.prueba_taller.Entidades.Trabajador;
import com.juanj.prueba_taller.MainActivity;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
public class TrabajadorActivity extends AppCompatActivity {

    ArrayList<Trabajador> listaTrabajadores;
    RecyclerView recyclerTrabajadores;
    AdaptadorTrabajadores adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador);

        construirRecycler();

    }

    private void construirRecycler() {
        listaTrabajadores=new ArrayList<>();
        recyclerTrabajadores=findViewById(R.id.RecyclerId);

        recyclerTrabajadores.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter= new AdaptadorTrabajadores(listaTrabajadores);

        recyclerTrabajadores.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                Trabajador trabajador= listaTrabajadores.get(recyclerTrabajadores.getChildAdapterPosition(v));
                bundle.putSerializable("id1",trabajador);
                int id=trabajador.getId();
                //Toast.makeText(getApplicationContext(),id+"",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(TrabajadorActivity.this, TrabajadorDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });

        recyclerTrabajadores.setAdapter(adapter);

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
            progressDialog = ProgressDialog.show(TrabajadorActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }
        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                  //  new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/trabajadores");
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/trabajadores");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Trabajador trabajador=null;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    trabajador=new Trabajador();
                    trabajador.setId(obj.getInt("id"));
                    trabajador.setNombre(obj.getString("nombre_trabajador"));
                    trabajador.setApellido1(obj.getString("apellido1_trabajador"));
                    trabajador.setApellido2(obj.getString("apellido2_trabajador"));
                    trabajador.setDocumento(obj.getString("codigo_trabajador"));



                    listaTrabajadores.add(trabajador);
                }
                progressDialog.dismiss();
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error en la petición", ex);
                System.out.println(ex.getMessage());
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                adapter.setListaTrabajadores(listaTrabajadores);
                adapter.notifyDataSetChanged();
            }
        }


    }

}
