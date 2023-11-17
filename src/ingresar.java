import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class ingresar extends JFrame{
    private JTextField UsuarioText;
    private JPasswordField contraText;
    private JButton botonValidarAdmin;
    private JButton botonValidarUser;
    JPanel panelIngresar;
    Connection conexion;
    PreparedStatement preparar;
    Statement traer;
    ResultSet resultado;

    public ingresar() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("Ventana cerrándose");
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
    }

    public void conectar(){
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_integrador", "root", "beacosta");
        }catch (SQLException e){
            throw new RuntimeException(e);

        }

    }
    public void validarUsuario() {
        conectar();
        String usuario = UsuarioText.getText();
        String contraseña = String.valueOf(contraText.getPassword());

        try {
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT usuario, contraseña FROM Usuarios WHERE usuario ='" + usuario + "' AND contraseña ='" + contraseña + "'");
            if (resultado.next()) {
                this.dispose();
                Usuarios usuario1 = new Usuarios();
                usuario1.setContentPane(new Usuarios().panel);
                usuario1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                usuario1.setVisible(true);
                usuario1.pack();
                JOptionPane.showMessageDialog(null, "Acceso permitido");
            } else {
                JOptionPane.showMessageDialog(null, "Error de acceso: usuario no registrado");
            }
            /* if (resultado.next()) {
                this.dispose();
                SwingUtilities.invokeLater(() -> {

                    Usuarios usuario1 = new Usuarios();
                    usuario1.setLocationRelativeTo(null);
                    usuario1.setContentPane(new Usuarios().panel);
                    usuario1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    usuario1.setVisible(true);
                    usuario1.pack();
                    //System.exit(0);
                });
                JOptionPane.showMessageDialog(null, "Acceso permitido");
            } else {
                JOptionPane.showMessageDialog(null, "Error de acceso: usuario no registrado");
            }*/
           /* if (resultado.next()) {
                this.dispose();
                Usuarios usuario1 = new Usuarios();
                usuario1.setLocationRelativeTo(null);
                usuario1.setContentPane(new Usuarios().panel);
                usuario1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                usuario1.setVisible(true);
                usuario1.pack();


                JOptionPane.showMessageDialog(null, "Acceso permitido");
            } else {
                JOptionPane.showMessageDialog(null, "Error de acceso: usuario no registrado");
            }*/
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
                inicioAdmin ingresar1 = new inicioAdmin();
                ingresar1.setLocationRelativeTo(null);
                ingresar1.setContentPane(new inicioAdmin().panelAdmin);
                ingresar1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ingresar1.setVisible(true);
                ingresar1.pack();

                JOptionPane.showMessageDialog(null, "Acceso permitido");
            } else {
                JOptionPane.showMessageDialog(null, "Error de acceso: admin no registrado");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}

/*public static void main(String[] args) {
        ingresar ingresar1 = new ingresar();
        ingresar1.setContentPane(new ingresar().panelIngresar);
        ingresar1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ingresar1.setVisible(true);
        ingresar1.pack();
    }*/


