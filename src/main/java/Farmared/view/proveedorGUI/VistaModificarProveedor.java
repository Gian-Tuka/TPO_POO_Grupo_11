package Farmared.view.proveedorGUI;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VistaModificarProveedor extends JDialog {

    private JTextField txtBuscarCuit;
    private JButton btnBuscar;

    private JTextField txtCuit, txtRazonSocial, txtFantasia, txtTelefono, txtCorreo;
    private JTextField txtCalle, txtNumero, txtCP, txtCiudad, txtPais;
    private JTextField txtNroIngBrutos, txtTopeDeuda;
    private JComboBox<String> comboCondicionIVA;
    private JList<String> listaRubros;
    private JButton btnModificar;
    private DefaultListModel<String> modeloRubros;

    public VistaModificarProveedor() {
        setTitle("Farmared - Modificar Proveedor");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por CUIT:"));
        txtBuscarCuit = new JTextField(15);
        panelBusqueda.add(txtBuscarCuit);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(15, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtCuit = new JTextField();
        txtCuit.setEditable(false);
        txtRazonSocial = new JTextField();
        txtFantasia = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtCalle = new JTextField();
        txtNumero = new JTextField();
        txtCP = new JTextField();
        txtCiudad = new JTextField();
        txtPais = new JTextField();
        txtNroIngBrutos = new JTextField();
        txtTopeDeuda = new JTextField("0.0");

        String[] ivas = {"RESPONSABLE_INSCRIPTO", "MONOTRIBUTISTA", "EXENTO"};
        comboCondicionIVA = new JComboBox<>(ivas);

        modeloRubros = new DefaultListModel<>();
        listaRubros = new JList<>(modeloRubros);
        listaRubros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollRubros = new JScrollPane(listaRubros);

        actualizarListaRubros();

        panelFormulario.add(new JLabel("CUIT:")); panelFormulario.add(txtCuit);
        panelFormulario.add(new JLabel("Razón Social:")); panelFormulario.add(txtRazonSocial);
        panelFormulario.add(new JLabel("Nombre Fantasía:")); panelFormulario.add(txtFantasia);
        panelFormulario.add(new JLabel("Teléfono:")); panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Correo:")); panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Calle:")); panelFormulario.add(txtCalle);
        panelFormulario.add(new JLabel("Número:")); panelFormulario.add(txtNumero);
        panelFormulario.add(new JLabel("C.P.:")); panelFormulario.add(txtCP);
        panelFormulario.add(new JLabel("Ciudad:")); panelFormulario.add(txtCiudad);
        panelFormulario.add(new JLabel("País:")); panelFormulario.add(txtPais);
        panelFormulario.add(new JLabel("Condición IVA:")); panelFormulario.add(comboCondicionIVA);
        panelFormulario.add(new JLabel("Nro. Ingresos Brutos:")); panelFormulario.add(txtNroIngBrutos);
        panelFormulario.add(new JLabel("Tope de Deuda ($):")); panelFormulario.add(txtTopeDeuda);
        panelFormulario.add(new JLabel("Rubros (Ctrl para varios):")); panelFormulario.add(scrollRubros);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        btnModificar = new JButton("Guardar Cambios");
        btnModificar.setEnabled(false);
        panelBoton.add(btnModificar);
        add(panelBoton, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarProveedor());
        btnModificar.addActionListener(e -> modificarProveedorEnBackend());
    }

    public void actualizarListaRubros() {
        modeloRubros.clear();
        ArrayList<String> nombresRubros = ControladorProveedores.getInstance().obtenerNombresRubros();
        for (String rubro : nombresRubros) {
            modeloRubros.addElement(rubro);
        }
    }

    private void buscarProveedor() {
        String cuit = txtBuscarCuit.getText();
        Validations v = new Validations();
        if (v.isNullOrEmpty(cuit)) {
            JOptionPane.showMessageDialog(this, "Ingrese un CUIT", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProveedorDTO proveedor = ControladorProveedores.getInstance().buscarProveedorDTOPorCuit(cuit);
        if (proveedor != null) {
            txtCuit.setText(proveedor.getCuit());
            txtRazonSocial.setText(proveedor.getRazonSocial());
            txtFantasia.setText(proveedor.getNombreFantasia());
            txtTelefono.setText(proveedor.getTelefono());
            txtCorreo.setText(proveedor.getCorreo());
            txtCalle.setText(proveedor.getCalle());
            txtNumero.setText(proveedor.getNumeroDpto());
            txtCP.setText(proveedor.getCodigoPostal());
            txtCiudad.setText(proveedor.getCiudad());
            txtPais.setText(proveedor.getPais());
            comboCondicionIVA.setSelectedItem(proveedor.getCondicionIVA());
            txtNroIngBrutos.setText(proveedor.getNroIngBru());
            txtTopeDeuda.setText(String.valueOf(proveedor.getTopeDeuda()));

            listaRubros.clearSelection();
            if (proveedor.getIdsRubros() != null) {
                ArrayList<Integer> indicesToSelect = new ArrayList<>();
                for (String r : proveedor.getIdsRubros()) {
                    for (int i = 0; i < modeloRubros.size(); i++) {
                        if (modeloRubros.get(i).equals(r)) {
                            indicesToSelect.add(i);
                        }
                    }
                }
                int[] sel = indicesToSelect.stream().mapToInt(i -> i).toArray();
                listaRubros.setSelectedIndices(sel);
            }
            btnModificar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el proveedor con CUIT: " + cuit, "Error", JOptionPane.ERROR_MESSAGE);
            btnModificar.setEnabled(false);
        }
    }

    private void modificarProveedorEnBackend() {
        try {
            Validations v = new Validations();
            ArrayList<String> rubrosSeleccionados = new ArrayList<>(listaRubros.getSelectedValuesList());

            if (!v.validCuit(txtCuit.getText())) {
                throw new Exception("Cuit invalido");
            }

            v.requireNonEmpty(txtRazonSocial.getText(), "La Razón Social es requerida");
            
            if (!v.validEmail(txtCorreo.getText())) {
                throw new Exception("El formato del correo es inválido");
            }
            if (!v.validPhone(txtTelefono.getText())) {
                throw new Exception("El número de teléfono es inválido");
            }
            if (!v.isFloat(txtTopeDeuda.getText())) {
                throw new Exception("El tope de deuda debe ser numérico");
            }

            ProveedorDTO dto = new ProveedorDTO(
                    txtCuit.getText(),
                    txtRazonSocial.getText(),
                    txtFantasia.getText(),
                    txtCalle.getText(),
                    txtNumero.getText(),
                    txtCP.getText(),
                    txtCiudad.getText(),
                    txtPais.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    (String) comboCondicionIVA.getSelectedItem(),
                    txtNroIngBrutos.getText(),
                    "",
                    Float.parseFloat(txtTopeDeuda.getText()),
                    rubrosSeleccionados
            );

            ControladorProveedores.getInstance().modificarProveedor(dto);

            JOptionPane.showMessageDialog(this,
                    "Proveedor modificado exitosamente",
                    "Modificación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
