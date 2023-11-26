import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class registroClientes extends JFrame {
    private JTextField nombreText;
    private JTextField direccionText;
    private JTextField contactoText;
    JPanel panelClientes;
    private JButton registrarButton;
    private JButton buscarButton;
    private JButton facturarButton;
    Connection conexion;
    PreparedStatement preparar;
    Statement traer;
    ResultSet resultado;

    public registroClientes() {
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarInformacion();
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarInformacion();
            }
        });
    }

    public void conectar() {
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_integrador", "root", "beacosta");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarInformacion() {
        conectar();

        String nombre = nombreText.getText();
        String direccion = direccionText.getText();

        double contacto;
        try {
            contacto = Double.parseDouble(contactoText.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en el formato del contacto. Debe ser un número válido.");
            return;
        }

        try {
            String query = "INSERT INTO clientes (nombre, direccion, contacto) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conexion.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, direccion);
            preparedStatement.setDouble(3, contacto);

            preparedStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Registro exitoso");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar");
        }
    }

   public void buscarInformacion() {
       conectar();

       double contacto;
       try {
           contacto = Double.parseDouble(contactoText.getText());
       } catch (NumberFormatException e) {
           JOptionPane.showMessageDialog(null, "Error en el formato del contacto. Debe ser un número válido.");
           return;
       }

       try {
           String query = "SELECT * FROM clientes WHERE contacto = ?";
           PreparedStatement preparedStatement = conexion.prepareStatement(query);
           preparedStatement.setDouble(1, contacto);

           ResultSet resultSet = preparedStatement.executeQuery();

           if (resultSet.next()) {
               String nombre = resultSet.getString("nombre");
               String direccion = resultSet.getString("direccion");
               String telefono = resultSet.getString("contacto");

               JOptionPane.showMessageDialog(null, "Información del cliente:\nNombre: " + nombre +
                       "\nDirección: " + direccion + "\nTeléfono: " + telefono);
           } else {
               JOptionPane.showMessageDialog(null, "No se encontró información para el contacto proporcionado.");
           }
       } catch (SQLException ex) {
           ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Error al buscar información");
       }
   }


    public static void main(String[] args) {
        registroClientes registro = new registroClientes();
        registro.setLocationRelativeTo(null);
        registro.setContentPane(new registroClientes().panelClientes);
        registro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registro.setVisible(true);
        registro.pack();
    }
}