/*
 * @(#)QuaquaPopupMenuUI.java
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

import ch.randelshofer.quaqua.color.PaintableColor;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

/**
 * QuaquaPopupMenuUI.
 *
 * @author Werner Randelshofer
 * @version $Id: QuaquaPopupMenuUI.java 174 2010-01-09 16:06:52Z wrandelshofer $
 */
public class QuaquaPopupMenuUI extends BasicPopupMenuUI implements QuaquaMenuPainterClient {

    public static ComponentUI createUI(JComponent x) {
        return new QuaquaPopupMenuUI();
    }

    public QuaquaPopupMenuUI() {
    }

    public void paintBackground(Graphics g, JComponent component, int menuWidth, int menuHeight) {
        Color bgColor = UIManager.getColor("PopupMenu.selectionBackground");
        AbstractButton menuItem = (AbstractButton) component;
        ButtonModel model = menuItem.getModel();
        Color oldColor = g.getColor();

        if (menuItem.isOpaque()) {
            if (model.isArmed() || model.isSelected()) {
                ((Graphics2D) g).setPaint(PaintableColor.getPaint(bgColor, component));
                g.fillRect(0, 0, menuWidth, menuHeight);
            } else {
                ((Graphics2D) g).setPaint(PaintableColor.getPaint(menuItem.getBackground(), component));
                g.fillRect(0, 0, menuWidth, menuHeight);
            }
            g.setColor(oldColor);
        }
    }

    /**
     * Returns the <code>Popup</code> that will be responsible for
     * displaying the <code>JPopupMenu</code>.
     *
     * @param popup JPopupMenu requesting Popup
     * @param x     Screen x location Popup is to be shown at
     * @param y     Screen y location Popup is to be shown at.
     * @return Popup that will show the JPopupMenu
     * @since 1.4
     *//*
    public Popup getPopup(JPopupMenu popup, int x, int y) {
            PopupFactory popupFactory = QuaquaPopupFactory.getSharedInstance();
            return popupFactory.getPopup(popup.getInvoker(), popup, x, y);
    }*/
}
