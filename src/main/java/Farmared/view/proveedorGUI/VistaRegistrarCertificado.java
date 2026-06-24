package Farmared.view.proveedorGUI;

import Farmared.controller.impuestos.ControladorImpuestos;
import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.impuesto.CertificadoNoRetencionDTO;
import Farmared.dto.impuesto.ImpuestoRetenibleDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VistaRegistrarCertificado extends JDialog {

    private String cuitProveedor;
    private JComboBox<ImpuestoRetenibleDTO> comboImpuesto;
    private JTextField txtFechaInicio;
    private JTextField txtFechaVencimiento;
    private JButton btnGuardar;

    public VistaRegistrarCertificado(Window parent, String cuitProveedor) {
        super(parent, "Registrar Certificado No Retención", ModalityType.APPLICATION_MODAL);
        this.cuitProveedor = cuitProveedor;
        
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboImpuesto = new JComboBox<>();
        ArrayList<ImpuestoRetenibleDTO> impuestos = ControladorImpuestos.getInstance().obtenerImpuestosDTO();
        for (ImpuestoRetenibleDTO imp : impuestos) {
            comboImpuesto.addItem(imp);
        }

        txtFechaInicio = new JTextField();
        txtFechaVencimiento = new JTextField();

        panelCentral.add(new JLabel("Impuesto:"));
        panelCentral.add(comboImpuesto);
        panelCentral.add(new JLabel("Fecha Inicio (dd/MM/yyyy):"));
        panelCentral.add(txtFechaInicio);
        panelCentral.add(new JLabel("Fecha Venc. (dd/MM/yyyy):"));
        panelCentral.add(txtFechaVencimiento);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelSur = new JPanel();
        btnGuardar = new JButton("Guardar Certificado");
        panelSur.add(btnGuardar);
        add(panelSur, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
    }

    private void guardar() {
        try {
            Validations v = new Validations();
            v.requireNonEmpty(txtFechaInicio.getText(), "La fecha de inicio es requerida");
            v.requireNonEmpty(txtFechaVencimiento.getText(), "La fecha de vencimiento es requerida");

            ImpuestoRetenibleDTO impuestoSeleccionado = (ImpuestoRetenibleDTO) comboImpuesto.getSelectedItem();
            if (impuestoSeleccionado == null) throw new Exception("Debe seleccionar un impuesto");

            CertificadoNoRetencionDTO dto = new CertificadoNoRetencionDTO(
                    cuitProveedor,
                    impuestoSeleccionado.getNroRetencion(),
                    txtFechaInicio.getText(),
                    txtFechaVencimiento.getText()
            );

            ControladorProveedores.getInstance().registrarCertificadoNoRetencion(dto);

            JOptionPane.showMessageDialog(this, "Certificado registrado con éxito.");
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
