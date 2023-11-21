import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Facturacion extends JFrame{
    private JTextField nombreCliente;
    private JTable tablaProductos;
    private JButton gaseosasButton;
    private JButton botonEntradas;
    private JButton postresButton;
    private JButton sopasButton;
    private JButton jugosButton;
    private JButton pescadosMariscosButton;
    private JButton ensaladasButton;
    private JButton carnesYAvesButton;
    private JButton menuInfantilButton;
    private JButton hamburguesasButton;
    private JButton pastasButton;
    private JButton porcionesButton;
    private JPanel panelFacturacion;
    private JList list1;
    Connection conexion;
    PreparedStatement preparar;
    Statement traer;
    ResultSet resultado;
    private double subtotal = 0.0;
    String [] campos ={"nombre","precio"};
    String[] registros = new String[100];
    DefaultTableModel modeloTabla = new DefaultTableModel(null, campos);
    DefaultListModel<String> listModel = new DefaultListModel<>();

    public Facturacion() {
        botonEntradas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarEntradas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        jugosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarJugos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        carnesYAvesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarCarnesAves();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        pescadosMariscosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarPescadosMariscos();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        sopasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarSopas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        postresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarPostres();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ensaladasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarEnsaladas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        gaseosasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarGaseosas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        menuInfantilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarMenuInfantil();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        hamburguesasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarHamburguesas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        pastasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarPastas();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        porcionesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    consultarPorciones();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==1){
                   JTable source = (JTable) e.getSource();
                   int row = source.rowAtPoint(e.getPoint());
                    int colNombre = 0;
                    int colPrecio = 1;
                   if (row >=0){
                       Object nombreProducto = source.getValueAt(row, colNombre);
                       Object precio = source.getValueAt(row, colPrecio);
                       subtotal += Double.parseDouble(precio.toString());
                       if (nombreProducto.equals(nombreCliente.getText())){
                           listModel.add(0,"Nombre Cliente: "+nombreCliente.getText());
                           listModel.add(1, "-----------------------------------");
                       }else{
                           String textoConcatenado = nombreProducto.toString() + "- $" + precio.toString();
                           listModel.addElement(textoConcatenado);
                       }
                       if(row == source.getRowCount()-1){
                           listModel.addElement("Subtotal: $" + subtotal);
                           listModel.addElement("----------------------------");
                       }

                   }
                }

            }
        });
        nombreCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.insertElementAt("Nombre del Cliente: " + nombreCliente.getText(), 0);
                listModel.insertElementAt("-----------------------------------", 1);
                nombreCliente.setText("");
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

    public void consultarEntradas() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='entrada'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarGaseosas() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='gaseosas'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarPostres() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='postres'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarSopas() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='sopas'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarHamburguesas() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='hamburguesa'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarPastas() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='pasta'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarPorciones() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='porciones'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarPescadosMariscos() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='pescados y mariscos'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarCarnesAves() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='carnes y aves'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarJugos() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='jugos'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarMenuInfantil() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='menu infantil'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public void consultarEnsaladas() throws SQLException {
        conectar();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        try {
            tablaProductos.setModel(modeloTabla);
            traer = conexion.createStatement();
            resultado = traer.executeQuery("SELECT nombre, precio FROM productos where descripcion ='ensalada'");
            while (resultado.next()) {
                registros[0] = resultado.getString("nombre");
                registros[1] = resultado.getString("precio");
                modeloTabla.addRow(registros);
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (traer != null) {
                traer.close();
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Facturacion ingresar1 = new Facturacion();
            ingresar1.setLocationRelativeTo(null);
            ingresar1.setContentPane(ingresar1.panelFacturacion);
            ingresar1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ingresar1.list1.setModel(ingresar1.listModel);
            ingresar1.setVisible(true);
            ingresar1.pack();
        });
    }
}
