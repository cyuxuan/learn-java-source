/*
 * @(#)IllegalStateException.java	1.6 01/12/12
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.lang;

/**
 * Signals that a method has been invoked at an illegal or
 * inappropriate time.  In other words, the Java environment or
 * Java application is not in an appropriate state for the requested
 * operation.
 *
 * @author  Jonni Kanerva
 * @version 1.6, 12/12/01
 * @since   JDK1.1
 */
public
class IllegalStateException extends RuntimeException {
    /**
     * Constructs an IllegalStateException with no detail message.
     * A detail message is a String that describes this particular exception.
     *
     * @since JDK1.1
     */
    public IllegalStateException() {
	super();
    }

    /**
     * Constructs an IllegalStateException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param s the String that contains a detailed message
     * @since JDK1.1
     */
    public IllegalStateException(String s) {
	super(s);
    }
}
