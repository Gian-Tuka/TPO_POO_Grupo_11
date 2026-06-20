package Farmared.view;

import javax.swing.*;
import java.awt.*;

public class OrdenDePagoDialog extends JDialog {

    private JComboBox<String> comboProveedor;
    private JTable tablaComprobantes;
    private JComboBox<String> comboFormaPago;

    public OrdenDePagoDialog(JFrame parent) {
        super(parent, "Emitir Orden de Pago", true);
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Panel superior - selección de Proveedor y Forma de Pago
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        formPanel.add(new JLabel("Proveedor:"));
        String[] proveedoresMock = {"-- Seleccionar --", "Proveedor Alfa S.A.", "Distribuidora Beta SRL"};
        comboProveedor = new JComboBox<>(proveedoresMock);
        formPanel.add(comboProveedor);

        formPanel.add(new JLabel("Forma de Pago:"));
        String[] formasPagoMock = {"-- Seleccionar --", "Efectivo", "Transferencia", "Cheque"};
        comboFormaPago = new JComboBox<>(formasPagoMock);
        formPanel.add(comboFormaPago);

        add(formPanel, BorderLayout.NORTH);

        // Tabla central - Comprobantes a incluir en la OP (selección múltiple)
        String[] columnas = {"Incluir", "N° Comprobante", "Fecha", "Monto"};
        Object[][] comprobantesMock = {
                {Boolean.FALSE, "FC-0001", "10/06/2026", "$15000"},
                {Boolean.FALSE, "FC-0002", "12/06/2026", "$8500"},
                {Boolean.FALSE, "FC-0003", "15/06/2026", "$22000"}
        };

        tablaComprobantes = new JTable(comprobantesMock, columnas) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };

        JScrollPane scrollPane = new JScrollPane(tablaComprobantes);
        add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEmitir = new JButton("Emitir OP");
        JButton btnCancelar = new JButton("Cancelar");

        botonera.add(btnCancelar);
        botonera.add(btnEmitir);
        add(botonera, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
        btnEmitir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Orden de Pago emitida!");
            dispose();
        });
    }

    public String getProveedor() { return (String) comboProveedor.getSelectedItem(); }
    public String getFormaPago() { return (String) comboFormaPago.getSelectedItem(); }
    public JTable getTablaComprobantes() { return tablaComprobantes; }
}