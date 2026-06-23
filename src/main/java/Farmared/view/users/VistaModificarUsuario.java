package Farmared.view.users;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.dto.user.UsuarioDTO;
import Farmared.model.user.Area;
import Farmared.model.user.Rol;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;

public class VistaModificarUsuario extends JDialog {

    private JTextField txtBuscarLegajo;
    private JButton btnBuscar;

    private JTextField txtNombre, txtApellido;
    private JComboBox<Rol> comboRol;
    private JComboBox<Area> comboArea;
    private JButton btnModificar;
    
    private UsuarioDTO usuarioActualModificando = null;

    public VistaModificarUsuario() {
        setTitle("Farmared - Modificar Usuario");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Busqueda superior
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.add(new JLabel("Buscar por Legajo:"));
        txtBuscarLegajo = new JTextField(15);
        panelBusqueda.add(txtBuscarLegajo);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // Formulario central
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        comboRol = new JComboBox<>(Rol.values());
        comboArea = new JComboBox<>(Area.values());

        // Inicialmente deshabilitado hasta buscar
        habilitarCampos(false);

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(txtApellido);
        panelFormulario.add(new JLabel("Rol:"));
        panelFormulario.add(comboRol);
        panelFormulario.add(new JLabel("Área:"));
        panelFormulario.add(comboArea);

        add(panelFormulario, BorderLayout.CENTER);

        // Panel Inferior para el botón modificar
        JPanel panelBoton = new JPanel();
        btnModificar = new JButton("Guardar Cambios");
        btnModificar.setEnabled(false);
        panelBoton.add(btnModificar);
        add(panelBoton, BorderLayout.SOUTH);

        // Listeners
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnModificar.addActionListener(e -> guardarCambios());
    }

    private void habilitarCampos(boolean estado) {
        txtNombre.setEnabled(estado);
        txtApellido.setEnabled(estado);
        comboRol.setEnabled(estado);
        comboArea.setEnabled(estado);
    }

    private void buscarUsuario() {
        String legajo = txtBuscarLegajo.getText().trim();
        if (legajo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar un legajo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            usuarioActualModificando = ControladorUsuariosYSeguridad.getInstance().consultarUsuario(legajo);
            
            txtNombre.setText(usuarioActualModificando.getNombre());
            txtApellido.setText(usuarioActualModificando.getApellido());
            comboRol.setSelectedItem(Rol.valueOf(usuarioActualModificando.getRol()));
            comboArea.setSelectedItem(Area.valueOf(usuarioActualModificando.getArea()));

            habilitarCampos(true);
            btnModificar.setEnabled(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            habilitarCampos(false);
            btnModificar.setEnabled(false);
            usuarioActualModificando = null;
        }
    }

    private void guardarCambios() {
        try {
            Validations v = new Validations();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            
            v.requireNonEmpty(nombre, "El nombre es obligatorio");
            v.requireNonEmpty(apellido, "El apellido es obligatorio");
            
            if (!v.contieneSoloLetras(nombre)) {
                throw new Exception("El nombre solo debe contener letras y espacios");
            }
            if (!v.contieneSoloLetras(apellido)) {
                throw new Exception("El apellido solo debe contener letras y espacios");
            }

            Rol rolSeleccionado = (Rol) comboRol.getSelectedItem();
            Area areaSeleccionada = (Area) comboArea.getSelectedItem();

            UsuarioDTO dtoActualizado = new UsuarioDTO(
                    usuarioActualModificando.getLegajo(),
                    nombre,
                    apellido,
                    rolSeleccionado.name(),
                    areaSeleccionada.name(),
                    usuarioActualModificando.isActivo()
            );

            ControladorUsuariosYSeguridad.getInstance().modificarUsuario(dtoActualizado);

            JOptionPane.showMessageDialog(this,
                    "Usuario modificado exitosamente.",
                    "Modificación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
