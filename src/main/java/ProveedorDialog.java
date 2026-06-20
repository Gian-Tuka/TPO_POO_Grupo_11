package view;

import javax.swing.*;
import java.awt.*;

public class ProveedorDialog extends JDialog {
    private JTextField txtNombre;
    private JTextField txtCuit;
    private JComboBox<String> comboProductosMock;

    public ProveedorDialog(JFrame parent) {
        super(parent, "Registrar Nuevo Proveedor", true); // true = Modal
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Nombre / Razón Social:"));
        txtNombre = new JTextField();
        formPanel.add(txtNombre);

        formPanel.add(new JLabel("CUIT:"));
        txtCuit = new JTextField();
        formPanel.add(txtCuit);

        // Aquí aplicamos el truco del Mock para los productos relacionados
        formPanel.add(new JLabel("Asociar Producto Inicial:"));
        String[] productosFalsos = {"-- Seleccionar --", "Tornillos de Acero", "Cajas de Cartón", "Cinta Embalar"};
        comboProductosMock = new JComboBox<>(productosFalsos);
        formPanel.add(comboProductosMock);

        add(formPanel, BorderLayout.CENTER);

        // Botones de guardar / cancelar
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        botonera.add(btnCancelar);
        botonera.add(btnGuardar);
        add(botonera, BorderLayout.SOUTH);

        // Eventos básicos
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            // Aquí irá la validación y llamada al controlador luego
            JOptionPane.showMessageDialog(this, "¡Proveedor guardado (Simulado)!");
            dispose();
        });
    }
}