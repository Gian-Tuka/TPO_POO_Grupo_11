package Farmared.view.proveedorGUI;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.proveedor.ProveedorDTO;

import javax.swing.*;
import java.awt.*;

public class VistaEliminarProveedor extends JDialog {

    private JTextField txtBuscarCuit;
    private JButton btnBuscar;
    private JButton btnEliminar;

    private JLabel lblDatos;

    public VistaEliminarProveedor() {
        setTitle("Farmared - Eliminar Proveedor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por CUIT:"));
        txtBuscarCuit = new JTextField(15);
        panelBusqueda.add(txtBuscarCuit);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new BorderLayout());
        panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lblDatos = new JLabel("<html>Ingrese un CUIT para buscar el proveedor a eliminar.</html>");
        panelDatos.add(lblDatos, BorderLayout.CENTER);
        add(panelDatos, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        btnEliminar = new JButton("Eliminar Proveedor");
        btnEliminar.setEnabled(false);
        btnEliminar.setForeground(Color.RED);
        panelBoton.add(btnEliminar);
        add(panelBoton, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarProveedor());
        btnEliminar.addActionListener(e -> eliminarProveedorEnBackend());
    }

    private void buscarProveedor() {
        String cuit = txtBuscarCuit.getText().trim();
        if (cuit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un CUIT", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProveedorDTO proveedor = ControladorProveedores.getInstance().buscarProveedorDTOPorCuit(cuit);
        if (proveedor != null) {
            String datos = "<html><b>Proveedor Encontrado:</b><br><br>"
                    + "<b>Razón Social:</b> " + proveedor.getRazonSocial() + "<br>"
                    + "<b>CUIT:</b> " + proveedor.getCuit() + "<br>"
                    + "<b>Teléfono:</b> " + proveedor.getTelefono() + "</html>";
            lblDatos.setText(datos);
            btnEliminar.setEnabled(true);
        } else {
            lblDatos.setText("<html>No se encontró el proveedor con CUIT: " + cuit + "</html>");
            btnEliminar.setEnabled(false);
        }
    }

    private void eliminarProveedorEnBackend() {
        String cuit = txtBuscarCuit.getText().trim();
        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea eliminar al proveedor con CUIT " + cuit + "?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ControladorProveedores.getInstance().eliminarProveedor(cuit);
                JOptionPane.showMessageDialog(this,
                        "Proveedor eliminado exitosamente",
                        "Eliminación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
