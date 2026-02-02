package com.foreshock.SDAInvoiceScanner.file;

import com.foreshock.SDAInvoiceScanner.checker.IBANCheckerService;
import com.foreshock.SDAInvoiceScanner.downloader.PDFDownloader;
import com.foreshock.SDAInvoiceScanner.parser.PDFReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InvoiceScannerServiceTest {

    private IBANCheckerService ibanCheckerService;
    private PDFReader pdfReader;
    private PDFDownloader pdfDownloaderMock;
    private InvoiceScannerService service;

    @BeforeEach
    void setUp() throws Exception {
        ibanCheckerService = mock(IBANCheckerService.class);
        pdfReader = mock(PDFReader.class);
        // prepare a mock PDFDownloader and inject it to avoid filesystem I/O
        pdfDownloaderMock = mock(PDFDownloader.class);
        // create service with injected mock downloader
        service = new InvoiceScannerService(ibanCheckerService, pdfReader, pdfDownloaderMock);
    }

    @Test
    void scan_shouldReturnBannedIbansMessage_whenMatchingIbansFound() throws Exception {
        String url = "http://example.com/invoice.pdf";
        String pdfText = "some pdf content";

        when(pdfReader.readPDF(anyString())).thenReturn(pdfText);

        Set<String> extracted = Set.of("DE44500105155407324931");
        when(ibanCheckerService.extract(pdfText)).thenReturn(extracted);
        when(ibanCheckerService.check(extracted)).thenReturn(extracted);

        String result = service.scan(url);

        assertThat(result).contains("The following banned IBANS were found:");
        assertThat(result).contains("DE44500105155407324931");

        // verify pdfDownloader.downloadFile was called with the provided url and a Path
        ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
        verify(pdfDownloaderMock).downloadFile(eq(url), pathCaptor.capture());
        Path passedPath = pathCaptor.getValue();
        assertThat(passedPath.toString()).contains("Rechnung_");
    }

    @Test
    void scan_shouldReturnNoBannedMessage_whenNoMatchingIbans() throws Exception {
        String url = "http://example.com/invoice.pdf";
        String pdfText = "some pdf content";

        when(pdfReader.readPDF(anyString())).thenReturn(pdfText);

        Set<String> extracted = Set.of("DE00123456789012345678");
        when(ibanCheckerService.extract(pdfText)).thenReturn(extracted);
        when(ibanCheckerService.check(extracted)).thenReturn(Collections.emptySet());

        String result = service.scan(url);

        assertThat(result).isEqualTo("No banned IBANs were found in this document");
        verify(pdfDownloaderMock).downloadFile(eq(url), any(Path.class));
    }

    @Test
    void scan_shouldReturnNoIbansMessage_whenNoIbansExtracted() throws Exception {
        String url = "http://example.com/invoice.pdf";
        String pdfText = "no ibans here";

        when(pdfReader.readPDF(anyString())).thenReturn(pdfText);
        when(ibanCheckerService.extract(pdfText)).thenReturn(Collections.emptySet());

        String result = service.scan(url);

        assertThat(result).isEqualTo("No IBANs were found in this document");
        verify(pdfDownloaderMock).downloadFile(eq(url), any(Path.class));
    }
}
