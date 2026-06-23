package Farmared.view.users;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.dto.user.UsuarioDTO;
import Farmared.view.LoginGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUIUsuarios extends JPanel {

    private DefaultTableModel modeloTablaUsuarios;

    public GUIUsuarios() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Barra de acciones
        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrear = new JButton("Crear Usuario");
        JButton btnModificar = new JButton("Modificar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");
        JButton btnCambiarPassword = new JButton("Cambiar Contraseña");
        JButton btnProbarLogin = new JButton("Probar Login");

        barraAcciones.add(btnCrear);
        barraAcciones.add(btnModificar);
        barraAcciones.add(btnEliminar);
        barraAcciones.add(btnCambiarPassword);
        barraAcciones.add(btnProbarLogin);
        this.add(barraAcciones, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Legajo", "Nombre", "Apellido", "Rol", "Área", "Estado"};
        modeloTablaUsuarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTablaUsuarios);
        JScrollPane scrollPane = new JScrollPane(tabla);
        this.add(scrollPane, BorderLayout.CENTER);

        // Llamada al método que llena la tabla
        actualizarTablaUsuarios();

        // Listeners de los botones
        btnCrear.addActionListener(e -> {
            VistaAltaUsuario vistaAlta = new VistaAltaUsuario();
            vistaAlta.setModal(true);
            vistaAlta.setLocationRelativeTo(this);
            vistaAlta.setVisible(true);
            actualizarTablaUsuarios();
        });

        btnModificar.addActionListener(e -> {
            VistaModificarUsuario vistaModificar = new VistaModificarUsuario();
            vistaModificar.setModal(true);
            vistaModificar.setLocationRelativeTo(this);
            vistaModificar.setVisible(true);
            actualizarTablaUsuarios();
        });

        btnEliminar.addActionListener(e -> {
            VistaEliminarUsuario vistaEliminar = new VistaEliminarUsuario();
            vistaEliminar.setModal(true);
            vistaEliminar.setLocationRelativeTo(this);
            vistaEliminar.setVisible(true);
            actualizarTablaUsuarios();
        });

        btnCambiarPassword.addActionListener(e -> {
            VistaCambiarPassword vistaPassword = new VistaCambiarPassword();
            vistaPassword.setModal(true);
            vistaPassword.setLocationRelativeTo(this);
            vistaPassword.setVisible(true);
        });

        btnProbarLogin.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            LoginGUI login = new LoginGUI(parentFrame);
            login.setVisible(true);
            
            if (login.isLoginExitoso()) {
                UsuarioDTO actual = ControladorUsuariosYSeguridad.getInstance().getUsuarioActual();
                JOptionPane.showMessageDialog(this,
                        "¡Login Exitoso!\nEl sistema se reiniciará con los permisos de: " + actual.getNombre() + " " + actual.getApellido() + " (" + actual.getRol() + ")",
                        "Prueba de Login",
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Reiniciar la vista principal
                if (parentFrame != null) {
                    parentFrame.dispose();
                }
                new Farmared.MenuPrincipal().setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(this,
                        "Login fallido o cancelado.",
                        "Prueba de Login",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void actualizarTablaUsuarios() {
        modeloTablaUsuarios.setRowCount(0);
        ArrayList<UsuarioDTO> listaUsuarios = ControladorUsuariosYSeguridad.getInstance().obtenerUsuariosDTO();
        for (UsuarioDTO u : listaUsuarios) {
            Object[] fila = {
                    u.getLegajo(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getRol(),
                    u.getArea(),
                    u.isActivo() ? "Activo" : "Inactivo"
            };
            modeloTablaUsuarios.addRow(fila);
        }
    }
}
