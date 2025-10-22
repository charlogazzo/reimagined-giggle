package com.foreshock.SDAInvoiceScanner.parser;

import com.foreshock.SDAInvoiceScanner.model.CountryIBAN;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Provides a method for reading PDF Files
 */
@Component
public class PDFReader {

    static Set<CountryIBAN> countryIBANSet;

    static {
        try {
            countryIBANSet = CountryParser.readIBANData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a PDF file stored on the local filesystem
     * The Apache PDFTextStripper class used here extracts text from the PDDocument
     * @param filePath path to the PDF file
     * @return A String whose contents are all the text contained within the PDF file
     */
    public String readPDF(String filePath) {
        String formattedText = "";

        try {
            PDDocument document = Loader.loadPDF(new File(filePath));
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // the new line characters are replaced with empty space characters in case an IBAN continues on a next line
            formattedText = pdfStripper.getText(document).replace('\n', ' ');
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return formattedText;
    }
}
