package com.juanj.prueba_taller.Detalles;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
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

import com.juanj.prueba_taller.Actividades.ClienteActivity;
import com.juanj.prueba_taller.Actividades.ServicioActivity;
import com.juanj.prueba_taller.Actividades.VehiculoActivity;
import com.juanj.prueba_taller.Adaptadores.AdaptadorVehiculos;
import com.juanj.prueba_taller.Entidades.Cliente;
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

import java.util.ArrayList;

public class ServicioDetalle extends AppCompatActivity {
    EditText matricula, tipoServicio, sistema, fecha_inicio,
            fecha_fin, presupuesto, pagado, nombreCliente, movil, estado, descripcion;

    Spinner spSistema, spTipo, spEstado;

    FloatingActionButton fab;

    Servicio servicio;
    int id;

    final String [] SISTEMAS={"1-dirección", "2-transmisión","3-freno","4-neumáticos", "5-chapa y pintura",
            "6-climatización", "7-interior", "8-lunas","9-mixto"};
    final String [] TIPOS={"itv","avería","mantenimiento"};
    final String [] ESTADOS={"pendiente", "iniciado","finalizado","recogido"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_detalle);
        matricula=findViewById(R.id.matricula_coche);
        tipoServicio=findViewById(R.id.tipo);
        sistema=findViewById(R.id.sistema);
        fecha_inicio=findViewById(R.id.fecha_inicio);
        fecha_fin=findViewById(R.id.fecha_fin);
        presupuesto=findViewById(R.id.presupuesto);
        pagado=findViewById(R.id.pagado);
        nombreCliente=findViewById(R.id.nombre_cliente);
        movil=findViewById(R.id.movil_cliente);
        estado=findViewById(R.id.estado);
        descripcion=findViewById(R.id.descripcion);
        spSistema=findViewById(R.id.spinnerSistema);
        spSistema.setVisibility(View.INVISIBLE);
        spTipo=findViewById(R.id.spinnerTipo);
        spTipo.setVisibility(View.INVISIBLE);
        spEstado=findViewById(R.id.spinnerEstado);
        spEstado.setVisibility(View.INVISIBLE);

        servicio=(Servicio) getIntent().getSerializableExtra("id1");

        id =servicio.get_id();
        //Toast.makeText(getApplicationContext(),id+"",Toast.LENGTH_SHORT).show();

        matricula.setText(servicio.getMatricula());
        tipoServicio.setText(servicio.getTipo());
        sistema.setText(servicio.getTipo_sistema());
        fecha_inicio.setText(servicio.getFecha_inicio());
        fecha_fin.setText(servicio.getFecha_fin());
        presupuesto.setText(servicio.getPresupuesto());
        pagado.setText(servicio.getPagado());
        nombreCliente.setText(servicio.getNombre_cliente());
        movil.setText(servicio.getMovil());
        estado.setText(servicio.getEstado());
        descripcion.setText(servicio.getDescripcion());

        matricula.setEnabled(false);
        tipoServicio.setEnabled(false);
        sistema.setEnabled(false);
        fecha_inicio.setEnabled(false);
        fecha_fin.setEnabled(false);
        presupuesto.setEnabled(false);
        pagado.setEnabled(false);
        nombreCliente.setEnabled(false);
        movil.setEnabled(false);
        estado.setEnabled(false);
        descripcion.setEnabled(false);

        fab =  findViewById(R.id.fab);
        fab.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                new TareaWSActualizar().execute();
                startActivity(new Intent(ServicioDetalle.this, ServicioActivity.class));
                finish();

            }
        });

        ArrayAdapter<String> adapterSistemas= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SISTEMAS);
        spSistema.setAdapter(adapterSistemas);
        spSistema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        sistema.setText("1");
                        break;
                    case 1:
                        sistema.setText("2");
                        break;
                    case 2:
                        sistema.setText("3");
                        break;
                    case 3:
                        sistema.setText("4");
                        break;
                    case 4:
                        sistema.setText("5");
                        break;
                    case 5:
                        sistema.setText("6");
                        break;
                    case 6:
                        sistema.setText("7");
                        break;
                    case 7:
                        sistema.setText("8");
                        break;
                    case 8:
                        sistema.setText("9");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterTipos= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TIPOS);
        spTipo.setAdapter(adapterTipos);
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        tipoServicio.setText("");
                        tipoServicio.setText("itv");
                        break;
                    case 1:
                        tipoServicio.setText("");
                        tipoServicio.setText("averia");
                        break;
                    case 2:
                        tipoServicio.setText("");
                        tipoServicio.setText("mantenimiento");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterEstados= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ESTADOS);
        spEstado.setAdapter(adapterEstados);
        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        estado.setText("");
                        estado.setText("pendiente");
                        break;
                    case 1:
                        estado.setText("");
                        estado.setText("iniciado");
                        break;
                    case 2:
                        estado.setText("");
                        estado.setText("finalizado");
                        break;
                    case 3:
                        estado.setText("");
                        estado.setText("recogido");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            case R.id.eliminar:
                AlertDialog.Builder alert = new AlertDialog.Builder(ServicioDetalle.this);

                alert.setTitle("Eliminar Servicio");
                alert.setMessage("¿Seguro que desea eliminar este servicio?\nLos cambios no se podrán revertir.");
                alert.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TareaWSEliminar tareaWSEliminar=new TareaWSEliminar();
                        tareaWSEliminar.execute();
                        Toast.makeText(getApplicationContext(),"Servicio eliminado",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ServicioDetalle.this, ServicioActivity.class));
                        finish();
                    }
                });
                alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Servicio no eliminado",Toast.LENGTH_SHORT).show();
                    }
                });

                alert.create().show();



                break;
            case R.id.modificar:
                spSistema.setVisibility(View.VISIBLE);
                spEstado.setVisibility(View.VISIBLE);
                spTipo.setVisibility(View.VISIBLE);
                descripcion.setEnabled(true);
                descripcion.setBackgroundColor(Color.YELLOW);
                descripcion.setTextColor(Color.GRAY);
                estado.setEnabled(true);
                estado.setBackgroundColor(Color.YELLOW);
                estado.setTextColor(Color.GRAY);
                tipoServicio.setEnabled(true);
                tipoServicio.setBackgroundColor(Color.YELLOW);
                tipoServicio.setTextColor(Color.GRAY);
                presupuesto.setEnabled(true);
                presupuesto.setBackgroundColor(Color.YELLOW);
                presupuesto.setTextColor(Color.GRAY);
                fecha_inicio.setEnabled(true);
                fecha_inicio.setBackgroundColor(Color.YELLOW);
                fecha_inicio.setTextColor(Color.GRAY);
                fecha_fin.setEnabled(true);
                fecha_fin.setBackgroundColor(Color.YELLOW);
                fecha_fin.setTextColor(Color.GRAY);
                pagado.setEnabled(true);
                pagado.setBackgroundColor(Color.YELLOW);
                pagado.setTextColor(Color.GRAY);
                sistema.setEnabled(true);
                sistema.setBackgroundColor(Color.YELLOW);
                sistema.setTextColor(Color.GRAY);


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


            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/servicio/"+id);
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("tipo", tipoServicio.getText().toString());
                dato.put("presupuesto", presupuesto.getText().toString());
                dato.put("fecha_inicio", fecha_inicio.getText().toString());
                dato.put("fecha_fin", fecha_fin.getText().toString());
                dato.put("pagado",pagado.getText().toString());
                dato.put("descripcion",descripcion.getText().toString());
                dato.put("sistema",sistema.getText().toString());
                dato.put("estado",estado.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());

                put.setEntity(entity);
                System.out.println(entity.toString());
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
                Toast.makeText(getApplicationContext(), "Servicio modificado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de eliminación en segundo plano
    private class TareaWSEliminar extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpDelete del =
                    new HttpDelete("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/servicio/"+servicio.get_id());

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
                Toast.makeText(getApplicationContext(), "Servicio eliminado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
