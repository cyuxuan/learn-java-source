/*
 * @(#)LocaleElements_fr_BE.java	1.7 01/12/12
 *
 * (C) Copyright Taligent, Inc. 1996, 1997 - All Rights Reserved
 * (C) Copyright IBM Corp. 1996, 1997 - All Rights Reserved
 *
 * Portions copyright (c) 2002 Sun Microsystems, Inc. All Rights Reserved.
 *
 *   The original version of this source code and documentation is copyrighted
 * and owned by Taligent, Inc., a wholly-owned subsidiary of IBM. These
 * materials are provided under terms of a License Agreement between Taligent
 * and Sun. This technology is protected by multiple US and International
 * patents. This notice and attribution to Taligent may not be removed.
 *   Taligent is a registered trademark of Taligent, Inc.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

/**
 *
 * Table of Java supplied standard locale elements
 *
 * automatically generated by java LocaleTool LocaleElements.java
 *
 * Date Created: Wed Aug 21 15:47:57  1996
 *
 *     Locale Elements and Patterns:  last update 10/23/96
 *
 *
 */

// WARNING : the format of this file will change in the future!

package java.text.resources;

import java.util.ListResourceBundle;

public class LocaleElements_fr_BE extends ListResourceBundle {
    /**
     * Overrides ListResourceBundle
     */
    public Object[][] getContents() {
        return new Object[][] {
            { "LocaleString", "fr_BE" }, // locale id based on iso codes
            { "LocaleID", "080c" }, // Windows id
            { "ShortCountry", "BEL" }, // iso-3 abbrev country name
            { "NumberPatterns", 
                new String[] { 
                    "#,##0.###;-#,##0.###", // decimal pattern
                    "#,##0.00 FB;-#,##0.00 FB", // currency pattern
                    "#,##0%" // percent pattern
                }
            },
            { "NumberElements", 
                new String[] { 
                    ",", // decimal separator
                    ".", // group (thousands) separator
                    ";", // list separator
                    "%", // percent sign
                    "0", // native 0 digit
                    "#", // pattern digit
                    "-", // minus sign
                    "E", // exponential
                    "\u2030", // per mille
                    "\u221e", // infinity
                    "\ufffd" // NaN
                }
            },
            { "CurrencyElements", 
                new String[] { 
                    "FB", // local currency symbol
                    "BEF", // intl currency symbol
                    "," // monetary decimal separator
                }
            },
            { "DateTimePatterns", 
                new String[] { 
                    "H' h 'mm' min 'ss' s 'z", // full time pattern
                    "H:mm:ss z", // long time pattern
                    "H:mm:ss", // medium time pattern
                    "H:mm", // short time pattern
                    "EEEE d MMMM yyyy", // full date pattern
                    "d MMMM yyyy", // long date pattern
                    "dd-MMM-yy", // medium date pattern
                    "d/MM/yy", // short date pattern
                    "{1} {0}" // date-time pattern
                }
            }
        };
    }
}
