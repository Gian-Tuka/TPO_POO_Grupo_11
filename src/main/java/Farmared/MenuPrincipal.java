package Farmared;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.view.LoginGUI;
import Farmared.view.proveedorGUI.GUIProveedor;
import Farmared.view.proveedorGUI.VistaAltaProveedor;
import Farmared.view.proveedorGUI.VistaModificarProveedor;
import Farmared.view.proveedorGUI.VistaEliminarProveedor;
import Farmared.view.rubro.VistaAltaRubro;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.proveedor.ProveedorDTO;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.ItemDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame {

    private JPanel cardPanel; // Contenedor dinámico
    private CardLayout cardLayout;
    private DefaultTableModel modeloTablaProveedores;
    private DefaultTableModel modeloTablaProductos;
    private DefaultTableModel modeloTablaServicios;

    public MenuPrincipal() {
        ControladorUsuariosYSeguridad authController = ControladorUsuariosYSeguridad.getInstance();
        setTitle("Sistema de Gestión Integrado");
        setSize(1024, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(41, 57, 80));
        sidebar.setLayout(new GridLayout(6, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(200, 720));

        JButton btnProveedores = crearBotónMenu("Proveedores");
        JButton btnProductos = crearBotónMenu("Productos y Servicios");
        JButton btnOC = crearBotónMenu("Órdenes de Compra");
        JButton btnOP = crearBotónMenu("Órdenes de Pago");
        JButton btnComprobantes = crearBotónMenu("Comprobantes");
        JButton btnSeguridad = crearBotónMenu("Seguridad");

        if (authController.tieneAccesoAModulo("PROVEEDORES")) sidebar.add(btnProveedores);
        if (authController.tieneAccesoAModulo("PRODUCTOS")) sidebar.add(btnProductos);
        if (authController.tieneAccesoAModulo("OC")) sidebar.add(btnOC);
        if (authController.tieneAccesoAModulo("OP")) sidebar.add(btnOP);
        if (authController.tieneAccesoAModulo("COMPROBANTES")) sidebar.add(btnComprobantes);
        if (authController.tieneAccesoAModulo("SEGURIDAD")) sidebar.add(btnSeguridad);

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new GUIProveedor(), "PROVEEDORES");
        cardPanel.add(crearPanelProductos(), "PRODUCTOS");
        cardPanel.add(crearPanelGenerico("Módulo de Órdenes de Compra (OC)"), "OC");
        cardPanel.add(crearPanelOrdenesPago(), "OP");
        cardPanel.add(crearPanelGenerico("Módulo de Comprobantes"), "COMPROBANTES");
        cardPanel.add(crearPanelGenerico("Módulo de Seguridad"), "SEGURIDAD");

        add(cardPanel, BorderLayout.CENTER);

        btnProveedores.addActionListener(e -> cardLayout.show(cardPanel, "PROVEEDORES"));
        btnProductos.addActionListener(e -> cardLayout.show(cardPanel, "PRODUCTOS"));
        btnOC.addActionListener(e -> cardLayout.show(cardPanel, "OC"));
        btnOP.addActionListener(e -> cardLayout.show(cardPanel, "OP"));
        btnComprobantes.addActionListener(e -> cardLayout.show(cardPanel, "COMPROBANTES"));
        btnSeguridad.addActionListener(e -> cardLayout.show(cardPanel, "SEGURIDAD"));
    }

    private JButton crearBotónMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
    }

    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Crear Producto");
        barraAcciones.add(btnNuevo);

        JButton btnNuevoServicio = new JButton("Crear Servicio");
        barraAcciones.add(btnNuevoServicio);

        JButton btnNuevaUnidad = new JButton("Crear Unidad de Medida");
        barraAcciones.add(btnNuevaUnidad);

        panel.add(barraAcciones, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tabla Productos
        String[] columnasProductos = {"Código", "Descripción", "Unidad", "IVA", "Rubro", "Precio Vigente"};
        modeloTablaProductos = new DefaultTableModel(columnasProductos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaProductos = new JTable(modeloTablaProductos);
        tabbedPane.addTab("Productos", new JScrollPane(tablaProductos));

        // Tabla Servicios
        String[] columnasServicios = {"Código", "Descripción", "Unidad", "IVA", "Rubro", "Precio Vigente"};
        modeloTablaServicios = new DefaultTableModel(columnasServicios, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaServicios = new JTable(modeloTablaServicios);
        tabbedPane.addTab("Servicios", new JScrollPane(tablaServicios));

        panel.add(tabbedPane, BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> {
            Farmared.view.ProductoDialog dialog = new Farmared.view.ProductoDialog(this);
            dialog.setVisible(true);
            actualizarTablasProductos();
        });

        btnNuevoServicio.addActionListener(e -> {
            Farmared.view.ServicioDialog dialog = new Farmared.view.ServicioDialog(this);
            dialog.setVisible(true);
            actualizarTablasProductos();
        });

        btnNuevaUnidad.addActionListener(e -> {
            Farmared.view.UnidadDialog dialog = new Farmared.view.UnidadDialog(this);
            dialog.setVisible(true);
        });

        actualizarTablasProductos();

        return panel;
    }

    private void actualizarTablasProductos() {
        modeloTablaProductos.setRowCount(0);
        ArrayList<ItemDTO> productos = ControladorProductosYServicios.getInstance().obtenerSoloProductos();
        for (ItemDTO p : productos) {
            Object[] fila = { p.getCodigo(), p.getDescripcionDeItem(), p.getUnidadMedida(), p.getTipoDeIVA(), p.getRubro(), p.getPrecioItem() };
            modeloTablaProductos.addRow(fila);
        }

        modeloTablaServicios.setRowCount(0);
        ArrayList<ItemDTO> servicios = ControladorProductosYServicios.getInstance().obtenerSoloServicios();
        for (ItemDTO s : servicios) {
            Object[] fila = { s.getCodigo(), s.getDescripcionDeItem(), s.getUnidadMedida(), s.getTipoDeIVA(), s.getRubro(), s.getPrecioItem() };
            modeloTablaServicios.addRow(fila);
        }
    }

    private JPanel crearPanelOrdenesPago() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Emitir Orden de Pago");
        barraAcciones.add(btnNuevo);
        panel.add(barraAcciones, BorderLayout.NORTH);

        String[] columnas = {"N° OP", "Proveedor", "Forma de Pago", "Monto Total"};
        Object[][] datosSimulados = {
                {"OP-001", "Proveedor Alfa S.A.", "Transferencia", "$15000"},
                {"OP-002", "Distribuidora Beta SRL", "Cheque", "$8500"}
        };

        JTable tabla = new JTable(datosSimulados, columnas);
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> {
            Farmared.view.OrdenDePagoDialog dialog = new Farmared.view.OrdenDePagoDialog(this);
            dialog.setVisible(true);
        });

        return panel;
    }

    private JPanel crearPanelGenerico(String titulo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.add(new JLabel(titulo, SwingConstants.CENTER));
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame base = new JFrame();
            LoginGUI login = new LoginGUI(base);
            login.setVisible(true);

            if (login.isLoginExitoso()) {
                new MenuPrincipal().setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}