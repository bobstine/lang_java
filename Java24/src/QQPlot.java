import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class QQPlot extends JFrame implements ChangeListener 
{
        int           gridSize      =  500;              // cache size for values of x and f(x)
        float[]    scatterXPos      = new float[gridSize];
        float[]    scatterYPos      = new float[gridSize];
        float[]      xDensityY      = new float[gridSize];
        float[]      yDensityY      = new float[gridSize];
        
        JPanel        controlPanel  = new JPanel();
        JTextField      pctText     = new JTextField("0.0", 6);
        JSlider         pctSlider;
        JPanel        plotPanel     = new JPanel();
        ScatterPanel    scatPanel;                       // fill after init array
        JPanel          infoPanel   = new JPanel();
        XDensityPanel   xDensity ;
        YDensityPanel   yDensity ;
        
        public QQPlot() {
            super("QQ Plot");
            setLookAndFeel();
            setSize(400,400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout (new GridLayout(2,1));  // control and plot panels
 
            int inset=5;
            fillPositionArrays();
            scatPanel   = new ScatterPanel(gridSize, scatterXPos, scatterYPos);
            xDensity    = new XDensityPanel(gridSize, scatterXPos, xDensityY);
            yDensity    = new YDensityPanel(gridSize, scatterYPos, yDensityY);          
            
            // build control panel
            controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, inset,inset));
            ButtonGroup buttons = new ButtonGroup();
            JRadioButton normalButton = new JRadioButton("Normal");    buttons.add(normalButton); 
            JRadioButton skewButton   = new JRadioButton("Skewed");    buttons.add(skewButton);
            JRadioButton fatButton    = new JRadioButton("Fat tails"); buttons.add(fatButton);
            JRadioButton dataButton   = new JRadioButton("Data");      buttons.add(dataButton);
            normalButton.setSelected(true);
            // normalButton.addActionListener(this);
            controlPanel.add(normalButton);
            controlPanel.add(  skewButton);
            controlPanel.add(   fatButton);
            controlPanel.add(  dataButton);
            controlPanel.add(new JLabel("Percentile: "));
            controlPanel.add(pctText);
            pctSlider = makePctSlider(0, 100, 0);
            controlPanel.add(pctSlider);
            add(controlPanel);
            
            // build plot panel
            plotPanel.setLayout(new GridBagLayout());
            GridBagConstraints c00 = new GridBagConstraints();
            c00.gridx =0; c00.gridy=0; c00.fill = GridBagConstraints.BOTH;
            c00.weightx=0.2; c00.weighty=0.8;
            plotPanel.add(yDensity,c00);
            GridBagConstraints c01 = new GridBagConstraints();
            c01.gridx = 1; c01.gridy=0; c01.fill = GridBagConstraints.BOTH;
            c01.weightx=0.8; c01.weighty=0.8;
            plotPanel.add(scatPanel,c01);
            GridBagConstraints c10 = new GridBagConstraints();
            c10.gridx = 0; c10.gridy=1; c10.fill = GridBagConstraints.BOTH;
            c10.weightx=0.2; c10.weighty=0.2;
            plotPanel.add(infoPanel,c10);
            GridBagConstraints c11 = new GridBagConstraints();
            c11.gridx = 1; c11.gridy=1; c11.fill = GridBagConstraints.BOTH;
            c11.weightx=0.8; c11.weighty=0.3;
            plotPanel.add(xDensity,c11);
            add(plotPanel);
            // pack();                             // components determine size
            setVisible(true);
        }
        
     
        private void fillPositionArrays()
        {
            scatterXPos[0] = (float) Gaussian.PhiInverse(0.001);
            xDensityY[0]   = (float) Gaussian.phi(scatterXPos[0]);
            scatterYPos[0] = (float) Gaussian.PhiInverse(0.001);
            yDensityY[0]   = (float) Gaussian.phi(scatterYPos[0]);
            for(int i=1; i<gridSize; ++i) {
                double p = (double)i; p /= gridSize;
                double x = Gaussian.PhiInverse(p);
                scatterXPos[i] = (float) x;
                xDensityY[i]   = (float) Gaussian.phi(x);
                scatterYPos[i] = (float) x; 
                yDensityY[i]   = (float) Gaussian.phi(x);
            }
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

        @Override
        public void stateChanged (ChangeEvent event) 
        {
            JSlider source = (JSlider) event.getSource();
            {
                int p = pctSlider.getValue();
                pctText.setText(Integer.toString(p));
                float pct = p; pct/=100; 
                xDensity.changePosition(pct);
                xDensity.repaint();
                yDensity.changePosition(pct);
                yDensity.repaint();
                scatPanel.changePosition(pct);
                scatPanel.repaint();
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

class ScatterPanel extends JPanel 
{
    int       size = 100;           // points in precomputed array for size
    int       inset = 2;
    float[]   xArray, yArray;
    Color     background;
    float     percentile;           // current position between 0 to 1
    float     xMin, xMax, xRange;
    float     yMin, yMax, yRange;
 
    ScatterPanel(int mySize, float[] x, float[] y) 
    {
        size = mySize;
        background = Color.magenta;
        percentile = 0;
        xArray=x;
        yArray=y;
        xMin = x[0]; xMax = x[size-1]; xRange = xMax-xMin;
        yMin = y[0]; yMax = y[size-1]; yRange = yMax-yMin;
    }
    
    @Override
    public Dimension getPreferredSize() 
    {
        return new Dimension(300, 300);
    }

    @Override
    public void paintComponent(Graphics comp)
    {   
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(background);
        comp2D.fillRect(0,0, getWidth(), getHeight());
        comp2D.setColor(Color.black);
        comp2D.drawLine(0,getHeight(),  getWidth(),0);
        // System.out.println("Scatter panel @ p = " + percentile);
        int iX0, iY0, iX1, iY1;
        iX1=iY1=0;
        iX0=xPos(xArray[0]); iY0=yPos(yArray[0]);
        for(int i=1; i<size; ++i) {
            double p = i; p=p/size;
            if (percentile <= p) { break; }
            iX1 = xPos(xArray[i]);
            iY1 = yPos(yArray[i]);
            // System.out.println("    draw " + xArray[i-1]+"  "+ yArray[i-1]+"  "+ xArray[i]+"  "+yArray[i]);
            comp2D.drawLine(iX0, iY0, iX1, iY1);
            iX0=iX1; iY0=iY1;
        }
    }
    
                     
    int xPos (float x) 
    {
       int w = getWidth()-inset;
       return (Math.round((float)w * (x - xMin)/xRange));
    }
    
    int yPos (float y)
    {
        int h = getHeight()-inset;
        return (h - Math.round((float)h * (y - yMin)/yRange));
    }
        
    public void changePosition (float pct) 
    {
        percentile = pct;
    }
}


class YDensityPanel extends JPanel 
{
    int     size = 100;           // points in precomputed array for size
    int     inset = 2;            // pixel adjust 
    float[] xArray, yArray;
    Color   background;
    float   percentile;           // current position between 0 to 1
    float   xMin, xMax, xRange;   // bounds on x-axis positions
    float   yMin, yMax, yRange;   // bounds on y-axis positions
    
    YDensityPanel(int mySize, float[] x, float[] y) 
    {
        size = mySize;
        xArray = x; yArray = y;
        background = Color.yellow;
        percentile = (float)0.0;
        xMin=x[0]; xMax = x[size-1];
        xRange = xMax-xMin;
        yMin = yMax = 0;
        for(int i=0; i<size; ++i) 
        {   if      (yArray[i] < yMin) yMin = yArray[i];
            else if (yArray[i] > yMax) yMax = yArray[i];
        }
        yRange = yMax-yMin;
    }
    
    @Override
    public Dimension getPreferredSize() 
    {
        return new Dimension(100, 300);
    }

    public void paintComponent(Graphics comp)
    {   
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(background);
        comp2D.fillRect(0,0, getWidth(), getHeight());
        comp2D.setColor(Color.black);
        int iX0, iY0, iX1,iY1;
        iX1=iY1=0;
        iX0 = xPos(xMin); iY0=yPos(yMin);
        int[] xPts = new int[size+3];
        int[] yPts = new int[size+3];
        xPts[0]=iX0; yPts[0]=iY0;
        int nFilled = (int) (percentile*size);
        for(int i=1; i<size; ++i) {
            iX1 = xPos(xArray[i]);
            iY1 = yPos(yArray[i]);
            comp2D.drawLine(iY0, iX0, iY1, iX1);
            iX0=iX1; iY0=iY1;
            xPts[i] = iX1; yPts[i]=iY1;
        }
        if (nFilled > 0)
        {   xPts[nFilled+1]=xPts[nFilled]; yPts[nFilled+1]=getWidth();
            xPts[nFilled+2]=getHeight()  ; yPts[nFilled+2]=getWidth();
            // System.out.println(" last POS ("+yPts[nFilled  ]+","+xPts[nFilled  ]+")  @ nFilled="+nFilled);        
            // System.out.println("   penult ("+yPts[nFilled+1]+","+xPts[nFilled+1]+")");        
            // System.out.println("     last ("+yPts[nFilled+2]+","+xPts[nFilled+2]+")");        
            comp2D.fillPolygon(yPts,xPts,nFilled+3);
        }
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
    int     size = 100;           // points in precomputed array for size
    int     inset = 2;            // pixel adjust 
    float[] xArray, yArray;
    Color   background;
    float   percentile;           // current position between 0 to 1
    float   xMin, xMax, xRange;   // bounds on x-axis positions
    float   yMin, yMax, yRange;   // bounds on y-axis positions
    
    XDensityPanel(int mySize, float[] x, float[] y) 
    {
        size = mySize;
        xArray = x; yArray = y;
        background = Color.yellow;
        percentile = (float)0.0;
        xMin = x[0];
        xMax = x[size-1];
        xRange = xMax-xMin;
        yMin = yMax = 0;
        for(int i=0; i<size; ++i) 
        {   if      (yArray[i] < yMin) yMin = yArray[i];
            else if (yArray[i] > yMax) yMax = yArray[i];
        }
        yRange = yMax-yMin;
        System.out.println("X terms   min="+xMin+"   max="+xMax+"  range ="+xRange);
        System.out.println("Y terms   min="+yMin+"   max="+yMax+"  range ="+yRange); 
    }
    
    @Override
    public Dimension getPreferredSize() 
    {
        return new Dimension(300, 100);
    }

    public void paintComponent(Graphics comp)
    {   
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(background);
        comp2D.fillRect(0,0, getWidth(), getHeight());
        comp2D.setColor(Color.black);
        int iX0, iY0, iX1, iY1;
        iX1=iY1=0;  
        iX0 = xPos(xMin); iY0=yPos(yMin);
        int[] xPts = new int[size+3];
        int[] yPts = new int[size+3];
        xPts[0]=iX0; yPts[0]=iY0;
        int nFilled = (int) (percentile*size);
        for(int i=1; i<size; ++i) {
            iX1 = xPos(xArray[i]);
            iY1 = yPos(yArray[i]);
            comp2D.drawLine(iX0, iY0, iX1, iY1);
            iX0=iX1; iY0=iY1;
            xPts[i] = iX1; yPts[i]=iY1;
        }
        // System.out.println(" last filled  ("+xPts[nFilled]+","+yPts[nFilled]+")  @ nFilled="+nFilled);        
        xPts[nFilled+1  ]=xPts[nFilled]; yPts[nFilled+1  ]=0;
        // System.out.println(" next to last ("+xPts[nFilled+1]+","+yPts[nFilled+1]+")");        
        xPts[nFilled+2]= 0;  yPts[nFilled+2]=0;
        // System.out.println("         last ("+xPts[nFilled+2]+","+yPts[nFilled+2]+")");        
        comp2D.fillPolygon(xPts,yPts,nFilled+3);
        // comp2D.drawLine(iX1, iY1, iX1, 0);
    }
    
    public int xPos (float x) 
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