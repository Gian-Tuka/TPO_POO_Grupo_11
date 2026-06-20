package Farmared.view.proveedorGUI;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.proveedor.ProveedorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUIProveedor extends JPanel {

    private DefaultTableModel modeloTablaProveedores;

    public GUIProveedor() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Barra de acciones
        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton crearProveedor = new JButton("Crear Proveedor");
        JButton modificarProveedor = new JButton("Modificar Proveedor");
        JButton eliminarProveedor = new JButton("Eliminar Proveedor");

        barraAcciones.add(crearProveedor);
        barraAcciones.add(modificarProveedor);
        barraAcciones.add(eliminarProveedor);
        this.add(barraAcciones, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Razón Social", "CUIT", "Teléfono", "Condición IVA"};
        modeloTablaProveedores = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTablaProveedores);
        JScrollPane scrollPane = new JScrollPane(tabla);
        this.add(scrollPane, BorderLayout.CENTER);

        // Llamada al método que llena la tabla
        actualizarTablaProveedores();

        // Listeners de los botones
        crearProveedor.addActionListener(e -> {
            VistaAltaProveedor vistaAltaProveedor = new VistaAltaProveedor();
            vistaAltaProveedor.setModal(true);
            vistaAltaProveedor.setVisible(true);
            actualizarTablaProveedores();
        });

        modificarProveedor.addActionListener(e -> {
            VistaModificarProveedor vistaModificarProveedor = new VistaModificarProveedor();
            vistaModificarProveedor.setModal(true);
            vistaModificarProveedor.setVisible(true);
            actualizarTablaProveedores();
        });

        eliminarProveedor.addActionListener(e -> {
            VistaEliminarProveedor vistaEliminarProveedor = new VistaEliminarProveedor();
            vistaEliminarProveedor.setModal(true);
            vistaEliminarProveedor.setVisible(true);
            actualizarTablaProveedores();
        });
    }

    private void actualizarTablaProveedores() {
        modeloTablaProveedores.setRowCount(0);
        ArrayList<ProveedorDTO> listaProveedores = ControladorProveedores.getInstance().obtenerProveedoresDTO();
        for (ProveedorDTO p : listaProveedores) {
            Object[] fila = {
                    p.getRazonSocial(),
                    p.getCuit(),
                    p.getTelefono(),
                    p.getCondicionIVA()
            };
            modeloTablaProveedores.addRow(fila);
        }
    }
}