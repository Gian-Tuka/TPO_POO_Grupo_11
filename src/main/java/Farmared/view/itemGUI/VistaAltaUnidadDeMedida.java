package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;

public class VistaAltaUnidadDeMedida extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<String> comboTipo;
    private JButton btnRegistrar;

    public VistaAltaUnidadDeMedida(Window parent) {
        super(parent, "Nueva Unidad de Medida", ModalityType.APPLICATION_MODAL);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtDescripcion = new JTextField();
        comboTipo = new JComboBox<>();

        String[] tipos = ControladorProductosYServicios.getInstance().obtenerTiposDeUnidad();
        for (String t : tipos) {
            comboTipo.addItem(t);
        }

        panelCentral.add(new JLabel("Descripción:"));
        panelCentral.add(txtDescripcion);
        panelCentral.add(new JLabel("Tipo de Unidad:"));
        panelCentral.add(comboTipo);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        btnRegistrar = new JButton("Registrar UDM");
        panelSur.add(btnRegistrar);
        add(panelSur, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarUDM());
    }

    private void registrarUDM() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtDescripcion.getText(), "Debe ingresar una descripción");

            UnidadDeMedidaDTO dto = new UnidadDeMedidaDTO(txtDescripcion.getText(), comboTipo.getSelectedItem().toString());
            ControladorProductosYServicios.getInstance().altaUnidadDeMedida(dto);

            JOptionPane.showMessageDialog(this, "Unidad de medida creada con éxito.");
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
