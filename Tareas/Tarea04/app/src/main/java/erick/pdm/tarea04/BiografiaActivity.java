package erick.pdm.tarea04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class BiografiaActivity extends AppCompatActivity {

    private StringBuilder listaDeComentarios = new StringBuilder();
    private SharedPreferences sharedPreferences;
    private String prefKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biografia);

        ImageView imgDetalle = findViewById(R.id.img_detalle);
        TextView txtNombreDetalle = findViewById(R.id.txt_nombre_detalle);
        TextView txtBiografia = findViewById(R.id.txt_biografia);
        TextInputEditText editNombre = findViewById(R.id.edit_nombre);
        TextInputEditText editComentario = findViewById(R.id.edit_comentario);
        Button btnPublicar = findViewById(R.id.btn_publicar);
        CheckBox checkBox = findViewById(R.id.checkBox);
        TextView txtListaComentarios = findViewById(R.id.txt_lista_comentarios);

        // Se usa el Intent
        String nombre = getIntent().getStringExtra("nombre");
        String biografia = getIntent().getStringExtra("biografia");
        int imagenResId = getIntent().getIntExtra("imagen", R.drawable.master_chief);

        txtNombreDetalle.setText(nombre);
        txtBiografia.setText(biografia);
        imgDetalle.setImageResource(imagenResId);

        // Para guardar comentarios
        sharedPreferences = getSharedPreferences("ComentariosPrefs", MODE_PRIVATE);
        prefKey = "comentarios_" + (nombre != null ? nombre : "default");

        String guardados = sharedPreferences.getString(prefKey, "");
        if (!guardados.isEmpty()) {
            listaDeComentarios.append(guardados);
            txtListaComentarios.setText(listaDeComentarios.toString());
        }

        btnPublicar.setOnClickListener(v -> {
            String usuario = editNombre.getText().toString().trim();
            String comentario = editComentario.getText().toString().trim();

            if (!usuario.isEmpty() && !comentario.isEmpty() && checkBox.isChecked()) {

                String nuevoComentario = usuario + ": " + comentario;

                if (listaDeComentarios.length() == 0) {
                    listaDeComentarios.append(nuevoComentario);
                } else {
                    listaDeComentarios.insert(0, nuevoComentario + "\n\n");
                }

                sharedPreferences.edit().putString(prefKey, listaDeComentarios.toString()).apply();

                // Actualizar la interfaz
                txtListaComentarios.setText(listaDeComentarios.toString());

                // Limpiar campos para el siguiente comentario
                editNombre.setText("");
                editComentario.setText("");
                checkBox.setChecked(false);
            }
        });
    }
}
