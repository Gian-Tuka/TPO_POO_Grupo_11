package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.dto.comprobante.DetalleComprobanteDTO;
import Farmared.dto.comprobante.FacturaDTO;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.model.comprobante.TipoFactura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VistaAltaFactura extends JDialog {

    private JComboBox<String> comboProveedores;
    private ArrayList<ProveedorDTO> listaProveedores;
    private JTextField txtDescripcion;
    private JTextField txtNroOC;
    private JComboBox<TipoFactura> comboTipoFactura;
    
    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;
    private ArrayList<DetalleComprobanteDTO> detallesActuales;

    public VistaAltaFactura(JFrame parent) {
        super(parent, "Alta de Factura", true);
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 250));

        detallesActuales = new ArrayList<>();

        // --- FORMULARIO NORTE ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(245, 245, 250));
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        listaProveedores = ControladorComprobantes.getInstance().obtenerProveedoresParaCombo();
        comboProveedores = new JComboBox<>();
        for (ProveedorDTO p : listaProveedores) {
            comboProveedores.addItem(p.getRazonSocial() + " (" + p.getCuit() + ")");
        }

        txtDescripcion = new JTextField(20);
        txtNroOC = new JTextField(10);
        comboTipoFactura = new JComboBox<>(TipoFactura.values());

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Proveedor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelForm.add(comboProveedores, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Nro OC:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelForm.add(txtNroOC, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Tipo Factura:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelForm.add(comboTipoFactura, gbc);

        add(panelForm, BorderLayout.NORTH);

        // --- DETALLES CENTRO ---
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de Factura"));
        
        String[] colDetalles = {"Código", "Descripción", "Cantidad", "Precio Unit.", "Subtotal"};
        modeloDetalles = new DefaultTableModel(colDetalles, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaDetalles = new JTable(modeloDetalles);
        panelDetalles.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        JPanel panelBotonesDetalle = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAgregarDetalle = new JButton("Agregar Línea");
        btnAgregarDetalle.addActionListener(e -> abrirDialogoDetalle());
        panelBotonesDetalle.add(btnAgregarDetalle);
        panelDetalles.add(panelBotonesDetalle, BorderLayout.SOUTH);

        add(panelDetalles, BorderLayout.CENTER);

        // --- BOTONES SUR ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.addActionListener(e -> guardarFactura());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void abrirDialogoDetalle() {
        JDialog diag = new JDialog(this, "Agregar Detalle", true);
        diag.setSize(400, 250);
        diag.setLocationRelativeTo(this);
        diag.setLayout(new GridBagLayout());
        
        ArrayList<ItemDTO> items = ControladorComprobantes.getInstance().obtenerItemsParaCombo();
        JComboBox<String> comboItems = new JComboBox<>();
        for (ItemDTO it : items) {
            comboItems.addItem(it.getDescripcionDeItem() + " (" + it.getCodigo() + ")");
        }

        JTextField txtCant = new JTextField("1", 5);
        JTextField txtPrecio = new JTextField("0.0", 8);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.gridx=0; gbc.gridy=0; diag.add(new JLabel("Item:"), gbc);
        gbc.gridx=1; diag.add(comboItems, gbc);
        
        gbc.gridx=0; gbc.gridy=1; diag.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx=1; diag.add(txtCant, gbc);
        
        gbc.gridx=0; gbc.gridy=2; diag.add(new JLabel("Precio Unitario:"), gbc);
        gbc.gridx=1; diag.add(txtPrecio, gbc);

        JButton btnOk = new JButton("Agregar");
        btnOk.addActionListener(e -> {
            try {
                int cant = Integer.parseInt(txtCant.getText());
                float precio = Float.parseFloat(txtPrecio.getText());
                int idx = comboItems.getSelectedIndex();
                if (idx >= 0) {
                    ItemDTO sel = items.get(idx);
                    DetalleComprobanteDTO det = new DetalleComprobanteDTO(sel.getCodigo(), cant, precio);
                    detallesActuales.add(det);
                    modeloDetalles.addRow(new Object[]{sel.getCodigo(), sel.getDescripcionDeItem(), cant, precio, cant * precio});
                    diag.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(diag, "Valores numéricos inválidos.");
            }
        });

        gbc.gridx=1; gbc.gridy=3; diag.add(btnOk, gbc);
        diag.setVisible(true);
    }

    private void guardarFactura() {
        int idx = comboProveedores.getSelectedIndex();
        if (idx < 0) return;
        
        String cuit = listaProveedores.get(idx).getCuit();
        String desc = txtDescripcion.getText();
        String oc = txtNroOC.getText();
        String tipo = comboTipoFactura.getSelectedItem().toString();

        FacturaDTO dto = new FacturaDTO(cuit, desc, oc, detallesActuales, tipo);
        
        try {
            ControladorComprobantes.getInstance().altaFactura(dto);
            JOptionPane.showMessageDialog(this, "Factura creada exitosamente.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
