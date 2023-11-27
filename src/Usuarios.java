import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Usuarios extends JFrame{
    public JPanel panel;
    private JTextField nombreText;
    private JTextField cantidadText;
    private JButton consultarBoton;
    private JButton registrarBoton;
    private JButton eliminarBoton;
    private JButton updateBoton;
    private JTable tablaDatos;
    Connection conexion;
    PreparedStatement preparar;
    PreparedStatement update;
    DefaultListModel modelo = new DefaultListModel();
    String [] campos ={"id","nombre_producto","cantidad"};
    String[] registros = new String[100];
    DefaultTableModel modeloTabla = new DefaultTableModel(null, campos);
    Statement traer;
    ResultSet resultado;

    public Usuarios() {
        try {
            consultar();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        tablaDatos.setModel(modeloTabla);

        registrarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent es) {
                try {
                    ingresar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        consultarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        eliminarBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarRegistros();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        updateBoton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarDatos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    public void conectar(){
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_integrador", "root", "beacosta");

            //conexion = DriverManager.getConnection("jdbc:mysql://27.0.0.1:3306/Usuarios","root","beacosta");
        }catch (SQLException e){
            throw new RuntimeException(e);

        }

    }

    public void consultar() throws SQLException {
        conectar();

        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);

        try {
            tablaDatos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT id, nombre_producto, cantidad FROM stock");

            while (resultado.next()) {
                registros[0] = resultado.getString("id");
                registros[1] = resultado.getString("nombre_producto");
                registros[2] = resultado.getString("cantidad");
                modeloTabla.addRow(registros);
            }
        } finally {
            // Cerrar recursos en el bloque finally
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }


    public void eliminarRegistros() throws SQLException {
        try {
            int filaSeleccionada = tablaDatos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila para eliminar.");
                return; // No row selected, exit the method
            }
            int idUsuario = Integer.parseInt((String) tablaDatos.getValueAt(filaSeleccionada, 0));
            String SQL = "DELETE FROM stock WHERE id = ?";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(SQL)) {
                preparedStatement.setInt(1, idUsuario);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Registro Eliminado");
                    consultar();
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el registro a eliminar.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al Eliminar Registro: " + e.getMessage());
        }
    }

    public void actualizarDatos() throws SQLException {
        try {
            String SQL = "update stock set nombre_producto=?, cantidad=? where id=?";

            int filaSeleccionado = tablaDatos.getSelectedRow();
            String daoId = (String) tablaDatos.getValueAt(filaSeleccionado, 0);
            PreparedStatement pst = conexion.prepareStatement(SQL);
            pst.setString(1, nombreText.getText());
            pst.setInt(2, Integer.parseInt(cantidadText.getText()));
            pst.setInt(3, Integer.parseInt(daoId));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Registro Editado Exitoso");

            // Optionally, you might want to refresh the table after editing
            consultar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Edicion" + e.getMessage());
        }
    }



    public void ingresar() throws SQLException {
        conectar();
        preparar = conexion.prepareStatement("Insert into stock(nombre_producto, cantidad) values (?,?) ");
       // preparar.setInt(1,Integer.parseInt(idText.getText()));
        preparar.setString(1, nombreText.getText());
        //preparar.setString(2, rolText.getText());
        preparar.setInt(2,Integer.parseInt(cantidadText.getText()));
        if (preparar.executeUpdate()>0){
            //lista.setModel(modelo);
           // modelo.removeAllElements();
            //modelo.addElement("ingreso de campo exitoso");
            JOptionPane.showMessageDialog(null, "Registro Exitoso");
            //idText.setText("");
            nombreText.setText("");
            cantidadText.setText("");

        }

    }
    /*public static void main(String[] args) {
        Usuarios usuario1 = new Usuarios();
        usuario1.setContentPane(new Usuarios().panel);
        usuario1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        usuario1.setVisible(true);
        usuario1.pack();
    }*/

}
