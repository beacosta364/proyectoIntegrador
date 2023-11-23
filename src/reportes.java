import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class reportes extends JFrame {
    private JList listaReportes;
    private JButton reporteSemanalButton;
    private JButton productosVendidosButton;
    private JButton reporteDiarioButton;
    private JButton datosEstadisticosButton;
    private JButton reporteMensualButton;
    private JPanel panelReportes;
    Connection conexion;
    PreparedStatement preparar;
    Statement traer;
    ResultSet resultado;

    public reportes() {
        reporteDiarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporteEnsaladas();
                generarReporteDiario();
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


    public void generarReporteDiario() {
        conectar();

        // Nueva consulta SQL
        String consulta = "SELECT DATE(fecha) AS ultimo_dia, SUM(precio) AS suma_precios " +
                "FROM venta " +
                "GROUP BY ultimo_dia " +
                "ORDER BY ultimo_dia DESC " +
                "LIMIT 1";

        // Obtener la fecha y hora actual del sistema
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHoraActualStr = fechaHoraActual.format(formatter);

        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(consulta)) {

            if (resultSet.next()) {
                // Obtener los resultados de la consulta
                double subTotalVentas = resultSet.getDouble("suma_precios");

                // Mostrar la fecha y hora en la parte superior del JList y otras leyendas
                DefaultListModel model = new DefaultListModel();
                model.addElement("Fecha y Hora: " + fechaHoraActualStr);
                model.addElement("Razón social:xxxxxxxxxxxxx");
                model.addElement("NIT:xxxxxxxxxxxxx");
                model.addElement("Dirección:xxxxxxxxxxxxx");
                model.addElement("----------------------------");
                model.addElement("Subtotal venta: " + subTotalVentas);

                // Calcular el INC (8% del subtotal)
                double inc = subTotalVentas * 0.08;
                model.addElement("INC (8%): $" + inc);
                // Calcular la propina (10% del subtotal)
                double propina = subTotalVentas * 0.10;
                model.addElement("Propina (10%): $" + propina);
                // Calcular el total (subtotal + INC + propina)
                double total = subTotalVentas + inc + propina;
                model.addElement("----------------------------");
                model.addElement("Total: $" + total);
                model.addElement("----------------------------");

                listaReportes.setModel(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void generarReporteEnsaladas() {
        conectar();

        // Nueva consulta SQL para el subtotal de ensaladas
        String consultaEnsaladas = "SELECT DATE(fecha) AS ultimo_dia, " +
                "SUM(CASE WHEN descripcion = 'ensalada' THEN precio ELSE 0 END) AS subtotal_ensaladas " +
                "FROM venta " +
                "GROUP BY ultimo_dia " +
                "ORDER BY ultimo_dia DESC " +
                "LIMIT 1";

        // Obtener la fecha y hora actual del sistema


        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(consultaEnsaladas)) {

            if (resultSet.next()) {
                // Obtener los resultados de la consulta de ensaladas
                double subtotalEnsaladas = resultSet.getDouble("subtotal_ensaladas");

                // Mostrar la fecha y hora en la parte superior del JList y otras leyendas
                DefaultListModel model = new DefaultListModel();
                model.addElement("Razón social:xxxxxxxxxxxxx");
                model.addElement("NIT:xxxxxxxxxxxxx");
                model.addElement("Dirección:xxxxxxxxxxxxx");
                model.addElement("----------------------------");
                model.addElement("Subtotal Ensaladas: " + subtotalEnsaladas);
                listaReportes.setModel(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conexion != null) {
                    conexion.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        reportes reporte = new reportes();
        reporte.setContentPane(new reportes().panelReportes);
        reporte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reporte.setVisible(true);
        reporte.pack();
    }
}

