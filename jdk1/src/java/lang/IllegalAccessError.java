/*
 * @(#)IllegalAccessError.java	1.10 01/12/12
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.lang;

/**
 * Thrown if an application attempts to access or modify a field, or 
 * to call a method that it does not have access to. 
 * <p>
 * Normally, this error is caught by the compiler; this error can 
 * only occur at run time if the definition of a class has 
 * incompatibly changed. 
 *
 * @author  unascribed
 * @version 1.10, 12/12/01
 * @since   JDK1.0
 */
public class IllegalAccessError extends IncompatibleClassChangeError {
    /**
     * Constructs an <code>IllegalAccessError</code> with no detail message.
     *
     * @since   JDK1.0
     */
    public IllegalAccessError() {
	super();
    }

    /**
     * Constructs an <code>IllegalAccessError</code> with the specified 
     * detail message. 
     *
     * @param   s   the detail message.
     * @since   JDK1.0
     */
    public IllegalAccessError(String s) {
	super(s);
    }
}
