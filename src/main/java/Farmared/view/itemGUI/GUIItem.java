package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.ItemDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUIItem extends JPanel {

    private DefaultTableModel modeloTablaItems;
    private JFrame ventanaPrincipal;

    public GUIItem(JFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Barra de acciones superior
        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrear = new JButton("Crear Ítem");
        JButton btnModificar = new JButton("Modificar Ítem");
        JButton btnEliminar = new JButton("Eliminar Ítem");
        JButton btnGestionarUDM = new JButton("Gestionar UDM");
        JButton btnPreciosProv = new JButton("Ver Precios por Prov.");

        barraAcciones.add(btnCrear);
        barraAcciones.add(btnModificar);
        barraAcciones.add(btnEliminar);
        barraAcciones.add(new JSeparator(SwingConstants.VERTICAL));
        barraAcciones.add(btnGestionarUDM);
        barraAcciones.add(btnPreciosProv);

        this.add(barraAcciones, BorderLayout.NORTH);

        // Tabla central unificada
        String[] columnas = {"Código", "Tipo", "Descripción", "Unidad", "Tipo UDM", "IVA", "Rubro"};
        modeloTablaItems = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaItems = new JTable(modeloTablaItems);
        tablaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaItems);
        this.add(scrollPane, BorderLayout.CENTER);

        // Listeners de los botones
        btnCrear.addActionListener(e -> {
            VistaAltaItem vistaAlta = new VistaAltaItem(ventanaPrincipal);
            vistaAlta.setVisible(true);
            actualizarTabla();
        });

        btnModificar.addActionListener(e -> {
            VistaModificarItem vistaModificar = new VistaModificarItem(ventanaPrincipal);
            vistaModificar.setVisible(true);
            actualizarTabla();
        });

        btnEliminar.addActionListener(e -> {
            VistaEliminarItem vistaEliminar = new VistaEliminarItem(ventanaPrincipal);
            vistaEliminar.setVisible(true);
            actualizarTabla();
        });

        btnGestionarUDM.addActionListener(e -> {
            VistaABMUnidadDeMedida vistaUDM = new VistaABMUnidadDeMedida(ventanaPrincipal);
            vistaUDM.setVisible(true);
            actualizarTabla(); 
        });

        btnPreciosProv.addActionListener(e -> {
            int fila = tablaItems.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un ítem de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String codigoItem = (String) modeloTablaItems.getValueAt(fila, 0);
            VistaPreciosPorProveedor vistaPrecios = new VistaPreciosPorProveedor(ventanaPrincipal, codigoItem);
            vistaPrecios.setVisible(true);
        });

        // Llenar tabla inicialmente
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTablaItems.setRowCount(0);
        ArrayList<ItemDTO> items = ControladorProductosYServicios.getInstance().obtenerItemsDTO();
        for (ItemDTO item : items) {
            Object[] fila = {
                    item.getCodigo(),
                    item.getTipoItem() != null ? item.getTipoItem() : "N/A",
                    item.getDescripcionDeItem(),
                    item.getDescripcionUnidadMedida(),
                    item.getTipoUDM(),
                    item.getTipoDeIVA(),
                    item.getRubro()
            };
            modeloTablaItems.addRow(fila);
        }
    }
}