package Farmared.view.users;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.dto.user.UsuarioDTO;

import javax.swing.*;
import java.awt.*;

public class VistaEliminarUsuario extends JDialog {

    private JTextField txtBuscarLegajo;
    private JButton btnBuscar;

    private JLabel lblNombre, lblApellido, lblRol, lblArea, lblEstado;
    private JButton btnEliminar;
    
    private UsuarioDTO usuarioActual = null;

    public VistaEliminarUsuario() {
        setTitle("Farmared - Eliminar Usuario");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Busqueda superior
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por Legajo:"));
        txtBuscarLegajo = new JTextField(15);
        panelBusqueda.add(txtBuscarLegajo);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // Datos del usuario
        JPanel panelDatos = new JPanel(new GridLayout(5, 2, 10, 10));
        panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblNombre = new JLabel("-");
        lblApellido = new JLabel("-");
        lblRol = new JLabel("-");
        lblArea = new JLabel("-");
        lblEstado = new JLabel("-");

        panelDatos.add(new JLabel("Nombre:"));
        panelDatos.add(lblNombre);
        panelDatos.add(new JLabel("Apellido:"));
        panelDatos.add(lblApellido);
        panelDatos.add(new JLabel("Rol:"));
        panelDatos.add(lblRol);
        panelDatos.add(new JLabel("Área:"));
        panelDatos.add(lblArea);
        panelDatos.add(new JLabel("Estado actual:"));
        panelDatos.add(lblEstado);

        add(panelDatos, BorderLayout.CENTER);

        // Panel Inferior para el botón eliminar
        JPanel panelBoton = new JPanel();
        btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.setForeground(Color.RED);
        btnEliminar.setEnabled(false);
        panelBoton.add(btnEliminar);
        add(panelBoton, BorderLayout.SOUTH);

        // Listeners
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
    }

    private void buscarUsuario() {
        String legajo = txtBuscarLegajo.getText().trim();
        if (legajo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un legajo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            usuarioActual = ControladorUsuariosYSeguridad.getInstance().consultarUsuario(legajo);
            
            lblNombre.setText(usuarioActual.getNombre());
            lblApellido.setText(usuarioActual.getApellido());
            lblRol.setText(usuarioActual.getRol());
            lblArea.setText(usuarioActual.getArea());
            lblEstado.setText(usuarioActual.isActivo() ? "Activo" : "Inactivo");

            if(usuarioActual.isActivo()) {
                btnEliminar.setEnabled(true);
            } else {
                btnEliminar.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Este usuario ya se encuentra inactivo.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            limpiarDatos();
            btnEliminar.setEnabled(false);
            usuarioActual = null;
        }
    }

    private void limpiarDatos() {
        lblNombre.setText("-");
        lblApellido.setText("-");
        lblRol.setText("-");
        lblArea.setText("-");
        lblEstado.setText("-");
    }

    private void eliminarUsuario() {
        if (usuarioActual == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea eliminar al usuario " + usuarioActual.getNombre() + " " + usuarioActual.getApellido() + "?",
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ControladorUsuariosYSeguridad.getInstance().eliminarUsuario(usuarioActual.getLegajo());
                JOptionPane.showMessageDialog(this,
                        "Usuario eliminado exitosamente (Borrado lógico).",
                        "Eliminación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
