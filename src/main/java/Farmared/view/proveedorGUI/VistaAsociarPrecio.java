package Farmared.view.proveedorGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VistaAsociarPrecio extends JDialog {

    private String cuitProveedor;
    private JComboBox<ItemDTO> comboItems;
    private JTextField txtPrecio;
    private JButton btnAsociar;

    public VistaAsociarPrecio(Window parent, String cuitProveedor) {
        super(parent, "Asociar Precio a Ítem", ModalityType.APPLICATION_MODAL);
        this.cuitProveedor = cuitProveedor;

        setSize(400, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboItems = new JComboBox<>();
        ArrayList<ItemDTO> items = ControladorProductosYServicios.getInstance().obtenerItemsDTO();
        for (ItemDTO item : items) {
            comboItems.addItem(item);
        }

        comboItems.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ItemDTO) {
                    ItemDTO dto = (ItemDTO) value;
                    setText(dto.getCodigo() + " - " + dto.getDescripcionDeItem());
                }
                return this;
            }
        });

        txtPrecio = new JTextField();

        panelCentral.add(new JLabel("Seleccione Ítem:"));
        panelCentral.add(comboItems);
        panelCentral.add(new JLabel("Precio ($):"));
        panelCentral.add(txtPrecio);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        btnAsociar = new JButton("Asociar Precio");
        panelSur.add(btnAsociar);
        add(panelSur, BorderLayout.SOUTH);

        btnAsociar.addActionListener(e -> guardar());
    }

    private void guardar() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtPrecio.getText(), "Debe ingresar un precio");

            ItemDTO itemSeleccionado = (ItemDTO) comboItems.getSelectedItem();
            if (itemSeleccionado == null) throw new Exception("Debe seleccionar un ítem");

            float precio = Float.parseFloat(txtPrecio.getText());

            ControladorProveedores.getInstance().registrarPrecioProveedor(cuitProveedor, itemSeleccionado.getCodigo(), precio);

            JOptionPane.showMessageDialog(this, "Precio asociado con éxito.");
            this.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
