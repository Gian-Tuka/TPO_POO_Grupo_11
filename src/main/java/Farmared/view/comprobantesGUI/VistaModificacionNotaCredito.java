package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.dto.comprobante.NotaCreditoDTO;
import Farmared.model.comprobante.EstadoComprobante;

import javax.swing.*;
import java.awt.*;

public class VistaModificacionNotaCredito extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<EstadoComprobante> comboEstado;
    private NotaCreditoDTO notaCredito;

    public VistaModificacionNotaCredito(JFrame parent, int nroComprobante) {
        super(parent, "Modificar Nota de Crédito", true);
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        // Load NotaCredito from list by looping
        for (NotaCreditoDTO nc : ControladorComprobantes.getInstance().obtenerNotasDeCreditoDTO()) {
            if (nc.getNroComprobante() == nroComprobante) {
                notaCredito = nc;
                break;
            }
        }

        if (notaCredito == null) {
            JOptionPane.showMessageDialog(this, "Error al cargar Nota de Crédito.");
            dispose();
            return;
        }

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; panelForm.add(new JLabel("Nro Comprobante:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(String.valueOf(notaCredito.getNroComprobante())), gbc);

        gbc.gridx=0; gbc.gridy=1; panelForm.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(notaCredito.getRazonSocialProveedor()), gbc);

        gbc.gridx=0; gbc.gridy=2; panelForm.add(new JLabel("Monto:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(String.format("$%.2f", notaCredito.getMonto())), gbc);

        txtDescripcion = new JTextField(notaCredito.getDescripcion(), 20);
        gbc.gridx=0; gbc.gridy=3; panelForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridx=1; panelForm.add(txtDescripcion, gbc);

        comboEstado = new JComboBox<>(EstadoComprobante.values());
        comboEstado.setSelectedItem(EstadoComprobante.valueOf(notaCredito.getEstado()));
        gbc.gridx=0; gbc.gridy=4; panelForm.add(new JLabel("Estado:"), gbc);
        gbc.gridx=1; panelForm.add(comboEstado, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(26, 188, 156));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardar() {
        notaCredito.setDescripcion(txtDescripcion.getText());
        notaCredito.setEstado(comboEstado.getSelectedItem().toString());
        
        try {
            ControladorComprobantes.getInstance().modificarNotaDeCredito(notaCredito);
            
            // Si pasamos a autorizado, ejecutar la logica de autorizacion
            if (comboEstado.getSelectedItem() == EstadoComprobante.AUTORIZADO) {
                 ControladorComprobantes.getInstance().autorizarNotaDeCredito(notaCredito.getNroComprobante());
            }

            JOptionPane.showMessageDialog(this, "Nota de Crédito modificada.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}
