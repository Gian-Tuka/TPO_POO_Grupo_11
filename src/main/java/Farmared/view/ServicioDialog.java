package Farmared.view;

import javax.swing.*;
import java.awt.*;

public class ServicioDialog extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<String> comboUnidadMedida;
    private JTextField txtPrecio;
    private JComboBox<String> comboTipoIVA;
    private JComboBox<String> comboRubro;

    public ServicioDialog(JFrame parent) {
        super(parent, "Registrar Servicio", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Descripción del Item:"));
        txtDescripcion = new JTextField();
        formPanel.add(txtDescripcion);

        formPanel.add(new JLabel("Unidad de Medida:"));
        String[] unidadesMock = {"-- Seleccionar --", "Hora", "Unidad", "Mensual"};
        comboUnidadMedida = new JComboBox<>(unidadesMock);
        formPanel.add(comboUnidadMedida);

        formPanel.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        formPanel.add(txtPrecio);

        formPanel.add(new JLabel("Tipo de IVA:"));
        String[] ivaMock = {"-- Seleccionar --", "21%", "10.5%", "Exento"};
        comboTipoIVA = new JComboBox<>(ivaMock);
        formPanel.add(comboTipoIVA);

        formPanel.add(new JLabel("Rubro:"));
        String[] rubrosMock = {"-- Seleccionar --", "Mantenimiento", "Limpieza", "Logística"};
        comboRubro = new JComboBox<>(rubrosMock);
        formPanel.add(comboRubro);

        add(formPanel, BorderLayout.CENTER);

        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        botonera.add(btnCancelar);
        botonera.add(btnGuardar);
        add(botonera, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Servicio guardado!");
            dispose();
        });
    }

    public String getDescripcion() { return txtDescripcion.getText(); }
    public String getUnidadMedida() { return (String) comboUnidadMedida.getSelectedItem(); }
    public String getPrecio() { return txtPrecio.getText(); }
    public String getTipoIVA() { return (String) comboTipoIVA.getSelectedItem(); }
    public String getRubro() { return (String) comboRubro.getSelectedItem(); }
}