package Farmared.view;

import javax.swing.*;
import java.awt.*;

public class UnidadDialog extends JDialog {

    private JTextField txtCodigoUnidad;
    private JTextField txtDescripcionUnidad;
    private JComboBox<String> comboTipoUnidad;

    public UnidadDialog(JFrame parent) {
        super(parent, "Registrar Unidad de Medida", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Código de Unidad:"));
        txtCodigoUnidad = new JTextField();
        formPanel.add(txtCodigoUnidad);

        formPanel.add(new JLabel("Descripción:"));
        txtDescripcionUnidad = new JTextField();
        formPanel.add(txtDescripcionUnidad);

        formPanel.add(new JLabel("Tipo de Unidad:"));
        String[] tiposMock = {"-- Seleccionar --", "Peso", "Volumen", "Cantidad"};
        comboTipoUnidad = new JComboBox<>(tiposMock);
        formPanel.add(comboTipoUnidad);

        add(formPanel, BorderLayout.CENTER);

        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        botonera.add(btnCancelar);
        botonera.add(btnGuardar);
        add(botonera, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Unidad de Medida guardada!");
            dispose();
        });
    }

    public String getCodigoUnidad() { return txtCodigoUnidad.getText(); }
    public String getDescripcionUnidad() { return txtDescripcionUnidad.getText(); }
    public String getTipoUnidad() { return (String) comboTipoUnidad.getSelectedItem(); }
}