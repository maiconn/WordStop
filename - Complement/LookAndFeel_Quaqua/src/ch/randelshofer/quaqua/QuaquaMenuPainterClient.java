/*
 * @(#)QuaquaMenuPainterClient.java  
 *
 * Copyright (c) 2003-2010 Werner Randelshofer
 * Hausmatt 10, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Werner Randelshofer. For details see accompanying license terms. 
 */

package ch.randelshofer.quaqua;

import java.awt.*;
import javax.swing.*;
/**
 * QuaquaMenuPainterClient.
 *
 * @author  Werner Randelshofer
 * @version $Id: QuaquaMenuPainterClient.java 174 2010-01-09 16:06:52Z wrandelshofer $
 */
public interface QuaquaMenuPainterClient {
    
   public void paintBackground(Graphics g, JComponent c, int i, int j);
   
    //public ThemeMenu getTheme();
}
