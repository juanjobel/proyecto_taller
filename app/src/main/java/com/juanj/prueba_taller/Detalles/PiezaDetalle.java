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

import com.juanj.prueba_taller.Actividades.ClienteActivity;
import com.juanj.prueba_taller.Actividades.PiezaActivity;
import com.juanj.prueba_taller.Entidades.Pieza;
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

public class PiezaDetalle extends AppCompatActivity {
    EditText nombre, marca, modelo, referencia, precio, proveedor;

    FloatingActionButton fab;
    Pieza pieza;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieza_detalle);
        nombre=findViewById(R.id.nombrePieza);
        marca =findViewById(R.id.marcaPieza);
        modelo =findViewById(R.id.modeloPieza);
        referencia =findViewById(R.id.referenciaPieza);
        precio=findViewById(R.id.precioPieza);
        proveedor=findViewById(R.id.nombreProveedor);

        pieza=(Pieza) getIntent().getSerializableExtra("id1");
        id=pieza.getId();

        nombre.setText(pieza.getNombre_pieza());
        marca.setText(pieza.getMarca_pieza());
        modelo.setText(pieza.getModelo_pieza());
        referencia.setText(pieza.getReferencia());
        precio.setText(pieza.getPrecio());
        proveedor.setText(pieza.getNombre_proveedor());

        nombre.setEnabled(false);
        marca.setEnabled(false);
        modelo.setEnabled(false);
        referencia.setEnabled(false);
        precio.setEnabled(false);
        proveedor.setEnabled(false);

        fab =  findViewById(R.id.fab);
        fab.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new TareaWSActualizar().execute();
                startActivity(new Intent(PiezaDetalle.this, PiezaActivity.class));
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
                marca.setEnabled(true);
                marca.setBackgroundColor(Color.YELLOW);
                marca.setTextColor(Color.GRAY);
                modelo.setEnabled(true);
                modelo.setBackgroundColor(Color.YELLOW);
                modelo.setTextColor(Color.GRAY);
                referencia.setEnabled(true);
                referencia.setBackgroundColor(Color.YELLOW);
                referencia.setTextColor(Color.GRAY);
                precio.setEnabled(true);
                precio.setBackgroundColor(Color.YELLOW);
                precio.setTextColor(Color.GRAY);
                proveedor.setEnabled(true);
                proveedor.setBackgroundColor(Color.YELLOW);
                proveedor.setTextColor(Color.GRAY);

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


            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/pieza/"+id);
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_pieza", nombre.getText().toString());
                dato.put("marca_pieza", marca.getText().toString());
                dato.put("modelo_pieza", modelo.getText().toString());
                dato.put("referencia", referencia.getText().toString());
                dato.put("precio",precio.getText().toString());
                dato.put("id_proveedor",proveedor.getText().toString());

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
                Toast.makeText(getApplicationContext(), "Pieza modificada correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
