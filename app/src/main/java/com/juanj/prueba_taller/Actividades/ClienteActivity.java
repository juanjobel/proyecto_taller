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

import com.juanj.prueba_taller.Adaptadores.AdaptadorClientes;
import com.juanj.prueba_taller.Detalles.ClienteDetalle;
import com.juanj.prueba_taller.Entidades.Cliente;
import com.juanj.prueba_taller.MainActivity;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ClienteActivity extends AppCompatActivity {

    private ArrayList<Cliente> listaClientes;
    private RecyclerView recyclerClientes;
    private AdaptadorClientes adapter;
    private LinearLayoutManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        manager= new LinearLayoutManager(this);
        construirRecycler();
    }

    private void construirRecycler() {
        listaClientes=new ArrayList<>();
        recyclerClientes=findViewById(R.id.RecyclerId);

        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter= new AdaptadorClientes(listaClientes);

        recyclerClientes.setAdapter(adapter);


        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                Cliente cliente= listaClientes.get(recyclerClientes.getChildAdapterPosition(v));
                String id=cliente.getMovil();
                bundle.putString("id1",id);
                bundle.putSerializable("cliente",cliente);
                //Toast.makeText(getApplicationContext(),id+"",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(ClienteActivity.this, ClienteDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });


        recyclerClientes.setAdapter(adapter);

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
            progressDialog = ProgressDialog.show(ClienteActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }
        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
//                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/clientes");

            new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/clientes");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Cliente cliente=null;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    cliente=new Cliente();
                    cliente.setId(obj.getInt("_id"));
                    cliente.setNombre(obj.getString("nombre_cliente"));
                    cliente.setApellido1(obj.getString("apellido1_cliente"));
                    cliente.setApellido2(obj.getString("apellido2_cliente"));
                    cliente.setDocumento(obj.getString("documento"));
                    cliente.setMovil(obj.getString("movil"));
                    cliente.setDireccion(obj.getString("direccion"));


                    listaClientes.add(cliente);
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
                adapter.setListaClientes(listaClientes);
                adapter.notifyDataSetChanged();
            }
        }


    }

}
