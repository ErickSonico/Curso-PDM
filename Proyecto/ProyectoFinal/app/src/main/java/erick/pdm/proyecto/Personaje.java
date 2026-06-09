package erick.pdm.proyecto;

public class Personaje {
    private int id;
    private String nombre;
    private String biografia;
    private int imagenResId;
    private String imagenUri;

    public Personaje(int id, String nombre, String biografia, int imagenResId, String imagenUri) {
        this.id = id;
        this.nombre = nombre;
        this.biografia = biografia;
        this.imagenResId = imagenResId;
        this.imagenUri = imagenUri;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getBiografia() {
        return biografia;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public String getImagenUri() {
        return imagenUri;
    }
}
