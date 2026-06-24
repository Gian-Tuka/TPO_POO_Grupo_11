package Farmared.view.ordenCompra;

import Farmared.controller.ordenes.ControladorDeOrdenDeCompra;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.ordenes.DetalleItemDTO;
import Farmared.dto.ordenes.OrdenDeCompraDTO;
import Farmared.dto.proveedor.ProveedorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrearOrdenCompraDialog extends JDialog {
    private JComboBox<String> comboProveedores;
    private JComboBox<String> comboItems;
    private DefaultTableModel tableModelDetalles;
    private JTable tablaDetalles;

    // Maps to keep the original objects by their display string
    private HashMap<String, ProveedorDTO> mapaProveedores = new HashMap<>();
    private HashMap<String, ItemDTO> mapaItems = new HashMap<>();

    // Current list of items to buy
    private List<DetalleItemDTO> detallesTemp = new ArrayList<>();

    public CrearOrdenCompraDialog(JFrame parent) {
        super(parent, "Crear Orden de Compra", true);
        setSize(600, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Top Panel: Form for Selection
        JPanel panelTop = new JPanel(new GridLayout(3, 2, 10, 10));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelTop.add(new JLabel("Seleccionar Proveedor:"));
        comboProveedores = new JComboBox<>();
        panelTop.add(comboProveedores);

        panelTop.add(new JLabel("Ítems Disponibles:"));
        comboItems = new JComboBox<>();
        panelTop.add(comboItems);

        JButton btnAgregarItem = new JButton("Agregar Ítem");
        panelTop.add(new JLabel("")); // Spacer
        panelTop.add(btnAgregarItem);

        add(panelTop, BorderLayout.NORTH);

        // Center Panel: Selected Items Table
        String[] columnas = {"Código", "Descripción", "Cantidad"};
        tableModelDetalles = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDetalles = new JTable(tableModelDetalles);
        add(new JScrollPane(tablaDetalles), BorderLayout.CENTER);

        // Bottom Panel: Actions
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnQuitar = new JButton("Quitar Ítem Seleccionado");
        JButton btnEmitir = new JButton("Emitir Orden de Compra");
        JButton btnCancelar = new JButton("Cancelar");

        panelBottom.add(btnQuitar);
        panelBottom.add(btnCancelar);
        panelBottom.add(btnEmitir);
        add(panelBottom, BorderLayout.SOUTH);

        // --- Logic and Events ---
        cargarProveedores();

        comboProveedores.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                cargarItemsPorProveedor();
                // Limpiar tabla de detalles al cambiar de proveedor
                detallesTemp.clear();
                actualizarTablaDetalles();
            }
        });

        btnAgregarItem.addActionListener(e -> {
            String selectedItemStr = (String) comboItems.getSelectedItem();
            if (selectedItemStr == null || selectedItemStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un ítem.");
                return;
            }

            ItemDTO itemDTO = mapaItems.get(selectedItemStr);
            if (itemDTO != null) {
                String cantidadStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad para " + itemDTO.getDescripcionDeItem() + ":");
                if (cantidadStr != null && !cantidadStr.trim().isEmpty()) {
                    try {
                        int cantidad = Integer.parseInt(cantidadStr);
                        if (cantidad <= 0) {
                            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                            return;
                        }

                        // Verificar si ya existe en la lista temporal para sumar cantidad
                        boolean existe = false;
                        for (DetalleItemDTO det : detallesTemp) {
                            if (det.getCodigoItem().equals(itemDTO.getCodigo())) {
                                det.setCantidad(det.getCantidad() + cantidad);
                                existe = true;
                                break;
                            }
                        }
                        if (!existe) {
                            detallesTemp.add(new DetalleItemDTO(itemDTO.getCodigo(), cantidad));
                        }
                        actualizarTablaDetalles();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Cantidad inválida. Ingrese un número entero.");
                    }
                }
            }
        });

        btnQuitar.addActionListener(e -> {
            int fila = tablaDetalles.getSelectedRow();
            if (fila != -1) {
                detallesTemp.remove(fila);
                actualizarTablaDetalles();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para quitar.");
            }
        });

        btnEmitir.addActionListener(e -> {
            if (detallesTemp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un ítem a la orden de compra.");
                return;
            }

            String selectedProvStr = (String) comboProveedores.getSelectedItem();
            ProveedorDTO provDTO = mapaProveedores.get(selectedProvStr);

            if (provDTO != null) {
                try {
                    OrdenDeCompraDTO nuevaOC = new OrdenDeCompraDTO(provDTO.getCuit(), new ArrayList<>(detallesTemp));
                    ControladorDeOrdenDeCompra.getInstance().emitirOC(nuevaOC);
                    JOptionPane.showMessageDialog(this, "Orden de Compra emitida exitosamente.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al emitir OC: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancelar.addActionListener(e -> dispose());
        
        // Initial load of items for the first provider
        if (comboProveedores.getItemCount() > 0) {
            cargarItemsPorProveedor();
        }
    }

    private void cargarProveedores() {
        comboProveedores.removeAllItems();
        mapaProveedores.clear();
        try {
            ArrayList<ProveedorDTO> proveedores = ControladorProveedores.getInstance().obtenerProveedoresDTO();
            for (ProveedorDTO p : proveedores) {
                String displayStr = p.getCuit() + " - " + p.getRazonSocial();
                mapaProveedores.put(displayStr, p);
                comboProveedores.addItem(displayStr);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void cargarItemsPorProveedor() {
        comboItems.removeAllItems();
        mapaItems.clear();
        String selectedProvStr = (String) comboProveedores.getSelectedItem();
        if (selectedProvStr == null) return;

        ProveedorDTO provDTO = mapaProveedores.get(selectedProvStr);
        if (provDTO != null) {
            try {
                ArrayList<ItemDTO> items = ControladorDeOrdenDeCompra.getInstance().obtenerItemsConPrecioPorProveedor(provDTO.getCuit());
                for (ItemDTO i : items) {
                    String displayStr = "[" + i.getCodigo() + "] " + i.getDescripcionDeItem();
                    mapaItems.put(displayStr, i);
                    comboItems.addItem(displayStr);
                }
            } catch (Exception e) {
                // Proveedor podría no existir o no tener ítems
                System.err.println("Error al cargar items del proveedor: " + e.getMessage());
            }
        }
    }

    private void actualizarTablaDetalles() {
        tableModelDetalles.setRowCount(0);
        for (DetalleItemDTO det : detallesTemp) {
            // Find description for display
            String desc = "Desconocido";
            for (ItemDTO item : mapaItems.values()) {
                if (item.getCodigo().equals(det.getCodigoItem())) {
                    desc = item.getDescripcionDeItem();
                    break;
                }
            }
            tableModelDetalles.addRow(new Object[]{det.getCodigoItem(), desc, det.getCantidad()});
        }
    }
}
