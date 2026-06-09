package erick.pdm.proyecto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private LinearLayout layoutDinamico;
    private boolean modoEdicion = false;
    private boolean modoEliminacion = false;
    private String queryActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personajes);

        dbHelper = new DatabaseHelper(this);
        layoutDinamico = findViewById(R.id.layout_dinamico_personajes);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        SearchView searchView = findViewById(R.id.searchView);
        NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollView);

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Menu de 3 puntos
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                if (nestedScrollView != null) {
                    nestedScrollView.smoothScrollTo(0, 0);
                }
                if (searchView != null) {
                    searchView.setIconified(false);
                    searchView.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT);
                    }
                }
                return true;
            } else if (item.getItemId() == R.id.action_info) {
                startActivity(new Intent(MainActivity.this, InformacionActivity.class));
                return true;
            } else if (item.getItemId() == R.id.acerca_de) {
                Toast.makeText(this, "Viva!", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        findViewById(R.id.fab_add).setOnClickListener(v -> {
            resetModos();
            startActivity(new Intent(MainActivity.this, AgregarPersonajeActivity.class));
        });

        // Lógica de la barra de búsqueda
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    queryActual = query;
                    cargarPersonajes();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    queryActual = newText;
                    cargarPersonajes();
                    return true;
                }
            });
        }

        cargarPersonajes();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            resetModos();
            if (id == R.id.nav_add) {
                startActivity(new Intent(MainActivity.this, AgregarPersonajeActivity.class));
            } else if (id == R.id.nav_edit) {
                modoEdicion = true;
                cargarPersonajes();
            } else if (id == R.id.nav_delete) {
                modoEliminacion = true;
                cargarPersonajes();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void resetModos() {
        modoEdicion = false;
        modoEliminacion = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPersonajes();
    }

    private void cargarPersonajes() {
        if (layoutDinamico == null) return;
        layoutDinamico.removeAllViews();
        
        List<Personaje> todosLosPersonajes = dbHelper.obtenerPersonajes();
        List<Personaje> personajesFiltrados = new ArrayList<>();

        // Filtrar los personajes con el texto de la búsqueda
        for (Personaje p : todosLosPersonajes) {
            if (queryActual == null || queryActual.isEmpty() || 
                p.getNombre().toLowerCase().contains(queryActual.toLowerCase())) {
                personajesFiltrados.add(p);
            }
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Personaje p : personajesFiltrados) {
            View itemView = inflater.inflate(R.layout.item_personaje, layoutDinamico, false);
            
            ImageView img = itemView.findViewById(R.id.img_item);
            TextView txt = itemView.findViewById(R.id.txt_item_nombre);
            Button btnSeleccionar = itemView.findViewById(R.id.btn_item_seleccionar);
            View layoutBotones = itemView.findViewById(R.id.layout_edit_delete);
            Button btnEditar = itemView.findViewById(R.id.btn_item_editar);
            Button btnEliminar = itemView.findViewById(R.id.btn_item_eliminar);

            txt.setText(p.getNombre());
            
            if (p.getImagenUri() != null && !p.getImagenUri().isEmpty()) {
                img.setImageURI(Uri.parse(p.getImagenUri()));
            } else {
                img.setImageResource(p.getImagenResId() != 0 ? p.getImagenResId() : R.drawable.master_chief);
            }

            // Lógica de visibilidad según si se quiere editar o eliminar
            if (modoEdicion || modoEliminacion) {
                btnSeleccionar.setVisibility(View.GONE);
                layoutBotones.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(modoEdicion ? View.VISIBLE : View.GONE);
                btnEliminar.setVisibility(modoEliminacion ? View.VISIBLE : View.GONE);
            } else {
                btnSeleccionar.setVisibility(View.VISIBLE);
                layoutBotones.setVisibility(View.GONE);
            }

            btnSeleccionar.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, BiografiaActivity.class);
                intent.putExtra("nombre", p.getNombre());
                intent.putExtra("biografia", p.getBiografia());
                intent.putExtra("imagen", p.getImagenResId());
                intent.putExtra("imagenUri", p.getImagenUri());
                startActivity(intent);
            });

            btnEditar.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AgregarPersonajeActivity.class);
                intent.putExtra("edit_id", p.getId());
                intent.putExtra("edit_nombre", p.getNombre());
                intent.putExtra("edit_bio", p.getBiografia());
                intent.putExtra("edit_uri", p.getImagenUri());
                startActivity(intent);
                resetModos();
            });

            btnEliminar.setOnClickListener(v -> {
                dbHelper.eliminarPersonaje(p.getId());
                Toast.makeText(this, "Personaje eliminado", Toast.LENGTH_SHORT).show();
                resetModos(); // Salir del modo tras eliminar
                cargarPersonajes();
            });
            
            layoutDinamico.addView(itemView);
        }
    }
}
