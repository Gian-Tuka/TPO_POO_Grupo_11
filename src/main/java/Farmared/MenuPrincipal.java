package Farmared;

import Farmared.view.proveedorGUI.VistaAltaProveedor;
import Farmared.view.rubro.VistaAltaRubro;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    private JPanel cardPanel; // Contenedor dinámico
    private CardLayout cardLayout;

    public MenuPrincipal() {
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

        sidebar.add(btnProveedores);
        sidebar.add(btnProductos);
        sidebar.add(btnOC);
        sidebar.add(btnOP);
        sidebar.add(btnComprobantes);
        sidebar.add(btnSeguridad);

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

        // Evento del botón nuevo para abrir el formulario emergente
        btnNuevo.addActionListener(e -> {
            view.ProveedorDialog dialog = new view.ProveedorDialog(this);
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
            new MenuPrincipal().setVisible(true);
        });
    }
}