package Farmared;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.dto.rubro.RubroDTO;
import Farmared.view.LoginGUI;
import Farmared.view.itemGUI.GUIItem;
import Farmared.view.ordenCompra.OrdenDeCompraGUI;
import Farmared.view.ordenDePago.VistaOrdenDePago;
import Farmared.view.proveedorGUI.GUIProveedor;
import Farmared.view.users.GUIUsuarios;
import Farmared.view.comprobantesGUI.GUIComprobantes;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.proveedor.ProveedorDTO;

import Farmared.controller.item.ControladorProductosYServicios;

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
        cargarDatosSimulados();
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
        JButton btnSeguridad = crearBotónMenu("Seguridad y Usuarios");

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
        cardPanel.add(new GUIItem(this), "PRODUCTOS");
        cardPanel.add(new OrdenDeCompraGUI(), "OC");
        cardPanel.add(new VistaOrdenDePago(), "OP");
        cardPanel.add(new GUIComprobantes(this), "COMPROBANTES");
        cardPanel.add(new GUIUsuarios(), "SEGURIDAD Y USUARIOS");

        add(cardPanel, BorderLayout.CENTER);

        btnProveedores.addActionListener(e -> cardLayout.show(cardPanel, "PROVEEDORES"));
        btnProductos.addActionListener(e -> cardLayout.show(cardPanel, "PRODUCTOS"));
        btnOC.addActionListener(e -> cardLayout.show(cardPanel, "OC"));
        btnOP.addActionListener(e -> cardLayout.show(cardPanel, "OP"));
        btnComprobantes.addActionListener(e -> cardLayout.show(cardPanel, "COMPROBANTES"));
        btnSeguridad.addActionListener(e -> cardLayout.show(cardPanel, "SEGURIDAD Y USUARIOS"));
    }

    private JButton crearBotónMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return btn;
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




    private void cargarDatosSimulados() {
        ControladorProveedores cProv = ControladorProveedores.getInstance();
        ControladorProductosYServicios cProdYServ = ControladorProductosYServicios.getInstance();

        if (cProv.obtenerProveedoresDTO().isEmpty() && cProv.obtenerRubrosDTO().isEmpty()) {
            try {
                ProveedorDTO dto1 = new ProveedorDTO("30-12345678-1", "Proveedor Alfa S.A.", "Alfa", "Calle Falsa", "123", "1000", "CABA", "Argentina", "4555-1234", "alfa@test.com", "RESPONSABLE_INSCRIPTO", "12345", "", 100000f, new ArrayList<>());
                ProveedorDTO dto2 = new ProveedorDTO("30-87654321-0", "Distribuidora Beta SRL", "Beta", "Avenida Siempreviva", "742", "1000", "CABA", "Argentina", "4555-5678", "beta@test.com", "MONOTRIBUTISTA", "54321", "", 50000f, new ArrayList<>());
                ProveedorDTO dto3 = new ProveedorDTO("27-11223344-5", "Logística Gamma", "Gamma", "Ruta 9", "Km 50", "1629", "Pilar", "Argentina", "4555-9012", "gamma@test.com", "EXENTO", "11223", "", 25000f, new ArrayList<>());

                cProv.registrarProveedor(dto1);
                cProv.registrarProveedor(dto2);
                cProv.registrarProveedor(dto3);

                cProv.altaRubro(new RubroDTO("Medicamentos", "BIENES"));
                cProv.altaRubro(new RubroDTO("Limpieza", "SERVICIOS"));
                cProv.altaRubro(new RubroDTO("Seguridad", "SERVICIOS"));

            } catch (Exception e) {
                System.out.println("Error general de simulación: " + e.getMessage());
                e.printStackTrace();
            }
        }
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