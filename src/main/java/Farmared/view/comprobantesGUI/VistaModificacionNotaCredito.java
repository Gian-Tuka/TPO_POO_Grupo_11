package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.model.comprobante.EstadoComprobante;

import javax.swing.*;
import java.awt.*;

public class VistaModificacionNotaCredito extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<EstadoComprobante> comboEstado;
    private String codigoComprobanteActual;

    public VistaModificacionNotaCredito(JFrame parent, String codigoComprobante) {
        super(parent, "Modificar Nota de Crédito", true);
        this.codigoComprobanteActual = codigoComprobante;
        setSize(500, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Descripción:"), gbc);
        txtDescripcion = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Estado:"), gbc);
        comboEstado = new JComboBox<>(EstadoComprobante.values());
        gbc.gridx = 1;
        panelForm.add(comboEstado, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardar() {
        try {
            if (comboEstado.getSelectedItem() == EstadoComprobante.PENDIENTE) {
                ControladorComprobantes.getInstance().autorizarComprobante(codigoComprobanteActual);
            }

            JOptionPane.showMessageDialog(this, "Comprobante procesado y actualizado correctamente.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}