package com.juanj.prueba_taller.Actividades;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class AddTrabajadorActivity extends AppCompatActivity {

    EditText nombre, apellido1, apellido2, codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trabajador);

        nombre=findViewById(R.id.nombreTrabajadorAdd);
        apellido1=findViewById(R.id.apellido1TrabajadorAdd);
        apellido2=findViewById(R.id.apellido2TrabajadorAdd);
        codigo=findViewById(R.id.codigoTrabajadorAdd);

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
                AlertDialog.Builder alert = new AlertDialog.Builder(AddTrabajadorActivity.this);

                alert.setTitle("Proveedor Insertado");
                alert.setMessage("Ya cuenta con un elemento nuevo.");

                alert.create().show();

                startActivity(new Intent(AddTrabajadorActivity.this, TrabajadorActivity.class));
                finish();



                break;


        }

        return super.onOptionsItemSelected(item);
    }

    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String...params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/trabajador");
            post.setHeader("content-type", "application/json");

            try
            {

                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_trabajador", nombre.getText().toString());
                dato.put("apellido1_trabajador",apellido1.getText().toString());
                dato.put("apellido2_trabajador",apellido2.getText().toString());
                dato.put("codigo_trabajador",codigo.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddTrabajadorActivity.this);

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
                Toast.makeText(getApplicationContext(), "Trabajador agregado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
