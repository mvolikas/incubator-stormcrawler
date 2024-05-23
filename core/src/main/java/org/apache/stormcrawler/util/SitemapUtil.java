package org.apache.stormcrawler.util;

import crawlercommons.sitemaps.Namespace;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Pattern matching utility for Sitemap sniffing.
 *
 * <ul>
 *   <li>Uses the <a
 *       href="https://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm">KMP</a>
 *       algorithm implementation from <a
 *       href="https://github.com/stevenhalim/cpbook-code/blob/master/ch6/string_matching.java">here</a>
 *   <li>Since the pattern (clue) is static, the preprocessing is done only once. Calls to
 *       KmpSearch() are linear in the text length.
 * </ul>
 */
public class SitemapUtil {
    private static final byte[] pattern = Namespace.SITEMAP.getBytes(StandardCharsets.UTF_8);
    private static final int m = pattern.length;

    private static final int[] backTable = KmpPreprocess();

    public static int[] KmpPreprocess() {
        int[] backTable = new int[m + 1];
        int i = 0;
        int j = -1;
        backTable[0] = -1;
        while (i < m) {
            while (j >= 0 && pattern[i] != pattern[j]) j = backTable[j];
            i++;
            j++;
            backTable[i] = j;
        }
        return backTable;
    }

    /**
     * @param text
     * @return The index of the first occurrence of the pattern in the text, or -1 if not found
     */
    public static int KmpSearch(byte[] text) {
        int n = text.length;
        int i = 0;
        int j = 0;
        while (i < n) {
            while (j >= 0 && text[i] != pattern[j]) j = backTable[j];
            i++;
            j++;
            if (j == m) return i - j;
        }
        return -1;
    }

    /**
     * Examines the first bytes of the content for a clue of whether this document is a sitemap,
     * based on namespaces. Works for XML and non-compressed documents only.
     */
    public static boolean sniff(byte[] content, int maxOffsetGuess) {
        byte[] beginning = content;
        if (content.length > maxOffsetGuess && maxOffsetGuess > 0) {
            beginning = Arrays.copyOfRange(content, 0, maxOffsetGuess);
        }
        int position = KmpSearch(beginning);
        return position != -1;
    }
}
