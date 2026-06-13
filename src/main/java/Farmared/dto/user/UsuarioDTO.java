package Farmared.dto.user;

import Farmared.model.user.Rol;

public class UsuarioDTO {
    private String nombre;
    private String apellido;
    private String rol;
    private String legajo;

    public UsuarioDTO(String legajo, String nombre, String apellido, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{ + nombre = " + nombre +
                ", apellido = " + apellido +
                ", rol = " + rol +
                ", legajo = " + legajo +
                '}';
    }

}
