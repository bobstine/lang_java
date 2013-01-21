import javax.swing.*;

public class SalutonFrame extends JFrame {
    public SalutonFrame() {
        super("Saluton Frame says hello");
        setLookAndFeel();
        setSize(350,100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception exc) {
            // nothing done
        }
    }
    public static void main(String[] args) {
        SalutonFrame sal = new SalutonFrame();
    }
}