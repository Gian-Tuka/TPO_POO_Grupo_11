package Farmared.controller.usuariosYSeguridad;

import Farmared.dto.user.UsuarioDTO;
import Farmared.exception.UsuarioNoEncontradoException;
import Farmared.model.user.Area;
import Farmared.model.user.Rol;
import Farmared.model.user.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ControladorUsuariosYSeguridad {

    private ArrayList<Usuario> usuarios = null;
    private Usuario usuarioActual;
    private static ControladorUsuariosYSeguridad controller = null;

    private ControladorUsuariosYSeguridad() {
        this.usuarios = new ArrayList<Usuario>();

        // 2. ADENTRO del constructor sí podés usar el .add() sin errores:
        Usuario supervisor = new Usuario("Carlos", "Gomez", Rol.SUPERVISOR, Area.SISTEMAS, "admin123");
        supervisor.setLegajo("LU1000"); // Legajo fijo para testear fácil
        usuarios.add(supervisor);

        Usuario empleadoCompras = new Usuario("Ana", "Martinez", Rol.EMPLEADO, Area.COMPRAS, "1315");
        empleadoCompras.setLegajo("LU2000");
        usuarios.add(empleadoCompras);

        Usuario empleadoTesoreria = new Usuario("Juan", "Rodriguez", Rol.EMPLEADO, Area.TESORERIA, "1315");
        empleadoTesoreria.setLegajo("LU3000");
        usuarios.add(empleadoTesoreria);
    }

    public synchronized static ControladorUsuariosYSeguridad getInstance(){

        if(controller == null){
            controller = new ControladorUsuariosYSeguridad();
        }
        return controller;
    }


    public UsuarioDTO altaUsuario(UsuarioDTO dto) {
        Usuario nuevo = toModel(dto);
        usuarios.add(nuevo);
        return toDTO(nuevo);
    }

    public UsuarioDTO obtenerUsuarioActual(String legajo) {
        Usuario usuario = buscarUsuario(legajo);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException(legajo);
        }
        this.usuarioActual = usuario;
        return toDTO(usuario);
    }

    public boolean tieneAccesoAModulo(String nombreModulo) {
        if (usuarioActual == null) return false;

        Rol rol = usuarioActual.getRol();
        Area area = usuarioActual.getArea();

        if (rol == Rol.SUPERVISOR || area == Area.SISTEMAS) {
            return true;
        }

        switch (nombreModulo.toUpperCase()) {
            case "PROVEEDORES":
            case "PRODUCTOS":
            case "OC":
                // Solo gente del área de Compras o Administración
                return (area == Area.COMPRAS || area == Area.ADMINISTRACION);

            case "OP":
            case "COMPROBANTES":
                // Solo gente de Tesorería o Administración
                return (area == Area.TESORERIA || area == Area.ADMINISTRACION);

            case "SEGURIDAD":
                //nadie excepto sistemas o supervisores.
                return false;

            default:
                return false;
        }
    }

    public boolean login(String legajo, String password) {
        Usuario usuario = buscarUsuario(legajo);
        if (usuario != null && usuario.validarPassword(password)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    private Usuario buscarUsuario(String legajo) {

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getLegajo().equals(legajo)) {
                System.out.println("Usuario encontrado: " + usuarios.get(i).getNombre());
                return usuarios.get(i);
            }
        }
        return null;
    }


    private static Usuario toModel(UsuarioDTO dto){
        return new Usuario(
                dto.getNombre(),
                dto.getApellido(),
                Rol.valueOf(dto.getRol()),
                Area.valueOf(dto.getArea()),
                "1415"
        );
    }


    private static UsuarioDTO toDTO(Usuario model){
        return new UsuarioDTO(
                model.getLegajo(),
                model.getNombre(),
                model.getApellido(),
                model.getRol().name(),
                model.getArea().name()
        );
    }
}
