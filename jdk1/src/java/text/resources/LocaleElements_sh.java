/*
 * @(#)LocaleElements_sh.java	1.10 01/12/12
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

public class LocaleElements_sh extends ListResourceBundle {
    /**
     * Overrides ListResourceBundle
     */
    public Object[][] getContents() {
        return new Object[][] {
            { "LocaleString", "sh_YU" }, // locale id based on iso codes
            { "LocaleID", "081a" }, // Windows id
            { "ShortLanguage", "srp" }, // iso-3 abbrev lang name
            { "ShortCountry", "YUG" }, // iso-3 abbrev country name
            { "Languages", // language names
                new String[][] {
                    { "sh", "Srpski" }
                }
            },
            { "Countries", // country names
                new String[][] {
                    { "YU", "Jugoslavija" }
                }
            },
            { "MonthNames",
                new String[] {
                    "Januar", // january
                    "Februar", // february
                    "Mart", // march
                    "April", // april
                    "Maj", // may
                    "Juni", // june
                    "Juli", // july
                    "Avgust", // august
                    "Septembar", // september
                    "Oktobar", // october
                    "Novembar", // november
                    "Decembar", // december
                    "" // month 13 if applicable
                }
            },
            { "MonthAbbreviations",
                new String[] {
                    "Jan", // abb january
                    "Feb", // abb february
                    "Mar", // abb march
                    "Apr", // abb april
                    "Maj", // abb may
                    "Jun", // abb june
                    "Jul", // abb july
                    "Avg", // abb august
                    "Sep", // abb september
                    "Okt", // abb october
                    "Nov", // abb november
                    "Dec", // abb december
                    "" // abb month 13 if applicable
                }
            },
            { "DayNames",
                new String[] {
                    "Nedelja", // Sunday
                    "Ponedeljak", // Monday
                    "Utorak", // Tuesday
                    "Sreda", // Wednesday
                    "\u010cetvrtak", // Thursday
                    "Petak", // Friday
                    "Subota" // Saturday
                }
            },
            { "DayAbbreviations",
                new String[] {
                    "Ned", // abb Sunday
                    "Pon", // abb Monday
                    "Uto", // abb Tuesday
                    "Sre", // abb Wednesday
                    "\u010cet", // abb Thursday
                    "Pet", // abb Friday
                    "Sub" // abb Saturday
                }
            },
            { "Eras",
                new String[] { // era strings
                    "p. n. e.",
                    "n. e."
                }
            },
            { "NumberPatterns",
                new String[] {
                    "#,##0.###;-#,##0.###", // decimal pattern
                    "'Din' #,##0.00;-'Din' #,##0.00", // currency pattern
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
                    "Din", // local currency symbol
                    "YUN", // intl currency symbol
                    "," // monetary decimal separator
                }
            },
            { "DateTimePatterns",
                new String[] {
                    "HH.mm.ss z", // full time pattern
                    "HH.mm.ss z", // long time pattern
                    "H.mm.ss", // medium time pattern
                    "H.mm", // short time pattern
                    "EEEE, yyyy, MMMM d", // full date pattern
                    "EEEE, yyyy, MMMM d", // long date pattern
                    "yyyy.M.d", // medium date pattern
                    "yy.M.d", // short date pattern
                    "{1} {0}" // date-time pattern
                }
            },
            { "CollationElements",
                /* for sh_SP, default sorting except for the following: */

                /* add dz "ligature" between d and d<stroke>. */
                /* add d<stroke> between d and e. */
                /* add lj "ligature" between l and l<stroke>. */
                /* add l<stroke> between l and m. */
                /* add nj "ligature" between n and o. */
                /* add z<abovedot> after z.       */

                "& D < dz, Dz, dZ, DZ < \u0111, \u0110"      /* dz + d<stk> */
                +"& L < lj, Lj, lJ, LJ < \u0142, \u0141"     /* lj + l<stk> */
                +"& N < nj, Nj, nJ, NJ"                      /* nj ligature */
                +"& Z < \u017c, \u017b"                      /* z<abovedot> */
            }
        };
    }
}
