package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.dto.comprobante.NotaDebitoDTO;
import Farmared.model.comprobante.EstadoComprobante;

import javax.swing.*;
import java.awt.*;

public class VistaModificacionNotaDebito extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<EstadoComprobante> comboEstado;
    private NotaDebitoDTO notaDebito;

    public VistaModificacionNotaDebito(JFrame parent, int nroComprobante) {
        super(parent, "Modificar Nota de Débito", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        for (NotaDebitoDTO nd : ControladorComprobantes.getInstance().obtenerNotasDeDebitoDTO()) {
            if (nd.getNroComprobante().equals(nroComprobante)) {
                notaDebito = nd;
                break;
            }
        }

        if (notaDebito == null) {
            JOptionPane.showMessageDialog(this, "Error al cargar Nota de Débito.");
            dispose();
            return;
        }

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; panelForm.add(new JLabel("Nro Comprobante:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(String.valueOf(notaDebito.getNroComprobante())), gbc);

        gbc.gridx=0; gbc.gridy=1; panelForm.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(notaDebito.getRazonSocialProveedor()), gbc);

        gbc.gridx=0; gbc.gridy=2; panelForm.add(new JLabel("Monto:"), gbc);
        gbc.gridx=1; panelForm.add(new JLabel(String.format("$%.2f", notaDebito.getMonto())), gbc);

        txtDescripcion = new JTextField(notaDebito.getDescripcion(), 20);
        gbc.gridx=0; gbc.gridy=3; panelForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridx=1; panelForm.add(txtDescripcion, gbc);

        comboEstado = new JComboBox<>(EstadoComprobante.values());
        comboEstado.setSelectedItem(EstadoComprobante.valueOf(notaDebito.getEstado()));
        gbc.gridx=0; gbc.gridy=4; panelForm.add(new JLabel("Estado:"), gbc);
        gbc.gridx=1; panelForm.add(comboEstado, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(41, 128, 185));
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
        notaDebito.setDescripcion(txtDescripcion.getText());
        notaDebito.setEstado(comboEstado.getSelectedItem().toString());
        
        try {
            ControladorComprobantes.getInstance().modificarNotaDeDebito(notaDebito);
            JOptionPane.showMessageDialog(this, "Nota de Débito modificada.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}
