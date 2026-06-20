package Farmared.view;
import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JDialog {
    private JTextField txtLegajo;
    private JPasswordField txtPassword;
    private boolean loginExitoso = false;

    public LoginGUI(JFrame parent) {
        super(parent, "Ingreso a Farmared", true); // true = Modal (bloquea el fondo)
        setSize(350, 220);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        // Formulario central
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));

        formPanel.add(new JLabel("Legajo de Usuario:"));
        txtLegajo = new JTextField();
        formPanel.add(txtLegajo);

        formPanel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        add(formPanel, BorderLayout.CENTER);

        // Botonera inferior
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalir = new JButton("Salir");
        JButton btnIngresar = new JButton("Ingresar");

        botonera.add(btnSalir);
        botonera.add(btnIngresar);
        add(botonera, BorderLayout.SOUTH);

        // Evento Salir: Cierra por completo la JVM
        btnSalir.addActionListener(e -> System.exit(0));

        // Evento Ingresar: Conecta con tu Controlador
        btnIngresar.addActionListener(e -> {
            String legajo = txtLegajo.getText();
            String password = new String(txtPassword.getPassword());

            Validations v = new Validations();
            if (v.isNullOrEmpty(legajo) || v.isNullOrEmpty(password)) {
                JOptionPane.showMessageDialog(this, "Por favor, complete ambos campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // controlador singleton
            ControladorUsuariosYSeguridad authController = ControladorUsuariosYSeguridad.getInstance();
            boolean esValido = authController.login(legajo, password);

            if (esValido) {
                this.loginExitoso = true;
                dispose(); // cierra el login y avanza al menu
            } else {
                JOptionPane.showMessageDialog(this, "Legajo o contraseña incorrectos.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isLoginExitoso() {
        return loginExitoso;
    }
}
