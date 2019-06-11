package com.juanj.prueba_taller.Detalles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.juanj.prueba_taller.Actividades.ServicioActivity;
import com.juanj.prueba_taller.Actividades.TrabajadorActivity;
import com.juanj.prueba_taller.Actividades.VehiculoActivity;
import com.juanj.prueba_taller.Adaptadores.AdaptadorServicios;
import com.juanj.prueba_taller.Adaptadores.AdaptadorVehiculos;
import com.juanj.prueba_taller.Entidades.Servicio;
import com.juanj.prueba_taller.Entidades.Vehiculo;
import com.juanj.prueba_taller.MainActivity;
import com.juanj.prueba_taller.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VehiculoDetalle extends AppCompatActivity {

    EditText matricula, marca, modelo, num_bastidor, nombre_cliente,movil;
    ImageView foto;

    ArrayList<Servicio> listaServicios;
    RecyclerView recyclerServicios;
    AdaptadorServicios adapter;

    FloatingActionButton fab;
    Vehiculo vehiculo;
    String mat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo_detalle);
        matricula=findViewById(R.id.detalleMatricula);
        marca=findViewById(R.id.detalleMarcaCoche);
        modelo=findViewById(R.id.detalleModeloCoche);
        num_bastidor=findViewById(R.id.detalleNum_bastidor);
        nombre_cliente=findViewById(R.id.detalleNombreCliente);
        movil=findViewById(R.id.detalleMovilCliente);
        foto=findViewById(R.id.detalleImagenCoche);

        mat=getIntent().getStringExtra("mat");
        vehiculo=(Vehiculo)getIntent().getSerializableExtra("coche");

        matricula.setText(vehiculo.getMatricula());
        marca.setText(vehiculo.getMarca_vehiculo());
        modelo.setText(vehiculo.getModelo_vehiculo());
        num_bastidor.setText(vehiculo.getNum_bastidor());
        nombre_cliente.setText(vehiculo.getNombre_cliente());
        movil.setText(vehiculo.getMovil());

        matricula.setEnabled(false);
        marca.setEnabled(false);
        modelo.setEnabled(false);
        num_bastidor.setEnabled(false);
        nombre_cliente.setEnabled(false);
        movil.setEnabled(false);

        fab =  findViewById(R.id.fab);
        fab.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new TareaWSActualizar().execute();
                startActivity(new Intent(VehiculoDetalle.this, VehiculoActivity.class));
                finish();

            }
        });

        construirRecycler();
    }

    private void construirRecycler() {
        listaServicios = new ArrayList<>();
        recyclerServicios = findViewById(R.id.recyclerVehiculos);

        recyclerServicios.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter = new AdaptadorServicios(listaServicios);

        recyclerServicios.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Servicio servicio = listaServicios.get(recyclerServicios.getChildAdapterPosition(v));
                bundle.putSerializable("id1", servicio);
                int id = servicio.get_id();
                //Toast.makeText(getApplicationContext(), id + "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(VehiculoDetalle.this, ServicioDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });

        recyclerServicios.setAdapter(adapter);

    }

    private void llenarLista() {

        TareaWSListar tareaWSListar = new TareaWSListar();
        tareaWSListar.execute();
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
            case R.id.eliminar:
                AlertDialog.Builder alert = new AlertDialog.Builder(VehiculoDetalle.this);

                alert.setTitle("Eliminar Vehículo");
                alert.setMessage("¿Seguro que desea eliminar este vehículo?\nLos cambios no se podrán revertir.");
                alert.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TareaWSEliminar tareaWSEliminar=new TareaWSEliminar();
                        tareaWSEliminar.execute();
                        Toast.makeText(getApplicationContext(),"Vehículo eliminado",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(VehiculoDetalle.this, VehiculoActivity.class));
                        finish();
                    }
                });
                alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Vehículo no eliminado",Toast.LENGTH_SHORT).show();
                    }
                });

                alert.create().show();



                break;
            case R.id.modificar:
                matricula.setEnabled(true);
                matricula.setBackgroundColor(Color.YELLOW);
                matricula.setTextColor(Color.GRAY);
                marca.setEnabled(true);
                marca.setBackgroundColor(Color.YELLOW);
                marca.setTextColor(Color.GRAY);
                modelo.setEnabled(true);
                modelo.setBackgroundColor(Color.YELLOW);
                modelo.setTextColor(Color.GRAY);
                num_bastidor.setEnabled(true);
                num_bastidor.setBackgroundColor(Color.YELLOW);
                num_bastidor.setTextColor(Color.GRAY);


                fab.setEnabled(true);
                fab.setRippleColor(Color.YELLOW);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;




        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(VehiculoDetalle.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }
        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    //new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/piezas");
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/servicio/matricula/"+mat);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Servicio servicio=null;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    servicio=new Servicio();
                    servicio.setDescripcion(obj.getString("descripcion"));
                    servicio.setEstado(obj.getString("estado"));
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String inputDateStr=obj.getString("fecha_inicio");
                    Date date = inputFormat.parse(inputDateStr);
                    String outputDateStr = outputFormat.format(date);
                    servicio.setFecha_inicio(outputDateStr);
                    if(obj.getString("fecha_fin").equals("null")){
                        servicio.setFecha_fin("Aún en taller");
                    }else{
                        inputDateStr=obj.getString("fecha_fin");
                        date = inputFormat.parse(inputDateStr);
                        outputDateStr = outputFormat.format(date);
                        servicio.setFecha_fin(outputDateStr);
                    }


                    servicio.setMatricula(obj.getString("matricula"));
                    servicio.setFoto(obj.getString("foto"));

                    if(obj.getString("pagado").equals("1")){
                        servicio.setPagado("pagado");
                    }else{
                        servicio.setPagado("no pagado");
                    }

                    servicio.setPresupuesto(obj.getString("presupuesto"));
                    servicio.setTipo(obj.getString("tipo"));
                    servicio.setTipo_sistema(obj.getString("tipo_sistema"));
                    servicio.setNombre_cliente(obj.getString("nombre_cliente"));
                    servicio.setMovil(obj.getString("movil"));

                    listaServicios.add(servicio);
                }
                progressDialog.dismiss();
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error en la petición", ex);
                System.out.println(ex.getMessage());
                progressDialog.dismiss();
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                adapter.setListaServicios(listaServicios);
                adapter.notifyDataSetChanged();
            }
        }


    }

    //Tarea Asíncrona para llamar al WS de eliminación en segundo plano
    private class TareaWSEliminar extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpDelete del =
                    new HttpDelete("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculo/"+getIntent().getStringExtra("mat"));

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
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
                Toast.makeText(getApplicationContext(), "Vehículo eliminado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //Tarea Asíncrona para llamar al WS de actualización en segundo plano
    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculo/"+mat);
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("marca_vehiculo", marca.getText().toString());
                dato.put("modelo_vehiculo", modelo.getText().toString());
                dato.put("matricula", matricula.getText().toString());
                dato.put("num_bastidor", num_bastidor.getText().toString());


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
                Toast.makeText(getApplicationContext(), "Vehículo modificado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }


}