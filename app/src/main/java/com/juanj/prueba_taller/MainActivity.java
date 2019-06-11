package com.juanj.prueba_taller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.juanj.prueba_taller.Actividades.AddClienteActivity;
import com.juanj.prueba_taller.Actividades.AddPiezaActivity;
import com.juanj.prueba_taller.Actividades.AddProveedorActivity;
import com.juanj.prueba_taller.Actividades.AddServicioActivity;
import com.juanj.prueba_taller.Actividades.AddTrabajadorActivity;
import com.juanj.prueba_taller.Actividades.AddVehiculoActivity;
import com.juanj.prueba_taller.Actividades.ClienteActivity;
import com.juanj.prueba_taller.Actividades.PiezaActivity;
import com.juanj.prueba_taller.Actividades.ProveedorActivity;
import com.juanj.prueba_taller.Actividades.ServicioActivity;
import com.juanj.prueba_taller.Actividades.TrabajadorActivity;
import com.juanj.prueba_taller.Actividades.VehiculoActivity;

public class MainActivity extends AppCompatActivity {

    Button btnCliente, btnVehiculo, btnTrabajador, btnServicio, btnPiezas, btnProveedor;
    private String mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Descomentar la siguiente linea para que funcione en movil fisico
      //  mAuth= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        btnCliente=findViewById(R.id.button);
        btnVehiculo=findViewById(R.id.button2);
        btnTrabajador=findViewById(R.id.button3);
        btnServicio=findViewById(R.id.button4);
        btnPiezas=findViewById(R.id.button5);
        btnProveedor=findViewById(R.id.button6);

        /*String telefono=mAuth.substring(3);

        Toast.makeText(MainActivity.this, telefono, Toast.LENGTH_SHORT).show();*/


        btnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, ClienteActivity.class));

            }
        });

        btnVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VehiculoActivity.class));

            }
        });

        btnTrabajador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TrabajadorActivity.class));

            }
        });

        btnServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicioActivity.class));

            }
        });

        btnPiezas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PiezaActivity.class));

            }
        });

        btnProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProveedorActivity.class));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemSelect = item.getItemId();

        switch (itemSelect) {
            case R.id.ic_add:

               String [] elementos = {"Cliente","Vehículo","Servicio","Pieza","Trabajador","Proveedor"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Elija un elemento")
                        .setItems(elementos, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which){
                                    case 0:
                                        startActivity(new Intent(MainActivity.this, AddClienteActivity.class));
                                    break;
                                    case 1:
                                        startActivity(new Intent(MainActivity.this, AddVehiculoActivity.class));
                                        break;
                                    case 2:
                                        startActivity(new Intent(MainActivity.this, AddServicioActivity.class));
                                        break;
                                    case 3:
                                        startActivity(new Intent(MainActivity.this, AddPiezaActivity.class));
                                        break;
                                    case 4:
                                        startActivity(new Intent(MainActivity.this, AddTrabajadorActivity.class));
                                        break;
                                    case 5:
                                        startActivity(new Intent(MainActivity.this, AddProveedorActivity.class));
                                        break;
                                }
                            }
                        });


                AlertDialog alertDialog=builder.create();
                alertDialog.show();

                break;

        }

        return super.onOptionsItemSelected(item);
    }
}
