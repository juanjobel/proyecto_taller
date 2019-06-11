package com.juanj.prueba_taller.Actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.juanj.prueba_taller.Entidades.Trabajador;
import com.juanj.prueba_taller.Entidades.Vehiculo;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.String.valueOf;

public class AddServicioActivity extends AppCompatActivity {
    EditText descripcion, estado , fechaInicio, fechaFin, sistema, foto, tipo, presupuesto, vehiculo, trabajador;
    Switch pagado;
    Spinner spVehiculo, spSistema, spTipo, spTrabajador, spEstado;

    ArrayList<Vehiculo> listaVehiculos;
    ArrayList<String> vehiculos;
    ArrayList<Trabajador> listaTrabajadores;
    ArrayList<String> trabajadores;

    Calendar c;
    DatePickerDialog dpdi, dpd;
    int dia, mes, anio;
    final String [] SISTEMAS={"Seleccione un sistema","1-dirección", "2-transmisión","3-freno","4-neumáticos", "5-chapa y pintura",
                              "6-climatización", "7-interior", "8-lunas","9-mixto"};
    final String [] TIPOS={"Seleccione un tipo de Servicio","itv","avería","mantenimiento"};
    final String [] ESTADOS={"Seleccione un estado","pendiente", "iniciado","finalizado","recogido"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_servicio);


        descripcion=findViewById(R.id.descripcionAdd);
        estado=findViewById(R.id.estadoAdd);
        fechaInicio=findViewById(R.id.fecha_inicioAdd);
        fechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c= Calendar.getInstance();
                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                anio= c.get(Calendar.YEAR);

                dpdi=new DatePickerDialog(AddServicioActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechaInicio.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }

                },dia, mes, anio);
                dpdi.show();
            }
        });
        fechaFin=findViewById(R.id.fecha_finAdd);
        fechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c= Calendar.getInstance();

                dia=c.get(Calendar.DAY_OF_MONTH);
                mes=c.get(Calendar.MONTH);
                anio= c.get(Calendar.YEAR);

                dpd=new DatePickerDialog(AddServicioActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fechaFin.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }

                },dia, mes, anio);
                dpd.show();
            }
        });
        sistema=findViewById(R.id.sistemaAdd);
        foto=findViewById(R.id.foto_servicioAdd);
        tipo=findViewById(R.id.tipoAdd);
        presupuesto=findViewById(R.id.presupuestoAdd);
        pagado=findViewById(R.id.pagadoAdd);
        vehiculo=findViewById(R.id.id_vehiculoAdd);
        trabajador=findViewById(R.id.id_trabajadorAdd);
        spVehiculo=findViewById(R.id.spinnerVehiculo);
        spSistema=findViewById(R.id.spinnerSistema);
        spTipo=findViewById(R.id.spinnerTipo);
        spTrabajador=findViewById(R.id.spinnerTrabajador);
        spEstado=findViewById(R.id.spinnerEstado);


        TareaWSListarVehiculos tareaWSListarVehiculos = new TareaWSListarVehiculos();
        tareaWSListarVehiculos.execute();
        listaVehiculos = new ArrayList<>();
        vehiculos =new ArrayList<>();
        vehiculos.add("Seleccione un vehiculo");
        ArrayAdapter<String> adapterVehiculos= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehiculos);
        spVehiculo.setAdapter(adapterVehiculos);
        spVehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int aparicion=vehiculos.get(position).indexOf(" ");
                vehiculo.setText(vehiculos.get(position).substring(0,aparicion));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TareaWSListarTrabajadores tareaWSListarTrabajadores = new TareaWSListarTrabajadores();
        tareaWSListarTrabajadores.execute();
        listaTrabajadores = new ArrayList<>();
        trabajadores =new ArrayList<>();
        trabajadores.add("Seleccione un trabajador");
        ArrayAdapter<String> adapterTrabajadores= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trabajadores);
        spTrabajador.setAdapter(adapterTrabajadores);
        spTrabajador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int aparicion=trabajadores.get(position).indexOf(" ");
                trabajador.setText(trabajadores.get(position).substring(0,aparicion));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterSistemas= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SISTEMAS);
        spSistema.setAdapter(adapterSistemas);
        spSistema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        sistema.setText("1");
                        break;
                    case 2:
                        sistema.setText("2");
                        break;
                    case 3:
                        sistema.setText("3");
                        break;
                    case 4:
                        sistema.setText("4");
                        break;
                    case 5:
                        sistema.setText("5");
                        break;
                    case 6:
                        sistema.setText("6");
                        break;
                    case 7:
                        sistema.setText("7");
                        break;
                    case 8:
                        sistema.setText("8");
                        break;
                    case 9:
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
                    case 1:
                        tipo.setText("");
                        tipo.setText("itv");
                        break;
                    case 2:
                        tipo.setText("");
                        tipo.setText("averia");
                        break;
                    case 3:
                        tipo.setText("");
                        tipo.setText("mantenimiento");
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
                    case 1:
                        estado.setText("");
                        estado.setText("pendiente");
                        break;
                    case 2:
                        estado.setText("");
                        estado.setText("iniciado");
                        break;
                    case 3:
                        estado.setText("");
                        estado.setText("finalizado");
                        break;
                    case 4:
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
                AlertDialog.Builder alert = new AlertDialog.Builder(AddServicioActivity.this);

                alert.setTitle("Servicio cuenta con un elemento nuevo.");

                alert.create().show();

                startActivity(new Intent(AddServicioActivity.this, ServicioActivity.class));
                finish();



                break;


        }

        return super.onOptionsItemSelected(item);
    }




    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListarVehiculos extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddServicioActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculos");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Vehiculo vehiculo = null;

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    vehiculo = new Vehiculo();
                    vehiculo.setId(obj.getInt("_id"));
                    vehiculo.setMatricula(obj.getString("matricula"));
                    vehiculo.setMovil(obj.getString("movil"));
                    vehiculo.setNombre_cliente(obj.getString("nombre_cliente"));
                    vehiculo.setApellido1_cliente(obj.getString("apellido1_cliente"));
                    vehiculo.setApellido2_cliente(obj.getString("apellido2_cliente"));

                    listaVehiculos.add(vehiculo);
                    vehiculos.add(vehiculo.getId()+" - "+vehiculo.getMatricula()+" - "+vehiculo.getNombre_cliente()+" "+
                            vehiculo.getApellido1_cliente()+" "+
                            vehiculo.getApellido2_cliente()+" "+vehiculo.getMovil());

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

    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListarTrabajadores extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;




        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddServicioActivity.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }
        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    //  new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/trabajadores");
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/trabajadores");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                Trabajador trabajador=null;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    trabajador=new Trabajador();
                    trabajador.setId(obj.getInt("id"));
                    trabajador.setNombre(obj.getString("nombre_trabajador"));
                    trabajador.setApellido1(obj.getString("apellido1_trabajador"));
                    trabajador.setApellido2(obj.getString("apellido2_trabajador"));


                    listaTrabajadores.add(trabajador);
                    trabajadores.add(trabajador.getId()+" - "+trabajador.getNombre()+
                            " "+trabajador.getApellido1()+" "+trabajador.getApellido2());
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

            }
        }


    }

    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String...params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/servicio");
            post.setHeader("content-type", "application/json");

            try
            {

                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("descripcion",descripcion.getText().toString());
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                String inputDateStr=fechaInicio.getText().toString();
                Date date = inputFormat.parse(inputDateStr);
                String outputDateStr = outputFormat.format(date);
                dato.put("fecha_inicio",outputDateStr);

                inputDateStr=fechaFin.getText().toString();
                date = inputFormat.parse(inputDateStr);
                outputDateStr = outputFormat.format(date);
                dato.put("fecha_fin",outputDateStr);
                dato.put("foto",foto.getText().toString());
                dato.put("presupuesto",presupuesto.getText().toString());
                if(pagado.isChecked()){
                    dato.put("pagado","1");

                }else{
                    dato.put("pagado","0");

                }
                dato.put("id_vehiculo",vehiculo.getText().toString());
                dato.put("id_trabajador",trabajador.getText().toString());
                dato.put("tipo",tipo.getText().toString());
                dato.put("sistema",sistema.getText().toString());
                dato.put("estado",estado.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddServicioActivity.this);

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
                Toast.makeText(getApplicationContext(), "Servicio agregado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
