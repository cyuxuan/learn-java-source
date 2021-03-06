/*
 * @(#)LocaleElements_sq.java	1.9 01/12/12
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

public class LocaleElements_sq extends ListResourceBundle {
    /**
     * Overrides ListResourceBundle
     */
    public Object[][] getContents() {
        return new Object[][] {
            { "LocaleString", "sq_AL" }, // locale id based on iso codes
            { "LocaleID", "041c" }, // Windows id
            { "ShortLanguage", "sqi" }, // iso-3 abbrev lang name
            { "ShortCountry", "ALB" }, // iso-3 abbrev country name
            { "Languages", // language names
                new String[][] {
                    { "sq", "shqipe" }
                }
            },
            { "Countries", // country names
                new String[][] {
                    { "AL", "Shqip\u00ebria" }
                }
            },
            { "MonthNames", 
                new String[] { 
                    "janar", // january
                    "shkurt", // february
                    "mars", // march
                    "prill", // april
                    "maj", // may
                    "qershor", // june
                    "korrik", // july
                    "gusht", // august
                    "shtator", // september
                    "tetor", // october
                    "n\u00ebntor", // november
                    "dhjetor", // december
                    "" // month 13 if applicable
                }
            },
            { "MonthAbbreviations", 
                new String[] { 
                    "Jan", // abb january
                    "Shk", // abb february
                    "Mar", // abb march
                    "Pri", // abb april
                    "Maj", // abb may
                    "Qer", // abb june
                    "Kor", // abb july
                    "Gsh", // abb august
                    "Sht", // abb september
                    "Tet", // abb october
                    "N\u00ebn", // abb november
                    "Dhj", // abb december
                    "" // abb month 13 if applicable
                }
            },
            { "DayNames", 
                new String[] { 
                    "e diel", // Sunday
                    "e h\u00ebn\u00eb", // Monday
                    "e mart\u00eb", // Tuesday
                    "e m\u00ebrkur\u00eb", // Wednesday
                    "e enjte", // Thursday
                    "e premte", // Friday
                    "e shtun\u00eb" // Saturday
                }
            },
            { "DayAbbreviations", 
                new String[] { 
                    "Die", // abb Sunday
                    "H\u00ebn", // abb Monday
                    "Mar", // abb Tuesday
                    "M\u00ebr", // abb Wednesday
                    "Enj", // abb Thursday
                    "Pre", // abb Friday
                    "Sht" // abb Saturday
                }
            },
            { "AmPmMarkers", 
                new String[] { 
                    "PD", // am marker
                    "MD" // pm marker
                }
            },
            { "Eras", 
                new String[] { // era strings
                    "p.e.r.", 
                    "n.e.r." 
                }
            },
            { "NumberPatterns", 
                new String[] { 
                    "#,##0.###;-#,##0.###", // decimal pattern
                    "Lek#,##0.###;-Lek#,##0.###", // currency pattern
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
                    "Lek", // local currency symbol
                    "ALL", // intl currency symbol
                    "," // monetary decimal separator
                }
            },
            { "DateTimePatterns", 
                new String[] { 
                    "h.mm.ss.a z", // full time pattern
                    "h.mm.ss.a z", // long time pattern
                    "h:mm:ss.a", // medium time pattern
                    "h.mm.a", // short time pattern
                    "yyyy-MM-dd", // full date pattern
                    "yyyy-MM-dd", // long date pattern
                    "yy-MM-dd", // medium date pattern
                    "yy-MM-dd", // short date pattern
                    "{1} {0}" // date-time pattern
                }
            },
            { "CollationElements", "@" } 
        };
    }
}
