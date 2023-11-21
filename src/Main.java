import javax.swing.*;

/*public class Main {

    public static void main(String[] args) {
        ingresar ingresar1 = new ingresar();
        ingresar1.setLocationRelativeTo(null);
        ingresar1.setContentPane(new ingresar().panelIngresar);
        ingresar1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ingresar1.setVisible(true);
        ingresar1.pack();
//Algoritmo de busqueda
//concatenar QUERY
    }
}*/
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ingresar ingresar1 = new ingresar();
            ingresar1.setLocationRelativeTo(null);
            ingresar1.setContentPane(new ingresar().panelIngresar);
            ingresar1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ingresar1.setVisible(true);
            ingresar1.pack();
        });
    }
}





