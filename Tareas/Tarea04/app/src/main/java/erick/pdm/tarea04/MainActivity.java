package erick.pdm.tarea04;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Tarea04_Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personajes);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Para los iconos del menu de tres puntos que me faltó de la tarea 3
        Menu menu = toolbar.getMenu();
        if (menu != null && menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                java.lang.reflect.Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (Exception e) {
                Log.e(TAG, "No se pudieron mostrar los íconos", e);
            }
        }

        // Abrir menú lateral
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Menú de tres puntitos (Overflow)
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

        // Botones para abrir las biografías con Intents
        findViewById(R.id.btn1).setOnClickListener(v -> abrirDetalle(
                getString(R.string.jefe_maestro),
                getString(R.string.bio_jefe_maestro),
                R.drawable.master_chief));

        findViewById(R.id.btn2).setOnClickListener(v -> abrirDetalle(
                getString(R.string.cortana),
                getString(R.string.bio_cortana),
                R.drawable.cortana));

        findViewById(R.id.btn3).setOnClickListener(v -> abrirDetalle(
                getString(R.string.thel_vadamee),
                getString(R.string.bio_thel_vadamee),
                R.drawable.thel_vadamee));

        findViewById(R.id.btn4).setOnClickListener(v -> abrirDetalle(
                getString(R.string.buck),
                getString(R.string.bio_buck),
                R.drawable.spartan_buck));

        findViewById(R.id.btn5).setOnClickListener(v -> abrirDetalle(
                getString(R.string.ur_didacta),
                getString(R.string.bio_ur_didacta),
                R.drawable.ur_didacta));

        // Navegación lateral
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

    /**
     * Lanza BiografiaActivity y usa el Intent para enviar los datos del personaje
     */
    private void abrirDetalle(String nombre, String biografia, int imagenResId) {
        Intent intent = new Intent(MainActivity.this, BiografiaActivity.class);
        intent.putExtra("nombre", nombre);
        intent.putExtra("biografia", biografia);
        intent.putExtra("imagen", imagenResId);
        startActivity(intent);
    }
}
