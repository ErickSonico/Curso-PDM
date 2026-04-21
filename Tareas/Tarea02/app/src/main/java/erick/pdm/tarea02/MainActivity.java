package erick.pdm.tarea02;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Tarea02_Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personajes);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Abrir menú lateral
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Menú de tres puntitos
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                Log.d(TAG, "Buscar");
                return true;
            } else if (item.getItemId() == R.id.action_info) {
                Log.d(TAG, "Información de la app");
                return true;
            } else if (item.getItemId() == R.id.acerca_de) {
                Log.d(TAG, "Viva Halo");
                return true;
            }
            return false;
        });

        // Opciones de personajes
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_add) {
                Log.d(TAG, "Agregar personaje");
            } else if (id == R.id.nav_delete) {
                Log.d(TAG, "Eliminar personaje");
            } else if (id == R.id.nav_edit) {
                Log.d(TAG, "Editar personaje");
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}