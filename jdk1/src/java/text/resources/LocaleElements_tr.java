/*
 * @(#)LocaleElements_tr.java	1.11 01/12/12
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

public class LocaleElements_tr extends ListResourceBundle {
    /**
     * Overrides ListResourceBundle
     */
    public Object[][] getContents() {
        return new Object[][] {
            { "LocaleString", "tr_TR" }, // locale id based on iso codes
            { "LocaleID", "041f" }, // Windows id
            { "ShortLanguage", "tur" }, // iso-3 abbrev lang name
            { "ShortCountry", "TUR" }, // iso-3 abbrev country name
            { "Languages", // language names
                new String[][] {
                    { "tr", "T\u00fcrk\u00e7e" }
                }
            },
            { "Countries", // country names
                new String[][] {
                    { "TR", "T\u00fcrkiye" }
                }
            },
            { "MonthNames",
                new String[] {
                    "Ocak", // january
                    "\u015eubat", // february
                    "Mart", // march
                    "Nisan", // april
                    "May\u0131s", // may
                    "Haziran", // june
                    "Temmuz", // july
                    "A\u011fustos", // august
                    "Eyl\u00fcl", // september
                    "Ekim", // october
                    "Kas\u0131m", // november
                    "Aral\u0131k", // december
                    "" // month 13 if applicable
                }
            },
            { "MonthAbbreviations",
                new String[] {
                    "Oca", // abb january
                    "\u015eub", // abb february
                    "Mar", // abb march
                    "Nis", // abb april
                    "May", // abb may
                    "Haz", // abb june
                    "Tem", // abb july
                    "A\u011fu", // abb august
                    "Eyl", // abb september
                    "Eki", // abb october
                    "Kas", // abb november
                    "Ara", // abb december
                    "" // abb month 13 if applicable
                }
            },
            { "DayNames",
                new String[] {
                    "Pazar", // Sunday
                    "Pazartesi", // Monday
                    "Sal\u0131", // Tuesday
                    "\u00c7ar\u015famba", // Wednesday
                    "Per\u015fembe", // Thursday
                    "Cuma", // Friday
                    "Cumartesi" // Saturday
                }
            },
            { "DayAbbreviations",
                new String[] {
                    "Paz", // abb Sunday
                    "Pzt", // abb Monday
                    "Sal", // abb Tuesday
                    "\u00c7ar", // abb Wednesday
                    "Per", // abb Thursday
                    "Cum", // abb Friday
                    "Cmt" // abb Saturday
                }
            },
            { "NumberPatterns",
                new String[] {
                    "#,##0.###;-#,##0.###", // decimal pattern
                    "#,##0.00 TL;-#,##0.00 TL", // currency pattern
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
                    "TL", // local currency symbol
                    "TRL", // intl currency symbol
                    "," // monetary decimal separator
                }
            },
            { "DateTimePatterns",
                new String[] {
                    "HH:mm:ss z", // full time pattern
                    "HH:mm:ss z", // long time pattern
                    "HH:mm:ss", // medium time pattern
                    "HH:mm", // short time pattern
                    "dd MMMM yyyy EEEE", // full date pattern
                    "dd MMMM yyyy EEEE", // long date pattern
                    "dd.MMM.yyyy", // medium date pattern
                    "dd.MM.yyyy", // short date pattern
                    "{1} {0}" // date-time pattern
                }
            },
            { "DateTimeElements",
                new String[] {
                    "2", // first day of week
                    "1" // min days in first week
                }
            },
            { "CollationElements",
                "& A < a\u0308 , A\u0308 "   // a-umlaut sorts between a and b
                + "& C < c\u0327 , C\u0327 " // c-cedilla sorts between c and d
                + "& G < g\u0306 , G\u0306 " // g-breve sorts between g and h
                + "& H < \u0131 , I , i , \u0130 "  // dotless i , dotted i
                + "< \u0132 , \u0133 "       // ij ligature sorts between i and j
                + "& O < o\u0308 , O\u0308 " // o-umlaut sorts between o and p
                + "& S < s\u0327 , S\u0327 " // s-cedilla sorts between s and t
                + "& U < u\u0308 , U\u0308 " // u-umlaut sorts between u and v
            }
        };
    }
}
