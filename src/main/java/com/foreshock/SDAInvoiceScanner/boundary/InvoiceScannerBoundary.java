package com.foreshock.SDAInvoiceScanner.boundary;

import com.foreshock.SDAInvoiceScanner.file.InvoiceScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class InvoiceScannerBoundary {

    @Autowired
    InvoiceScannerService invoiceScannerService;

    @PostMapping("/scanpdf")
    public String scanDocumentFromUrl(@RequestBody String url) throws IOException {
        return invoiceScannerService.scan(url);
    }


}
