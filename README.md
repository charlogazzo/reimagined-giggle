
# Project Title

This program performs checks the contents of PDF files for any blacklisted IBANs used within the document. This solution is part of an anti-money laundering effort.



The API can be accessed through the "/scanpdf" url


## Documentation

Pre-requisites for running this program are:

JDK 21 installation
Apache Maven 3.8.x

External libraries used in this project are:

Apache PDFBox: This is used to read and parse data from within the PDF file.

Lombok: Logging activity within the project


## Run Locally

In order to run this project and test its functionality:

 Navigate to the root folder /SDAInvoiceScanner

 Run 
    
    mvn dependency:resolve

    
    mvn spring-boot:run

I have provided a link to a sample PDF file with a list of IBANs to be scanned.

Within the IBANCheckerService class, there is a hard-coded list of banned IBANs and these are the IBANs which the program will scan the list for. (Of course, in a real imlpementation, the IBANs will be better represented and not hard-coded).

To access the IBAN checker run the curl command:

    curl -X POST http://localhost:8080/scanpdf
    -H "Content-Type: text/plain"
    --data "https://drive.google.com/uc?export=download&id=1ImBV3D85yqqia37NIOKvW5ZEBTvFLk5b"

## Optimizations

To improve this solution:

1. The list of banned IBANs could be gotten fromma more secure repository.

2. Checks for IBANs for multiple countries could also be added. This would involve adding implementations to the TextChecker interface to check for different IBAN formats.

3. Currently, an interface for checking numbers has been added. This can be used to check currency values within the document if such a feature is required in the future.
