package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.dto.item.ItemDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VistaEliminarItem extends JDialog {

    private JTextField txtCodigoBuscado;
    private JButton btnBuscar;

    private JLabel lblDetalles;
    private JButton btnEliminar;
    private String codigoAEliminar;

    public VistaEliminarItem(Window parent) {
        super(parent, "Farmared - Eliminar Ítem", ModalityType.APPLICATION_MODAL);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel(new FlowLayout());
        panelNorte.add(new JLabel("Código de Ítem a eliminar:"));
        txtCodigoBuscado = new JTextField(10);
        panelNorte.add(txtCodigoBuscado);
        btnBuscar = new JButton("Buscar");
        panelNorte.add(btnBuscar);
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        lblDetalles = new JLabel("Ingrese un código y presione Buscar", SwingConstants.CENTER);
        panelCentral.add(lblDetalles, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        btnEliminar = new JButton("Eliminar Ítem");
        btnEliminar.setEnabled(false);
        panelSur.add(btnEliminar);
        add(panelSur, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarItem());
        btnEliminar.addActionListener(e -> eliminarItem());
    }

    private void buscarItem() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtCodigoBuscado.getText(), "Debe ingresar un código");

            ArrayList<ItemDTO> items = ControladorProductosYServicios.getInstance().obtenerItemsDTO();
            ItemDTO itemEncontrado = null;
            for (ItemDTO dto : items) {
                if (dto.getCodigo().equalsIgnoreCase(txtCodigoBuscado.getText())) {
                    itemEncontrado = dto;
                    break;
                }
            }

            if (itemEncontrado == null) {
                throw new Exception("Ítem no encontrado o ya está inactivo.");
            }

            codigoAEliminar = itemEncontrado.getCodigo();
            lblDetalles.setText("<html><div style='text-align: center;'>" +
                    "<b>Tipo:</b> " + itemEncontrado.getTipoItem() + "<br>" +
                    "<b>Descripción:</b> " + itemEncontrado.getDescripcionDeItem() + "<br>" +
                    "<b>Rubro:</b> " + itemEncontrado.getRubro() + "<br><br>" +
                    "¿Desea dar de baja este ítem?</div></html>");
            
            btnEliminar.setEnabled(true);

        } catch (Exception ex) {
            lblDetalles.setText("Ítem no encontrado.");
            btnEliminar.setEnabled(false);
            codigoAEliminar = null;
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarItem() {
        try {
            if (codigoAEliminar != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar lógicamente este ítem?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    ControladorProductosYServicios.getInstance().eliminarItem(codigoAEliminar);
                    JOptionPane.showMessageDialog(this, "Ítem eliminado correctamente.");
                    this.dispose();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
