package Farmared.view.rubro;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.rubro.RubroDTO;
import Farmared.view.proveedorGUI.VistaAltaProveedor; // Import necesario

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaAltaRubro extends JFrame {

    private JTextField txtNombre;
    private JComboBox<String> comboTipo;
    private JButton btnRegistrar;
    private JTextArea areaListado;
    private VistaAltaProveedor vistaProveedor; // <-- MODIFICADO: Guardamos la referencia de la otra vista

    // MODIFICADO: El constructor ahora recibe a la VistaAltaProveedor
    public VistaAltaRubro(VistaAltaProveedor vistaProveedor) {
        this.vistaProveedor = vistaProveedor; // Asignamos la referencia

        setTitle("Farmared - Alta de Rubro");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNombre = new JTextField();
        comboTipo = new JComboBox<>(new String[]{"BIENES", "SERVICIO"});
        btnRegistrar = new JButton("Registrar Rubro");

        panelForm.add(new JLabel("Nombre del Rubro:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Tipo:"));
        panelForm.add(comboTipo);

        add(panelForm, BorderLayout.NORTH);

        areaListado = new JTextArea();
        areaListado.setEditable(false);
        add(new JScrollPane(areaListado), BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnRegistrar);
        add(panelBoton, BorderLayout.SOUTH);

        // MODIFICADO: Completamos tu ActionListener
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarRubro();
            }
        });
    }

    private void registrarRubro() {
        try {
            String nombre = txtNombre.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();

            if (nombre.isEmpty()) {
                throw new Exception("El nombre del rubro no puede estar vacío.");
            }

            // 1. Creas el DTO y llamas a tu controlador para persistir (Ajusta según tu backend)
            RubroDTO dto = new RubroDTO(null, nombre, tipo);
            ControladorProveedores.getInstance().altaRubro(dto);

            // 2. Agregamos el texto al JTextArea local para feedback visual inmediato
            areaListado.append("- " + nombre + " (" + tipo + ")\n");
            txtNombre.setText(""); // Limpiar campo

            // Le avisamos a la pantalla de proveedores que se refresque
            if (vistaProveedor != null) {
                vistaProveedor.actualizarListaRubros();
            }

            JOptionPane.showMessageDialog(this, "Rubro registrado exitosamente.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
