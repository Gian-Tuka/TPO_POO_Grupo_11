package Farmared.view.ordenDePago;

import Farmared.controller.ordenesDePago.ControladorDeOrdenesDePago;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.ordenesDePago.DetalleCancelacionDTO;
import Farmared.dto.ordenesDePago.FormaDePagoDTO;
import Farmared.dto.ordenesDePago.OrdenDePagoDTO;
import Farmared.dto.proveedor.ProveedorDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VistaOrdenDePago extends JPanel {

    // Componentes principales
    private JComboBox<String> comboProveedor;
    private ArrayList<ProveedorDTO> listaProveedores;

    private JTable tablaComprobantes;
    private DefaultTableModel modeloTabla;

    private JLabel lblTotalBruto;
    private JLabel lblRetenciones;
    private JLabel lblTotalNeto;

    // Checkboxes y Paneles
    private JCheckBox chkEfectivo, chkTransferencia, chkChequePropio, chkChequeTerceros;
    private JPanel panelEfectivo, panelTransferencia, panelChequePropio, panelChequeTerceros;

    // Campos de texto para formas de pago
    private JTextField txtMontoEfectivo;
    private JTextField txtMontoTransferencia, txtBancoTransferencia, txtCbuTransferencia;
    private JTextField txtMontoChequePropio, txtNroChequePropio, txtBancoChequePropio, txtFechaEmisionPropio, txtFechaVencimientoPropio, txtFirmantePropio;
    private JTextField txtMontoChequeTerceros, txtNroChequeTerceros, txtBancoChequeTerceros, txtFechaEmisionTerceros, txtFechaVencimientoTerceros, txtFirmanteTerceros, txtCuitTercero;

    private float montoBrutoActual = 0f;
    private float totalNetoActual = 0f;

    public VistaOrdenDePago() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(41, 128, 185));
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Emisión de Orden de Pago");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JButton btnImpuestos = new JButton("⚙ Configurar Impuestos");
        btnImpuestos.setBackground(Color.WHITE);
        btnImpuestos.setForeground(new Color(41, 128, 185));
        btnImpuestos.setFocusPainted(false);
        btnImpuestos.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            new VistaGestorImpuestos((JFrame) parentWindow).setVisible(true);
        });
        panelHeader.add(btnImpuestos, BorderLayout.EAST);
        
        add(panelHeader, BorderLayout.NORTH);

        // Body (Scrollable)
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        panelPrincipal.add(crearPanelProveedor());
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(crearPanelComprobantes());
        panelPrincipal.add(Box.createVerticalStrut(15));
        panelPrincipal.add(crearPanelPagos());

        JScrollPane scroll = new JScrollPane(panelPrincipal);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Footer (Resumen y botones)
        add(crearPanelFooter(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelProveedor() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "1. Selección de Proveedor"));

        listaProveedores = ControladorProveedores.getInstance().obtenerProveedoresDTO();
        comboProveedor = new JComboBox<>();
        comboProveedor.addItem("Seleccione un Proveedor...");
        for (ProveedorDTO p : listaProveedores) {
            comboProveedor.addItem(p.getRazonSocial() + " (CUIT: " + p.getCuit() + ")");
        }
        comboProveedor.addActionListener(e -> actualizarComprobantes());

        panel.add(new JLabel("Proveedor:"), BorderLayout.WEST);
        panel.add(comboProveedor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelComprobantes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "2. Comprobantes Pendientes de Pago"));

        String[] columnas = {"Seleccionar", "Nro Comprobante", "Estado", "Saldo Pendiente", "Monto a Pagar"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return (col == 0) ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0 || col == 4; // Solo seleccion y monto a pagar
            }
        };
        tablaComprobantes = new JTable(modeloTabla);
        tablaComprobantes.setRowHeight(25);

        panel.add(new JScrollPane(tablaComprobantes), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelPagos() {
        JPanel panelGeneral = new JPanel();
        panelGeneral.setLayout(new BoxLayout(panelGeneral, BoxLayout.Y_AXIS));
        panelGeneral.setBackground(Color.WHITE);
        panelGeneral.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), "3. Formas de Pago"));

        JPanel panelChecks = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelChecks.setBackground(Color.WHITE);
        
        chkEfectivo = new JCheckBox("Efectivo");
        chkTransferencia = new JCheckBox("Transferencia");
        chkChequePropio = new JCheckBox("Cheque Propio");
        chkChequeTerceros = new JCheckBox("Cheque de Terceros");

        chkEfectivo.setBackground(Color.WHITE);
        chkTransferencia.setBackground(Color.WHITE);
        chkChequePropio.setBackground(Color.WHITE);
        chkChequeTerceros.setBackground(Color.WHITE);

        panelChecks.add(chkEfectivo);
        panelChecks.add(chkTransferencia);
        panelChecks.add(chkChequePropio);
        panelChecks.add(chkChequeTerceros);

        panelGeneral.add(panelChecks);

        // Subpaneles
        panelEfectivo = crearSubPanelPagos(new String[]{"Monto:"}, new JTextField[]{txtMontoEfectivo = new JTextField()});
        panelTransferencia = crearSubPanelPagos(new String[]{"Monto:", "Banco:", "CBU:"}, new JTextField[]{txtMontoTransferencia = new JTextField(), txtBancoTransferencia = new JTextField(), txtCbuTransferencia = new JTextField()});
        
        txtMontoChequePropio = new JTextField(); txtNroChequePropio = new JTextField(); txtBancoChequePropio = new JTextField(); 
        txtFechaEmisionPropio = new JTextField("dd/MM/yyyy"); txtFechaVencimientoPropio = new JTextField("dd/MM/yyyy"); txtFirmantePropio = new JTextField();
        panelChequePropio = crearSubPanelPagos(
                new String[]{"Monto:", "Nro Cheque:", "Banco:", "Fecha Emisión:", "Fecha Vencimiento:", "Legajo Firmante:"}, 
                new JTextField[]{txtMontoChequePropio, txtNroChequePropio, txtBancoChequePropio, txtFechaEmisionPropio, txtFechaVencimientoPropio, txtFirmantePropio}
        );

        txtMontoChequeTerceros = new JTextField(); txtNroChequeTerceros = new JTextField(); txtBancoChequeTerceros = new JTextField();
        txtFechaEmisionTerceros = new JTextField("dd/MM/yyyy"); txtFechaVencimientoTerceros = new JTextField("dd/MM/yyyy"); txtFirmanteTerceros = new JTextField(); txtCuitTercero = new JTextField();
        panelChequeTerceros = crearSubPanelPagos(
                new String[]{"Monto:", "Nro Cheque:", "Banco:", "Fecha Emisión:", "Fecha Vencimiento:", "Legajo Firmante:", "CUIT Tercero:"}, 
                new JTextField[]{txtMontoChequeTerceros, txtNroChequeTerceros, txtBancoChequeTerceros, txtFechaEmisionTerceros, txtFechaVencimientoTerceros, txtFirmanteTerceros, txtCuitTercero}
        );

        chkEfectivo.addActionListener(e -> panelEfectivo.setVisible(chkEfectivo.isSelected()));
        chkTransferencia.addActionListener(e -> panelTransferencia.setVisible(chkTransferencia.isSelected()));
        chkChequePropio.addActionListener(e -> panelChequePropio.setVisible(chkChequePropio.isSelected()));
        chkChequeTerceros.addActionListener(e -> panelChequeTerceros.setVisible(chkChequeTerceros.isSelected()));

        panelGeneral.add(panelEfectivo);
        panelGeneral.add(panelTransferencia);
        panelGeneral.add(panelChequePropio);
        panelGeneral.add(panelChequeTerceros);

        return panelGeneral;
    }

    private JPanel crearSubPanelPagos(String[] etiquetas, JTextField[] campos) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 20, 5, 20));
        for (int i = 0; i < etiquetas.length; i++) {
            panel.add(new JLabel(etiquetas[i]));
            panel.add(campos[i]);
        }
        panel.setVisible(false);
        return panel;
    }

    private JPanel crearPanelFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        // Resumen a la izquierda
        JPanel panelResumen = new JPanel(new GridLayout(3, 1));
        panelResumen.setBorder(new EmptyBorder(10, 15, 10, 15));
        lblTotalBruto = new JLabel("Total Bruto: $0.00");
        lblRetenciones = new JLabel("Retenciones: $0.00");
        lblTotalNeto = new JLabel("Neto a Pagar: $0.00");
        lblTotalNeto.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelResumen.add(lblTotalBruto);
        panelResumen.add(lblRetenciones);
        panelResumen.add(lblTotalNeto);
        footer.add(panelResumen, BorderLayout.WEST);

        // Botones a la derecha
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        
        JButton btnPrecalcular = new JButton("Pre-calcular Retenciones");
        btnPrecalcular.addActionListener(e -> calcularTotales());
        
        JButton btnEmitir = new JButton("Emitir Orden de Pago");
        btnEmitir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEmitir.setBackground(new Color(39, 174, 96));
        btnEmitir.setForeground(Color.WHITE);
        btnEmitir.addActionListener(e -> emitirOP());

        panelBotones.add(btnPrecalcular);
        panelBotones.add(btnEmitir);
        footer.add(panelBotones, BorderLayout.EAST);

        return footer;
    }

    private void actualizarComprobantes() {
        modeloTabla.setRowCount(0);
        int idx = comboProveedor.getSelectedIndex();
        if (idx <= 0) return;

        String cuit = listaProveedores.get(idx - 1).getCuit();
        ArrayList<DetalleCancelacionDTO> pendientes = ControladorDeOrdenesDePago.getInstance().obtenerComprobantesPendientes(cuit);

        for (DetalleCancelacionDTO det : pendientes) {
            modeloTabla.addRow(new Object[]{
                    Boolean.FALSE,
                    det.getNroComprobante(),
                    det.getEstado(),
                    String.valueOf(det.getMonto()), // Saldo pendiente sugerido
                    String.valueOf(det.getMonto())  // Monto a pagar (editable)
            });
        }
    }

    private void calcularTotales() {
        int idx = comboProveedor.getSelectedIndex();
        if (idx <= 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        montoBrutoActual = 0f;
        for (int row = 0; row < modeloTabla.getRowCount(); row++) {
            Boolean seleccionado = (Boolean) modeloTabla.getValueAt(row, 0);
            if (Boolean.TRUE.equals(seleccionado)) {
                try {
                    float montoFila = Float.parseFloat((String) modeloTabla.getValueAt(row, 4));
                    float saldoMaximo = Float.parseFloat((String) modeloTabla.getValueAt(row, 3));
                    if (montoFila > saldoMaximo) {
                        JOptionPane.showMessageDialog(this, "El monto a pagar no puede ser mayor al saldo pendiente en el comprobante.", "Atención", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    montoBrutoActual += montoFila;
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Hay montos inválidos en la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        if (montoBrutoActual == 0f) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar comprobantes para pagar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cuit = listaProveedores.get(idx - 1).getCuit();
        try {
            float retenciones = ControladorDeOrdenesDePago.getInstance().preCalcularRetenciones(cuit, montoBrutoActual);
            totalNetoActual = montoBrutoActual - retenciones;

            lblTotalBruto.setText("Total Bruto: $" + montoBrutoActual);
            lblRetenciones.setText("Retenciones: $" + retenciones);
            lblTotalNeto.setText("Neto a Pagar: $" + totalNetoActual);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error calculando retenciones: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void emitirOP() {
        try {
            int idx = comboProveedor.getSelectedIndex();
            if (idx <= 0) throw new RuntimeException("Debe seleccionar un proveedor.");
            String cuitProveedor = listaProveedores.get(idx - 1).getCuit();

            calcularTotales(); // Forzar recalculado por las dudas
            if (montoBrutoActual == 0f) return;

            // Armar comprobantes cancelados
            ArrayList<DetalleCancelacionDTO> comprobantes = new ArrayList<>();
            for (int row = 0; row < modeloTabla.getRowCount(); row++) {
                Boolean seleccionado = (Boolean) modeloTabla.getValueAt(row, 0);
                if (Boolean.TRUE.equals(seleccionado)) {
                    String nroComp = (String) modeloTabla.getValueAt(row, 1);
                    float monto = Float.parseFloat((String) modeloTabla.getValueAt(row, 4));
                    comprobantes.add(new DetalleCancelacionDTO(nroComp, monto));
                }
            }

            // Armar formas de pago
            ArrayList<FormaDePagoDTO> formasDePago = new ArrayList<>();
            if (chkEfectivo.isSelected()) {
                formasDePago.add(new FormaDePagoDTO("EFECTIVO", Float.parseFloat(txtMontoEfectivo.getText()), null, null, null, null, null, null, null));
            }
            if (chkTransferencia.isSelected()) {
                formasDePago.add(new FormaDePagoDTO("TRANSFERENCIA", Float.parseFloat(txtMontoTransferencia.getText()), txtBancoTransferencia.getText(), txtCbuTransferencia.getText(), null, null, null, null, null));
            }
            if (chkChequePropio.isSelected()) {
                formasDePago.add(new FormaDePagoDTO("CHEQUE_PROPIO", Float.parseFloat(txtMontoChequePropio.getText()), txtBancoChequePropio.getText(), null, txtNroChequePropio.getText(), txtFechaEmisionPropio.getText(), txtFechaVencimientoPropio.getText(), txtFirmantePropio.getText(), null));
            }
            if (chkChequeTerceros.isSelected()) {
                formasDePago.add(new FormaDePagoDTO("CHEQUE_TERCEROS", Float.parseFloat(txtMontoChequeTerceros.getText()), txtBancoChequeTerceros.getText(), null, txtNroChequeTerceros.getText(), txtFechaEmisionTerceros.getText(), txtFechaVencimientoTerceros.getText(), txtFirmanteTerceros.getText(), txtCuitTercero.getText()));
            }

            if (formasDePago.isEmpty()) {
                throw new RuntimeException("Debe seleccionar al menos una forma de pago.");
            }

            OrdenDePagoDTO dtoEntrada = new OrdenDePagoDTO(cuitProveedor, comprobantes, formasDePago);
            OrdenDePagoDTO resultado = ControladorDeOrdenesDePago.getInstance().emitirOP(dtoEntrada);

            JOptionPane.showMessageDialog(this,
                    "Orden de Pago emitida con éxito.\n" +
                            "Nro OP: " + resultado.getNroOP() + "\n" +
                            "Neto pagado: $" + resultado.getTotalNetoOP() + "\n" +
                            "Retenciones aplicadas: " + resultado.getRetencionesEfectuadas().size(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarComprobantes(); // refresca la tabla

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}