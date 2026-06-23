package Farmared.view.users;

import Farmared.controller.usuariosYSeguridad.ControladorUsuariosYSeguridad;
import Farmared.dto.user.UsuarioDTO;
import Farmared.model.user.Area;
import Farmared.model.user.Rol;
import Farmared.utils.Validations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaAltaUsuario extends JDialog {

    private JTextField txtNombre, txtApellido;
    private JComboBox<Rol> comboRol;
    private JComboBox<Area> comboArea;
    private JButton btnRegistrar;

    public VistaAltaUsuario() {
        setTitle("Farmared - Alta de Usuario");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNombre = new JTextField();
        txtApellido = new JTextField();
        comboRol = new JComboBox<>(Rol.values());
        comboArea = new JComboBox<>(Area.values());

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Apellido:"));
        panelFormulario.add(txtApellido);
        panelFormulario.add(new JLabel("Rol:"));
        panelFormulario.add(comboRol);
        panelFormulario.add(new JLabel("Área:"));
        panelFormulario.add(comboArea);

        add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        btnRegistrar = new JButton("Registrar Usuario");
        panelBoton.add(btnRegistrar);
        add(panelBoton, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
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

            UsuarioDTO dto = new UsuarioDTO(
                    nombre,
                    apellido,
                    rolSeleccionado.name(),
                    areaSeleccionada.name()
            );

            UsuarioDTO resultado = ControladorUsuariosYSeguridad.getInstance().altaUsuario(dto);

            JOptionPane.showMessageDialog(this,
                    "Usuario registrado exitosamente con legajo: " + resultado.getLegajo(),
                    "Alta Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
