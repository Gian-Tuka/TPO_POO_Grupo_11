package Farmared.view.proveedorGUI;

import Farmared.controller.proveedores.ControladorProveedores;
import Farmared.dto.proveedor.ProveedorDTO;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VistaAltaProveedor extends JDialog {

    // Componentes del formulario
    private JTextField txtCuit, txtRazonSocial, txtFantasia, txtTelefono, txtCorreo;
    private JTextField txtCalle, txtNumero, txtCP, txtCiudad, txtPais;
    private JTextField txtNroIngBrutos, txtTopeDeuda;
    private JComboBox<String> comboCondicionIVA;
    private JList<String> listaRubros; // Permite seleccionar varios
    private JButton btnRegistrar;
    private DefaultListModel<String> modeloRubros;

    public VistaAltaProveedor() {
        setTitle("Farmared - Alta de Proveedor");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel central con un GridLayout (filas, columnas)
        JPanel panelFormulario = new JPanel(new GridLayout(15, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inicializar campos
        txtCuit = new JTextField();
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

        // Combo Box para IVA (Estos Strings deben coincidir con tu Enum CondicionIVA)
        String[] ivas = {"RESPONSABLE_INSCRIPTO", "MONOTRIBUTISTA", "EXENTO"};
        comboCondicionIVA = new JComboBox<>(ivas);

//        ControladorProveedores ctrlProv = ControladorProveedores.getInstance();
//
//        ArrayList<String> nombresRubros = ctrlProv.obtenerNombresRubros();
//        String[] rubrosArray = nombresRubros.toArray(new String[0]);
//
//        listaRubros = new JList<>(rubrosArray);
//        listaRubros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//        JScrollPane scrollRubros = new JScrollPane(listaRubros);
        // MODIFICADO: Inicializamos el modelo dinámico vacío
        modeloRubros = new DefaultListModel<>();

        // MODIFICADO: Vinculamos la JList al modelo dinámico
        listaRubros = new JList<>(modeloRubros);
        listaRubros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollRubros = new JScrollPane(listaRubros);

        // Se llama al método que carga los datos por primera vez
        actualizarListaRubros();


        // Agregamos al panel (Etiqueta + Campo)
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

        // Panel Inferior para el botón
        JPanel panelBoton = new JPanel();
        btnRegistrar = new JButton("Registrar Proveedor");
        panelBoton.add(btnRegistrar);
        add(panelBoton, BorderLayout.SOUTH);

        // ACÁ ESTÁ LA MAGIA DEL MVC: El evento del botón
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarProveedorEnBackend();
            }
        });
    }

    public void actualizarListaRubros() {
        // Limpiamos los elementos viejos del modelo visual
        modeloRubros.clear();

        // Buscamos la lista actualizada desde el controlador
        ArrayList<String> nombresRubros = ControladorProveedores.getInstance().obtenerNombresRubros();

        // Volcamos los nuevos strings en el modelo visual
        for (String rubro : nombresRubros) {
            modeloRubros.addElement(rubro);
        }
    }

    private void registrarProveedorEnBackend() {
        try {

            Validations v = new Validations();

            // Obtenemos los rubros seleccionados en la JList
            ArrayList<String> rubrosSeleccionados = new ArrayList<>(listaRubros.getSelectedValuesList());


            if (!v.validCuit(txtCuit.getText())) {
                throw new Exception("Cuit invalido");
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
                    "", // fecha la genera el sistema
                    Float.parseFloat(txtTopeDeuda.getText()),
                    rubrosSeleccionados
            );

            ProveedorDTO resultado = ControladorProveedores.getInstance().registrarProveedor(dto);


            // 3. Muestra confirmación con el DTO devuelto
            JOptionPane.showMessageDialog(this,
                    "Proveedor registrado: " + resultado.getRazonSocial(),
                    "Alta Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaAltaProveedor().setVisible(true);
        });
    }
}