package Farmared.model.user;

public class Usuario {
    private String legajo;
    private String nombre;
    private String apellido;
    private Rol rol;


    public Usuario( String nombre, String apellido, Rol rol) {
        this.legajo = generarLegajo();
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
    }


    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    private String generarLegajo() {
        //legajo = numero random entre 1000 y 9999
        int numeroRandom = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(numeroRandom); // Convierte el int a String
    }

    @Override
    public String toString() {
        return "Usuario{ + nombre = " + nombre +
                ", apellido = " + apellido +
                ", rol = " + rol +
                ", legajo = " + legajo +
                '}';
    }
}
