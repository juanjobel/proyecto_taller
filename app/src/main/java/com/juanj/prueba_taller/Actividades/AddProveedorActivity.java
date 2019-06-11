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

public class AddProveedorActivity extends AppCompatActivity {

    EditText nombre, domicilio, telefono, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proveedor);

        nombre=findViewById(R.id.nombreProveedorAdd);
        domicilio=findViewById(R.id.domicilioAdd);
        telefono=findViewById(R.id.telefonoAdd);
        email=findViewById(R.id.emailAdd);

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

                if(validarTelefono()) {
                    TareaWSInsertar tareaWSInsertar = new TareaWSInsertar();
                    tareaWSInsertar.execute();
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddProveedorActivity.this);

                    alert.setTitle("Proveedor Insertado");
                    alert.setMessage("Ya cuenta con un elemento nuevo.");

                    alert.create().show();

                    startActivity(new Intent(AddProveedorActivity.this, ProveedorActivity.class));
                    finish();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddProveedorActivity.this);

                    alert.setTitle("Error");
                    alert.setMessage("Ha introducido un número de teléfono incorrecto");

                    alert.create().show();
                }



                break;


        }

        return super.onOptionsItemSelected(item);
    }


    private boolean validarTelefono() {
        if((telefono.getText().toString().length()==9) &&
                (telefono.getText().toString().charAt(0)=='8' ||
                        telefono.getText().toString().charAt(0)=='9')){
            return true;
        }else{
            return false;
        }
    }
    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String...params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/proveedor");
            post.setHeader("content-type", "application/json");

            try
            {

                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_proveedor", nombre.getText().toString());
                dato.put("domicilio",domicilio.getText().toString());
                dato.put("telefono",telefono.getText().toString());
                dato.put("email",email.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddProveedorActivity.this);

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
                Toast.makeText(getApplicationContext(), "Proveedor agregado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
