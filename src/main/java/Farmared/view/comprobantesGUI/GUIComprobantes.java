package Farmared.view.comprobantesGUI;

import Farmared.controller.comprobantes.ControladorComprobantes;
import Farmared.dto.comprobante.FacturaDTO;
import Farmared.dto.comprobante.NotaCreditoDTO;
import Farmared.dto.comprobante.NotaDebitoDTO;

import javax.swing.*;
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
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(new Color(245, 245, 250));

        // --- BARRA DE ACCIONES ---
        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        barraAcciones.setBackground(new Color(245, 245, 250));

        JButton btnAltaFactura = crearBoton("Alta Factura", new Color(46, 204, 113));
        JButton btnModFactura = crearBoton("Modificar Factura", new Color(52, 152, 219));
        JButton btnAltaND = crearBoton("Alta Nota Débito", new Color(155, 89, 182));
        JButton btnModND = crearBoton("Modificar Nota Débito", new Color(41, 128, 185));
        JButton btnAltaNC = crearBoton("Alta Nota Crédito", new Color(230, 126, 34));
        JButton btnModNC = crearBoton("Modificar Nota Crédito", new Color(26, 188, 156));

        barraAcciones.add(btnAltaFactura);
        barraAcciones.add(btnModFactura);
        barraAcciones.add(btnAltaND);
        barraAcciones.add(btnModND);
        barraAcciones.add(btnAltaNC);
        barraAcciones.add(btnModNC);

        this.add(barraAcciones, BorderLayout.NORTH);

        // --- TABLAS ---
        JPanel panelTablas = new JPanel(new GridLayout(1, 3, 10, 0));
        panelTablas.setBackground(new Color(245, 245, 250));

        String[] columnas = {"Nro", "Proveedor", "Descripción", "Monto", "Estado"};

        // Facturas
        modeloFacturas = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaFacturas = crearTabla(modeloFacturas);
        panelTablas.add(crearPanelTabla("Facturas", tablaFacturas));

        // Notas de Débito
        modeloNotasDebito = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaNotasDebito = crearTabla(modeloNotasDebito);
        panelTablas.add(crearPanelTabla("Notas de Débito", tablaNotasDebito));

        // Notas de Crédito
        modeloNotasCredito = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaNotasCredito = crearTabla(modeloNotasCredito);
        panelTablas.add(crearPanelTabla("Notas de Crédito", tablaNotasCredito));

        this.add(panelTablas, BorderLayout.CENTER);

        // --- LISTENERS ---
        btnAltaFactura.addActionListener(e -> {
            VistaAltaFactura vista = new VistaAltaFactura(ventanaPrincipal);
            vista.setVisible(true);
            actualizarTablas();
        });

        btnModFactura.addActionListener(e -> {
            int fila = tablaFacturas.getSelectedRow();
            if (fila >= 0) {
                int nro = (int) tablaFacturas.getValueAt(fila, 0);
                VistaModificacionFactura vista = new VistaModificacionFactura(ventanaPrincipal, nro);
                vista.setVisible(true);
                actualizarTablas();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una factura a modificar.");
            }
        });

        btnAltaND.addActionListener(e -> {
            VistaAltaNotaDebito vista = new VistaAltaNotaDebito(ventanaPrincipal);
            vista.setVisible(true);
            actualizarTablas();
        });

        btnModND.addActionListener(e -> {
            int fila = tablaNotasDebito.getSelectedRow();
            if (fila >= 0) {
                int nro = (int) tablaNotasDebito.getValueAt(fila, 0);
                VistaModificacionNotaDebito vista = new VistaModificacionNotaDebito(ventanaPrincipal, nro);
                vista.setVisible(true);
                actualizarTablas();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una nota de débito a modificar.");
            }
        });

        btnAltaNC.addActionListener(e -> {
            VistaAltaNotaCredito vista = new VistaAltaNotaCredito(ventanaPrincipal);
            vista.setVisible(true);
            actualizarTablas();
        });

        btnModNC.addActionListener(e -> {
            int fila = tablaNotasCredito.getSelectedRow();
            if (fila >= 0) {
                int nro = (int) tablaNotasCredito.getValueAt(fila, 0);
                VistaModificacionNotaCredito vista = new VistaModificacionNotaCredito(ventanaPrincipal, nro);
                vista.setVisible(true);
                actualizarTablas();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una nota de crédito a modificar.");
            }
        });

        actualizarTablas();
    }

    private JButton crearBoton(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(230, 126, 34));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return tabla;
    }

    private JPanel crearPanelTabla(String titulo, JTable tabla) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245, 245, 250));
        JLabel lbl = new JLabel(titulo, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(44, 62, 80));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        p.add(lbl, BorderLayout.NORTH);
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    public void actualizarTablas() {
        modeloFacturas.setRowCount(0);
        ArrayList<FacturaDTO> facturas = ControladorComprobantes.getInstance().obtenerFacturasDTO();
        for (FacturaDTO f : facturas) {
            modeloFacturas.addRow(new Object[]{f.getNroComprobante(), f.getRazonSocialProveedor(), f.getDescripcion(), f.getMontoTotal(), f.getEstado()});
        }

        modeloNotasDebito.setRowCount(0);
        ArrayList<NotaDebitoDTO> notasDebito = ControladorComprobantes.getInstance().obtenerNotasDeDebitoDTO();
        for (NotaDebitoDTO nd : notasDebito) {
            modeloNotasDebito.addRow(new Object[]{nd.getNroComprobante(), nd.getRazonSocialProveedor(), nd.getDescripcion(), nd.getMonto(), nd.getEstado()});
        }

        modeloNotasCredito.setRowCount(0);
        ArrayList<NotaCreditoDTO> notasCredito = ControladorComprobantes.getInstance().obtenerNotasDeCreditoDTO();
        for (NotaCreditoDTO nc : notasCredito) {
            modeloNotasCredito.addRow(new Object[]{nc.getNroComprobante(), nc.getRazonSocialProveedor(), nc.getDescripcion(), nc.getMonto(), nc.getEstado()});
        }
    }
}
