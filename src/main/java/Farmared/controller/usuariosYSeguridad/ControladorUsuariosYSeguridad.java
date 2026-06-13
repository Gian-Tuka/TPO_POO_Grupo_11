package Farmared.controller.usuariosYSeguridad;

import Farmared.dto.user.UsuarioDTO;
import Farmared.exception.UsuarioNoEncontradoException;
import Farmared.model.user.Rol;
import Farmared.model.user.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ControladorUsuariosYSeguridad {

    private List<Usuario> usuarios = null;
    private Usuario usuarioActual;
    private static ControladorUsuariosYSeguridad controller = null;

    private ControladorUsuariosYSeguridad() {
        this.usuarios = new ArrayList<Usuario>();
    }

    public synchronized static ControladorUsuariosYSeguridad getInstance(){

        if(controller == null){
            controller = new ControladorUsuariosYSeguridad();
        }
        return controller;
    }


    public UsuarioDTO obtenerUsuarioActual(String legajo) {

        Usuario usuario = buscarUsuario(legajo);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException(legajo);
        }
        this.usuarioActual = usuario;

        return toDto(usuario);
    }

    private Usuario buscarUsuario(String legajo) {

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getLegajo().equals(legajo)) {
                return usuarios.get(i);
            }
        }
        return null;
    }


    private static Usuario toModel(UsuarioDTO dto){
        return new Usuario(
                dto.getNombre(),
                dto.getApellido(),
                Rol.valueOf(dto.getRol())
        );
    }


    private static UsuarioDTO toDto(Usuario model){
        return new UsuarioDTO(
                model.getLegajo(),
                model.getNombre(),
                model.getApellido(),
                model.getRol().name()
        );
    }
}
