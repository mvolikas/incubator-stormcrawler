package org.apache.stormcrawler.util;

import com.google.common.primitives.Bytes;
import crawlercommons.sitemaps.Namespace;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

public class SitemapUtilTest {
    private final byte[] pattern = Namespace.SITEMAP.getBytes(StandardCharsets.UTF_8);

    @Test
    public void testKmpSearch() throws IOException {
        byte[] text = readFileFromResources("digitalpebble.sitemap.xml");
        int positionKmp = SitemapUtil.KmpSearch(text);
        int positionIndexOf = Bytes.indexOf(text, pattern);
        org.junit.Assert.assertEquals(positionKmp, positionIndexOf);
    }

    @Test
    public void testSitemapSniffTrue() throws IOException {
        byte[] text = readFileFromResources("digitalpebble.sitemap.extensions.links.xml");
        org.junit.Assert.assertTrue(SitemapUtil.sniff(text, 1000));
    }

    @Test
    public void testSitemapSniffFalse() throws IOException {
        byte[] text = readFileFromResources("longtext.html");
        org.junit.Assert.assertFalse(SitemapUtil.sniff(text, 1000000));
    }

    private byte[] readFileFromResources(String fileName) throws IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).getPath());
        return Files.readAllBytes(path);
    }
}
