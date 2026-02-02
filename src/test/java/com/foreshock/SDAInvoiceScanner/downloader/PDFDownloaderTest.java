package com.foreshock.SDAInvoiceScanner.downloader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PDFDownloaderTest {

    @Test
    void downloadFile_shouldSavePdf_whenResourceExists(@TempDir Path tempDir) throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        Resource resource = mock(Resource.class);

        byte[] content = "PDF-DATA".getBytes();
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(content));
        when(restTemplate.getForEntity("http://example.com/file.pdf", Resource.class))
                .thenReturn(ResponseEntity.ok(resource));

        PDFDownloader downloader = new PDFDownloader(restTemplate);

        Path savePath = tempDir.resolve("nested/dir/test.pdf");

        // execute
        downloader.downloadFile("http://example.com/file.pdf", savePath);

        // verify file exists and content matches
        assertThat(Files.exists(savePath)).isTrue();
        byte[] fileBytes = Files.readAllBytes(savePath);
        assertThat(fileBytes).isEqualTo(content);
    }

    @Test
    void downloadFile_shouldThrowIOException_whenResourceIsNull(@TempDir Path tempDir) {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForEntity("http://example.com/missing.pdf", Resource.class))
                .thenReturn(ResponseEntity.ok(null));

        PDFDownloader downloader = new PDFDownloader(restTemplate);

        Path savePath = tempDir.resolve("out.pdf");

        assertThrows(IOException.class, () -> downloader.downloadFile("http://example.com/missing.pdf", savePath));
    }

    @Test
    void downloadFile_shouldThrowIOException_whenResourceDoesNotExist(@TempDir Path tempDir) {
        RestTemplate restTemplate = mock(RestTemplate.class);
        Resource resource = mock(Resource.class);

        when(resource.exists()).thenReturn(false);
        when(restTemplate.getForEntity("http://example.com/bad.pdf", Resource.class))
                .thenReturn(ResponseEntity.ok(resource));

        PDFDownloader downloader = new PDFDownloader(restTemplate);

        Path savePath = tempDir.resolve("out.pdf");

        assertThrows(IOException.class, () -> downloader.downloadFile("http://example.com/bad.pdf", savePath));
    }
}
