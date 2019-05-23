package chatbot;

public class Club {

    private String nombre;
    private String reglas;
    private String almuerzo;
    private String remision;

    public Club() {
    }

    public Club(String nombre, String reglas, String almuerzo, String remision) {
        this.nombre = nombre;
        this.reglas = reglas;
        this.almuerzo = almuerzo;
        this.remision = remision;
    }

    public String getNombre() {
        return nombre;
    }

    public String getReglas() {
        return reglas;
    }

    public String getAlmuerzo() {
        return almuerzo;
    }

    public String getRemision() {
        return remision;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setReglas(String reglas) {
        this.reglas = reglas;
    }

    public void setAlmuerzo(String almuerzo) {
        this.almuerzo = almuerzo;
    }

    public void setRemision(String remision) {
        this.remision = remision;
    }
}
