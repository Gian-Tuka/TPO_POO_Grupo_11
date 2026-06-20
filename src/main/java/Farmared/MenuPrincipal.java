package Farmared;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.view.LoginGUI;
import Farmared.view.proveedorGUI.VistaAltaProveedor;
import Farmared.view.proveedorGUI.VistaModificarProveedor;
import Farmared.view.proveedorGUI.VistaEliminarProveedor;
import Farmared.view.rubro.VistaAltaRubro;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.proveedor.ProveedorDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class MenuPrincipal extends JFrame {

    private JPanel cardPanel; // Contenedor dinámico
    private CardLayout cardLayout;
    private DefaultTableModel modeloTablaProveedores;

    public MenuPrincipal() {
        ControladorUsuariosYSeguridad authController = ControladorUsuariosYSeguridad.getInstance();
        setTitle("Sistema de Gestión Integrado");
        setSize(1024, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout Principal: Menú a la izquierda, Contenido a la derecha
        setLayout(new BorderLayout());

        // 1. Panel Lateral (Sidebar)
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(41, 57, 80)); // Azul oscuro profesional
        sidebar.setLayout(new GridLayout(6, 1, 5, 5)); // 6 módulos
        sidebar.setPreferredSize(new Dimension(200, 720));

        // Botones del menú
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

        // 2. Panel Central Dinámico (CardLayout)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Añadimos las vistas de los módulos (por ahora paneles de ejemplo)
        cardPanel.add(crearPanelProveedores(), "PROVEEDORES");
        cardPanel.add(crearPanelGenerico("Módulo de Productos"), "PRODUCTOS");
        cardPanel.add(crearPanelGenerico("Módulo de Órdenes de Compra (OC)"), "OC");
        cardPanel.add(crearPanelGenerico("Módulo de Órdenes de Pago (OP)"), "OP");
        cardPanel.add(crearPanelGenerico("Módulo de Comprobantes"), "COMPROBANTES");
        cardPanel.add(crearPanelGenerico("Módulo de Seguridad"), "SEGURIDAD");

        add(cardPanel, BorderLayout.CENTER);

        // Action Listeners para cambiar de pantalla al hacer clic
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

    // PANTALLA DEL MÓDULO DE PROVEEDORES //TODO: Reemplazar por cada C.U: ABM de Proveedores
    private JPanel crearPanelProveedores() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Barra de acciones superior (CRUD)
        JPanel barraAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Crear Proveedor");
        JButton btnModificar = new JButton("Modificar Proveedor");
        JButton btnEliminar = new JButton("Eliminar Proveedor");

        barraAcciones.add(btnNuevo);
        barraAcciones.add(btnModificar);
        barraAcciones.add(btnEliminar);
        panel.add(barraAcciones, BorderLayout.NORTH);

        // Tabla de historial dinámica
        String[] columnas = {"Razón Social", "CUIT", "Teléfono", "Condición IVA"};
        modeloTablaProveedores = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable directamente
            }
        };
        
        JTable tabla = new JTable(modeloTablaProveedores);
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Cargar los datos la primera vez
        actualizarTablaProveedores();

        // CREAR PROVEEDOR
        btnNuevo.addActionListener(e -> {
            VistaAltaProveedor vistaAltaProveedor = new VistaAltaProveedor();
            vistaAltaProveedor.setModal(true);
            vistaAltaProveedor.setVisible(true);
            actualizarTablaProveedores();
        });

        // MODIFICAR PROVEEDOR
        btnModificar.addActionListener(e -> {
            VistaModificarProveedor vistaModificarProveedor = new VistaModificarProveedor();
            vistaModificarProveedor.setModal(true);
            vistaModificarProveedor.setVisible(true);
            actualizarTablaProveedores();
        });

        // ELIMINAR PROVEEDOR
        btnEliminar.addActionListener(e -> {
            VistaEliminarProveedor vistaEliminarProveedor = new VistaEliminarProveedor();
            vistaEliminarProveedor.setModal(true);
            vistaEliminarProveedor.setVisible(true);
            actualizarTablaProveedores();
        });

        return panel;
    }

    private void actualizarTablaProveedores() {
        modeloTablaProveedores.setRowCount(0); // Limpiar la tabla
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

    //TODO: reemplazar por cada una de las opciones para cada modulo
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

            // Evaluar login
            if (login.isLoginExitoso()) {
                new MenuPrincipal().setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}