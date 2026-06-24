package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.dto.comprobante.FacturaDTO;
import Farmared.model.comprobante.EstadoComprobante;

import javax.swing.*;
import java.awt.*;

public class VistaModificacionFactura extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<EstadoComprobante> comboEstado;
    private FacturaDTO factura;

    public VistaModificacionFactura(JFrame parent, int nroComprobante) {
        super(parent, "Modificar Factura", true);
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        try {
            factura = ControladorComprobantes.getInstance().consultarFactura(nroComprobante);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar factura.");
            dispose();
            return;
        }

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; panelForm.add(new JLabel("Nro Factura:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(String.valueOf(factura.getNroComprobante())), gbc);

        gbc.gridx=0; gbc.gridy=1; panelForm.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(factura.getRazonSocialProveedor()), gbc);

        gbc.gridx=0; gbc.gridy=2; panelForm.add(new JLabel("Monto Total:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(String.format("$%.2f", factura.getMontoTotal())), gbc);

        txtDescripcion = new JTextField(factura.getDescripcion(), 20);
        gbc.gridx=0; gbc.gridy=3; panelForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridx=1; panelForm.add(txtDescripcion, gbc);

        comboEstado = new JComboBox<>(EstadoComprobante.values());
        comboEstado.setSelectedItem(EstadoComprobante.valueOf(factura.getEstado()));
        gbc.gridx=0; gbc.gridy=4; panelForm.add(new JLabel("Estado:"), gbc);
        gbc.gridx=1; panelForm.add(comboEstado, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardar() {
        factura.setDescripcion(txtDescripcion.getText());
        factura.setEstado(comboEstado.getSelectedItem().toString());
        
        try {
            ControladorComprobantes.getInstance().modificarFactura(factura);
            JOptionPane.showMessageDialog(this, "Factura modificada.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}
