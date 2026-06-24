package Farmared.view.ordenCompra;

import Farmared.controller.ordenes.ControladorDeOrdenDeCompra;
import Farmared.dto.ordenes.OrdenDeCompraDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrdenDeCompraGUI extends JPanel {
    private JTable tablaOC;
    private DefaultTableModel tableModel;

    public OrdenDeCompraGUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel Superior con Botones
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrear = new JButton("Crear Orden de Compra");
        JButton btnModificar = new JButton("Modificar Orden de Compra");
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnAutorizar = new JButton("Autorizar Seleccionada");

        panelSuperior.add(btnCrear);
        panelSuperior.add(btnModificar);
        panelSuperior.add(btnAutorizar);
        panelSuperior.add(btnRefrescar);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla Central
        String[] columnas = {"Nro OC", "Fecha Emisión", "CUIT Prov.", "Razón Social", "Estado", "Total", "Creador"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        tablaOC = new JTable(tableModel);
        tablaOC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaOC), BorderLayout.CENTER);

        // Eventos
        btnRefrescar.addActionListener(e -> cargarDatos());

        btnCrear.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            CrearOrdenCompraDialog dialog = new CrearOrdenCompraDialog(topFrame);
            dialog.setVisible(true);
            cargarDatos(); // Refrescar al cerrar
        });

        btnModificar.addActionListener(e -> {
            int filaSeleccionada = tablaOC.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una Orden de Compra para modificar.");
                return;
            }
            String nroOC = (String) tableModel.getValueAt(filaSeleccionada, 0);
            JOptionPane.showMessageDialog(this, "El controlador actualmente no soporta la modificación de OCs emitidas. (Funcionalidad pendiente en el backend para la OC: " + nroOC + ")");
        });

        btnAutorizar.addActionListener(e -> {
            int filaSeleccionada = tablaOC.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una Orden de Compra para autorizar.");
                return;
            }
            String nroOC = (String) tableModel.getValueAt(filaSeleccionada, 0);
            String estado = (String) tableModel.getValueAt(filaSeleccionada, 4);

            if (!"PENDIENTE_AUTORIZACION".equals(estado)) {
                JOptionPane.showMessageDialog(this, "Solo se pueden autorizar OCs en estado PENDIENTE_AUTORIZACION.");
                return;
            }

            String legajo = JOptionPane.showInputDialog(this, "Ingrese el Legajo del Supervisor autorizante:");
            if (legajo != null && !legajo.trim().isEmpty()) {
                String comentario = JOptionPane.showInputDialog(this, "Ingrese un comentario u observación:");
                if (comentario != null) {
                    try {
                        ControladorDeOrdenDeCompra.getInstance().autorizarOC(nroOC, legajo, comentario);
                        JOptionPane.showMessageDialog(this, "Orden de Compra autorizada con éxito.");
                        cargarDatos();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error al autorizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Carga Inicial
        cargarDatos();
    }

    private void cargarDatos() {
        tableModel.setRowCount(0);
        try {
            List<OrdenDeCompraDTO> ordenes = ControladorDeOrdenDeCompra.getInstance().obtenerOrdenesDeCompraDTO();
            for (OrdenDeCompraDTO dto : ordenes) {
                tableModel.addRow(new Object[]{
                        dto.getNroOC(),
                        dto.getFechaEmision(),
                        dto.getCuitProveedor(),
                        dto.getRazonSocialProveedor(),
                        dto.getEstado(),
                        String.format("$%.2f", dto.getTotal()),
                        dto.getCreadorLegajo()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar órdenes de compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
