package erick.pdm.proyecto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;


public class AgregarPersonajeActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ImageView imgPreview;
    private Uri selectedImageUri;
    private int editId = -1;

    private final ActivityResultLauncher<String[]> mGetContent = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imgPreview.setImageURI(uri);
                    try {
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_personaje);

        dbHelper = new DatabaseHelper(this);

        TextView txtTitulo = findViewById(R.id.titulo_agregar);
        imgPreview = findViewById(R.id.img_preview);
        Button btnSeleccionarImagen = findViewById(R.id.btn_seleccionar_imagen);
        TextInputEditText editNombre = findViewById(R.id.edit_nombre_personaje);
        TextInputEditText editBio = findViewById(R.id.edit_bio_personaje);
        Button btnGuardar = findViewById(R.id.btn_guardar_personaje);


        Intent intent = getIntent();
        if (intent.hasExtra("edit_id")) {
            editId = intent.getIntExtra("edit_id", -1);
            editNombre.setText(intent.getStringExtra("edit_nombre"));
            editBio.setText(intent.getStringExtra("edit_bio"));
            String uriStr = intent.getStringExtra("edit_uri");
            if (uriStr != null && !uriStr.isEmpty()) {
                selectedImageUri = Uri.parse(uriStr);
                imgPreview.setImageURI(selectedImageUri);
            }
            txtTitulo.setText("Editar Personaje");
            btnGuardar.setText("Actualizar Personaje");
        }

        btnSeleccionarImagen.setOnClickListener(v -> mGetContent.launch(new String[]{"image/*"}));

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String bio = editBio.getText().toString().trim();
            String uriString = (selectedImageUri != null) ? selectedImageUri.toString() : "";

            if (!nombre.isEmpty() && !bio.isEmpty()) {
                if (editId == -1) {
                    // Modo Agregar
                    int defaultRes = (selectedImageUri == null) ? R.drawable.master_chief : 0;
                    dbHelper.agregarPersonaje(nombre, bio, defaultRes, uriString);
                    Toast.makeText(this, "Personaje guardado", Toast.LENGTH_SHORT).show();
                } else {
                    // Modo Editar
                    dbHelper.actualizarPersonaje(editId, nombre, bio, uriString);
                    Toast.makeText(this, "Personaje actualizado", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
