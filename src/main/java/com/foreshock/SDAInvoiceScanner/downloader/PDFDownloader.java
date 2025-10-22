package com.foreshock.SDAInvoiceScanner.downloader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


@Slf4j
@Service
public class PDFDownloader extends AbstractDownloader {
    public PDFDownloader(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Override
    protected void processDownloadedFile(InputStream in, Path fileSavePath) throws IOException {
        Files.createDirectories(fileSavePath.getParent());
        Files.copy(in, fileSavePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("PDF file saved to {}", fileSavePath);
    }
}
