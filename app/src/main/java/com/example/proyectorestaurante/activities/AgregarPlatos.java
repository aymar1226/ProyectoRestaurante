package com.example.proyectorestaurante.activities;

import androidx.annotation.Nullable;
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
import com.example.proyectorestaurante.utils.ImageUploader;
import com.example.proyectorestaurante.R;
import com.example.proyectorestaurante.utils.SessionManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgregarPlatos extends AppCompatActivity {
    private SessionManager sessionManager;
    EditText nombre,descripcion,precio_plato;
    Button btninsert,selectImagenButton;
    Spinner spinnerPlato;

    private StorageReference storageReference;
    ImageUploader imageUploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_platos);
        nombre = findViewById(R.id.txtNombre);
        descripcion = findViewById(R.id.txtDescripcion);
        precio_plato = findViewById(R.id.precio);
        btninsert = findViewById(R.id.btnAdd);
        spinnerPlato = findViewById(R.id.spinner_categoria);
        selectImagenButton = findViewById(R.id.btn_select_image);


        // Obtén la referencia a Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        //Instancia de la clase para subir imagenes
        imageUploader = new ImageUploader(AgregarPlatos.this, storageReference);


        selectImagenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUploader.seleccionarImagen();
            }
        });


        Connection connection = ConexionDB.obtenerConexion();

        //Obtener Categorias
        List<String> categoria;
        categoria = obtenerCategorias(connection);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoria);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlato.setAdapter(adapter);
        sessionManager = new SessionManager(getApplicationContext());

        btninsert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String txt_nombre = nombre.getText().toString();
                String txt_descripcion = descripcion.getText().toString();
                double precio= Double.parseDouble(precio_plato.getText().toString());
                String txt_categoria = (String) spinnerPlato.getSelectedItem();
                Connection connection = ConexionDB.obtenerConexion();

                String imagen_name=imageUploader.getImageName();
                if(imagen_name==null) {
                    imagen_name = "predet.jpg";
                }
                try {
                    String categoriaquery="SELECT id_categoria FROM categoria where nombre LIKE '%"+txt_categoria+"%'";
                    Statement st = connection.createStatement();
                    ResultSet resultSet = st.executeQuery(categoriaquery);
                    String idUsuario = sessionManager.obtenerId();
                    String idcategoria="";
                    if (resultSet.next()) {
                        idcategoria = resultSet.getString("id_categoria");
                    }

                    if (connection != null) {
                        String sqlinsert="INSERT INTO plato (nombre, descripcion,precio,id_categoria,imagen,id_usuario)" +
                                "VALUES ('"+txt_nombre+"', '"+txt_descripcion+"','"+precio+"','"+idcategoria+"','"+imagen_name+"','"+idUsuario+"')";
                        int rowsAffected = st.executeUpdate(sqlinsert);
                        if (rowsAffected > 0) {
                            Toast.makeText(getApplicationContext(), "Plato agregado exitosamente", Toast.LENGTH_SHORT).show();
                            gestionarImagen();

                        } else {
                            Toast.makeText(getApplicationContext(), "No se pudo agregar el plato", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<String> obtenerCategorias(Connection connection) {


        List<String> listaCategorias = new ArrayList<>();

        try {
            String query = "SELECT nombre FROM categoria";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String categoria = resultSet.getString("nombre");
                listaCategorias.add(categoria);
            }
            statement.close();
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCategorias;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pasa los resultados de la actividad a la instancia de ImageUploader
        imageUploader.onActivityResult(requestCode, resultCode, data);
    }

    public void gestionarImagen(){
        //Subir imagen
        imageUploader.subirImagen(new Runnable() {
            @Override
            public void run() {
                //Te redirige al crud plato cuando acabe de subir la imagen
                Intent intent = new Intent(AgregarPlatos.this, Crud_Platos.class);
                startActivity(intent);
            }
        });
    }


}