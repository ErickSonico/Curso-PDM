package erick.pdm.proyecto;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/*
 * Esta actividad es para mostrar la información de la app cuando se selecciona esa opción en el menu de 3 puntos
 */
public class InformacionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Información");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
