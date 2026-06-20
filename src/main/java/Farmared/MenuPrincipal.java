package Farmared;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.view.LoginGUI;
import Farmared.view.proveedorGUI.VistaAltaProveedor;
import Farmared.view.rubro.VistaAltaRubro;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private JPanel cardPanel; // Contenedor dinámico
    private CardLayout cardLayout;

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
        cardPanel.add(crearPanelProductos(), "PRODUCTOS");
        cardPanel.add(crearPanelGenerico("Módulo de Órdenes de Compra (OC)"), "OC");
        cardPanel.add(crearPanelOrdenesPago(), "OP");
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

        // Tabla de historial (Mocks / Datos simulados)
        String[] columnas = {"ID", "Nombre/Razón Social", "CUIT", "Teléfono", "Estado"};
        Object[][] datosSimulados = {
                {"1", "Proveedor Alfa S.A.", "30-12345678-9", "4555-1234", "Activo"},
                {"2", "Distribuidora Beta SRL", "30-87654321-9", "4555-5678", "Activo"},
                {"3", "Logística Gamma", "27-11223344-5", "4555-9012", "Inactivo"}
        };

        JTable tabla = new JTable(datosSimulados, columnas);
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        // CREAR PROVEEDOR
        btnNuevo.addActionListener(e -> {
            VistaAltaProveedor vistaAltaProveedor = new VistaAltaProveedor();
            vistaAltaProveedor.setVisible(true);
        });

        return panel;
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

        String[] columnas = {"Descripción", "Unidad de Medida", "Precio", "IVA", "Rubro"};
        Object[][] datosSimulados = {
                {"Amoxicilina 500mg", "Caja", "$1500", "21%", "Medicamentos"},
                {"Alcohol en Gel", "Litro", "$800", "10.5%", "Higiene"}
        };

        JTable tabla = new JTable(datosSimulados, columnas);
        JScrollPane scrollPane = new JScrollPane(tabla);
        panel.add(scrollPane, BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> {
            Farmared.view.ProductoDialog dialog = new Farmared.view.ProductoDialog(this);
            dialog.setVisible(true);
        });

        btnNuevoServicio.addActionListener(e -> {
            Farmared.view.ServicioDialog dialog = new Farmared.view.ServicioDialog(this);
            dialog.setVisible(true);
        });

        btnNuevaUnidad.addActionListener(e -> {
            Farmared.view.UnidadDialog dialog = new Farmared.view.UnidadDialog(this);
            dialog.setVisible(true);
        });

        return panel;
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