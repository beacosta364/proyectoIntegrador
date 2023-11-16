import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class productos extends JFrame{
    private JTextField nombreText;
    private JTextField descripcionText;
    private JTextField precioText;
    private JTable tablaProductos;
    private JButton botonConsultar;
    private JButton botonRegistrar;
    private JButton botonEliminar;
    private JButton botonUpdate;
    JPanel productoPanel;
    private JButton closeButton;
    Connection conexion;
    PreparedStatement preparar;
    PreparedStatement update;
    String [] campos ={"id","nombre","descripcion","precio"};
    String[] registros = new String[100];
    //DefaultTableModel modeloTabla = new DefaultTableModel(null, campos);
    DefaultTableModel modeloTabla = new DefaultTableModel(null, campos);
    Statement traer;
    ResultSet resultado;

    public productos() {
        try {
            consultar();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        tablaProductos.setModel(modeloTabla);

        botonConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        botonRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ingresar();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        botonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarDatos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        botonEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarRegistros();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
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
    public void consultar() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT id, nombre, descripcion, precio FROM productos");
            while (resultado.next()) {
                registros[0] = resultado.getString("id");
                registros[1] = resultado.getString("nombre");
                registros[2] = resultado.getString("descripcion");
                registros[3] = resultado.getString("precio");
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
        preparar = conexion.prepareStatement("Insert into productos(nombre, descripcion, precio) values (?,?,?) ");
        preparar.setString(1, nombreText.getText());
        preparar.setString(2, descripcionText.getText());
        preparar.setInt(3,Integer.parseInt(precioText.getText()));
        if (preparar.executeUpdate()>0){
            JOptionPane.showMessageDialog(null, "Registro Exitoso");

            nombreText.setText("");
            descripcionText.setText("");
            precioText.setText("");
        }

    }


    public void actualizarDatos() throws SQLException {
        try {
            String SQL = "update productos set nombre=?, descripcion=?, precio=? where id=?";

            int filaSeleccionado = tablaProductos.getSelectedRow();

            String daoId = (String) tablaProductos.getValueAt(filaSeleccionado, 0);

            PreparedStatement pst = conexion.prepareStatement(SQL);
            pst.setString(1, nombreText.getText());
            pst.setString(2, descripcionText.getText());
            pst.setInt(3, Integer.parseInt(precioText.getText()));

            // Establecer el valor del cuarto parámetro (id)
            pst.setString(4, daoId);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Registro Editado Exitoso");
            consultar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error de Edicion" + e.getMessage());
        }
    }
    public void eliminarRegistros() throws SQLException {
        try {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione una fila para eliminar.");
                return; // No row selected, exit the method
            }
            int idUsuario = Integer.parseInt((String) tablaProductos.getValueAt(filaSeleccionada, 0));
            String SQL = "DELETE FROM productos WHERE id = ?";
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

    /*public static void main(String[] args) {
        productos producto1 = new productos();
        producto1.setContentPane(new productos().productoPanel);
        producto1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        producto1.setVisible(true);
        producto1.pack();
    }*/
}
