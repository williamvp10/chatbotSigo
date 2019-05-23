package chatbot;

public class Evento {

    private String nombre;
    private String fecha;
    private String url;

    public Evento() {
    }

    public Evento(String nombre, String fecha, String url) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUrl() {
        return url;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
