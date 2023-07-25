package com.example.proyectorestaurante.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.proyectorestaurante.Clases.Usuario;
import com.example.proyectorestaurante.R;

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ImageView imageViewBack = findViewById(R.id.imageViewBack);
        TextView textViewPresionar = findViewById(R.id.textViewPresionar);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textViewPresionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para ir a otra página XML (otra actividad)
                Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

    private void actualizarInterfaz(Usuario usuario) {
        if (usuario != null) {
            TextView textViewNombre = findViewById(R.id.textViewNombre);
            TextView textViewApellido = findViewById(R.id.textViewApellido);
            TextView textViewCorreo = findViewById(R.id.textViewCorreo);

            // Actualizar los TextView con los datos del usuario
            textViewNombre.setText(usuario.getNombre());
            textViewApellido.setText(usuario.getApellido());
            textViewCorreo.setText(usuario.getCorreo());
        } else {
            // Mostrar un mensaje de error o hacer otra acción si no se encuentra el usuario.
        }
    }
    public void irAInicio(View view) {
        Intent intent = new Intent(PerfilActivity.this, PrincipalActivity.class);
        startActivity(intent);
    }
    public void irOtroActivity(View view) {
        Intent intent = new Intent(PerfilActivity.this, PerfilActivity.class);
        startActivity(intent);
    }
    public void irAPlatos(View view) {
        Intent intent = new Intent(PerfilActivity.this, Crud_Platos.class);
        startActivity(intent);
    }
    public void irAPersonal(View view) {
        Intent intent = new Intent(PerfilActivity.this, Crud_Personal.class);
        startActivity(intent);
    }
    public void irAQR(View view) {
        Intent intent = new Intent(PerfilActivity.this, QRActivity.class);
        startActivity(intent);
    }
}
