/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package my.boxshader;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author bob
 */
public class ShadePanel extends javax.swing.JPanel {
    
    float   mPercentile;           // current position between 0 to 1
    
    public ShadePanel() 
    {
       mPercentile = 0;
    }
   
    @Override
    public void paintComponent(Graphics comp)
    {   
        Graphics2D comp2D = (Graphics2D) comp;
        comp2D.setColor(Color.red);
        comp2D.fillRect(0,0, 
                Math.round(mPercentile * getWidth ()), 
                Math.round(mPercentile * getHeight()));
    }
    
   void set_percentile (float pct) 
    {
        mPercentile = pct;
        System.out.println("Shade panel percentile set to " + mPercentile);
        // System.out.println("                width now set " + Math.round(mPercentile * getWidth ()));
    }
}
