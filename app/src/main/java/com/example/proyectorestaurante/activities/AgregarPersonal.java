package com.example.proyectorestaurante.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectorestaurante.utils.ConexionDB;
import com.example.proyectorestaurante.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgregarPersonal extends AppCompatActivity {

    //Se declaran las variables
    //campos de entrada de texto
    private EditText nombre, apellido, cargo, direccion, telefono, dni;
    //para el boton
    private Button btninsert;
    //para la lista desplegable
    private Spinner spinner_personal;

    //se realiza la configuración inicial de la actividad cuando se crea
    //Se asocian las variables a los elementos de la interfaz a través de sus identificadores(R.id)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_personal);
        nombre = findViewById(R.id.txtNombre);
        apellido = findViewById(R.id.txtApellido);
        spinner_personal = findViewById(R.id.spinner_personal);
        direccion = findViewById(R.id.txtDireccion);
        telefono = findViewById(R.id.txtTelefono);
        dni = findViewById(R.id.txtdni);
        btninsert = findViewById(R.id.btnAdd);

        spinner_personal.setPrompt("cargo");

        //Se obtiene una conexión a la base de datos utilizando el método
        Connection connection = ConexionDB.obtenerConexion();

        //Cargar cargos en spinner a través del método obtenerCargos():
        List<String> cargo;
        cargo = obtenerCargos(connection);
        //Se crea un ArrayAdapter para el Spinner,
        //utilizando la lista de nombres de cargos como fuente de datos,
        //y se establece este adaptador en el Spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cargo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_personal.setAdapter(adapter);

        //Crear personal, que se activa cuando el usuario hace clic en el botón "Agregar":
        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se obtienen los valores ingresados por el usuario en los campos de texto y en el Spinner.
                String txt_nombre = nombre.getText().toString();
                String txt_apellido = apellido.getText().toString();
                String txt_direccion = direccion.getText().toString();
                String txt_cargo = (String) spinner_personal.getSelectedItem();
                String txt_telefono = telefono.getText().toString();
                String txt_dni = dni.getText().toString();
                Connection connection = ConexionDB.obtenerConexion();
                String imagen_name = "predet.jpg";
                try {
                    //Obtener el cargo del sepinner mediante una consulta en SQL
                    String cargoquery="SELECT id_cargo FROM cargo where nombre_cargo LIKE '%"+txt_cargo+"%'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(cargoquery);
                    String idcargo="";
                    if (resultSet.next()) {
                         idcargo = resultSet.getString("id_cargo");
                    }
                    //Crear un nuevo personal de la base de datos con los datos proporcionados
                    // por el usuario, incluido el id_cargo obtenido previamente.
                    if (connection != null) {
                        String sqlinsert = "INSERT INTO personal (nombre, apellido, id_cargo, direccion, telefono,dni)" +
                                "VALUES ('" + txt_nombre + "', '" + txt_apellido + "','" + idcargo + "','" + txt_direccion + "','" + txt_telefono + "','" + txt_dni + "')";
                        int rowsAffected = st.executeUpdate(sqlinsert);
                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Personal agregado exitosamente", Toast.LENGTH_SHORT).show();
                            //Redirige al crud
                            //Si la inserción es exitosa (si se afectan filas en la base de datos),
                            // se muestra un mensaje de éxito y se redirige al usuario a otra actividad llamada Crud_Personal.
                            Intent intent = new Intent(AgregarPersonal.this, Crud_Personal.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "No se pudo agregar el personal", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //El método obtenerCargos
    // se utiliza para obtener una lista de nombres de cargos desde la base de datos:
    public List<String> obtenerCargos(Connection connection) {

        //Se realiza una consulta SQL a la tabla cargo para obtener los nombres de los cargos.
        List<String> listaCargos = new ArrayList<>();

        try {
            String query = "SELECT nombre_cargo FROM cargo";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String cargo = resultSet.getString("nombre_cargo");
                listaCargos.add(cargo);
            }
            statement.close();
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
            return listaCargos;

    }

}