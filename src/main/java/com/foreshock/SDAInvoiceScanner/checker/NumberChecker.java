package com.foreshock.SDAInvoiceScanner.checker;

import java.util.List;
import java.util.Map;

/**
 * This interface provides methods for performing validation on numerical values
 */
public interface NumberChecker {
    Map<Boolean, List<String>> checkInteger(Integer number);

    Map<Boolean, List<String>> checkFloat(Float number);
}
