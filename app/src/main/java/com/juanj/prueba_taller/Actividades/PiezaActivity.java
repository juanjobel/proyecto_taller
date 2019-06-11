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

import com.juanj.prueba_taller.Adaptadores.AdaptadorPiezas;
import com.juanj.prueba_taller.Detalles.PiezaDetalle;
import com.juanj.prueba_taller.Entidades.Pieza;
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

public class PiezaActivity extends AppCompatActivity {

    ArrayList<Pieza> listaPiezas;
    RecyclerView recyclerPiezas;
    AdaptadorPiezas adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieza);

        construirRecycler();

    }
    private void construirRecycler() {
        listaPiezas =new ArrayList<>();
        recyclerPiezas =findViewById(R.id.recyclerPiezas);

        recyclerPiezas.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter= new AdaptadorPiezas(listaPiezas);

        recyclerPiezas.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                Pieza pieza= listaPiezas.get(recyclerPiezas.getChildAdapterPosition(v));
                bundle.putSerializable("id1",pieza);
                int id=pieza.getId();
                //Toast.makeText(getApplicationContext(),id+"",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(PiezaActivity.this, PiezaDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });

        recyclerPiezas.setAdapter(adapter);

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
            progressDialog = ProgressDialog.show(PiezaActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }
        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    //new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/piezas");
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/piezas");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Pieza pieza=null;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    pieza=new Pieza();
                    pieza.setId(obj.getInt("_id"));
                    pieza.setNombre_pieza(obj.getString("nombre_pieza"));
                    pieza.setMarca_pieza(obj.getString("marca_pieza"));
                    pieza.setModelo_pieza(obj.getString("modelo_pieza"));
                    pieza.setReferencia(obj.getString("referencia"));
                    pieza.setPrecio(obj.getString("precio")+"€");
                    //pieza.setFoto_pieza(obj.getInt("foto_pieza"));
                    pieza.setNombre_proveedor(obj.getString("nombre_proveedor"));





                    listaPiezas.add(pieza);
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
                adapter.setListaPiezas(listaPiezas);
                adapter.notifyDataSetChanged();
            }
        }


    }

}
