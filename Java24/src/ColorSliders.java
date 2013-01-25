import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class ColorSliders extends JFrame implements ChangeListener 
{
        ColorPanel canvas;
        JSlider redSlider; JTextField redText;
        JSlider grnSlider; JTextField grnText;
        JSlider bluSlider; JTextField bluText;
        
        public ColorSliders() {
            super("Color Slider");
            setLookAndFeel();
            setSize(400,300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            canvas = new ColorPanel();
            
            redSlider = makeColorSlider(0,255, 255); // starts out all red 
            redText = new JTextField("255", 5);
            grnSlider = makeColorSlider(0,255,0);
            grnText = new JTextField(  "0", 5);
            bluSlider = makeColorSlider(0,255,0);
            bluText = new JTextField(  "0", 5);
                    
            setLayout (new GridLayout(4,1));
            
            FlowLayout rightLayout = new FlowLayout(FlowLayout.RIGHT);
            
            JPanel redPanel = new JPanel();
            redPanel.setLayout(rightLayout);
            redPanel.add(new JLabel("Red: "));
            redPanel.add(redText);
            redPanel.add(redSlider);
            add(redPanel);
            
            JPanel grnPanel = new JPanel();
            grnPanel.setLayout(rightLayout);
            grnPanel.add(new JLabel("Green: "));
            grnPanel.add(grnText);
            grnPanel.add(grnSlider);
            add(grnPanel);

            JPanel bluPanel = new JPanel();
            bluPanel.setLayout(rightLayout);
            bluPanel.add(new JLabel("Blue: "));
            bluPanel.add(bluText);
            bluPanel.add(bluSlider);
            add(bluPanel);
            
            add(canvas);
            setVisible(true);
        }
                
        private JSlider makeColorSlider(int r, int g, int b)
        {
            JSlider slider = new JSlider(r,g,b);
            slider.setMajorTickSpacing(50);
            slider.setMinorTickSpacing(10);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(this);
            return slider;
        }

        public void stateChanged (ChangeEvent event) 
        {
            JSlider source = (JSlider) event.getSource();
            // if(source.getValueIsAdjusting() != true)
            {
                int r = redSlider.getValue();
                int g = grnSlider.getValue();
                int b = bluSlider.getValue();
                Color currentColor = new Color(r,g,b);
                redText.setText(Integer.toString(r));
                grnText.setText(Integer.toString(g));
                bluText.setText(Integer.toString(b));
                canvas.changeColor(currentColor);
                canvas.repaint();
            }
        }
        
        public Insets getInsets() 
        {   
            Insets border = new Insets(45,10,10,10);
            return border;
        }
        
        private void setLookAndFeel()
        {
            try{ 
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch(Exception exc) {
                // ignore error
            }
        }
        
        public static void main(String[] args)
        {
            ColorSliders cs = new ColorSliders();
        }
                
}

class ColorPanel extends JPanel 
{
    Color background;
    
    ColorPanel() {
        background = Color.red;
    }
    
    public void paintComponent(Graphics comp)
    {   
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(background);
        comp2D.fillRect(0,0, getWidth(), getHeight());
    }
    
    void changeColor (Color newBackground) 
    {
        background = newBackground;
    }
}
