package Farmared.view.itemGUI;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.item.UnidadDeMedidaDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VistaAltaItem extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<UnidadDeMedidaDTO> comboUDM;
    private JButton btnNuevaUDM;
    private JComboBox<String> comboIVA;
    private JComboBox<String> comboRubro;
    private JButton btnNuevoRubro;
    private JRadioButton radioProducto;
    private JRadioButton radioServicio;
    private JButton btnRegistrar;

    public VistaAltaItem(Window parent) {
        super(parent, "Farmared - Alta de Ítem", ModalityType.APPLICATION_MODAL);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tipo de ítem
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioProducto = new JRadioButton("Producto", true);
        radioServicio = new JRadioButton("Servicio");
        ButtonGroup bgTipo = new ButtonGroup();
        bgTipo.add(radioProducto);
        bgTipo.add(radioServicio);
        panelTipo.add(radioProducto);
        panelTipo.add(radioServicio);

        txtDescripcion = new JTextField();
        
        comboUDM = new JComboBox<>();
        btnNuevaUDM = new JButton("+");
        JPanel panelUDM = new JPanel(new BorderLayout(5, 0));
        panelUDM.add(comboUDM, BorderLayout.CENTER);
        panelUDM.add(btnNuevaUDM, BorderLayout.EAST);

        comboIVA = new JComboBox<>();
        comboRubro = new JComboBox<>();
        btnNuevoRubro = new JButton("+");
        JPanel panelRubro = new JPanel(new BorderLayout(5, 0));
        panelRubro.add(comboRubro, BorderLayout.CENTER);
        panelRubro.add(btnNuevoRubro, BorderLayout.EAST);

        cargarCombos();

        panelFormulario.add(new JLabel("Tipo de Ítem:"));
        panelFormulario.add(panelTipo);
        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(new JLabel("Unidad de Medida:"));
        panelFormulario.add(panelUDM);
        panelFormulario.add(new JLabel("Tipo de IVA:"));
        panelFormulario.add(comboIVA);
        panelFormulario.add(new JLabel("Rubro:"));
        panelFormulario.add(panelRubro);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        btnRegistrar = new JButton("Registrar Ítem");
        panelBoton.add(btnRegistrar);
        add(panelBoton, BorderLayout.SOUTH);

        // Listeners
        btnNuevaUDM.addActionListener(e -> {
            VistaABMUnidadDeMedida vistaUDM = new VistaABMUnidadDeMedida(this);
            vistaUDM.setVisible(true);
            cargarCombos(); // Refrescar UDM después de crear
        });

        btnNuevoRubro.addActionListener(e -> {
            VistaABMRubro vistaRubro = new VistaABMRubro(this);
            vistaRubro.setVisible(true);
            cargarCombos(); // Refrescar Rubros después de crear
        });

        btnRegistrar.addActionListener(e -> registrarItem());
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

    private void registrarItem() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtDescripcion.getText(), "La descripción es requerida");

            if (comboUDM.getSelectedItem() == null) {
                throw new Exception("Debe seleccionar una unidad de medida");
            }
            if (comboRubro.getSelectedItem() == null) {
                throw new Exception("Debe seleccionar un rubro");
            }

            UnidadDeMedidaDTO udmSeleccionada = (UnidadDeMedidaDTO) comboUDM.getSelectedItem();
            String tipoItem = radioProducto.isSelected() ? "PRODUCTO" : "SERVICIO";

            ItemDTO dto = new ItemDTO(
                    txtDescripcion.getText(),
                    udmSeleccionada.getDescripcionUnidad(),
                    udmSeleccionada.getCodigoUnidad(),
                    comboIVA.getSelectedItem().toString(),
                    comboRubro.getSelectedItem().toString()
            );
            dto.setTipoItem(tipoItem);

            ControladorProductosYServicios.getInstance().registrarItem(dto);

            JOptionPane.showMessageDialog(this,
                    "Ítem registrado con éxito",
                    "Alta Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
