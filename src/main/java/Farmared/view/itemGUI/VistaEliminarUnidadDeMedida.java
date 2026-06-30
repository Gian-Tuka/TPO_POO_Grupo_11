package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;

public class VistaEliminarUnidadDeMedida extends JDialog {

    private JTextField txtBuscarDescripcion;
    private JButton btnBuscar;
    private JButton btnEliminar;
    private JLabel lblDatos;

    // Guardamos el código para realizar la eliminación en el backend
    private String codigoUnidadAEliminar;

    public VistaEliminarUnidadDeMedida(Window parent) {
        super(parent, "Farmared - Eliminar Unidad de Medida", ModalityType.APPLICATION_MODAL);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por Descripción:"));
        txtBuscarDescripcion = new JTextField(15);
        panelBusqueda.add(txtBuscarDescripcion);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelDatos = new JPanel(new BorderLayout());
        panelDatos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lblDatos = new JLabel("<html>Ingrese una descripción para buscar la unidad de medida a eliminar.</html>");
        panelDatos.add(lblDatos, BorderLayout.CENTER);
        add(panelDatos, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        btnEliminar = new JButton("Eliminar Unidad de Medida");
        btnEliminar.setEnabled(false);
        btnEliminar.setForeground(Color.RED);
        panelBoton.add(btnEliminar);
        add(panelBoton, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarUnidadDeMedida());
        btnEliminar.addActionListener(e -> eliminarUnidadDeMedidaEnBackend());
    }

    private void buscarUnidadDeMedida() {
        String descripcionABuscar = txtBuscarDescripcion.getText().trim();
        Validations v = new Validations();

        if (v.isNullOrEmpty(descripcionABuscar)) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UnidadDeMedidaDTO udm = ControladorProductosYServicios.getInstance().buscarUnidadDeMedidaDTOPorDescripcion(descripcionABuscar);

        if (udm != null) {
            codigoUnidadAEliminar = udm.getCodigoUnidad();

            String datos = "<html><b>Unidad de Medida Encontrada:</b><br><br>"
                    + "<b>Código Interno:</b> " + udm.getCodigoUnidad() + "<br>"
                    + "<b>Descripción:</b> " + udm.getDescripcionUnidad() + "<br>"
                    + "<b>Tipo de Unidad:</b> " + udm.getTipoDeUnidad() + "</html>";
            lblDatos.setText(datos);
            btnEliminar.setEnabled(true);
        } else {
            lblDatos.setText("<html>No se encontró la unidad de medida con descripción: " + descripcionABuscar + "</html>");
            btnEliminar.setEnabled(false);
        }
    }

    private void eliminarUnidadDeMedidaEnBackend() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar la unidad de medida seleccionada?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Invocamos tu método original pasándole el código extraído
                ControladorProductosYServicios.getInstance().eliminarUnidadDeMedida(codigoUnidadAEliminar);

                JOptionPane.showMessageDialog(this,
                        "Unidad de medida eliminada exitosamente",
                        "Eliminación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}