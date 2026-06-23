package Farmared.view.users;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.dto.user.UsuarioDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;

public class VistaCambiarPassword extends JDialog {

    private JTextField txtBuscarLegajo;
    private JButton btnBuscar;

    private JLabel lblNombre, lblApellido;
    private JPasswordField txtNuevaPassword;
    private JButton btnCambiar;
    
    private UsuarioDTO usuarioActual = null;

    public VistaCambiarPassword() {
        setTitle("Farmared - Cambiar Contraseña");
        setSize(450, 400);
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

        // Formulario
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblNombre = new JLabel("-");
        lblApellido = new JLabel("-");
        txtNuevaPassword = new JPasswordField();
        txtNuevaPassword.setEnabled(false);

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(lblNombre);
        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(lblApellido);
        panelFormulario.add(new JLabel("Nueva Contraseña:"));
        panelFormulario.add(txtNuevaPassword);

        add(panelFormulario, BorderLayout.CENTER);

        // Panel Inferior
        JPanel panelBoton = new JPanel();
        btnCambiar = new JButton("Confirmar Cambio");
        btnCambiar.setEnabled(false);
        panelBoton.add(btnCambiar);
        add(panelBoton, BorderLayout.SOUTH);

        // Listeners
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnCambiar.addActionListener(e -> {
            try {
                cambiarPassword();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
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
            txtNuevaPassword.setEnabled(true);
            btnCambiar.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            lblNombre.setText("-");
            lblApellido.setText("-");
            txtNuevaPassword.setEnabled(false);
            btnCambiar.setEnabled(false);
            usuarioActual = null;
        }
    }

    private void cambiarPassword() throws Exception {
        Validations v = new Validations();
        if (usuarioActual == null) return;

        String nuevaPassword = new String(txtNuevaPassword.getPassword()).trim();
        if (nuevaPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!v.passwordValida(nuevaPassword)) {
            throw new Exception("La contraseña no cumple con alguno de los requisitos. Minimo: Una mayuscula, una minuscula, un numero y 8 caracteres.");
        }

        try {
            ControladorUsuariosYSeguridad.getInstance().cambiarPassword(usuarioActual.getLegajo(), nuevaPassword);
            
            JOptionPane.showMessageDialog(this,
                    "Contraseña actualizada exitosamente.",
                    "Cambio Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cambiar contraseña: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
