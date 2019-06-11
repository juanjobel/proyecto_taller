package com.juanj.prueba_taller.Detalles;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.juanj.prueba_taller.Actividades.PiezaActivity;
import com.juanj.prueba_taller.Actividades.ProveedorActivity;
import com.juanj.prueba_taller.Entidades.Proveedor;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ProveedorDetalle extends AppCompatActivity {

    //Atributos de la clase
    EditText nombreProveedor, domicilioProveedor, telefonoProveedor, emailProveedor;

    FloatingActionButton fab;
    Proveedor proveedor;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_detalle);
        nombreProveedor=findViewById(R.id.nombreProveedorDetalle);
        domicilioProveedor=findViewById(R.id.domicilioDetalle);
        telefonoProveedor=findViewById(R.id.telefonoProveedor);
        emailProveedor=findViewById(R.id.email);

        proveedor=(Proveedor) getIntent().getSerializableExtra("id1");
        id=proveedor.getId();

        nombreProveedor.setText(proveedor.getNombre_proveedor());
        domicilioProveedor.setText(proveedor.getDomicilio());
        telefonoProveedor.setText(proveedor.getTelefono());
        emailProveedor.setText(proveedor.getEmail());

        nombreProveedor.setEnabled(false);
        telefonoProveedor.setEnabled(false);
        domicilioProveedor.setEnabled(false);
        emailProveedor.setEnabled(false);

        fab =  findViewById(R.id.fab);
        fab.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(validarTelefono()){
                new TareaWSActualizar().execute();
                startActivity(new Intent(ProveedorDetalle.this, ProveedorActivity.class));
                finish();
            }else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ProveedorDetalle.this);

                alert.setTitle("Error");
                alert.setMessage("Ha introducido un número de teléfono incorrecto");

                alert.create().show();
            }

            }
        });

    }

    private boolean validarTelefono() {
        if((telefonoProveedor.getText().toString().length()==9) &&
                (telefonoProveedor.getText().toString().charAt(0)=='8' ||
                        telefonoProveedor.getText().toString().charAt(0)=='9')){
            return true;
        }else{
            return false;
        }
    }

    //Método que crea el menú superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //Método que permite modificar un proveedor de la base de datos
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelect = item.getItemId();

        switch (itemSelect) {

            case R.id.modificar:
                nombreProveedor.setEnabled(true);
                nombreProveedor.setBackgroundColor(Color.YELLOW);
                nombreProveedor.setTextColor(Color.GRAY);
                telefonoProveedor.setEnabled(true);
                telefonoProveedor.setBackgroundColor(Color.YELLOW);
                telefonoProveedor.setTextColor(Color.GRAY);
                domicilioProveedor.setEnabled(true);
                domicilioProveedor.setBackgroundColor(Color.YELLOW);
                domicilioProveedor.setTextColor(Color.GRAY);
                emailProveedor.setEnabled(true);
                emailProveedor.setBackgroundColor(Color.YELLOW);
                emailProveedor.setTextColor(Color.GRAY);


                fab.setEnabled(true);
                fab.setRippleColor(Color.YELLOW);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //Tarea Asíncrona para llamar al WS de actualización en segundo plano
    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/proveedor/"+id);
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_proveedor", nombreProveedor.getText().toString());
                dato.put("domicilio", domicilioProveedor.getText().toString());
                dato.put("telefono", telefonoProveedor.getText().toString());
                dato.put("email", emailProveedor.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());

                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());

                if (!respStr.equals("true"))
                    resul = false;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getApplicationContext(), "Proveedor modificado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
