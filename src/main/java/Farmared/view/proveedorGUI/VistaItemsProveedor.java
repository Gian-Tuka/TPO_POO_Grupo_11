package Farmared.view.proveedorGUI;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.PrecioProveedorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VistaItemsProveedor extends JDialog {

    private String cuitProveedor;
    private DefaultTableModel modeloTabla;

    public VistaItemsProveedor(Window parent, String cuitProveedor) {
        super(parent, "Ítems Asociados - Proveedor " + cuitProveedor, ModalityType.APPLICATION_MODAL);
        this.cuitProveedor = cuitProveedor;

        setSize(500, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columnas = {"Código Ítem", "Descripción", "Precio", "Fecha Actualización"};
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
            ArrayList<PrecioProveedorDTO> items = ControladorProveedores.getInstance().obtenerItemsPorProveedor(cuitProveedor);
            modeloTabla.setRowCount(0);
            for (PrecioProveedorDTO dto : items) {
                Object[] fila = {
                        dto.getCodigoItem(),
                        dto.getDescripcionItem(),
                        "$" + dto.getPrecio(),
                        dto.getFecha()
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar ítems: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
}
