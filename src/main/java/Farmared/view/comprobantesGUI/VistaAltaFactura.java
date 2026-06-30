package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.controller.ordenes.ControladorDeOrdenDeCompra;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.comprobante.DetalleComprobanteDTO;
import Farmared.dto.comprobante.FacturaDTO;
import Farmared.dto.item.PrecioProveedorDTO;
import Farmared.dto.ordenesDeCompra.DetalleOCDTO;
import Farmared.dto.ordenesDeCompra.OrdenDeCompraDTO;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.model.comprobante.TipoFactura;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class VistaAltaFactura extends JDialog {

    private JComboBox<String> comboProveedores;
    private ArrayList<ProveedorDTO> listaProveedores;

    private JComboBox<String> comboOC;
    private ArrayList<OrdenDeCompraDTO> listaOCDelProveedor;

    private JTextField txtDescripcion;
    private JCheckBox chkCompraDirecta;
    private JComboBox<TipoFactura> comboTipoFactura;

    private JTable tablaDetalles;
    private DefaultTableModel modeloDetalles;
    private ArrayList<DetalleComprobanteDTO> detallesActuales;

    private JButton btnAgregarItem;
    private JButton btnEditarItem;
    private JButton btnEliminarItem;

    public VistaAltaFactura(JFrame parent) {
        super(parent, "Alta de Factura", true);
        setSize(800, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        detallesActuales = new ArrayList<>();

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(52, 152, 219));
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Nueva Factura de Proveedor");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        // Formulario con BoxLayout (sin GridBagLayout)
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(new EmptyBorder(15, 20, 10, 20));

        listaProveedores = ControladorProveedores.getInstance().obtenerProveedoresDTO();
        comboProveedores = new JComboBox<>();
        comboProveedores.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboProveedores.addItem("Seleccione un Proveedor...");
        for (ProveedorDTO p : listaProveedores) {
            comboProveedores.addItem(p.getRazonSocial() + " (CUIT: " + p.getCuit() + ")");
        }
        comboProveedores.addActionListener(e -> actualizarOCs());

        chkCompraDirecta = new JCheckBox("Es Compra Directa (Sin OC previa)");
        chkCompraDirecta.setBackground(Color.WHITE);
        chkCompraDirecta.addActionListener(e -> alternarModoCompra());

        listaOCDelProveedor = new ArrayList<>();
        comboOC = new JComboBox<>();
        comboOC.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboOC.setEnabled(false);
        comboOC.addActionListener(e -> cargarItemsDeOC());

        comboTipoFactura = new JComboBox<>(TipoFactura.values());
        comboTipoFactura.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        txtDescripcion = new JTextField();
        txtDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        panelForm.add(crearFila("Proveedor:", comboProveedores));
        panelForm.add(Box.createVerticalStrut(8));
        panelForm.add(chkCompraDirecta);
        panelForm.add(Box.createVerticalStrut(8));
        panelForm.add(crearFila("Orden de Compra:", comboOC));
        panelForm.add(Box.createVerticalStrut(8));
        panelForm.add(crearFila("Tipo Factura:", comboTipoFactura));
        panelForm.add(Box.createVerticalStrut(8));
        panelForm.add(crearFila("Descripción:", txtDescripcion));

        add(panelForm, BorderLayout.NORTH);

        // Tabla de ítems de la factura
        JPanel panelTablaContenedor = new JPanel(new BorderLayout());
        panelTablaContenedor.setBorder(new EmptyBorder(0, 20, 10, 20));
        panelTablaContenedor.setBackground(Color.WHITE);

        modeloDetalles = new DefaultTableModel(
                new Object[]{"Código Ítem", "Descripción", "Cantidad", "Precio Unitario", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaDetalles = new JTable(modeloDetalles);
        tablaDetalles.setRowHeight(25);
        tablaDetalles.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDetalles.getTableHeader().setBackground(new Color(245, 245, 250));
        panelTablaContenedor.add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        JPanel panelTablaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panelTablaBotones.setBackground(Color.WHITE);

        btnAgregarItem = new JButton("Agregar Ítem");
        btnAgregarItem.setEnabled(false);
        btnAgregarItem.addActionListener(e -> agregarItemDirecto());

        btnEditarItem = new JButton("Editar Ítem Seleccionado");
        btnEditarItem.addActionListener(e -> editarItem());

        btnEliminarItem = new JButton("Quitar Ítem");
        btnEliminarItem.addActionListener(e -> quitarItem());

        panelTablaBotones.add(btnAgregarItem);
        panelTablaBotones.add(btnEditarItem);
        panelTablaBotones.add(btnEliminarItem);
        panelTablaContenedor.add(panelTablaBotones, BorderLayout.SOUTH);

        add(panelTablaContenedor, BorderLayout.CENTER);

        // Botones guardar / cancelar
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelAcciones.setBackground(new Color(245, 245, 250));
        panelAcciones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGridGuardar = new JButton("Registrar Factura");
        btnGridGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGridGuardar.setBackground(new Color(46, 204, 113));
        btnGridGuardar.setForeground(Color.WHITE);
        btnGridGuardar.setFocusPainted(false);
        btnGridGuardar.addActionListener(e -> guardarFactura());

        panelAcciones.add(btnCancelar);
        panelAcciones.add(btnGridGuardar);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    // Helper para crear una fila etiqueta + campo
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

    // Al cambiar proveedor, actualizar lista de OCs disponibles
    private void actualizarOCs() {
        int idx = comboProveedores.getSelectedIndex();
        comboOC.removeAllItems();
        listaOCDelProveedor.clear();
        detallesActuales.clear();
        actualizarTabla();

        if (idx <= 0 || chkCompraDirecta.isSelected()) {
            comboOC.setEnabled(false);
            return;
        }

        String cuit = listaProveedores.get(idx - 1).getCuit();
        ArrayList<OrdenDeCompraDTO> todas = ControladorDeOrdenDeCompra.getInstance().obtenerOrdenesDeCompraDTO();

        listaOCDelProveedor = (ArrayList<OrdenDeCompraDTO>) todas.stream()
                .filter(oc -> oc.getCuitProveedor().equals(cuit) &&
                        (oc.getEstado().equals("APROBADA") || oc.getEstado().equals("APROBADA_AUTORIZACION")))
                .collect(Collectors.toList());

        comboOC.addItem("Seleccione una OC...");
        for (OrdenDeCompraDTO oc : listaOCDelProveedor) {
            comboOC.addItem(oc.getNroOC() + " - Total: $" + oc.getTotal() + " (" + oc.getEstado() + ")");
        }
        comboOC.setEnabled(true);
    }

    // Al elegir una OC, autocompletar la tabla con sus ítems y precios
    private void cargarItemsDeOC() {
        int idx = comboOC.getSelectedIndex();
        detallesActuales.clear();

        if (idx > 0) {
            OrdenDeCompraDTO oc = listaOCDelProveedor.get(idx - 1);
            for (DetalleOCDTO detOC : oc.getDetalles()) {
                detallesActuales.add(new DetalleComprobanteDTO(
                        detOC.getCodigoItem(),
                        detOC.getDescripcionItem(),
                        detOC.getCantidad(),
                        detOC.getPrecioUnitario(),
                        detOC.getSubtotal()
                ));
            }
        }
        actualizarTabla();
    }

    // Alternar entre modo OC y Compra Directa
    private void alternarModoCompra() {
        boolean esDirecta = chkCompraDirecta.isSelected();
        comboOC.setEnabled(!esDirecta);
        btnAgregarItem.setEnabled(esDirecta);
        btnEliminarItem.setEnabled(esDirecta);

        if (esDirecta) {
            comboOC.removeAllItems();
            detallesActuales.clear();
            actualizarTabla();
        } else {
            actualizarOCs();
        }
    }

    // Agregar ítem manualmente en modo compra directa
    private void agregarItemDirecto() {
        int idxProv = comboProveedores.getSelectedIndex();
        if (idxProv <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor primero.");
            return;
        }
        String cuit = listaProveedores.get(idxProv - 1).getCuit();
        ArrayList<PrecioProveedorDTO> precios = ControladorProveedores.getInstance().obtenerItemsPorProveedor(cuit);

        DialogoSeleccionarItem dialog = new DialogoSeleccionarItem(this, precios);
        dialog.setVisible(true);

        DetalleComprobanteDTO nuevo = dialog.getDetalleSeleccionado();
        if (nuevo != null) {
            detallesActuales.add(nuevo);
            actualizarTabla();
        }
    }

    // Editar cantidad/precio de un ítem (genera desvío si difiere de OC)
    private void editarItem() {
        int row = tablaDetalles.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para editar.");
            return;
        }

        DetalleComprobanteDTO det = detallesActuales.get(row);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtCant = new JTextField(String.valueOf(det.getCantidad()));
        JTextField txtPrecio = new JTextField(String.valueOf(det.getPrecioUnitario()));

        panel.add(new JLabel("Nueva Cantidad:"));
        panel.add(txtCant);
        panel.add(new JLabel("Nuevo Precio Unitario ($):"));
        panel.add(txtPrecio);

        int res = JOptionPane.showConfirmDialog(this, panel, "Editar - " + det.getDescripcionItem(), JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int c = Integer.parseInt(txtCant.getText().trim());
                float p = Float.parseFloat(txtPrecio.getText().trim());
                if (c <= 0 || p < 0) throw new NumberFormatException();
                det.setCantidad(c);
                det.setPrecioUnitario(p);
                det.setSubTotal(c * p);
                actualizarTabla();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valores inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void quitarItem() {
        int row = tablaDetalles.getSelectedRow();
        if (row >= 0) {
            detallesActuales.remove(row);
            actualizarTabla();
        }
    }

    private void actualizarTabla() {
        modeloDetalles.setRowCount(0);
        for (DetalleComprobanteDTO d : detallesActuales) {
            modeloDetalles.addRow(new Object[]{
                    d.getCodigoItem(),
                    d.getDescripcionItem(),
                    d.getCantidad(),
                    d.getPrecioUnitario(),
                    d.getSubTotal()
            });
        }
    }

    private void guardarFactura() {
        int idxProv = comboProveedores.getSelectedIndex();
        if (idxProv <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor.");
            return;
        }

        String cuit = listaProveedores.get(idxProv - 1).getCuit();
        String desc = txtDescripcion.getText().trim();
        String tipo = comboTipoFactura.getSelectedItem().toString();
        String nroOC = "";

        if (!chkCompraDirecta.isSelected()) {
            int idxOC = comboOC.getSelectedIndex();
            if (idxOC <= 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una Orden de Compra o marcar Compra Directa.");
                return;
            }
            nroOC = listaOCDelProveedor.get(idxOC - 1).getNroOC();
        }

        if (detallesActuales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe haber al menos un ítem en la factura.");
            return;
        }

        FacturaDTO dto = new FacturaDTO(cuit, desc, nroOC, detallesActuales, tipo);

        try {
            FacturaDTO resultado = ControladorComprobantes.getInstance().registrarFactura(dto);
            String estado = resultado.getEstado();
            String msg = "Factura registrada con éxito.\nEstado: " + estado;
            if (estado.equals("PENDIENTE_AUTORIZACION")) {
                msg += "\n⚠ Requiere autorización de supervisor (compra directa o desvíos detectados).";
            } else {
                msg += "\n✓ Impactó en la Cuenta Corriente del proveedor.";
            }
            JOptionPane.showMessageDialog(this, msg, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar Factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}