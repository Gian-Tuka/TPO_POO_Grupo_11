package Farmared.view.proveedorGUI;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.comprobante.ComprobanteDTO;
import Farmared.dto.proveedor.CuentaCorrienteDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VistaCuentaCorriente extends JDialog {

    private String cuitProveedor;
    private JLabel lblTope;
    private JLabel lblDeuda;
    private DefaultTableModel modeloTabla;

    public VistaCuentaCorriente(Window parent, String cuitProveedor) {
        super(parent, "Cuenta Corriente - Proveedor " + cuitProveedor, ModalityType.APPLICATION_MODAL);
        this.cuitProveedor = cuitProveedor;

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelNorte = new JPanel(new GridLayout(2, 1));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTope = new JLabel("Tope de Deuda: $0.0");
        lblDeuda = new JLabel("Deuda Actual: $0.0");
        lblTope.setFont(new Font("Arial", Font.BOLD, 14));
        lblDeuda.setFont(new Font("Arial", Font.BOLD, 14));
        panelNorte.add(lblTope);
        panelNorte.add(lblDeuda);
        add(panelNorte, BorderLayout.NORTH);

        String[] columnas = {"Tipo", "Número", "Fecha", "Monto Total"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() {
        try {
            CuentaCorrienteDTO cc = ControladorProveedores.getInstance().obtenerCuentaCorrienteDTO(cuitProveedor);
            lblTope.setText("Tope de Deuda: $" + cc.getTopeDeuda());
            lblDeuda.setText("Deuda Actual: $" + cc.getDeudaActual());

            modeloTabla.setRowCount(0);
            ArrayList<ComprobanteDTO> comprobantes = cc.getComprobantes();
            for (ComprobanteDTO c : comprobantes) {
                Object[] fila = {
                        c.getTipoComprobante(),
                        c.getNumero(),
                        c.getFecha(),
                        "$" + c.getMontoTotal()
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar cuenta corriente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
}
