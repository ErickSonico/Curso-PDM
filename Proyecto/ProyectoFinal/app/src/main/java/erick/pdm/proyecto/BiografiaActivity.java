package erick.pdm.proyecto;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class BiografiaActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String nombrePersonaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biografia);

        dbHelper = new DatabaseHelper(this);

        ImageView imgDetalle = findViewById(R.id.img_detalle);
        TextView txtNombreDetalle = findViewById(R.id.txt_nombre_detalle);
        TextView txtBiografia = findViewById(R.id.txt_biografia);
        TextInputEditText editNombre = findViewById(R.id.edit_nombre);
        TextInputEditText editComentario = findViewById(R.id.edit_comentario);
        Button btnPublicar = findViewById(R.id.btn_publicar);
        TextView txtListaComentarios = findViewById(R.id.txt_lista_comentarios);

        // Obtener datos del intent
        nombrePersonaje = getIntent().getStringExtra("nombre");
        String biografia = getIntent().getStringExtra("biografia");
        int imagenResId = getIntent().getIntExtra("imagen", R.drawable.master_chief);
        String imagenUri = getIntent().getStringExtra("imagenUri");

        txtNombreDetalle.setText(nombrePersonaje);
        txtBiografia.setText(biografia);

        // Mostrar imagen
        if (imagenUri != null && !imagenUri.isEmpty()) {
            try {
                imgDetalle.setImageURI(Uri.parse(imagenUri));
            } catch (Exception e) {
                imgDetalle.setImageResource(R.drawable.master_chief);
            }
        } else {
            imgDetalle.setImageResource(imagenResId != 0 ? imagenResId : R.drawable.master_chief);
        }

        // Cargar comentarios desde la bd
        loadComments(txtListaComentarios);

        btnPublicar.setOnClickListener(v -> {
            String usuario = editNombre.getText().toString().trim();
            String comentarioText = editComentario.getText().toString().trim();

            if (!usuario.isEmpty() && !comentarioText.isEmpty()) {
                // Guardar en la bd
                dbHelper.agregarComentario(nombrePersonaje, usuario, comentarioText);

                // Actualizar la interfaz
                loadComments(txtListaComentarios);

                // Limpiar campos para el siguiente comentario
                editNombre.setText("");
                editComentario.setText("");
            }
        });
    }

    private void loadComments(TextView txtListaComentarios) {
        List<String> comments = dbHelper.obtenerComentarios(nombrePersonaje);
        if (comments.isEmpty()) {
            txtListaComentarios.setText("Aún no hay comentarios.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String c : comments) {
                sb.append(c).append("\n\n");
            }
            txtListaComentarios.setText(sb.toString());
        }
    }
}
