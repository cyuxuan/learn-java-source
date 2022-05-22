/*
 * @(#)ComponentPeer.java	1.24 01/12/12
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.awt.peer;

import java.awt.*;
import java.awt.image.ImageProducer;
import java.awt.image.ImageObserver;
import java.awt.image.ColorModel;

public interface ComponentPeer {
    void    	    	setVisible(boolean b);
    void    	    	setEnabled(boolean b);
    void		paint(Graphics g);
    void		repaint(long tm, int x, int y, int width, int height);
    void		print(Graphics g);
    void		setBounds(int x, int y, int width, int height);
    void                handleEvent(AWTEvent e);
    Point		getLocationOnScreen();
    Dimension		getPreferredSize();
    Dimension		getMinimumSize();
    ColorModel		getColorModel();
    java.awt.Toolkit	getToolkit();
    Graphics		getGraphics();
    FontMetrics		getFontMetrics(Font font);
    void		dispose();
    void		setForeground(Color c);
    void		setBackground(Color c);
    void		setFont(Font f);
    void 		setCursor(Cursor cursor);
    void		requestFocus();
    boolean		isFocusTraversable();

    Image 		createImage(ImageProducer producer);
    Image 		createImage(int width, int height);
    boolean		prepareImage(Image img, int w, int h, ImageObserver o);
    int			checkImage(Image img, int w, int h, ImageObserver o);

    /**
     * DEPRECATED:  Replaced by getPreferredSize().
     */
    Dimension		preferredSize();

    /**
     * DEPRECATED:  Replaced by getMinimumSize().
     */
    Dimension		minimumSize();

    /**
     * DEPRECATED:  Replaced by setVisible(boolean).
     */
    void		show();

    /**
     * DEPRECATED:  Replaced by setVisible(boolean).
     */
    void		hide();

    /**
     * DEPRECATED:  Replaced by setEnabled(boolean).
     */
    void		enable();

    /**
     * DEPRECATED:  Replaced by setEnabled(boolean).
     */
    void		disable();

    /**
     * DEPRECATED:  Replaced by setBounds(int, int, int, int).
     */
    void		reshape(int x, int y, int width, int height);

}
