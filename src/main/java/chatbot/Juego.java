package chatbot;

public class Juego {

    private String nombre;
    private String descripcion;
    private String url;

    public Juego() {
    }

    public Juego(String nombre, String descripcion, String url) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
