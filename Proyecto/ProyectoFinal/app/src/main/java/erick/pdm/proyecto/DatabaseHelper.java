package erick.pdm.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HaloAppDB";
    private static final int DATABASE_VERSION = 7;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE personajes (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, biografia TEXT, imagenRes INTEGER, imagenUri TEXT)");
        db.execSQL("CREATE TABLE comentarios (id INTEGER PRIMARY KEY AUTOINCREMENT, personaje TEXT, usuario TEXT, comentario TEXT)");

        // Los personajes con los que viene la app por defecto
        insertarOriginal(db, R.string.jefe_maestro, R.string.bio_jefe_maestro, R.drawable.master_chief);
        insertarOriginal(db, R.string.cortana, R.string.bio_cortana, R.drawable.cortana);
        insertarOriginal(db, R.string.thel_vadamee, R.string.bio_thel_vadamee, R.drawable.thel_vadamee);
        insertarOriginal(db, R.string.buck, R.string.bio_buck, R.drawable.spartan_buck);
        insertarOriginal(db, R.string.ur_didacta, R.string.bio_ur_didacta, R.drawable.ur_didacta);
    }

    private void insertarOriginal(SQLiteDatabase db, int nameRes, int bioRes, int imgRes) {
        ContentValues v = new ContentValues();
        v.put("nombre", context.getString(nameRes));
        v.put("biografia", context.getString(bioRes));
        v.put("imagenRes", imgRes);
        v.put("imagenUri", "");
        db.insert("personajes", null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int newV) {
        db.execSQL("DROP TABLE IF EXISTS personajes");
        db.execSQL("DROP TABLE IF EXISTS comentarios");
        onCreate(db);
    }

    public void agregarPersonaje(String n, String b, int r, String u) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("nombre", n); v.put("biografia", b); v.put("imagenRes", r); v.put("imagenUri", u);
        db.insert("personajes", null, v);
    }

    public void actualizarPersonaje(int id, String n, String b, String u) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("nombre", n);
        v.put("biografia", b);
        if (u != null && !u.isEmpty()) {
            v.put("imagenUri", u);
            v.put("imagenRes", 0);
        }
        db.update("personajes", v, "id=?", new String[]{String.valueOf(id)});
    }

    public void eliminarPersonaje(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("personajes", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Personaje> obtenerPersonajes() {
        List<Personaje> lista = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM personajes", null);
        if (c.moveToFirst()) {
            do {
                lista.add(new Personaje(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getString(4)));
            } while (c.moveToNext());
        }
        c.close();
        return lista;
    }

    public void agregarComentario(String p, String u, String com) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("personaje", p); v.put("usuario", u); v.put("comentario", com);
        db.insert("comentarios", null, v);
    }

    public List<String> obtenerComentarios(String personaje) {
        List<String> lista = new ArrayList<>();
        Cursor c = getReadableDatabase().query("comentarios", null, "personaje=?", new String[]{personaje}, null, null, "id DESC");
        if (c.moveToFirst()) {
            do { lista.add(c.getString(2) + ": " + c.getString(3)); } while (c.moveToNext());
        }
        c.close();
        return lista;
    }
}
