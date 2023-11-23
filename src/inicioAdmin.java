import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class inicioAdmin extends JFrame{
    private JButton STOCKButton;
    private JButton modiificarPreciosButton;
    private JButton registrosCuentasAccesoButton;
    private JButton reportesButton;
    JPanel panelAdmin;
    private JButton botonFacturacion;

    public inicioAdmin() {
        STOCKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuarios usuario1 = new Usuarios();
                usuario1.setContentPane(new Usuarios().panel);
                usuario1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                usuario1.setVisible(true);
                usuario1.pack();
            }
        });
        modiificarPreciosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productos producto1 = new productos();
                producto1.setContentPane(new productos().productoPanel);
                producto1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                producto1.setVisible(true);
                producto1.pack();
            }
        });
        registrosCuentasAccesoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login login1 = new login();
                login1.setContentPane(new login().panellog);
                login1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                login1.setVisible(true);
                login1.pack();
            }
        });
        botonFacturacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Facturacion ingresar1 = new Facturacion();
                ingresar1.setLocationRelativeTo(null);
                ingresar1.setContentPane(ingresar1.panelFacturacion);
                ingresar1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ingresar1.list1.setModel(ingresar1.listModel);
                ingresar1.setVisible(true);
                ingresar1.pack();
            }
        });
    }
    /*public static void main(String[] args) {
        inicioAdmin ingresar1 = new inicioAdmin();
        ingresar1.setLocationRelativeTo(null);
        ingresar1.setContentPane(new inicioAdmin().panelAdmin);
        ingresar1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ingresar1.setVisible(true);
        ingresar1.pack();

    }*/
}
