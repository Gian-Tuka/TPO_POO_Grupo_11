package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.comprobante.DetalleComprobanteDTO;
import Farmared.dto.comprobante.FacturaDTO;
import Farmared.dto.comprobante.NotaCreditoDTO;
import Farmared.dto.proveedor.ProveedorDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class VistaAltaNotaCredito extends JDialog {

    private JComboBox<String> comboProveedores;
    private ArrayList<ProveedorDTO> listaProveedores;
    private JComboBox<String> comboFacturas;
    private ArrayList<FacturaDTO> listaFacturasDelProveedor;

    private JTextField txtDescripcion;
    private JTextField txtMonto;
    private JCheckBox chkUsarMonto;

    public VistaAltaNotaCredito(JFrame parent) {
        super(parent, "Alta Nota de Crédito", true);
        setSize(520, 430);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(46, 204, 113));
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Nueva Nota de Crédito");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        // Formulario
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(new EmptyBorder(20, 30, 10, 30));

        listaProveedores = ControladorProveedores.getInstance().obtenerProveedoresDTO();
        listaFacturasDelProveedor = new ArrayList<>();

        comboProveedores = new JComboBox<>();
        comboProveedores.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboProveedores.addItem("Seleccione un Proveedor...");
        for (ProveedorDTO p : listaProveedores) {
            comboProveedores.addItem(p.getRazonSocial() + " (CUIT: " + p.getCuit() + ")");
        }
        comboProveedores.addActionListener(e -> actualizarFacturas());

        comboFacturas = new JComboBox<>();
        comboFacturas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboFacturas.setEnabled(false);
        comboFacturas.addActionListener(e -> calcularMontoDesdeFactura());

        txtDescripcion = new JTextField();
        txtDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        txtMonto = new JTextField();
        txtMonto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Checkbox: ingresar monto manualmente
        chkUsarMonto = new JCheckBox("Ingresar monto manualmente (sin calcular desde factura)");
        chkUsarMonto.setBackground(Color.WHITE);
        chkUsarMonto.setSelected(true);
        chkUsarMonto.addActionListener(e -> alternarModoMonto());

        panelForm.add(crearFila("Proveedor:", comboProveedores));
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(crearFila("Factura Asociada:", comboFacturas));
        panelForm.add(Box.createVerticalStrut(5));
        panelForm.add(chkUsarMonto);
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(crearFila("Descripción:", txtDescripcion));
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(crearFila("Monto Total ($):", txtMonto));

        add(panelForm, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(new Color(245, 245, 250));
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = new JButton("Guardar Nota de Crédito");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardar());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearFila(String etiqueta, JComponent campo) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(Color.WHITE);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setPreferredSize(new Dimension(160, 35));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fila.add(lbl, BorderLayout.WEST);
        fila.add(campo, BorderLayout.CENTER);
        return fila;
    }

    private void actualizarFacturas() {
        int idx = comboProveedores.getSelectedIndex();
        comboFacturas.removeAllItems();
        listaFacturasDelProveedor.clear();
        txtMonto.setText("");

        if (idx <= 0) {
            comboFacturas.setEnabled(false);
            return;
        }

        String cuit = listaProveedores.get(idx - 1).getCuit();
        ArrayList<FacturaDTO> todas = ControladorComprobantes.getInstance().obtenerFacturasDTO();
        listaFacturasDelProveedor = (ArrayList<FacturaDTO>) todas.stream()
                .filter(f -> f.getCuitProveedor().equals(cuit))
                .collect(Collectors.toList());

        comboFacturas.addItem("Sin Factura Asociada");
        for (FacturaDTO f : listaFacturasDelProveedor) {
            comboFacturas.addItem(f.getNroComprobante() + " - " + f.getDescripcion() + " ($" + f.getMontoTotal() + ")");
        }
        comboFacturas.setEnabled(true);
    }

    private void calcularMontoDesdeFactura() {
        if (chkUsarMonto.isSelected()) return; // Modo manual, no tocar
        int idx = comboFacturas.getSelectedIndex();
        if (idx > 0) {
            FacturaDTO fc = listaFacturasDelProveedor.get(idx - 1);
            float total = 0;
            for (DetalleComprobanteDTO det : fc.getDetalles()) {
                total += det.getSubTotal();
            }
            if (total == 0) total = fc.getMontoTotal();
            txtMonto.setText(String.valueOf(total));
            txtMonto.setEditable(false);
        } else {
            txtMonto.setText("");
            txtMonto.setEditable(false);
        }
    }

    private void alternarModoMonto() {
        boolean esManual = chkUsarMonto.isSelected();
        txtMonto.setEditable(esManual);
        if (esManual) {
            txtMonto.setText("");
        } else {
            calcularMontoDesdeFactura();
        }
    }

    private void guardar() {
        try {
            int idxProv = comboProveedores.getSelectedIndex();
            if (idxProv <= 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String cuit = listaProveedores.get(idxProv - 1).getCuit();
            String desc = txtDescripcion.getText().trim();
            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            float monto;
            try {
                monto = Float.parseFloat(txtMonto.getText().trim());
                if (monto <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El monto debe ser un número mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nroFactura = "";
            int idxFac = comboFacturas.getSelectedIndex();
            if (idxFac > 0) {
                nroFactura = listaFacturasDelProveedor.get(idxFac - 1).getNroComprobante();
            }

            NotaCreditoDTO dto = new NotaCreditoDTO(cuit, desc, monto, nroFactura);
            ControladorComprobantes.getInstance().registrarNotaCredito(dto);
            JOptionPane.showMessageDialog(this, "Nota de Crédito registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        }
    }
}