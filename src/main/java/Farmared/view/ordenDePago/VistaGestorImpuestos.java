package Farmared.view.ordenDePago;

import Farmared.controller.impuestos.ControladorImpuestos;
import Farmared.dto.impuesto.ImpuestoRetenibleDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class VistaGestorImpuestos extends JDialog {

    private JComboBox<String> comboImpuestos;
    private ArrayList<ImpuestoRetenibleDTO> listaImpuestos;
    
    private JTextField txtMinimo;
    private JTextField txtMaximo;
    private JTextField txtPorcentaje;

    public VistaGestorImpuestos(JFrame parent) {
        super(parent, "Gestor de Impuestos y Rangos", true);
        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(41, 128, 185));
        panelHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitulo = new JLabel("Configurar Rangos de Retención");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        add(panelHeader, BorderLayout.NORTH);

        // Form
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(new EmptyBorder(20, 30, 20, 30));

        listaImpuestos = ControladorImpuestos.getInstance().obtenerImpuestosDTO();
        comboImpuestos = new JComboBox<>();
        comboImpuestos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        if (listaImpuestos.isEmpty()) {
            comboImpuestos.addItem("No hay impuestos registrados");
        } else {
            for (ImpuestoRetenibleDTO imp : listaImpuestos) {
                comboImpuestos.addItem(imp.getDescripcionRetencion() + " (" + imp.getNroRetencion() + ")");
            }
        }

        txtMinimo = new JTextField();
        txtMinimo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        txtMaximo = new JTextField();
        txtMaximo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        txtPorcentaje = new JTextField();
        txtPorcentaje.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        panelForm.add(crearFila("Impuesto:", comboImpuestos));
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(crearFila("Monto Mínimo ($):", txtMinimo));
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(crearFila("Monto Máximo ($):", txtMaximo));
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(crearFila("Retención (%):", txtPorcentaje));

        add(panelForm, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.setBackground(new Color(245, 245, 250));
        panelBotones.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        JButton btnCancelar = new JButton("Cerrar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = new JButton("Agregar Rango");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBackground(new Color(41, 128, 185));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> agregarRango());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private JPanel crearFila(String etiqueta, JComponent campo) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(Color.WHITE);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setPreferredSize(new Dimension(140, 35));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        fila.add(lbl, BorderLayout.WEST);
        fila.add(campo, BorderLayout.CENTER);
        return fila;
    }

    private void agregarRango() {
        if (listaImpuestos.isEmpty()) return;
        
        try {
            int idx = comboImpuestos.getSelectedIndex();
            String nroRetencion = listaImpuestos.get(idx).getNroRetencion();
            
            float min = Float.parseFloat(txtMinimo.getText().trim());
            float max = Float.parseFloat(txtMaximo.getText().trim());
            float porc = Float.parseFloat(txtPorcentaje.getText().trim());
            
            if (min >= max) {
                JOptionPane.showMessageDialog(this, "El mínimo debe ser menor al máximo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (porc < 0 || porc > 100) {
                JOptionPane.showMessageDialog(this, "El porcentaje debe estar entre 0 y 100.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            ControladorImpuestos.getInstance().agregarRangoAImpuesto(nroRetencion, min, max, porc);
            JOptionPane.showMessageDialog(this, "Rango agregado con éxito al impuesto " + nroRetencion + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            txtMinimo.setText("");
            txtMaximo.setText("");
            txtPorcentaje.setText("");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
