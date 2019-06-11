package com.juanj.prueba_taller.Actividades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.juanj.prueba_taller.Adaptadores.AdaptadorProveedores;
import com.juanj.prueba_taller.Detalles.ProveedorDetalle;
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

public class AddPiezaActivity extends AppCompatActivity {

    //Atributos de la clase
    EditText nombre, marca , modelo, referencia, precio, foto, proveedor;
    Spinner spProveedor;

    ArrayList<Proveedor> listaProveedores;
    ArrayList<String> elementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pieza);


        nombre=findViewById(R.id.nombrePiezaAdd);
        marca=findViewById(R.id.marcaAdd);
        modelo=findViewById(R.id.modeloAdd);
        referencia=findViewById(R.id.referenciaAdd);
        precio=findViewById(R.id.precioAdd);
        foto=findViewById(R.id.foto_piezaAdd);
        proveedor=findViewById(R.id.proveedorAdd);
        spProveedor=findViewById(R.id.spinnerProveedor);

        TareaWSListar tareaWSListar = new TareaWSListar();
        tareaWSListar.execute();
        listaProveedores= new ArrayList<>();
        elementos=new ArrayList<>();
        elementos.add("Seleccione un proveedor");

        ArrayAdapter<String>adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,elementos);
        spProveedor.setAdapter(adapter);



    }

    //Método que crea el menú superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        return true;
    }

    //Método que permite guardar un nuevo cliente en la base de datos
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelect = item.getItemId();

        switch (itemSelect) {
            case R.id.ic_save:


                TareaWSInsertar tareaWSInsertar=new TareaWSInsertar();
                tareaWSInsertar.execute();
                AlertDialog.Builder alert = new AlertDialog.Builder(AddPiezaActivity.this);

                alert.setTitle("Cliente Insertado");
                alert.setMessage("Ya cuenta con un elemento nuevo.");

                alert.create().show();

                startActivity(new Intent(AddPiezaActivity.this, PiezaActivity.class));
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
            progressDialog = ProgressDialog.show(AddPiezaActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest//proveedores");

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


                    listaProveedores.add(proveedor);
                    elementos.add(proveedor.getId()+" - "+proveedor.getNombre_proveedor());


                }
                progressDialog.dismiss();
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error su puta madre!", ex);
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

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/pieza");
            post.setHeader("content-type", "application/json");

            try
            {

                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_pieza", nombre.getText().toString());
                dato.put("marca_pieza",marca.getText().toString());
                dato.put("modelo_pieza",modelo.getText().toString());
                dato.put("referencia",referencia.getText().toString());
                dato.put("precio",precio.getText().toString());
                dato.put("foto_pieza",foto.getText().toString());
                dato.put("id_proveedor",proveedor.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddPiezaActivity.this);

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
                Toast.makeText(getApplicationContext(), "Pieza agregada correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
