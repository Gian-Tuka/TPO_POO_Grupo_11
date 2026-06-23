package Farmared.view;

import Farmared.model.item.TipoDeUnidad;
import javax.swing.*;
import java.awt.*;

public class UnidadDialog extends JDialog {

    private JTextField txtDescripcionUnidad;
    private JComboBox<TipoDeUnidad> comboTipoUnidad;

    public UnidadDialog(JFrame parent) {
        super(parent, "Registrar Unidad de Medida", true);
        setSize(400, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Descripción:"));
        txtDescripcionUnidad = new JTextField();
        formPanel.add(txtDescripcionUnidad);

        formPanel.add(new JLabel("Tipo de Unidad:"));
        comboTipoUnidad = new JComboBox<>(TipoDeUnidad.values());
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
            JOptionPane.showMessageDialog(this, "¡Unidad de Medida guardada (Simulado)!");
            dispose();
        });
    }

    public String getDescripcionUnidad() { return txtDescripcionUnidad.getText(); }
    public TipoDeUnidad getTipoUnidad() { return (TipoDeUnidad) comboTipoUnidad.getSelectedItem(); }
}