package com.foreshock.SDAInvoiceScanner.downloader;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * This class defines methods for downloading files from the internet and processing them further
 */
public abstract class AbstractDownloader {

    protected final RestTemplate restTemplate;

    public AbstractDownloader(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Downloads a  file from a given URL
     * @param fileUrl URL of the file
     * @param fileSavePath Path on local file system where the file is to be saved
     * @throws IOException
     */
    public void downloadFile(String fileUrl, Path fileSavePath) throws IOException {
        ResponseEntity<Resource> response = restTemplate.getForEntity(fileUrl, Resource.class);
        Resource resource = response.getBody();

        if (resource == null || !resource.exists()) {
            throw new IOException("Unable to download file from " + fileUrl);
        }

        // the child class implementation is called to further process the file
        processDownloadedFile(resource.getInputStream(), fileSavePath);
    }

    /**
     * Subclasses define their own implementation based on file type and other file-specific requirements
     * @param in Specifies the InputStream from the downloaded resource
     * @param fileSavePath Path on local file system where the file is to be saved
     */
    protected abstract void processDownloadedFile(InputStream in, Path fileSavePath) throws IOException;
}
