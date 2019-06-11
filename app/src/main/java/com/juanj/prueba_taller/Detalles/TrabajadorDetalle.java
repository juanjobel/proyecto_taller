package com.juanj.prueba_taller.Detalles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.juanj.prueba_taller.Actividades.ProveedorActivity;
import com.juanj.prueba_taller.Actividades.TrabajadorActivity;
import com.juanj.prueba_taller.Actividades.VehiculoActivity;
import com.juanj.prueba_taller.Entidades.Trabajador;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class TrabajadorDetalle extends AppCompatActivity {
    EditText nombre, apellido1, apellido2, documento;

    FloatingActionButton fab;
    Trabajador trabajador;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador_detalle);
        nombre=findViewById(R.id.nombreTrabajador);
        apellido1 =findViewById(R.id.apellido1Trabajador);
        apellido2 =findViewById(R.id.apellido2Trabajador);
        documento =findViewById(R.id.documentoTrabajador);

        trabajador=(Trabajador) getIntent().getSerializableExtra("id1");
        id=trabajador.getId();

        nombre.setText(trabajador.getNombre());
        apellido1.setText(trabajador.getApellido1());
        apellido2.setText(trabajador.getApellido2());
        documento.setText(trabajador.getDocumento());

        nombre.setEnabled(false);
        apellido2.setEnabled(false);
        apellido1.setEnabled(false);
        documento.setEnabled(false);

        fab =  findViewById(R.id.fab);
        fab.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new TareaWSActualizar().execute();
                startActivity(new Intent(TrabajadorDetalle.this, TrabajadorActivity.class));
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelect = item.getItemId();

        switch (itemSelect) {

            case R.id.modificar:
                nombre.setEnabled(true);
                nombre.setBackgroundColor(Color.YELLOW);
                nombre.setTextColor(Color.GRAY);
                apellido1.setEnabled(true);
                apellido1.setBackgroundColor(Color.YELLOW);
                apellido1.setTextColor(Color.GRAY);
                apellido2.setEnabled(true);
                apellido2.setBackgroundColor(Color.YELLOW);
                apellido2.setTextColor(Color.GRAY);
                documento.setEnabled(true);
                documento.setBackgroundColor(Color.YELLOW);
                documento.setTextColor(Color.GRAY);

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


            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/trabajador/"+id);
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_trabajador", nombre.getText().toString());
                dato.put("apellido1_trabajador", apellido1.getText().toString());
                dato.put("apellido2_trabajador", apellido2.getText().toString());
                dato.put("codigo_trabajador", documento.getText().toString());


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
                Toast.makeText(getApplicationContext(), "Trabajador modificado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
