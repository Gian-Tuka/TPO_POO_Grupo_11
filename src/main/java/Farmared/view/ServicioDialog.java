package Farmared.view;

import Farmared.controller.item.ControladorProductosYServicios;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.item.ItemDTO;
import Farmared.dto.rubro.RubroDTO;
import Farmared.model.item.TipoDeIVA;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ServicioDialog extends JDialog {

    private JTextField txtDescripcion;
    private JComboBox<String> comboUnidadMedida;
    private JTextField txtPrecio;
    private JComboBox<String> comboTipoIVA;
    private JComboBox<String> comboRubro;

    public ServicioDialog(JFrame parent) {
        super(parent, "Registrar Servicio", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Descripción del Item:"));
        txtDescripcion = new JTextField();
        formPanel.add(txtDescripcion);

        formPanel.add(new JLabel("Unidad de Medida:"));
        ArrayList<String> unidadesDesc = ControladorProductosYServicios.getInstance().obtenerDescripcionesUnidades();
        comboUnidadMedida = new JComboBox<>(unidadesDesc.toArray(new String[0]));
        formPanel.add(comboUnidadMedida);

        formPanel.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        formPanel.add(txtPrecio);

        formPanel.add(new JLabel("Tipo de IVA:"));
        TipoDeIVA[] valoresIVA = TipoDeIVA.values();
        String[] ivaNames = new String[valoresIVA.length];
        for (int i = 0; i < valoresIVA.length; i++) {
            ivaNames[i] = valoresIVA[i].name();
        }
        comboTipoIVA = new JComboBox<>(ivaNames);
        formPanel.add(comboTipoIVA);

        formPanel.add(new JLabel("Rubro:"));
        ArrayList<String> rubros = ControladorProveedores.getInstance().obtenerNombresRubros();
        comboRubro = new JComboBox<>(rubros.toArray(new String[0]));
        formPanel.add(comboRubro);

        add(formPanel, BorderLayout.CENTER);

        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        botonera.add(btnCancelar);
        botonera.add(btnGuardar);
        add(botonera, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> {
            try {
                String desc = txtDescripcion.getText();
                String comboUnidad = (String) comboUnidadMedida.getSelectedItem();
                String codigoUnidad = ControladorProductosYServicios.getInstance().obtenerCodigoUnidadPorDescripcionCombo(comboUnidad);
                String iva = (String) comboTipoIVA.getSelectedItem();
                String nombreRubro = (String) comboRubro.getSelectedItem();
                
                // We need to pass the ID of the Rubro to registrarItem
                String idRubro = null;
                for (RubroDTO r : ControladorProveedores.getInstance().obtenerRubrosDTO()) {
                    if (r.getNombre().equals(nombreRubro)) {
                        idRubro = r.getId();
                        break;
                    }
                }

                if (idRubro == null) throw new Exception("Rubro inválido");

                ControladorProductosYServicios.getInstance().registrarItem(desc, codigoUnidad, iva, idRubro, "SERVICIO");
                
                JOptionPane.showMessageDialog(this, "¡Servicio guardado!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el servicio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public String getDescripcion() { return txtDescripcion.getText(); }
    public String getUnidadMedida() { return (String) comboUnidadMedida.getSelectedItem(); }
    public String getPrecio() { return txtPrecio.getText(); }
    public String getTipoIVA() { return (String) comboTipoIVA.getSelectedItem(); }
    public String getRubro() { return (String) comboRubro.getSelectedItem(); }
}