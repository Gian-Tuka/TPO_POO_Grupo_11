package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VistaModificarItem extends JDialog {

    private JTextField txtCodigoBuscado;
    private JButton btnBuscar;

    private JTextField txtDescripcion;
    private JComboBox<UnidadDeMedidaDTO> comboUDM;
    private JComboBox<String> comboIVA;
    private JComboBox<String> comboRubro;
    private JButton btnNuevoRubro;
    private JButton btnGuardar;

    private ItemDTO itemActual;

    public VistaModificarItem(Window parent) {
        super(parent, "Farmared - Modificar Ítem", ModalityType.APPLICATION_MODAL);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel(new FlowLayout());
        panelNorte.add(new JLabel("Código de Ítem a modificar:"));
        txtCodigoBuscado = new JTextField(10);
        panelNorte.add(txtCodigoBuscado);
        btnBuscar = new JButton("Buscar");
        panelNorte.add(btnBuscar);
        add(panelNorte, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtDescripcion = new JTextField();
        txtDescripcion.setEnabled(false);
        comboUDM = new JComboBox<>();
        comboUDM.setEnabled(false);
        comboIVA = new JComboBox<>();
        comboIVA.setEnabled(false);
        comboRubro = new JComboBox<>();
        comboRubro.setEnabled(false);
        btnNuevoRubro = new JButton("+");
        btnNuevoRubro.setEnabled(false);
        JPanel panelRubro = new JPanel(new BorderLayout(5, 0));
        panelRubro.add(comboRubro, BorderLayout.CENTER);
        panelRubro.add(btnNuevoRubro, BorderLayout.EAST);

        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(new JLabel("Unidad de Medida:"));
        panelFormulario.add(comboUDM);
        panelFormulario.add(new JLabel("Tipo de IVA:"));
        panelFormulario.add(comboIVA);
        panelFormulario.add(new JLabel("Rubro:"));
        panelFormulario.add(panelRubro);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setEnabled(false);
        panelBoton.add(btnGuardar);
        add(panelBoton, BorderLayout.SOUTH);

        // Listeners
        btnBuscar.addActionListener(e -> buscarItem());
        btnGuardar.addActionListener(e -> guardarCambios());
        
        btnNuevoRubro.addActionListener(e -> {
            VistaABMRubro vistaRubro = new VistaABMRubro(this);
            vistaRubro.setVisible(true);
            cargarCombos(); // Refrescar Rubros después de crear
            
            // Re-seleccionar lo que estaba (si es posible)
            if (itemActual != null) {
                comboRubro.setSelectedItem(itemActual.getRubro());
            }
        });
    }

    private void cargarCombos() {
        comboUDM.removeAllItems();
        ArrayList<UnidadDeMedidaDTO> udms = ControladorProductosYServicios.getInstance().obtenerUnidadesDeMedidaDTO();
        for (UnidadDeMedidaDTO udm : udms) {
            comboUDM.addItem(udm);
        }

        comboIVA.removeAllItems();
        String[] ivas = ControladorProductosYServicios.getInstance().obtenerTiposDeIva();
        for (String iva : ivas) {
            comboIVA.addItem(iva);
        }

        comboRubro.removeAllItems();
        ArrayList<String> rubros = ControladorProveedores.getInstance().obtenerNombresRubros();
        for (String r : rubros) {
            comboRubro.addItem(r);
        }
    }

    private void buscarItem() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtCodigoBuscado.getText(), "Debe ingresar un código");

            ArrayList<ItemDTO> items = ControladorProductosYServicios.getInstance().obtenerItemsDTO();
            itemActual = null;
            for (ItemDTO dto : items) {
                if (dto.getCodigo().equalsIgnoreCase(txtCodigoBuscado.getText())) {
                    itemActual = dto;
                    break;
                }
            }

            if (itemActual == null) {
                throw new Exception("Ítem no encontrado o está inactivo.");
            }

            cargarCombos();

            txtDescripcion.setText(itemActual.getDescripcionDeItem());
            txtDescripcion.setEnabled(true);
            
            // Seleccionar combo UDM
            for (int i = 0; i < comboUDM.getItemCount(); i++) {
                if (comboUDM.getItemAt(i).getCodigoUnidad().equals(itemActual.getTipoUDM())) {
                    comboUDM.setSelectedIndex(i);
                    break;
                }
            }

            comboIVA.setSelectedItem(itemActual.getTipoDeIVA());
            comboRubro.setSelectedItem(itemActual.getRubro());

            comboUDM.setEnabled(true);
            comboIVA.setEnabled(true);
            comboRubro.setEnabled(true);
            btnNuevoRubro.setEnabled(true);
            btnGuardar.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarCambios() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtDescripcion.getText(), "La descripción es requerida");

            UnidadDeMedidaDTO udmSeleccionada = (UnidadDeMedidaDTO) comboUDM.getSelectedItem();

            itemActual.setDescripcionDeItem(txtDescripcion.getText());
            itemActual.setTipoUDM(udmSeleccionada.getCodigoUnidad());
            itemActual.setDescripcionUnidadMedida(udmSeleccionada.getDescripcionUnidad());
            itemActual.setTipoDeIVA(comboIVA.getSelectedItem().toString());
            itemActual.setRubro(comboRubro.getSelectedItem().toString());

            ControladorProductosYServicios.getInstance().modificarItem(itemActual);

            JOptionPane.showMessageDialog(this, "Ítem modificado con éxito.", "Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
