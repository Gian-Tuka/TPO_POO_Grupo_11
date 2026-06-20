package Farmared.model.ordenCompra;

import Farmared.model.user.Usuario;
import Farmared.model.user.Rol;

import java.util.Date;

public class Autorizacion {
    private Usuario supervisor;
    private Date fechaAutorizacion;
    private String comentario;

    public Autorizacion(String descripcion) {
        this.comentario = descripcion;
        this.fechaAutorizacion = new Date();
    }

    public Date getDate() {
        return fechaAutorizacion;
    }

    public Usuario getUsuario() {
        return supervisor;
    }

    private Boolean validRol(Usuario usuario) {
        return usuario.getRol() == Rol.SUPERVISOR;
    }

    // Setters added to allow assignment since they aren't in the constructor
    public void setSupervisor(Usuario supervisor) {
        if (validRol(supervisor)) {
            this.supervisor = supervisor;
        } else {
            throw new IllegalArgumentException("El usuario debe tener rol SUPERVISOR para autorizar.");
        }
    }
}
