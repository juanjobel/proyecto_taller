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
import android.widget.EditText;
import android.widget.Toast;

import com.juanj.prueba_taller.Actividades.ChatActivity;
import com.juanj.prueba_taller.Actividades.ClienteActivity;
import com.juanj.prueba_taller.Adaptadores.AdaptadorVehiculos;
import com.juanj.prueba_taller.Entidades.Cliente;
import com.juanj.prueba_taller.Entidades.Vehiculo;
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

public class ClienteDetalle extends AppCompatActivity {
    EditText nombreCliente, apellido1cliente, apellido2cliente, direccionCliente,
            telefonoCliente, documentoCliente;

    FloatingActionButton fab,chat;

    ArrayList<Vehiculo> listaVehiculos;
    RecyclerView recyclerVehiculos;
    AdaptadorVehiculos adapter;

    Cliente cliente;
    int id;
    String movil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_detalle);



        nombreCliente=findViewById(R.id.nombreCliente);
        apellido1cliente=findViewById(R.id.apellido1Cliente);
        apellido2cliente=findViewById(R.id.apellido2Cliente);
        direccionCliente=findViewById(R.id.direccionCliente);
        documentoCliente=findViewById(R.id.documentoCliente);
        telefonoCliente=findViewById(R.id.telefonoCliente);

        movil=getIntent().getStringExtra("id1");

        cliente=(Cliente)getIntent().getSerializableExtra("cliente");
        id=cliente.getId();
        System.out.println(id);
        construirRecycler();
        nombreCliente.setText(cliente.getNombre());
        cliente.setNombre(cliente.getNombre());
        apellido1cliente.setText(cliente.getApellido1());
        apellido2cliente.setText(cliente.getApellido2());
        direccionCliente.setText(cliente.getDireccion());
        documentoCliente.setText(cliente.getDocumento());
        telefonoCliente.setText(cliente.getMovil());

        nombreCliente.setEnabled(false);
        apellido2cliente.setEnabled(false);
        apellido1cliente.setEnabled(false);
        direccionCliente.setEnabled(false);
        documentoCliente.setEnabled(false);
        telefonoCliente.setEnabled(false);


        fab =  findViewById(R.id.fab);
        fab.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(validarMovil() && validarDocumento()){
                new TareaWSActualizar().execute();
                startActivity(new Intent(ClienteDetalle.this, ClienteActivity.class));
                finish();
            }else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ClienteDetalle.this);

                alert.setTitle("Error");
                alert.setMessage("Ha introducido un número de móvil incorrecto o el documento no es válido");

                alert.create().show();
            }


            }
        });

        chat=findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClienteDetalle.this,ChatActivity.class));
            }
        });

    }

    private boolean validarMovil() {
        if((telefonoCliente.getText().toString().length()==9) &&
                (telefonoCliente.getText().toString().charAt(0)=='6' ||
                 telefonoCliente.getText().toString().charAt(0)=='7')){
            return true;
        }else{
            return false;
        }
    }

    private boolean validarDocumento(){
        String letraMayuscula = "";

        if(documentoCliente.length() != 9 || Character.isLetter(this.documentoCliente.getText().toString().charAt(8)) == false ) {
            return false;
        }

        letraMayuscula = (this.documentoCliente.getText().toString().substring(8)).toUpperCase();


        if(soloNumeros() == true && letraDNI().equals(letraMayuscula)) {
            return true;
        }
        else {
            return false;
        }

    }
    private boolean soloNumeros() {

        int i, j = 0;
        String numero = "";
        String miDNI = "";
        String[] unoNueve = {"0","1","2","3","4","5","6","7","8","9"};

        for(i = 0; i < this.documentoCliente.getText().length() - 1; i++) {
            numero = this.documentoCliente.getText().toString().substring(i, i+1);

            for(j = 0; j < unoNueve.length; j++) {
                if(numero.equals(unoNueve[j])) {
                    miDNI += unoNueve[j];
                }
            }
        }

        if(miDNI.length() != 8) {
            return false;
        }
        else {
            return true;
        }
    }
    private String letraDNI() {

        int miDNI = Integer.parseInt(this.documentoCliente.getText().toString().substring(0,8));
        int resto = 0;
        String miLetra = "";
        String[] asignacionLetra = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};

        resto = miDNI % 23;

        miLetra = asignacionLetra[resto];

        return miLetra;
    }
    private void construirRecycler() {
        listaVehiculos = new ArrayList<>();
        recyclerVehiculos = findViewById(R.id.recyclerVehiculos);

        recyclerVehiculos.setLayoutManager(new LinearLayoutManager(this));

        llenarLista();

        adapter = new AdaptadorVehiculos(listaVehiculos);

        recyclerVehiculos.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Vehiculo vehiculo = listaVehiculos.get(recyclerVehiculos.getChildAdapterPosition(v));
                String matricula=vehiculo.getMatricula();
                bundle.putSerializable("coche", vehiculo);

                bundle.putString("mat",matricula);
                //Toast.makeText(getApplicationContext(), matricula + "", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ClienteDetalle.this, VehiculoDetalle.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();

            }
        });

        recyclerVehiculos.setAdapter(adapter);

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
                AlertDialog.Builder alert = new AlertDialog.Builder(ClienteDetalle.this);

                alert.setTitle("Eliminar Cliente");
                alert.setMessage("¿Seguro que desea eliminar este cliente?\nDebe eliminar antes los vehículos asociados." +
                        "\nLos cambios no se podrán revertir.");
                alert.setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TareaWSEliminar tareaWSEliminar=new TareaWSEliminar();
                        tareaWSEliminar.execute();
                        Toast.makeText(getApplicationContext(),"Cliente eliminado",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ClienteDetalle.this, ClienteActivity.class));
                        finish();
                    }
                });
                alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),"Cliente no eliminado",Toast.LENGTH_SHORT).show();
                    }
                });

                alert.create().show();



                break;
            case R.id.modificar:
                nombreCliente.setEnabled(true);
                nombreCliente.setBackgroundColor(Color.YELLOW);
                nombreCliente.setTextColor(Color.GRAY);
                apellido2cliente.setEnabled(true);
                apellido2cliente.setBackgroundColor(Color.YELLOW);
                apellido2cliente.setTextColor(Color.GRAY);
                apellido1cliente.setEnabled(true);
                apellido1cliente.setBackgroundColor(Color.YELLOW);
                apellido1cliente.setTextColor(Color.GRAY);
                direccionCliente.setEnabled(true);
                direccionCliente.setBackgroundColor(Color.YELLOW);
                direccionCliente.setTextColor(Color.GRAY);
                documentoCliente.setEnabled(true);
                documentoCliente.setBackgroundColor(Color.YELLOW);
                documentoCliente.setTextColor(Color.GRAY);
                telefonoCliente.setEnabled(true);
                telefonoCliente.setBackgroundColor(Color.YELLOW);
                telefonoCliente.setTextColor(Color.GRAY);

                fab.setEnabled(true);
                fab.setRippleColor(Color.YELLOW);

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //Tarea Asíncrona para llamar al WS de listado en segundo plano
    private class TareaWSListar extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ClienteDetalle.this,
                    "Realizando cambios en la base de datos",
                    "Espere un momento");
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    //   new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculos");
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/vehiculo/movil/"+movil);

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());


                JSONArray respJSON = new JSONArray(respStr);

                Vehiculo vehiculo = null;
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    vehiculo = new Vehiculo();
                    // vehiculo.setId(obj.getInt("_id"));
                    vehiculo.setMatricula(obj.getString("matricula"));
                    vehiculo.setMarca_vehiculo(obj.getString("marca_vehiculo"));
                    vehiculo.setModelo_vehiculo(obj.getString("modelo_vehiculo"));
                    vehiculo.setNum_bastidor(obj.getString("num_bastidor"));
                    vehiculo.setNombre_cliente(obj.getString("nombre_cliente"));
                    //pieza.setFoto_pieza(obj.getInt("foto_pieza"));
                    vehiculo.setMovil(obj.getString("movil"));


                    listaVehiculos.add(vehiculo);
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
                adapter.setListaVehiculos(listaVehiculos);
                adapter.notifyDataSetChanged();
            }
        }

    }

    //Tarea Asíncrona para llamar al WS de actualización en segundo plano
    private class TareaWSActualizar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpPut put = new HttpPut("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/cliente/"+id);
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_cliente", nombreCliente.getText().toString());
                dato.put("apellido1_cliente", apellido1cliente.getText().toString());
                dato.put("apellido2_cliente", apellido2cliente.getText().toString());
                dato.put("movil", telefonoCliente.getText().toString());
                dato.put("documento",documentoCliente.getText().toString());
                dato.put("direccion",direccionCliente.getText().toString());


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
                Toast.makeText(getApplicationContext(), "Cliente modificado correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Tarea Asíncrona para llamar al WS de eliminación en segundo plano
    private class TareaWSEliminar extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpDelete del =
                    new HttpDelete("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/cliente/"+getIntent().getStringExtra("id1"));

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
                Toast.makeText(getApplicationContext(), "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
