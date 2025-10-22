package com.foreshock.SDAInvoiceScanner.checker;

import java.util.Collection;

/**
 * This interface provides methods for performing validation on Strings
 */
public interface TextChecker {

    /**
     * Validates a collection of Strings according to set criteria
     * @param inputCollection containing Strings
     * @return Strings matching the defined criteria
     */
    Collection<String> check(Collection<String> inputCollection);

    /**
     * Extracts text from within the given text
     * @param text text to be scanned through for matching text
     * @return a Collection containing all matching text snippets
     */
    Collection<String> extract(String text);
}
