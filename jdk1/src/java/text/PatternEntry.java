/*
 * @(#)PatternEntry.java	1.17 01/12/12
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
package java.text;

import java.lang.Character;

/**
 * Utility class for normalizing and merging patterns for collation.
 * This is to be used with MergeCollation for adding patterns to an
 * existing rule table.
 * @see        MergeCollation
 * @version    1.17 12/12/01
 * @author     Mark Davis, Helena Shih
 */

class PatternEntry {
    /**
     * Gets the current extension, quoted
     */
    public void appendQuotedExtension(StringBuffer toAddTo) {
        appendQuoted(extension,toAddTo);
    }

    /**
     * Gets the current chars, quoted
     */
    public void appendQuotedChars(StringBuffer toAddTo) {
        appendQuoted(chars,toAddTo);
    }

    /**
     * WARNING this is used for searching in a Vector.
     * Because Vector.indexOf doesn't take a comparator,
     * this method is ill-defined and ignores strength.
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        PatternEntry other = (PatternEntry) obj;
        boolean result = (chars.equals(other.chars) &&
                          extension.equals(other.extension));
        return result;
    }

    /**
     * For debugging.
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        addToBuffer(result, true, false, null);
        return result.toString();
    }

    /**
     * Gets the strength of the entry.
     */
    int getStrength()
    {
	return strength;
    }

    /**
     * Gets the expanding characters of the entry.
     */
    String getExtension()
    {
	    return extension;
    }

    /**
     * Gets the core characters of the entry.
     */
    String getChars()
    {
	    return chars;
    }

    // ===== privates =====

    PatternEntry() {
    }

    PatternEntry(int strength,
                 StringBuffer chars,
                 StringBuffer extension)
    {
        this.strength = strength;
        this.chars = chars.toString();
        this.extension = extension.toString();
    }

    void addToBuffer(StringBuffer toAddTo,
                     boolean showExtension,
                     boolean showWhiteSpace,
                     PatternEntry lastEntry)
    {
        if (showWhiteSpace && toAddTo.length() > 0)
            if (strength == Collator.PRIMARY || lastEntry != null)
                toAddTo.append('\n');
            else
                toAddTo.append(' ');
        if (lastEntry != null) {
            toAddTo.append('&');
            if (showWhiteSpace)
                toAddTo.append(' ');
            lastEntry.appendQuotedChars(toAddTo);
            appendQuotedExtension(toAddTo);
            if (showWhiteSpace)
                toAddTo.append(' ');
        }
        switch (strength) {
        case Collator.IDENTICAL: toAddTo.append('='); break;
        case Collator.TERTIARY:  toAddTo.append(','); break;
        case Collator.SECONDARY: toAddTo.append(';'); break;
        case Collator.PRIMARY:   toAddTo.append('<'); break;
        case RESET: toAddTo.append('&'); break;
        case UNSET: toAddTo.append('?'); break;
        }
        if (showWhiteSpace)
            toAddTo.append(' ');
        appendQuoted(chars,toAddTo);
        if (showExtension && extension.length() != 0) {
            toAddTo.append('/');
            appendQuoted(extension,toAddTo);
        }
    }

    int getNextEntry(String pattern,
                     int i) throws ParseException
    {
        int newStrength = UNSET;
        StringBuffer newChars = new StringBuffer();
        StringBuffer newExtension = new StringBuffer();
        boolean inChars = true;
        boolean inQuote = false;
 mainLoop:
        while (i < pattern.length()) {
            char ch = pattern.charAt(i);
            if (inQuote) {
                if (ch == '\'') {
                    inQuote = false;
                } else {
                    if (newChars.length() == 0) newChars.append(ch);
                    else if (inChars) newChars.append(ch);
                    else newExtension.append(ch);
                }
            } else switch (ch) {
            case '=': if (newStrength != UNSET) break mainLoop;
                newStrength = Collator.IDENTICAL; break;
            case ',': if (newStrength != UNSET) break mainLoop;
                newStrength = Collator.TERTIARY; break;
            case ';': if (newStrength != UNSET) break mainLoop;
                newStrength = Collator.SECONDARY; break;
            case '<': if (newStrength != UNSET) break mainLoop;
                newStrength = Collator.PRIMARY; break;
            case '&': if (newStrength != UNSET) break mainLoop;
                newStrength = RESET; break;
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ': break; // skip whitespace TODO use Character
            case '/': inChars = false; break;
            case '\'':
                inQuote = true;
                ch = pattern.charAt(++i);
                if (newChars.length() == 0) newChars.append(ch);
                else if (inChars) newChars.append(ch);
                else newExtension.append(ch);
                break;
            default:
                if (newStrength == UNSET) {
                    throw new ParseException
                        ("missing char (=,;<&) : " +
                         pattern.substring(i,
                            (i+10 < pattern.length()) ?
                             i+10 : pattern.length()),
                         i);
                }
                if (PatternEntry.isSpecialChar(ch) && (inQuote == false))
                    throw new ParseException
                        ("Unquoted punctuation character : " + Integer.toString(ch, 16), i);
                if (inChars) {
                    newChars.append(ch);
                } else {
                    newExtension.append(ch);
                }
                break;
            }
            i++;
        }
        if (newStrength == UNSET)
            return -1;
        if (newChars.length() == 0) {
            throw new ParseException
                ("missing chars (=,;<&): " +
                  pattern.substring(i,
                      (i+10 < pattern.length()) ?
                       i+10 : pattern.length()),
                 i);
        }
        strength = newStrength;
        chars = newChars.toString();
        extension = newExtension.toString();
        return i;
    }

    static boolean isSpecialChar(char ch) {
        return (((ch <= '\u002F') && (ch >= '\u0020')) ||
                ((ch <= '\u003F') && (ch >= '\u003A')) ||
                ((ch <= '\u0060') && (ch >= '\u005B')) ||
                ((ch <= '\u007E') && (ch >= '\u007B')));
    }

    static void appendQuoted(String chars, StringBuffer toAddTo) {
        boolean inQuote = false;
        char ch = chars.charAt(0);
        if (Character.isSpaceChar(ch)) {
            inQuote = true;
            toAddTo.append('\'');
        } else {
          if (PatternEntry.isSpecialChar(ch)) {
                inQuote = true;
                toAddTo.append('\'');
            } else {
                switch (ch) {
                    case 0x0010: case '\f': case '\r':
                    case '\t': case '\n':  case '@':
                    inQuote = true;
                    toAddTo.append('\'');
                    break;
                case '\'':
                    inQuote = true;
                    toAddTo.append('\'');
                    break;
                default:
                    if (inQuote) {
                        inQuote = false; toAddTo.append('\'');
                    }
                    break;
                }
           }
        }
        toAddTo.append(chars);
        if (inQuote)
            toAddTo.append('\'');
    }

    static final int RESET = -2;
    static final int UNSET = -1;

    int strength = UNSET;
    String chars = "";
    String extension = "";
}
