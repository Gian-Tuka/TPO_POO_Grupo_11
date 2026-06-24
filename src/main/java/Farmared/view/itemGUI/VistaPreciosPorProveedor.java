package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.PrecioProveedorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VistaPreciosPorProveedor extends JDialog {

    private String codigoItem;
    private DefaultTableModel modeloTabla;

    public VistaPreciosPorProveedor(Window parent, String codigoItem) {
        super(parent, "Precios por Proveedor - Ítem " + codigoItem, ModalityType.APPLICATION_MODAL);
        this.codigoItem = codigoItem;

        setSize(500, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columnas = {"CUIT Proveedor", "Razón Social", "Precio", "Fecha Actualización"};
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
            ArrayList<PrecioProveedorDTO> precios = ControladorProductosYServicios.getInstance().obtenerProveedoresPorItem(codigoItem);
            modeloTabla.setRowCount(0);
            for (PrecioProveedorDTO dto : precios) {
                Object[] fila = {
                        dto.getCuitProveedor(),
                        dto.getRazonSocial(),
                        "$" + dto.getPrecio(),
                        dto.getFecha()
                };
                modeloTabla.addRow(fila);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar precios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
}
