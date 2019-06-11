package com.juanj.prueba_taller.Actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.juanj.prueba_taller.Entidades.Cliente;
import com.juanj.prueba_taller.Entidades.Proveedor;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddVehiculoActivity extends AppCompatActivity {
    EditText matricula, marca , modelo, numero_bastidor, combustible, foto, cliente;
    Spinner spCliente;

    ArrayList<Cliente> listaClientes;
    ArrayList<String> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehiculo);


        matricula=findViewById(R.id.matriculaAdd);
        marca=findViewById(R.id.marcaCocheAdd);
        modelo=findViewById(R.id.modeloCocheAdd);
        numero_bastidor=findViewById(R.id.numBastidorAdd);
        combustible=findViewById(R.id.combustibleAdd);
        foto=findViewById(R.id.foto_cocheAdd);
        cliente=findViewById(R.id.clienteAdd);
        spCliente=findViewById(R.id.spinnerCliente);

        TareaWSListar tareaWSListar = new TareaWSListar();
        tareaWSListar.execute();
        listaClientes = new ArrayList<>();
        clientes =new ArrayList<>();
        clientes.add("Seleccione un cliente");

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clientes);
        spCliente.setAdapter(adapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelect = item.getItemId();

        switch (itemSelect) {
            case R.id.ic_save:


                TareaWSInsertar tareaWSInsertar=new TareaWSInsertar();
                tareaWSInsertar.execute();
                AlertDialog.Builder alert = new AlertDialog.Builder(AddVehiculoActivity.this);

                alert.setTitle("Vehículo Insertado");
                alert.setMessage("Ya cuenta con un elemento nuevo.");

                alert.create().show();

                startActivity(new Intent(AddVehiculoActivity.this, VehiculoActivity.class));
                finish();



                break;


        }

        return super.onOptionsItemSelected(item);
    }




    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddVehiculoActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/clientes");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Cliente cliente = null;

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    cliente = new Cliente();
                    cliente.setId(obj.getInt("_id"));
                    cliente.setMovil(obj.getString("movil"));


                    listaClientes.add(cliente);
                    clientes.add(cliente.getId()+" - "+cliente.getMovil());


                }
                progressDialog.dismiss();
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error en la petición", ex);
                System.out.println(ex.getMessage());
                progressDialog.dismiss();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {

            }
        }


    }

    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String...params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculo");
            post.setHeader("content-type", "application/json");

            try
            {

                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("matricula", matricula.getText().toString());
                dato.put("num_bastidor",numero_bastidor.getText().toString());
                dato.put("marca_vehiculo",marca.getText().toString());
                dato.put("modelo_vehiculo",modelo.getText().toString());
                dato.put("tipo_combustible",combustible.getText().toString());
                dato.put("foto_vehiculo",foto.getText().toString());
                dato.put("id_cliente",cliente.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddVehiculoActivity.this);

                alert.setTitle("Error al insertar");
                alert.setMessage("No se ha podido insertar el elemento. Consulte con el desarrollador de la aplicación");

                alert.create().show();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
            }else{
                Toast.makeText(getApplicationContext(), "Vehículo agregado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
