import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class QQPlot extends JFrame implements ChangeListener 
{
        JTextField pctText;
        JSlider pctSlider; 
        XDensityPanel xDensity;
        YDensityPanel yDensity;
        
        public QQPlot() {
            super("QQ Plot");
            setLookAndFeel();
            setSize(400,300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            
            pctSlider = makePctSlider(0, 100, 0); // starts out at 0 
            pctText = new JTextField("0.0", 6);
            setLayout (new GridLayout(3,1));
            FlowLayout rightLayout = new FlowLayout(FlowLayout.RIGHT);

            JPanel pctPanel = new JPanel();
            pctPanel.setLayout(rightLayout);
            pctPanel.add(new JLabel("Percentile: "));
            pctPanel.add(pctText);
            pctPanel.add(pctSlider);
            add(pctPanel);
            
            xDensity = new XDensityPanel();
            add(xDensity);
            yDensity = new YDensityPanel();
            add(yDensity);
            setVisible(true);
        }
                
        private JSlider makePctSlider(int r, int g, int b)
        {
            JSlider slider = new JSlider(r,g,b);
            slider.setMajorTickSpacing(25);
            slider.setMinorTickSpacing( 5);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(this);
            return slider;
        }

        public void stateChanged (ChangeEvent event) 
        {
            JSlider source = (JSlider) event.getSource();
            {
                int p = pctSlider.getValue();
                pctText.setText(Integer.toString(p));
                float pct = p; 
                xDensity.changePosition(pct/100);
                xDensity.repaint();
                yDensity.changePosition(pct/100);
                yDensity.repaint();
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
            QQPlot cs = new QQPlot();
        }
                
}


class YDensityPanel extends JPanel 
{
    int    size = 1000;           // points in precomputed array for size
    int    inset = 2;            // pixel adjust 
    Color  background;
    float  percentile;           // current position between 0 to 1
    float  xMin, xMax, xRange;   // bounds on x-axis positions
    float  yMin, yMax, yRange;   // bounds on y-axis positions
    
    
    YDensityPanel() 
    {
        background = Color.cyan;
        percentile = (float)0.0;
        // depend on Gaussian
        xMin = (float) Gaussian.PhiInverse(0.001);
        xMax = (float) Gaussian.PhiInverse(0.999);
        xRange = xMax-xMin;
        yMin = (float) Gaussian.phi(xMin);
        yMax = (float) Gaussian.phi(0);
        yRange = yMax-yMin;
    }
    
    public void paintComponent(Graphics comp)
    {   
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(background);
        comp2D.fillRect(0,0, getWidth(), getHeight());
        comp2D.setColor(Color.black);
        float x;
        int iX0, iY0, iX1=0,iY1=0;
        iX0 = xPos(xMin); iY0=yPos(yMin);
        for(int i=1; i<size; ++i) {
            double p = i; p=p/size;
            if (percentile <= p) { break; }
            iX1 = xPos(x = (float)Gaussian.PhiInverse(p));
            iY1 = yPos((float)Gaussian.phi(x));
            // System.out.println(" drawing coor  ("+iX0+","+iY0+")  ("+iX1+","+iY1+")");        
            comp2D.drawLine(iY0, iX0, iY1, iX1);
            iX0=iX1; iY0=iY1;
        }
        comp2D.drawLine(iY1, iX1, getWidth(), iX1);
    }
    
  
    int xPos (float x) 
    {
        int h = getHeight()-inset;
        return (h - Math.round((float)h * (x - xMin)/xRange));
    }
    
    int yPos (float y)
    {
        int w = getWidth()-inset;
        return (w - Math.round((float)w * (y - yMin)/yRange));
        
    }
    void changePosition (float pct) 
    {
        percentile = pct;
    }
}



class XDensityPanel extends JPanel 
{
    int    size = 1000;           // points in precomputed array for size
    int    inset = 2;            // pixel adjust 
    Color  background;
    float  percentile;           // current position between 0 to 1
    float  xMin, xMax, xRange;   // bounds on x-axis positions
    float  yMin, yMax, yRange;   // bounds on y-axis positions
    
    
    XDensityPanel() 
    {
        background = Color.yellow;
        percentile = (float)0.0;
        // depend on Gaussian
        xMin = (float) Gaussian.PhiInverse(0.001);
        xMax = (float) Gaussian.PhiInverse(0.999);
        xRange = xMax-xMin;
        yMin = (float) Gaussian.phi(xMin);
        yMax = (float) Gaussian.phi(0);
        yRange = yMax-yMin;
        System.out.println("X terms   min="+xMin+"   max="+xMax+"  range ="+xRange);
        System.out.println("Y terms   min="+yMin+"   max="+yMax+"  range ="+yRange); 
    }
    
    public void paintComponent(Graphics comp)
    {   
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(background);
        comp2D.fillRect(0,0, getWidth(), getHeight());
        comp2D.setColor(Color.black);
        float x;
        int iX0, iY0, iX1=0,iY1=0;
        iX0 = xPos(xMin); iY0=yPos(yMin);
        for(int i=1; i<size; ++i) {
            double p = i; p=p/size;
            if (percentile <= p) { break; }
            iX1 = xPos(x = (float)Gaussian.PhiInverse(p));
            iY1 = yPos((float)Gaussian.phi(x));
            // System.out.println(" drawing coor  ("+iX0+","+iY0+")  ("+iX1+","+iY1+")");        
            comp2D.drawLine(iX0, iY0, iX1, iY1);
            iX0=iX1; iY0=iY1;
        }
        comp2D.drawLine(iX1, iY1, iX1, 0);
    }
    
    int xPos (float x) 
    {
        int w = getWidth()-inset;
        return (Math.round((float)w * (x - xMin)/xRange));
    }
    
    int yPos (float y)
    {
        int h = getHeight()-inset;
        return (Math.round((float)h * (y - yMin)/yRange));
        
    }
    void changePosition (float pct) 
    {
        percentile = pct;
    }
}


class Gaussian {

    // return phi(x) = standard Gaussian pdf
    public static double phi(double x) {
        return Math.exp(-x*x / 2) / Math.sqrt(2 * Math.PI);
    }

    // return phi(x, mu, signma) = Gaussian pdf with mean mu and stddev sigma
    public static double phi(double x, double mu, double sigma) {
        return phi((x - mu) / sigma) / sigma;
    }

    // return Phi(z) = standard Gaussian cdf using Taylor approximation
    public static double Phi(double z) {
        if (z < -8.0) return 0.0;
        if (z >  8.0) return 1.0;
        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum  = sum + term;
            term = term * z * z / i;
        }
        return 0.5 + sum * phi(z);
    }

    // return Phi(z, mu, sigma) = Gaussian cdf with mean mu and stddev sigma
    public static double Phi(double z, double mu, double sigma) {
        return Phi((z - mu) / sigma);
    } 

    // Compute z such that Phi(z) = y via bisection search
    public static double PhiInverse(double y) {
        return PhiInverse(y, .00000001, -8, 8);
    } 

    // bisection search
    private static double PhiInverse(double y, double delta, double lo, double hi) {
        double mid = lo + (hi - lo) / 2;
        if (hi - lo < delta) return mid;
        if (Phi(mid) > y) return PhiInverse(y, delta, lo, mid);
        else              return PhiInverse(y, delta, mid, hi);
    }

}