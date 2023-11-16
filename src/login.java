import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame{
    JPanel panellog;
    private JTextField UsuarioText;
    private JPasswordField contraText;
    private JButton botonIngresarUser;
    private JButton botonValidarUser;
    private JButton botonValidarAdmin;
    private JButton botonIngresarAdmin;
    private JButton botoncerrar;
    Connection conexion;
    PreparedStatement preparar;
    Statement traer;
    ResultSet resultado;

    public login() {
        botonIngresarUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarUsuario();
                    JOptionPane.showMessageDialog(null,"registro exitoso");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"registro fallido"+ex.getMessage());
                }
            }
        });
        botonIngresarAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarAdmin();
                    JOptionPane.showMessageDialog(null,"registro exitoso");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null,"registro fallido"+ex.getMessage());
                }
            }
        });
        botonValidarUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarUsuario();

            }
        });
        botonValidarAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarAdmin();
            }
        });

        botoncerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public void conectar(){
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_integrador", "root", "beacosta");
        }catch (SQLException e){
            throw new RuntimeException(e);

        }

    }

    public void agregarUsuario() throws SQLException {
        conectar();
        preparar = conexion.prepareStatement("Insert into Usuarios(usuario, contraseña) values (?,?) ");
        preparar.setString(1, UsuarioText.getText());
        preparar.setString(2, String.valueOf(contraText.getText()));
        preparar.executeUpdate();
    }
    public void agregarAdmin() throws SQLException {
        conectar();
        preparar = conexion.prepareStatement("Insert into administradores(usuario, contraseña) values (?,?) ");
        preparar.setString(1, UsuarioText.getText());
        preparar.setString(2, String.valueOf(contraText.getText()));
        preparar.executeUpdate();
    }

    public void validarUsuario() {
        conectar();
        String usuario = UsuarioText.getText();
        String contraseña = String.valueOf(contraText.getPassword());

        try {
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT usuario, contraseña FROM Usuarios WHERE usuario ='" + usuario + "' AND contraseña ='" + contraseña + "'");

            if (resultado.next()) {
                Usuarios usuario1 = new Usuarios();
                usuario1.setLocationRelativeTo(null);
                usuario1.setContentPane(new Usuarios().panel);
                usuario1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                usuario1.setVisible(true);
                usuario1.pack();
                this.dispose();
                JOptionPane.showMessageDialog(null, "Acceso permitido");
            } else {
                JOptionPane.showMessageDialog(null, "Error de acceso: usuario no registrado");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public void validarAdmin() {
        conectar();
        String usuario = UsuarioText.getText();
        String contraseña = String.valueOf(contraText.getPassword());

        try {
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT usuario, contraseña FROM administradores WHERE usuario ='" + usuario + "' AND contraseña ='" + contraseña + "'");

            if (resultado.next()) {
                Usuarios usuario1 = new Usuarios();
                usuario1.setLocationRelativeTo(null);
                usuario1.setContentPane(new Usuarios().panel);
                usuario1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                usuario1.setVisible(true);
                usuario1.pack();
                this.dispose();
                JOptionPane.showMessageDialog(null, "Acceso permitido");
            } else {
                JOptionPane.showMessageDialog(null, "Error de acceso: admin no registrado");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    /*public static void main(String[] args) {
        login login1 = new login();
        login1.setContentPane(new login().panellog);
        login1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login1.setVisible(true);
        login1.pack();
    }*/
}


