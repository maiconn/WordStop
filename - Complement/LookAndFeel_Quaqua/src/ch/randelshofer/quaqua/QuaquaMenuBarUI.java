/*
 * @(#)QuaquaMenuBarUI.java  
 *
 * Copyright (c) 2004-2010 Werner Randelshofer
 * Hausmatt 10, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Werner Randelshofer. For details see accompanying license terms. 
 */

package ch.randelshofer.quaqua;

import ch.randelshofer.quaqua.border.BackgroundBorder;
import ch.randelshofer.quaqua.color.PaintableColor;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
/**
 * QuaquaMenuBarUI.
 *
 * @author  Werner Randelshofer
 * @version $Id: QuaquaMenuBarUI.java 174 2010-01-09 16:06:52Z wrandelshofer $
 */
public class QuaquaMenuBarUI extends BasicMenuBarUI {
    
    /** Creates a new instance. */
    public QuaquaMenuBarUI() {
    }
    
    public static ComponentUI createUI(JComponent x) {
        return new QuaquaMenuBarUI();
    }
    public void paint(Graphics gr, JComponent c) {
        Graphics2D g = (Graphics2D) gr;
        int w = c.getWidth();
        int h = c.getHeight();
        
        g.setPaint(PaintableColor.getPaint(c.getBackground(), c));
        g.fillRect(0, 0, w, h);
        
        if (c.getBorder() instanceof BackgroundBorder) {
            Border bb = ((BackgroundBorder) c.getBorder()).getBackgroundBorder();
            bb.paintBorder(c, gr, 0, 0, w, h);
        }
        
        Color shadow = UIManager.getColor("MenuBar.shadow");
        if (shadow != null) {
            g.setColor(shadow);
            g.fillRect(0, h - 1, w, 1);
        }
    }
}
