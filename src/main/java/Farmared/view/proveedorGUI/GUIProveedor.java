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

        // Barra de acciones superior (General)
        JPanel barraAccionesNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrarRubro = new JButton("Registrar Rubro");
        barraAccionesNorte.add(btnRegistrarRubro);
        this.add(barraAccionesNorte, BorderLayout.NORTH);

        // Barra de acciones secundaria (ABM y específicos)
        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton crearProveedor = new JButton("Crear Proveedor");
        JButton modificarProveedor = new JButton("Modificar Proveedor");
        JButton eliminarProveedor = new JButton("Eliminar Proveedor");
        
        JButton btnCertificado = new JButton("Registrar Cert. No Ret.");
        JButton btnCC = new JButton("Cuenta Corriente");
        JButton btnItems = new JButton("Ver Ítems");
        JButton btnAsociarPrecio = new JButton("Asociar Precio");

        barraAcciones.add(crearProveedor);
        barraAcciones.add(modificarProveedor);
        barraAcciones.add(eliminarProveedor);
        barraAcciones.add(new JSeparator(SwingConstants.VERTICAL));
        barraAcciones.add(btnCertificado);
        barraAcciones.add(btnCC);
        barraAcciones.add(btnItems);
        barraAcciones.add(btnAsociarPrecio);
        
        this.add(barraAcciones, BorderLayout.SOUTH);

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

        btnRegistrarRubro.addActionListener(e -> {
            Farmared.view.itemGUI.VistaABMRubro vistaRubro = new Farmared.view.itemGUI.VistaABMRubro(null);
            vistaRubro.setVisible(true);
        });

        btnCertificado.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String cuit = (String) modeloTablaProveedores.getValueAt(fila, 1);
            VistaRegistrarCertificado vistaCert = new VistaRegistrarCertificado(null, cuit);
            vistaCert.setVisible(true);
        });

        btnCC.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String cuit = (String) modeloTablaProveedores.getValueAt(fila, 1);
            VistaCuentaCorriente vistaCC = new VistaCuentaCorriente(null, cuit);
            vistaCC.setVisible(true);
        });

        btnItems.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String cuit = (String) modeloTablaProveedores.getValueAt(fila, 1);
            VistaItemsProveedor vistaItems = new VistaItemsProveedor(null, cuit);
            vistaItems.setVisible(true);
        });

        btnAsociarPrecio.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String cuit = (String) modeloTablaProveedores.getValueAt(fila, 1);
            VistaAsociarPrecio vistaAsociar = new VistaAsociarPrecio(null, cuit);
            vistaAsociar.setVisible(true);
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