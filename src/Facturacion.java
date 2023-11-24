import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    JPanel panelFacturacion;
    JList list1;
    private JButton facturarButton;
    private JButton calcularButton;
    private JButton eliminarButton;
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
                if (e.getClickCount() == 1) {
                    JTable source = (JTable) e.getSource();
                    int row = source.rowAtPoint(e.getPoint());
                    int colNombre = 0;
                    int colPrecio = 1;
                    if (row >= 0) {
                        Object nombreProducto = source.getValueAt(row, colNombre);
                        Object precio = source.getValueAt(row, colPrecio);
                        if (nombreProducto.equals(nombreCliente.getText())) {
                            listModel.add(0, "Nombre Cliente: " + nombreCliente.getText());
                            listModel.add(1, "-----------------------------------");
                        } else {
                            String textoConcatenado = nombreProducto.toString() + "- $" + precio.toString();
                            listModel.addElement(textoConcatenado);
                            subtotal += Double.parseDouble(precio.toString());

                            //insertar en la base de datos venta
                            /*try {
                                insertarRegistroVenta(nombreProducto.toString(), Double.parseDouble(precio.toString()));
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }*/
                            try {
                                insertarRegistroVenta(nombreProducto.toString(), Double.parseDouble(precio.toString()), obtenerDescripcionProducto(nombreProducto.toString()));
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
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
        facturarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generarPDF();
                } catch (DocumentException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        calcularButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularTotales();
            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener el índice seleccionado en la JList
                int selectedIndex = list1.getSelectedIndex();

                if (selectedIndex != -1) {
                    // Obtener el elemento seleccionado en la JList
                    String elementoSeleccionado = listModel.getElementAt(selectedIndex);

                    // Verificar si el elemento pertenece a la JTable o es un nombre de cliente
                    if (!elementoSeleccionado.startsWith("Nombre Cliente")) {
                        // Extraer el nombre del producto del elemento
                        String nombreProducto = elementoSeleccionado.split("-")[0].trim();

                        // Restar el precio del producto eliminado al subtotal
                        double precio = Double.parseDouble(elementoSeleccionado.split("-")[1].replace("$", "").trim());
                        subtotal -= precio;

                        // Eliminar el elemento de la JList
                        listModel.remove(selectedIndex);

                        // eliminar registro de la base de datos
                        try {
                            eliminarRegistroVenta(nombreProducto);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No puedes eliminar el elemento.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecciona un elemento para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


    }
    private void calcularTotales() {
        // Calcular el INC (8% del subtotal)
        double inc = subtotal * 0.08;

        // Calcular la propina (10% del subtotal)
        double propina = subtotal * 0.10;

        // Calcular el total (subtotal + INC + propina)
        double total = subtotal + inc + propina;
        // Agregar los nuevos elementos al listModel
        listModel.addElement("Subtotal: $" + subtotal);
        listModel.addElement("INC (8%): $" + inc);
        listModel.addElement("Propina (10%): $" + propina);
        listModel.addElement("----------------------------");
        listModel.addElement("Total: $" + total);
    }


    private void generarPDF() throws DocumentException, IOException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("factura.pdf"));
            document.open();
            for (int i = 0; i < listModel.size(); i++) {
                Paragraph paragraph = new Paragraph(listModel.getElementAt(i));
                document.add(paragraph);
            }
            JOptionPane.showMessageDialog(null, "Factura generada correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Cerrar el documento fuera del bloque try-catch
            if (document != null && document.isOpen()) {
                document.close();
            }
        }
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
    /*public void insertarRegistroVenta(String nombreProducto, double precio) throws SQLException {
        conectar();
        try {
            preparar = conexion.prepareStatement("INSERT INTO venta (nombre, precio, fecha) VALUES (?, ?, NOW())");
            preparar.setString(1, nombreProducto);
            preparar.setDouble(2, precio);
            preparar.executeUpdate();
        } finally {
            if (preparar != null) {
                preparar.close();
            }
        }
    }*/

    public void insertarRegistroVenta(String nombreProducto, double precio, String descripcion) throws SQLException {
        conectar();
        try {
            preparar = conexion.prepareStatement("INSERT INTO venta (nombre, precio, fecha, descripcion) VALUES (?, ?, NOW(), ?)");
            preparar.setString(1, nombreProducto);
            preparar.setDouble(2, precio);
            preparar.setString(3, descripcion);
            preparar.executeUpdate();

            // Actualizar el stock después de la venta
            actualizarStockVenta(nombreProducto, 1);
        } finally {
            if (preparar != null) {
                preparar.close();
            }
        }
    }


    /*        Este es el actual
    public void insertarRegistroVenta(String nombreProducto, double precio, String descripcion) throws SQLException {
        conectar();
        try {
            preparar = conexion.prepareStatement("INSERT INTO venta (nombre, precio, fecha, descripcion) VALUES (?, ?, NOW(), ?)");
            preparar.setString(1, nombreProducto);
            preparar.setDouble(2, precio);
            preparar.setString(3, descripcion);
            preparar.executeUpdate();
        } finally {
            if (preparar != null) {
                preparar.close();
            }
        }
    }*/
    /*private void eliminarRegistroVenta(String nombreProducto) throws SQLException {
        conectar();
        try {
            preparar = conexion.prepareStatement("DELETE FROM venta WHERE nombre = ?");
            preparar.setString(1, nombreProducto);
            preparar.executeUpdate();
        } finally {
            if (preparar != null) {
                preparar.close();
            }
        }
    }*/
    private void eliminarRegistroVenta(String nombreProducto) throws SQLException {
        conectar();
        /*try {
            preparar = conexion.prepareStatement("DELETE FROM venta WHERE nombre = ? ORDER BY id DESC LIMIT 1");
            preparar.setString(1, nombreProducto);
            preparar.executeUpdate();
        } finally {
            if (preparar != null) {
                preparar.close();
            }
        }*/
        try {
            preparar = conexion.prepareStatement("DELETE FROM venta WHERE nombre = ? ORDER BY id DESC LIMIT 1");
            preparar.setString(1, nombreProducto);
            preparar.executeUpdate();

            // Restaurar la cantidad al eliminar la venta
            restaurarStockVenta(nombreProducto, 1);
        } finally {
            if (preparar != null) {
                preparar.close();
            }
        }
    }
    private String obtenerDescripcionProducto(String nombreProducto) throws SQLException {
        conectar();
        String descripcion = "";
        try {
            preparar = conexion.prepareStatement("SELECT descripcion FROM productos WHERE nombre = ?");
            preparar.setString(1, nombreProducto);
            resultado = preparar.executeQuery();
            if (resultado.next()) {
                descripcion = resultado.getString("descripcion");
            }
        } finally {
            if (resultado != null) {
                resultado.close();
            }
            if (preparar != null) {
                preparar.close();
            }
        }
        return descripcion;
    }

    private void actualizarStockVenta(String nombreProducto, double cantidad) throws SQLException {
        conectar();
        try {
            preparar = conexion.prepareStatement("UPDATE stockProductos SET cantidad = cantidad - ? WHERE nombre = ?");
            preparar.setDouble(1, cantidad);
            preparar.setString(2, nombreProducto);
            preparar.executeUpdate();
        } finally {
            if (preparar != null) {
                preparar.close();
            }
        }
    }
    private void restaurarStockVenta(String nombreProducto, double cantidad) throws SQLException {
        conectar();
        try {
            preparar = conexion.prepareStatement("UPDATE stockProductos SET cantidad = cantidad + ? WHERE nombre = ?");
            preparar.setDouble(1, cantidad);
            preparar.setString(2, nombreProducto);
            preparar.executeUpdate();
        } finally {
            if (preparar != null) {
                preparar.close();
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
