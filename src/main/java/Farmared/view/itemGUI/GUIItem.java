package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.ItemDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUIItem extends JPanel {

    private DefaultTableModel modeloTablaProductos;
    private DefaultTableModel modeloTablaServicios;
    private JFrame ventanaPrincipal;

    public GUIItem(JFrame ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Crear Producto");
        barraAcciones.add(btnNuevo);

        JButton btnNuevoServicio = new JButton("Crear Servicio");
        barraAcciones.add(btnNuevoServicio);

        JButton btnNuevaUnidad = new JButton("Crear Unidad de Medida");
        barraAcciones.add(btnNuevaUnidad);

        this.add(barraAcciones, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tabla Productos
        String[] columnasProductos = {"Código", "Descripción", "Unidad", "Tipo unidad", "IVA", "Rubro", "Precio Vigente"};
        modeloTablaProductos = new DefaultTableModel(columnasProductos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaProductos = new JTable(modeloTablaProductos);
        tabbedPane.addTab("Productos", new JScrollPane(tablaProductos));



//        String[] columnas = {"Razón Social", "CUIT", "Teléfono", "Condición IVA"};
//        modeloTablaProveedores = new DefaultTableModel(columnas, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//
//        JTable tabla = new JTable(modeloTablaProveedores);
//        JScrollPane scrollPane = new JScrollPane(tabla);
//        this.add(scrollPane, BorderLayout.CENTER);
//
//        // Llamada al método que llena la tabla
//        actualizarTablaProveedores();
//





        // Tabla Servicios
        String[] columnasServicios = {"Código", "Descripción", "Unidad", "IVA", "Rubro", "Precio Vigente"};
        modeloTablaServicios = new DefaultTableModel(columnasServicios, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tablaServicios = new JTable(modeloTablaServicios);
        tabbedPane.addTab("Servicios", new JScrollPane(tablaServicios));

        JScrollPane scrollPaneServicios = new JScrollPane(tablaServicios);
        this.add(tabbedPane, BorderLayout.CENTER);


        // Listeners optimizados para refrescar solo lo que corresponde
        btnNuevo.addActionListener(e -> {
            Farmared.view.ProductoDialog dialog = new Farmared.view.ProductoDialog(ventanaPrincipal);
            dialog.setVisible(true);
            actualizarTablaProductos(); // <--- Solo productos
        });

        btnNuevoServicio.addActionListener(e -> {
            Farmared.view.ServicioDialog dialog = new Farmared.view.ServicioDialog(ventanaPrincipal);
            dialog.setVisible(true);
            actualizarTablaServicios(); // <--- Solo servicios
        });

        btnNuevaUnidad.addActionListener(e -> {
            Farmared.view.UnidadDialog dialog = new Farmared.view.UnidadDialog(ventanaPrincipal);
            dialog.setVisible(true);
            // Si la unidad afecta visualmente a ambos, podrías llamar a ambos métodos acá
        });

        actualizarTablaProductos();
        actualizarTablaServicios();
    }

    public void actualizarTablaProductos() {
        modeloTablaProductos.setRowCount(0);
        ArrayList<ItemDTO> productos = ControladorProductosYServicios.getInstance().obtenerSoloProductos();
        for (ItemDTO p : productos) {
            Object[] fila = {
                    p.getCodigo(),
                    p.getDescripcionDeItem(),
                    p.getDescripcionUnidadMedida(),
                    p.getTipoUDM(),
                    p.getTipoDeIVA(),
                    p.getRubro(),
                    p.getPrecioVigente() };
            modeloTablaProductos.addRow(fila);
        }
    }

    public void actualizarTablaServicios() {
        modeloTablaServicios.setRowCount(0);
        ArrayList<ItemDTO> servicios = ControladorProductosYServicios.getInstance().obtenerSoloServicios();
        for (ItemDTO s : servicios) {
            Object[] fila = {
                    s.getCodigo(),
                    s.getDescripcionDeItem(),
                    s.getDescripcionUnidadMedida(),
                    s.getTipoDeIVA(),
                    s.getRubro(),
                    s.getPrecioVigente() };
            modeloTablaServicios.addRow(fila);
        }
    }
}