package Farmared.dto.user;

import Farmared.model.user.Rol;
import Farmared.model.user.Usuario;

public class UsuarioDTO {
    private String legajo;
    private String nombre;
    private String apellido;
    private String rol;
    private String area;
    private boolean activo;

    //DTO para generar el alta desde la view (sin legajo)
    public UsuarioDTO(String nombre, String apellido, String rol,  String area) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.legajo = null;
        this.area = area;
    }
    // DTO para mostrar informacion en la view
    public UsuarioDTO(String legajo, String nombre, String apellido, String rol,  String area, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.legajo = legajo;
        this.area = area;
        this.activo = activo;
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

    public String getArea() {return area;}
    public void setArea(String area) {this.area = area;}

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "UsuarioDTO{ nombre = " + nombre +
                ", apellido = " + apellido +
                ", rol = " + rol +
                ", legajo = " + legajo +
                '}';
    }

}
