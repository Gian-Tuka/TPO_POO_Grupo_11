package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.dto.comprobante.FacturaDTO;
import Farmared.dto.comprobante.NotaCreditoDTO;
import Farmared.dto.comprobante.NotaDebitoDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class GUIComprobantes extends JPanel {

    private DefaultTableModel modeloFacturas;
    private DefaultTableModel modeloNotasCredito;
    private DefaultTableModel modeloNotasDebito;
    private JTable tablaFacturas;
    private JTable tablaNotasCredito;
    private JTable tablaNotasDebito;
    private JFrame ventanaPrincipal;

    public GUIComprobantes(JFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.setLayout(new BorderLayout(15, 15));
        this.setBorder(new EmptyBorder(25, 25, 25, 25));
        this.setBackground(Color.WHITE);

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("Gestión de Comprobantes", SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(44, 62, 80));
        add(lblTitulo, BorderLayout.NORTH);

        // --- CONTENIDO CENTRAL ---
        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setBackground(Color.WHITE);

        // Barra de acciones
        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        barraAcciones.setBackground(Color.WHITE);

        JButton btnAltaFactura = crearBotonAccion("Nueva Factura", new Color(52, 152, 219));
        btnAltaFactura.addActionListener(e -> {
            VistaAltaFactura v = new VistaAltaFactura(ventanaPrincipal);
            v.setVisible(true);
            actualizarTablas();
        });

        JButton btnAltaNC = crearBotonAccion("Nueva Nota Crédito", new Color(46, 204, 113));
        btnAltaNC.addActionListener(e -> {
            VistaAltaNotaCredito v = new VistaAltaNotaCredito(ventanaPrincipal);
            v.setVisible(true);
            actualizarTablas();
        });

        JButton btnAltaND = crearBotonAccion("Nueva Nota Débito", new Color(155, 89, 182));
        btnAltaND.addActionListener(e -> {
            VistaAltaNotaDebito v = new VistaAltaNotaDebito(ventanaPrincipal);
            v.setVisible(true);
            actualizarTablas();
        });

        JButton btnAuditar = crearBotonAccion("Autorizar / Auditar Seleccionado", new Color(241, 196, 15));
        btnAuditar.addActionListener(e -> abrirAuditoriaComprobante());

        barraAcciones.add(btnAltaFactura);
        barraAcciones.add(btnAltaNC);
        barraAcciones.add(btnAltaND);
        barraAcciones.add(btnAuditar);
        panelCentro.add(barraAcciones, BorderLayout.NORTH);

        // --- SECCIÓN TABLAS ---
        JPanel contenedorTablas = new JPanel(new GridLayout(3, 1, 0, 20));
        contenedorTablas.setBackground(Color.WHITE);

        modeloFacturas = new DefaultTableModel(new Object[]{"Nro Comprobante", "Proveedor", "Descripción", "Total", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaFacturas = crearTablaEstilizada(modeloFacturas);
        contenedorTablas.add(crearPanelTabla("Facturas Recibidas", tablaFacturas, new Color(52, 152, 219)));

        modeloNotasDebito = new DefaultTableModel(new Object[]{"Nro Comprobante", "Proveedor", "Descripción", "Monto", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaNotasDebito = crearTablaEstilizada(modeloNotasDebito);
        contenedorTablas.add(crearPanelTabla("Notas de Débito", tablaNotasDebito, new Color(155, 89, 182)));

        modeloNotasCredito = new DefaultTableModel(new Object[]{"Nro Comprobante", "Proveedor", "Descripción", "Monto", "Estado"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaNotasCredito = crearTablaEstilizada(modeloNotasCredito);
        contenedorTablas.add(crearPanelTabla("Notas de Crédito", tablaNotasCredito, new Color(46, 204, 113)));

        panelCentro.add(contenedorTablas, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        actualizarTablas();
    }

    private JButton crearBotonAccion(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTable crearTablaEstilizada(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(30);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setShowVerticalLines(false);
        t.setGridColor(new Color(230, 230, 230));

        JTableHeader header = t.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 245, 250));
        header.setForeground(new Color(44, 62, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        return t;
    }

    private JPanel crearPanelTabla(String titulo, JTable tabla, Color accentColor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 4, 1, 0, accentColor));
        
        JLabel lbl = new JLabel("  " + titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(44, 62, 80));
        lbl.setBorder(new EmptyBorder(10, 5, 10, 5));
        
        headerPanel.add(lbl, BorderLayout.CENTER);

        p.add(headerPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        p.add(scrollPane, BorderLayout.CENTER);
        
        return p;
    }

    private void abrirAuditoriaComprobante() {
        String codigoSeleccionado = null;
        int tipoComprobante = -1; // 0: Factura, 1: ND, 2: NC

        if (tablaFacturas.getSelectedRow() >= 0) {
            codigoSeleccionado = tablaFacturas.getValueAt(tablaFacturas.getSelectedRow(), 0).toString();
            tipoComprobante = 0;
        } else if (tablaNotasDebito.getSelectedRow() >= 0) {
            codigoSeleccionado = tablaNotasDebito.getValueAt(tablaNotasDebito.getSelectedRow(), 0).toString();
            tipoComprobante = 1;
        } else if (tablaNotasCredito.getSelectedRow() >= 0) {
            codigoSeleccionado = tablaNotasCredito.getValueAt(tablaNotasCredito.getSelectedRow(), 0).toString();
            tipoComprobante = 2;
        }

        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un comprobante de alguna tabla para auditar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (tipoComprobante == 2) {
            VistaModificacionNotaCredito v = new VistaModificacionNotaCredito(ventanaPrincipal, codigoSeleccionado);
            v.setVisible(true);
        } else {
            int conf = JOptionPane.showConfirmDialog(this, "¿Desea dar autorización formal al comprobante " + codigoSeleccionado + "?", "Auditoría de Comprobantes", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    ControladorComprobantes.getInstance().autorizarComprobante(codigoSeleccionado);
                    JOptionPane.showMessageDialog(this, "Comprobante autorizado correctamente. Se han impactado los saldos en la Cuenta Corriente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al autorizar: " + ex.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        actualizarTablas();
    }

    public void actualizarTablas() {
        modeloFacturas.setRowCount(0);
        ArrayList<FacturaDTO> facturas = ControladorComprobantes.getInstance().obtenerFacturasDTO();
        for (FacturaDTO f : facturas) {
            modeloFacturas.addRow(new Object[]{f.getNroComprobante(), f.getRazonSocialProveedor(), f.getDescripcion(), f.getMontoTotal(), f.getEstado()});
        }

        modeloNotasDebito.setRowCount(0);
        ArrayList<NotaDebitoDTO> intentND = ControladorComprobantes.getInstance().obtenerNotasDeDebitoDTO();
        for (NotaDebitoDTO nd : intentND) {
            modeloNotasDebito.addRow(new Object[]{nd.getNroComprobante(), nd.getRazonSocialProveedor(), nd.getDescripcion(), nd.getMonto(), nd.getEstado()});
        }

        modeloNotasCredito.setRowCount(0);
        ArrayList<NotaCreditoDTO> intentNC = ControladorComprobantes.getInstance().obtenerNotasDeCreditoDTO();
        for (NotaCreditoDTO nc : intentNC) {
            modeloNotasCredito.addRow(new Object[]{nc.getNroComprobante(), nc.getRazonSocialProveedor(), nc.getDescripcion(), nc.getMonto(), nc.getEstado()});
        }
    }
}