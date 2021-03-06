/*
 * @(#)ContainerPeer.java	1.8 01/12/12
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package java.awt.peer;

import java.awt.*;

public interface ContainerPeer extends ComponentPeer {
    Insets getInsets();
    void beginValidate();
    void endValidate();

    /**
     * DEPRECATED:  Replaced by getInsets().
     */
    Insets insets();
}
