package Farmared.view.itemGUI;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.rubro.RubroDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;

public class VistaABMRubro extends JDialog {

    private JTextField txtNombre;
    private JComboBox<String> comboTipo;
    private JButton btnRegistrar;

    public VistaABMRubro(Window parent) {
        super(parent, "Nuevo Rubro", ModalityType.APPLICATION_MODAL);
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNombre = new JTextField();
        comboTipo = new JComboBox<>(new String[]{"BIENES", "SERVICIOS"});

        panelCentral.add(new JLabel("Nombre del Rubro:"));
        panelCentral.add(txtNombre);
        panelCentral.add(new JLabel("Tipo de Rubro:"));
        panelCentral.add(comboTipo);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        btnRegistrar = new JButton("Registrar Rubro");
        panelSur.add(btnRegistrar);
        add(panelSur, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarRubro());
    }

    private void registrarRubro() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtNombre.getText(), "Debe ingresar un nombre para el rubro");

            RubroDTO dto = new RubroDTO(txtNombre.getText(), comboTipo.getSelectedItem().toString());
            ControladorProveedores.getInstance().altaRubro(dto);

            JOptionPane.showMessageDialog(this, "Rubro creado con éxito.");
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
