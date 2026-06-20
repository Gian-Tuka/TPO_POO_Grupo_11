package Farmared.model.user;

import Farmared.utils.GeneradorDeCodigos;

public class Usuario {
    private String legajo;
    private String nombre;
    private String apellido;
    private Rol rol;
    private Area area;
    private String password;


    public Usuario(String nombre, String apellido, Rol rol, Area area, String password) {
        this.legajo = generarLegajo();
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.area = area;
        this.password = password;
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

    public Rol getRol() {return rol;}

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

    private void setPassword(String password) {
        this.password = password;
    }


    public Boolean validarPassword(String password) {
        return password.equals(this.password);
    }

    private String generarLegajo() {
        GeneradorDeCodigos gdc = new GeneradorDeCodigos();
        return gdc.generarCodigo("Lu", 4);
    }

    @Override
    public String toString() {
        return "Usuario: \n nombre : " + nombre +
                ", apellido : " + apellido +
                ", rol : " + rol +
                ", legajo : " + legajo +
                '}';
    }
}
