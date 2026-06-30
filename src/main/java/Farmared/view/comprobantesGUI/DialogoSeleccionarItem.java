package Farmared.view.comprobantesGUI;

import Farmared.dto.comprobante.DetalleComprobanteDTO;
import Farmared.dto.item.PrecioProveedorDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class DialogoSeleccionarItem extends JDialog {

    private JComboBox<String> comboItems;
    private ArrayList<PrecioProveedorDTO> listaPrecios;
    private JTextField txtCantidad;
    private DetalleComprobanteDTO detalleSeleccionado = null;

    public DialogoSeleccionarItem(JDialog parent, ArrayList<PrecioProveedorDTO> preciosDisponibles) {
        super(parent, "Seleccionar Ítem", true);
        this.listaPrecios = preciosDisponibles;

        setSize(450, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 14);

        comboItems = new JComboBox<>();
        comboItems.setFont(fontInput);
        if (listaPrecios.isEmpty()) {
            comboItems.addItem("No hay ítems con precios definidos.");
            comboItems.setEnabled(false);
        } else {
            for (PrecioProveedorDTO pp : listaPrecios) {
                comboItems.addItem(pp.getDescripcionItem() + " - $" + pp.getPrecio());
            }
        }

        txtCantidad = new JTextField();
        txtCantidad.setFont(fontInput);

        JLabel lblItem = new JLabel("Ítem:");
        lblItem.setFont(fontLabel);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; panelForm.add(lblItem, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; panelForm.add(comboItems, gbc);

        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setFont(fontLabel);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3; panelForm.add(lblCant, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7; panelForm.add(txtCantidad, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(new Color(245, 245, 250));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(fontLabel);
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setFont(fontLabel);
        btnAceptar.setBackground(new Color(52, 152, 219));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setEnabled(!listaPrecios.isEmpty());
        btnAceptar.addActionListener(e -> aceptar());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void aceptar() {
        int idx = comboItems.getSelectedIndex();
        if (idx < 0) return;

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una cantidad numérica válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PrecioProveedorDTO pp = listaPrecios.get(idx);
        float precioUnitario = Float.parseFloat(pp.getPrecio());

        detalleSeleccionado = new DetalleComprobanteDTO(
                pp.getCodigoItem(),
                pp.getDescripcionItem(),
                cantidad,
                precioUnitario,
                precioUnitario * cantidad
        );

        dispose();
    }

    public DetalleComprobanteDTO getDetalleSeleccionado() {
        return detalleSeleccionado;
    }
}
