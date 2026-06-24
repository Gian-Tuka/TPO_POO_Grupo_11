package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.dto.comprobante.NotaCreditoDTO;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.model.comprobante.EstadoComprobante;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VistaAltaNotaCredito extends JDialog {

    private JComboBox<String> comboProveedores;
    private ArrayList<ProveedorDTO> listaProveedores;
    private JTextField txtDescripcion;
    private JTextField txtMonto;
    private JTextField txtNroFactura;

    public VistaAltaNotaCredito(JFrame parent) {
        super(parent, "Alta Nota de Crédito", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        listaProveedores = ControladorComprobantes.getInstance().obtenerProveedoresParaCombo();
        comboProveedores = new JComboBox<>();
        for (ProveedorDTO p : listaProveedores) {
            comboProveedores.addItem(p.getRazonSocial() + " (" + p.getCuit() + ")");
        }

        txtDescripcion = new JTextField(20);
        txtMonto = new JTextField(10);
        txtNroFactura = new JTextField(10);

        gbc.gridx=0; gbc.gridy=0; panelForm.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx=1; panelForm.add(comboProveedores, gbc);

        gbc.gridx=0; gbc.gridy=1; panelForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridx=1; panelForm.add(txtDescripcion, gbc);

        gbc.gridx=0; gbc.gridy=2; panelForm.add(new JLabel("Monto:"), gbc);
        gbc.gridx=1; panelForm.add(txtMonto, gbc);

        gbc.gridx=0; gbc.gridy=3; panelForm.add(new JLabel("Factura Asociada (Nro):"), gbc);
        gbc.gridx=1; panelForm.add(txtNroFactura, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(230, 126, 34)); // Orange theme for NC
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
        try {
            int idx = comboProveedores.getSelectedIndex();
            if (idx < 0) return;
            
            String cuit = listaProveedores.get(idx).getCuit();
            String desc = txtDescripcion.getText();
            float monto = Float.parseFloat(txtMonto.getText());
            int nroFac = 0;
            if (!txtNroFactura.getText().trim().isEmpty()) {
                nroFac = Integer.parseInt(txtNroFactura.getText().trim());
            }

            NotaCreditoDTO dto = new NotaCreditoDTO(cuit, desc, monto, nroFac);
            NotaCreditoDTO result = ControladorComprobantes.getInstance().altaNotaDeCredito(dto);

            if (EstadoComprobante.PENDIENTE_AUTORIZACION.name().equals(result.getEstado())) {
                JOptionPane.showMessageDialog(this, 
                    "La nota de crédito quedó en estado PENDIENTE DE AUTORIZACIÓN porque el monto supera el tope de deuda del proveedor.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Nota de Crédito creada exitosamente.");
            }
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique los campos numéricos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
