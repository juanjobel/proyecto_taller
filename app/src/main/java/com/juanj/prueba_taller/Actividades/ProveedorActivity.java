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

import com.juanj.prueba_taller.Adaptadores.AdaptadorProveedores;
import com.juanj.prueba_taller.Detalles.ProveedorDetalle;
import com.juanj.prueba_taller.Entidades.Proveedor;
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

public class ProveedorActivity extends AppCompatActivity {

    ArrayList<Proveedor> listaProveedores;
    RecyclerView recyclerProveedores;
    AdaptadorProveedores adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor);
        construirRecycler();

    }

    private void construirRecycler() {
        listaProveedores = new ArrayList<>();
        recyclerProveedores = findViewById(R.id.RecyclerId);

        recyclerProveedores.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter = new AdaptadorProveedores(listaProveedores);

        recyclerProveedores.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Proveedor proveedor = listaProveedores.get(recyclerProveedores.getChildAdapterPosition(v));
                bundle.putSerializable("id1", proveedor);
                int id = proveedor.getId();
                //Toast.makeText(getApplicationContext(), id + "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProveedorActivity.this, ProveedorDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });

        recyclerProveedores.setAdapter(adapter);

    }

    private void llenarLista() {

        TareaWSListar tareaWSListar = new TareaWSListar();
        tareaWSListar.execute();
    }


    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ProveedorActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/proveedores");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Proveedor proveedor = null;
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    proveedor = new Proveedor();
                    proveedor.setId(obj.getInt("_id"));
                    proveedor.setNombre_proveedor(obj.getString("nombre_proveedor"));
                    proveedor.setDomicilio(obj.getString("domicilio"));
                    proveedor.setTelefono(obj.getString("telefono"));
                    proveedor.setEmail(obj.getString("email"));



                    listaProveedores.add(proveedor);
                }
                progressDialog.dismiss();
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error su puta madre!", ex);
                System.out.println(ex.getMessage());
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                adapter.setListaProveedores(listaProveedores);
                adapter.notifyDataSetChanged();
            }
        }


    }
}