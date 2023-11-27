import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class stockProductosVenta extends JFrame {
    JPanel panelStockPV;
    private JTextField nombreText;
    private JTextField cantidadText;
    private JTable tabladatos;
    private JButton consultarButton;
    private JButton registrarButton;
    private JButton eliminarButton;
    private JButton updateButton;
    Connection conexion;
    PreparedStatement preparar;
    PreparedStatement update;
    DefaultListModel modelo = new DefaultListModel();
    String [] campos ={"id","cantidad","nombre"};
    String[] registros = new String[100];
    DefaultTableModel modeloTabla = new DefaultTableModel(null, campos);
    Statement traer;
    ResultSet resultado;

    public stockProductosVenta() {
        try {
            consultar();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        tabladatos.setModel(modeloTabla);
        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ingresar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarRegistros();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
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
            tabladatos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT id, cantidad, nombre FROM stockProductos");

            while (resultado.next()) {
                registros[0] = resultado.getString("id");
                registros[1] = resultado.getString("cantidad");
                registros[2] = resultado.getString("nombre");
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
    public void ingresar() throws SQLException {
        conectar();
        preparar = conexion.prepareStatement("Insert into stockProductos(cantidad, nombre) values (?,?) ");
        // preparar.setInt(1,Integer.parseInt(idText.getText()));
        preparar.setString(2, nombreText.getText());
        //preparar.setString(2, rolText.getText());
        preparar.setInt(1,Integer.parseInt(cantidadText.getText()));
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
    public void eliminarRegistros() throws SQLException {
        try {
            int filaSeleccionada = tabladatos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila para eliminar.");
                return; // No row selected, exit the method
            }
            int idUsuario = Integer.parseInt((String) tabladatos.getValueAt(filaSeleccionada, 0));
            String SQL = "DELETE FROM stockProductos WHERE id = ?";
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
            String SQL = "update stockProductos set cantidad=?, nombre=? where id=?";

            int filaSeleccionado = tabladatos.getSelectedRow();
            String daoId = (String) tabladatos.getValueAt(filaSeleccionado, 0);
            PreparedStatement pst = conexion.prepareStatement(SQL);
            pst.setString(2, nombreText.getText());
            pst.setInt(1, Integer.parseInt(cantidadText.getText()));
            pst.setInt(3, Integer.parseInt(daoId));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Registro Editado Exitoso");

            // Optionally, you might want to refresh the table after editing
            consultar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Edicion" + e.getMessage());
        }
    }
    /*public static void main(String[] args) {
        stockProductosVenta ingresar1 = new stockProductosVenta();
        ingresar1.setLocationRelativeTo(null);
        ingresar1.setContentPane(new stockProductosVenta().panelStockPV);
        ingresar1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ingresar1.setVisible(true);
        ingresar1.pack();
    }*/
}
