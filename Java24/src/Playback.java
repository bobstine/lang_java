import javax.swing.*;
import java.awt.*;

public class Playback extends JFrame {
    public Playback() {
        super("Playback"); // window name
        setLookAndFeel();
        setSize(400,160);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout layout = new FlowLayout();
        setLayout (layout);
        addButton ("Play");
        addButton ("Stop");
        addButton ("Pause");
        JLabel lab = new JLabel("Type a name here:", JLabel.LEFT);
        JTextField txt = new JTextField("--- your text here ---",30);
        add(lab);
        add(txt);
        JCheckBox cb = new JCheckBox("Check box");
        add(cb);
        setVisible(true);
    }
    
    private void addButton(String name) {
        JButton b = new JButton(name);
        add(b);
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
        Playback pb = new Playback();
    }
}