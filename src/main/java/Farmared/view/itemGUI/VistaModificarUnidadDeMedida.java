package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;

public class VistaModificarUnidadDeMedida extends JDialog {

    private JTextField txtBuscarDescripcion;
    private JButton btnBuscar;

    private JTextField txtDescripcion;
    private JComboBox<String> comboTipo;
    private JButton btnModificar;

    // Guardamos el código internamente para saber qué objeto modificar en el backend
    private String codigoUnidadOriginal;

    public VistaModificarUnidadDeMedida(Window parent) {
        super(parent, "Farmared - Modificar Unidad de Medida", ModalityType.APPLICATION_MODAL);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por Descripción:"));
        txtBuscarDescripcion = new JTextField(15);
        panelBusqueda.add(txtBuscarDescripcion);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(2, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtDescripcion = new JTextField();
        comboTipo = new JComboBox<>();

        String[] tipos = ControladorProductosYServicios.getInstance().obtenerTiposDeUnidad();
        for (String t : tipos) {
            comboTipo.addItem(t);
        }

        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(new JLabel("Tipo de Unidad:"));
        panelFormulario.add(comboTipo);
        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        btnModificar = new JButton("Guardar Cambios");
        btnModificar.setEnabled(false);
        panelBoton.add(btnModificar);
        add(panelBoton, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarUnidadDeMedida());
        btnModificar.addActionListener(e -> modificarUnidadDeMedidaEnBackend());
    }

    private void buscarUnidadDeMedida() {
        String descripcionABuscar = txtBuscarDescripcion.getText().trim();
        Validations v = new Validations();

        if (v.isNullOrEmpty(descripcionABuscar)) {
            JOptionPane.showMessageDialog(this, "Ingrese una descripción para buscar", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UnidadDeMedidaDTO udm = ControladorProductosYServicios.getInstance().buscarUnidadDeMedidaDTOPorDescripcion(descripcionABuscar);

        if (udm != null) {
            // Capturamos el código único que vino en el DTO encontrado
            codigoUnidadOriginal = udm.getCodigoUnidad();

            // Seteamos los campos editables con los valores actuales
            txtDescripcion.setText(udm.getDescripcionUnidad());
            comboTipo.setSelectedItem(udm.getTipoDeUnidad());

            btnModificar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la unidad de medida: " + descripcionABuscar, "Error", JOptionPane.ERROR_MESSAGE);
            btnModificar.setEnabled(false);
        }
    }

    private void modificarUnidadDeMedidaEnBackend() {
        try {
            Validations v = new Validations();
            String nuevaDescripcion = txtDescripcion.getText().trim();

            v.requireNonEmpty(nuevaDescripcion, "La descripción es requerida");

            // Construimos el DTO con el código original y las modificaciones de la UI
            UnidadDeMedidaDTO dto = new UnidadDeMedidaDTO(codigoUnidadOriginal, nuevaDescripcion, comboTipo.getSelectedItem().toString());

            // Invocamos directamente tu método original de un solo parámetro
            ControladorProductosYServicios.getInstance().modificarUnidadDeMedida(dto);

            JOptionPane.showMessageDialog(this,
                    "Unidad de medida modificada exitosamente",
                    "Modificación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}