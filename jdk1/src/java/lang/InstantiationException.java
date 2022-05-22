/*
 * @(#)InstantiationException.java	1.11 01/12/12
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.lang;

/**
 * Thrown when an application tries to create an instance of a class 
 * using the <code>newInstance</code> method in class 
 * <code>Class</code>, but the specified class object cannot be 
 * instantiated because it is an interface or is an abstract class. 
 *
 * @author  unascribed
 * @version 1.11, 12/12/01
 * @see     java.lang.Class#newInstance()
 * @since   JDK1.0
 */
public
class InstantiationException extends Exception {
    /**
     * Constructs an <code>InstantiationException</code> with no detail message.
     *
     * @since   JDK1.0
     */
    public InstantiationException() {
	super();
    }

    /**
     * Constructs an <code>InstantiationException</code> with the 
     * specified detail message. 
     *
     * @param   s   the detail message.
     * @since   JDK1.0
     */
    public InstantiationException(String s) {
	super(s);
    }
}
