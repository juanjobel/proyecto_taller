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

public class AddClienteActivity extends AppCompatActivity {

    //Atributos de la clase
    EditText nombre, apellido1, apellido2, documento, movil, direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);

        nombre=findViewById(R.id.nombreClienteAdd);
        apellido1=findViewById(R.id.apellido1ClienteAdd);
        apellido2=findViewById(R.id.apellido2ClienteAdd);
        documento=findViewById(R.id.documentoAdd);
        movil=findViewById(R.id.movilAdd);
        direccion=findViewById(R.id.direccionAdd);

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

                if(validarMovil() && validarDocumento()){
                    TareaWSInsertar tareaWSInsertar=new TareaWSInsertar();
                    tareaWSInsertar.execute();
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddClienteActivity.this);

                    alert.setTitle("Cliente Insertado");
                    alert.setMessage("Ya cuenta con un elemento nuevo.");

                    alert.create().show();

                    startActivity(new Intent(AddClienteActivity.this, ClienteActivity.class));
                    finish();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(AddClienteActivity.this);

                    alert.setTitle("Error");
                    alert.setMessage("Ha introducido un número de móvil incorrecto");

                    alert.create().show();
                }

                break;


        }

        return super.onOptionsItemSelected(item);
    }

    //Método que comprueba que el móvil insertado es correcto
    private boolean validarMovil() {
        if((movil.getText().toString().length()==9) &&
                (movil.getText().toString().charAt(0)=='6' ||
                        movil.getText().toString().charAt(0)=='7')){
            return true;
        }else{
            return false;
        }
    }

    //Método que comprueba si el DNI es correcto
    private boolean validarDocumento(){
        String letraMayuscula = "";

        if(documento.length() != 9 || Character.isLetter(this.documento.getText().toString().charAt(8)) == false ) {
            return false;
        }

        letraMayuscula = (this.documento.getText().toString().substring(8)).toUpperCase();


        if(soloNumeros() == true && letraDNI().equals(letraMayuscula)) {
            return true;
        }
        else {
            return false;
        }

    }

    //Método que comprueba que los 8 primeros caracteres son números
    private boolean soloNumeros() {

        int i, j = 0;
        String numero = "";
        String miDNI = "";
        String[] unoNueve = {"0","1","2","3","4","5","6","7","8","9"};

        for(i = 0; i < this.documento.getText().length() - 1; i++) {
            numero = this.documento.getText().toString().substring(i, i+1);

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

    //Método que comprueba que la letra del DNI es correcta
    private String letraDNI() {

        int miDNI = Integer.parseInt(this.documento.getText().toString().substring(0,8));
        int resto = 0;
        String miLetra = "";
        String[] asignacionLetra = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};

        resto = miDNI % 23;

        miLetra = asignacionLetra[resto];

        return miLetra;
    }


    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String...params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("https://dam2.ieslamarisma.net/2019/juanjobeltran/taller_rest/cliente");
            post.setHeader("content-type", "application/json");

            try
            {

                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
                dato.put("nombre_cliente", nombre.getText().toString());
                dato.put("apellido1_cliente",apellido1.getText().toString());
                dato.put("apellido2_cliente",apellido2.getText().toString());
                dato.put("documento",documento.getText().toString());
                dato.put("movil",movil.getText().toString());
                dato.put("direccion",direccion.getText().toString());


                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                AlertDialog.Builder alert = new AlertDialog.Builder(AddClienteActivity.this);

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
                Toast.makeText(getApplicationContext(), "Cliente agregado correctamente", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
