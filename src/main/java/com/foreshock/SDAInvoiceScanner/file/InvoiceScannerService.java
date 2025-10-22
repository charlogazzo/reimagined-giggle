package com.foreshock.SDAInvoiceScanner.file;

import com.foreshock.SDAInvoiceScanner.checker.IBANCheckerService;
import com.foreshock.SDAInvoiceScanner.downloader.PDFDownloader;
import com.foreshock.SDAInvoiceScanner.parser.PDFReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * This service class combines all other services to download PDFs, scan and extract text from them, and return the
 * text snippets that match the given criteria
 */
@Slf4j
@Service
public class InvoiceScannerService {

    // Change this to a desired directory for saving downloads
    public static final String FILE_SAVE_DIRECTORY = "/home/charles/Downloads/";

    private final PDFReader pdfReader;
    private final PDFDownloader pdfDownloader;
    private final IBANCheckerService ibanCheckerService;
    RestTemplate restTemplate;

    public InvoiceScannerService(IBANCheckerService ibanCheckerService, PDFReader pdfReader, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.pdfDownloader = new PDFDownloader(restTemplate);
        this.ibanCheckerService = ibanCheckerService;
        this.pdfReader = pdfReader;
    }

    /**
     * This method accepts a String url and returns text snippets (IBANs) that match certain criteria
     * @param url URL to the PDF file
     * @return A String message containing all matching IBANs or a message stating that none were found
     * @throws IOException
     */
    public String scan(String url) throws IOException {
        StringBuilder result = new StringBuilder();

        String fileName = "Rechnung_" + LocalDateTime.now() + ".pdf";
        String fullFileName = FILE_SAVE_DIRECTORY + fileName;

        // file is downloaded
        pdfDownloader.downloadFile(url, Path.of(fullFileName));

        // text is read from the PDF file
        String readFromPDF = pdfReader.readPDF(fullFileName);
        log.info("The pdf file contents: {}", readFromPDF);

        // IBANs are read from the file
        Set<String> extractedIbans = ibanCheckerService.extract(readFromPDF);

        // check against banned IBANs
        if (!extractedIbans.isEmpty()) {
            Set<String> matchingIbans = ibanCheckerService.check(extractedIbans);

            if (!matchingIbans.isEmpty()) {
                result.append("The following banned IBANS were found:\n\n");
                for (String iban : matchingIbans) {
                    result.append(iban);
                    result.append("\n");
                }
            } else {
                result.append("No banned IBANs were found in this document");
            }
        } else {
            result.append("No IBANs were found in this document");
        }

        return result.toString();
    }
}
